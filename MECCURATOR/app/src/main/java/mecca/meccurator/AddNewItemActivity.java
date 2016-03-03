package mecca.meccurator;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;

import java.sql.Blob;
import java.util.ArrayList;

public class AddNewItemActivity extends AppCompatActivity {


    protected static final String ARTFILE = "artfile.sav";

    //public static ArrayList<Art> artwork;

    /* initialize all input fields */
    private EditText inputTitle;
    private EditText inputArtist;
    private EditText inputDescription;
    private EditText inputDimensions;
    private EditText inputMinPrice;

    /* also need an input field for photos but idk anything yet so */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /* declare all input fields
           maybe can move it out of the onCreate w/e
         */

        inputTitle = (EditText) findViewById(R.id.enterTitle);
        inputArtist = (EditText) findViewById(R.id.enterArtist);
        inputDescription = (EditText) findViewById(R.id.enterDescription);
        inputMinPrice = (EditText) findViewById(R.id.enterMinPrice);
        inputDimensions = (EditText) findViewById(R.id.enterDimensions);

    }

    public void saveEntry(View view){


        float minprice;


        /* get text from EditText */
        String title = inputTitle.getText().toString();
        String artist = inputArtist.getText().toString();
        String description = inputDescription.getText().toString();
        String dimensions = inputDimensions.getText().toString();
        String status = "available";
        String owner = "who?";
        String borrower = "";



        /* check for valid input FIX THIS */
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

        ArtList.allArt = new ArrayList<Art>();
        ///ArtList.allArt.add(newestArt);

        //ArtList artwork = null;
        //artwork.addItem(newestArt);

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
