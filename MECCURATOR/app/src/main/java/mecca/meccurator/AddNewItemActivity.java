package mecca.meccurator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.commons.lang3.ObjectUtils;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.util.concurrent.ExecutionException;

/**
 * Displays a form for the user to fill out to create a new item listing.
 * Saves new item to allArt.
 * TODO Add picture to elastic search
 */
public class AddNewItemActivity extends AppCompatActivity {

    /* file that item listings are saved in */
    protected static final String ARTFILE = "artfile.sav";
    protected static final String OFFLINEART = "offlineart.sav";

    boolean connected;

    /* initialize all input fields */
    private EditText inputTitle;
    private EditText inputArtist;
    private EditText inputDescription;
    private EditText inputLengthDimensions;
    private EditText inputWidthDimensions;
    private EditText inputMinPrice;
    private TextView inputStatus;
    private ImageView inputImage;
    /* also need an input field for photos but idk anything yet so */
    private String current_user;

    private Bitmap thumbnail;

    private static final int REQUEST_CAPTURING_IMAGE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        /**
         * declare all input fields
         * maybe can move it out of the onCreate w/e
         */

        inputTitle = (EditText) findViewById(R.id.enterTitle);
        inputArtist = (EditText) findViewById(R.id.enterArtist);
        inputDescription = (EditText) findViewById(R.id.enterDescription);
        inputMinPrice = (EditText) findViewById(R.id.enterMinPrice);
        inputLengthDimensions = (EditText) findViewById(R.id.enterLengthDimensions);
        inputWidthDimensions = (EditText) findViewById(R.id.enterWidthDimensions);
        inputStatus = (TextView) findViewById(R.id.enterStatus);
        inputImage = (ImageView) findViewById(R.id.imageView1);

        // http://developer.android.com/training/camera/photobasics.html
        ImageButton pictureButton = (ImageButton) findViewById(R.id.pictureButton);
        pictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_CAPTURING_IMAGE);
                }
            }
        });

        // Get username from ViewLoginActivity
        Intent intentRcvEdit = getIntent();
        current_user = intentRcvEdit.getStringExtra("current_user");

        final Button saveItemButton = (Button) findViewById(R.id.save);

        saveItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEntry(v);

            }
        });
    }

    public void saveEntry(View view){

        float minprice;

        /* get text from EditText */
        String title = inputTitle.getText().toString();
        String artist = inputArtist.getText().toString();
        String description = inputDescription.getText().toString();
        String dimensionsLength = inputLengthDimensions.getText().toString();
        String dimensionsWidth = inputWidthDimensions.getText().toString();
        String dimensions = dimensionsLength + "x" + dimensionsWidth;
        String status = inputStatus.getText().toString();
        String owner = current_user;
        String borrower = "";

        // check for valid input
        if(title.equals("")){
            inputTitle.setError("Empty Field!");
            return;
        }

        if(artist.equals("")){
            inputArtist.setError("Empty Field!");
            return;
        }

        if(dimensionsLength.equals("")){
            inputLengthDimensions.setError("Empty Field!");
            return;
        }

        if(dimensionsWidth.equals("")){
            inputWidthDimensions.setError("Empty Field!");
            return;
        }

        if(description.equals("")){
            inputDescription.setError("Empty Field!");
            return;
        }

        try {
            minprice = Float.parseFloat(inputMinPrice.getText().toString());
        } catch(NumberFormatException wrong){
            inputMinPrice.setError("Invalid Input...");
            return;
        }

        /* add new entry to list of items */
        //TODO: add owner and other attributes by pulling from lists also PHOTO
        Art newestArt = new Art(status, owner, borrower, description, artist, title, dimensions, minprice, thumbnail);
        newestArt.addThumbnail(thumbnail);

        isConnected();
        String art_id = ""; // Initialize

        if (connected) {
            // Add the art to Elasticsearch
            ElasticsearchArtController.AddArtTask addArtTask = new ElasticsearchArtController.AddArtTask();
            addArtTask.execute(newestArt);

            try {
                art_id = addArtTask.get();
                Log.i("adds art_id is", art_id);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // add it to the offLineArt file
                ArtList.offLineArt.add(newestArt);
            } catch (NullPointerException e) {
                ArtList offLineArt = new ArtList();
                ArtList.offLineArt.add(newestArt);
            }
        }

        ArtList.allArt.get(ArtList.allArt.size()-1).setId(art_id);

        try{
            ArtList.allArt.add(newestArt);
        }catch(NullPointerException e){
            ArtList allArt = new ArtList();
            ArtList.allArt.add(newestArt);
        }

        /* toast message */
        // new func: displayToast or something?
        Context context = getApplicationContext();
        CharSequence saved = "Artwork Saved!";
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, saved, duration).show();

        /* end add activity */
        saveOffline();
        saveInFile();
        finish();
    }

    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(ARTFILE, 0);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(ArtList.allArt, out);
            out.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    private void saveOffline() {
        try {
            FileOutputStream fos = openFileOutput(OFFLINEART, 0);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(ArtList.offLineArt, out);
            out.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    // http://developer.android.com/training/camera/photobasics.html
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        //// TODO: 16-03-25 ADD SIZE CHECKING 
        if (requestCode == REQUEST_CAPTURING_IMAGE && resultCode == RESULT_OK){
            Bundle extras = intent.getExtras();
            thumbnail = (Bitmap) extras.get("data");
            inputImage.setImageBitmap(thumbnail);

            if(thumbnail != null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                    if(thumbnail.getByteCount() < 65536){

                    }
                    else{

                        Context context = getApplicationContext();
                        CharSequence saved = String.valueOf(thumbnail.getByteCount());
                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(context, saved, duration).show();
                    }
                }
            }
        }
    }

    public void deletePhoto(View view) {
        thumbnail = null;
        inputImage.setImageBitmap(null);
    }

    public void isConnected() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }
    }
}