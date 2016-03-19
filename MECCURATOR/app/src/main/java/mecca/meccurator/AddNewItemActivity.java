package mecca.meccurator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;

import java.sql.Array;
import java.sql.Blob;
import java.util.ArrayList;

/**
 * Displays a form for the user to fill out to create a new item listing.
 * Saves new item to allArt.
 */
public class AddNewItemActivity extends AppCompatActivity {

    /* file that item listings are saved in */
    protected static final String ARTFILE = "artfile.sav";

    /* initialize all input fields */
    private EditText inputTitle;
    private EditText inputArtist;
    private EditText inputDescription;
    private EditText inputLengthDimensions;
    private EditText inputWidthDimensions;
    private EditText inputMinPrice;
    private TextView inputStatus;
    /* also need an input field for photos but idk anything yet so */
    public String current_user;

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

        // Get username from ViewLoginActivity
        Intent intentRcvEdit = getIntent();
        current_user = intentRcvEdit.getStringExtra("current_user");
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
        Art newestArt = new Art(status, owner, borrower, description, artist, title, dimensions, minprice );

        //so this should be artwork.add(newestArt), when artwork is instantiated publicly

        try{
            ArtList.allArt.add(newestArt);
        }catch(NullPointerException e){
            ArtList allArt = new ArtList();
            ArtList.allArt.add(newestArt);
        }

        // Add the tweet to Elasticsearch
        ElasticsearchArtController.AddArtTask addTweetTask = new ElasticsearchArtController.AddArtTask();
        addTweetTask.execute(newestArt);

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

    protected void saveInFile() {
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
}
