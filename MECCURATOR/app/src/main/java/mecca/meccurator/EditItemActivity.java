package mecca.meccurator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.ExecutionException;

/**
 * Displays editable form for user to update their item listing
 * The list of bids place on the item may be view from this page
 *  TODO Add picture to elastic search
 */
public class EditItemActivity extends AppCompatActivity {

    int pos;
    public String current_user;

    private ImageButton pictureButton;
    private Bitmap thumbnail;
    private ImageView inputImage;

    static final int REQUEST_CAPTURING_IMAGE = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);


        Intent edit = getIntent();
        pos = edit.getIntExtra("position", 0);
        current_user = edit.getStringExtra("current_user");
        inputImage = (ImageView) findViewById(R.id.imageView1);
        loadValues();

        // http://developer.android.com/training/camera/photobasics.html
        pictureButton = (ImageButton) findViewById(R.id.pictureButton);
        pictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_CAPTURING_IMAGE);
                }
            }
        });

        if(ArtList.allArt.get(pos).getStatus().equals("borrowed")){
            Button button = (Button)findViewById(R.id.item_bids);
            button.setText("Set Available");
        }

        Button deleteItem = (Button) findViewById(R.id.delete);
        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {deleteEntry(v);
            }
        });

        Button viewBids = (Button) findViewById(R.id.item_bids);
        viewBids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {ViewItemBidsButton(v);}

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadValues();

        if(ArtList.allArt.get(pos).getStatus().equals("borrowed")){
            Button button = (Button)findViewById(R.id.item_bids);
            button.setText("Set Available");
        }
    }

    public void deleteEntry(View view) {

        // Delete item from server
        ElasticsearchArtController.RemoveArtTask removeArtTask = new ElasticsearchArtController.RemoveArtTask();
        removeArtTask.execute(ArtList.allArt.get(pos));

        // Delete item locally
        ArtList.allArt.remove(pos);

        Context context = getApplicationContext();
        CharSequence text = "Art Deleted!";
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, text, duration).show();

        saveInFile();
        finish();
    }

    protected void loadValues() {

        /* get values to be edited and fill boxes */
        EditText inputTitle = (EditText) findViewById(R.id.enterTitle);
        EditText inputArtist = (EditText) findViewById(R.id.enterArtist);
        EditText inputDescription = (EditText) findViewById(R.id.enterDescription);
        EditText inputMinPrice = (EditText) findViewById(R.id.enterMinPrice);
        EditText inputLengthDimensions = (EditText) findViewById(R.id.enterLengthDimensions);
        EditText inputWidthDimensions = (EditText) findViewById(R.id.enterWidthDimensions);
        ImageView inputImage = (ImageView) findViewById(R.id.imageView1);
        TextView showStatus = (TextView) findViewById(R.id.enterStatus);


        //make sure it's empty first
        inputArtist.getText().clear();
        inputDescription.getText().clear();
        inputTitle.getText().clear();
        inputMinPrice.getText().clear();
        inputLengthDimensions.getText().clear();
        inputWidthDimensions.getText().clear();


        /* append data into EditText box */
        inputArtist.append(ArtList.allArt.get(pos).getArtist());
        inputDescription.append(ArtList.allArt.get(pos).getDescription());
        inputTitle.append(ArtList.allArt.get(pos).getTitle());
        inputMinPrice.append(Float.toString(ArtList.allArt.get(pos).getMinprice()));
        inputLengthDimensions.append(ArtList.allArt.get(pos).getLength());
        inputWidthDimensions.append(ArtList.allArt.get(pos).getWidth());

        if(ArtList.allArt.get(pos).getStatus().equals("borrowed")){
            showStatus.setText(String.format("%s by %s", ArtList.allArt.get(pos).getStatus(), ArtList.allArt.get(pos).getBorrower()));
            //make uneditable if bidded or borrowed
            //TODO MAKE NEW FUNC AND PUT BIDDED TOO
            inputArtist.setKeyListener(null);
            inputDescription.setKeyListener(null);
            inputTitle.setKeyListener(null);
            inputMinPrice.setKeyListener(null);
            inputLengthDimensions.setKeyListener(null);
            inputWidthDimensions.setKeyListener(null);
        } else{
            showStatus.setText(ArtList.allArt.get(pos).getStatus());
        }


        thumbnail = ArtList.allArt.get(pos).getThumbnail();
        inputImage.setImageBitmap(thumbnail);
    }

    protected void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(AddNewItemActivity.ARTFILE, 0);

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

    public void saveEntry(View view){

        Art art =  ArtList.allArt.get(pos);

        // Delete item from server
        ElasticsearchArtController.RemoveArtTask removeArtTask = new ElasticsearchArtController.RemoveArtTask();
        removeArtTask.execute(art);

        float minprice;

        EditText inputTitle = (EditText) findViewById(R.id.enterTitle);
        EditText inputArtist = (EditText) findViewById(R.id.enterArtist);
        EditText inputDescription = (EditText) findViewById(R.id.enterDescription);
        EditText inputMinPrice = (EditText) findViewById(R.id.enterMinPrice);
        EditText inputLengthDimensions = (EditText) findViewById(R.id.enterLengthDimensions);
        EditText inputWidthDimensions = (EditText) findViewById(R.id.enterWidthDimensions);

        /* get text from EditText */
        String title = inputTitle.getText().toString();
        String artist = inputArtist.getText().toString();
        String description = inputDescription.getText().toString();
        String dimensionsLength = inputLengthDimensions.getText().toString();
        String dimensionsWidth = inputWidthDimensions.getText().toString();
        String dimensions = dimensionsLength + "x" + dimensionsWidth;
        String status = art.getStatus();
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
        // Save bids
        BidList bids_placed = art.getBidLists();
        newestArt.setBids(bids_placed);  // Transfer over old bids

        // Add the art to Elasticsearch
        ElasticsearchArtController.AddArtTask addArtTask = new ElasticsearchArtController.AddArtTask();
        addArtTask.execute(newestArt);

        String art_id = ""; // Initialize
        try {
            art_id = addArtTask.get();
            Log.i("adds art_id is", art_id);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //so this should be artwork.add(newestArt), when artwork is instantiated publicly
        ArtList.allArt.remove(pos);

        newestArt.setId(art_id); // set id locally

        ArtList.allArt.add(pos, newestArt);

        /* toast message */
        // new func: displayToast or something?
        Context context = getApplicationContext();
        CharSequence saved = "Artwork Saved!";
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, saved, duration).show();

        /* end add activity */
        saveInFile();
        finish();

    }

    public void saveEntry(){

        Art art =  ArtList.allArt.get(pos);

        // Delete item from server
        ElasticsearchArtController.RemoveArtTask removeArtTask = new ElasticsearchArtController.RemoveArtTask();
        removeArtTask.execute(art);

        float minprice;

        EditText inputTitle = (EditText) findViewById(R.id.enterTitle);
        EditText inputArtist = (EditText) findViewById(R.id.enterArtist);
        EditText inputDescription = (EditText) findViewById(R.id.enterDescription);
        EditText inputMinPrice = (EditText) findViewById(R.id.enterMinPrice);
        EditText inputLengthDimensions = (EditText) findViewById(R.id.enterLengthDimensions);
        EditText inputWidthDimensions = (EditText) findViewById(R.id.enterWidthDimensions);

        /* get text from EditText */
        String title = inputTitle.getText().toString();
        String artist = inputArtist.getText().toString();
        String description = inputDescription.getText().toString();
        String dimensionsLength = inputLengthDimensions.getText().toString();
        String dimensionsWidth = inputWidthDimensions.getText().toString();
        String dimensions = dimensionsLength + "x" + dimensionsWidth;
        String status = art.getStatus();
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
        // Save bids
        BidList bids_placed = art.getBidLists();
        newestArt.setBids(bids_placed);  // Transfer over old bids

        // Add the art to Elasticsearch
        ElasticsearchArtController.AddArtTask addArtTask = new ElasticsearchArtController.AddArtTask();
        addArtTask.execute(newestArt);

        String art_id = ""; // Initialize
        try {
            art_id = addArtTask.get();
            Log.i("adds art_id is", art_id);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //so this should be artwork.add(newestArt), when artwork is instantiated publicly
        ArtList.allArt.remove(pos);

        newestArt.setId(art_id); // set id locally

        ArtList.allArt.add(pos, newestArt);

        /* toast message */
        // new func: displayToast or something?
        Context context = getApplicationContext();
        CharSequence saved = "Artwork Saved!";
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, saved, duration).show();

        /* end add activity */
        saveInFile();

    }

    // Click to view bids on this item
    public void ViewItemBidsButton(View view) {

        Art art = ArtList.allArt.get(pos);

        if(ArtList.allArt.get(pos).getStatus().equals("borrowed")){
            art.setStatus("available");
            art.setBorrower("");
            //save entry and so it saves to the server
            saveEntry();
            Button button = (Button)findViewById(R.id.item_bids);
            button.setText("View Item Bids");
            loadValues();


        }
        else{
            Intent intent = new Intent(this, ViewItemBidsActivity.class);
            intent.putExtra("current_user", current_user);
            intent.putExtra("position", pos);

            startActivity(intent);
        }

    }

    // http://developer.android.com/training/camera/photobasics.html
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        //// TODO: 16-03-25 ADD SIZE CHECKING 
        if (requestCode == REQUEST_CAPTURING_IMAGE && resultCode == RESULT_OK) {
            Bundle extras = intent.getExtras();
            thumbnail = (Bitmap) extras.get("data");
            inputImage.setImageBitmap(thumbnail);
        }
    }

    public void deletePhoto(View view) {

        thumbnail = null;
        inputImage.setImageBitmap(thumbnail);
    }
}
