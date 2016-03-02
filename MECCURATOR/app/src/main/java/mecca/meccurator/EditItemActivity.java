package mecca.meccurator;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class EditItemActivity extends AppCompatActivity {

    /* initialize all input fields
    protected EditText inputTitle;
    protected EditText inputArtist;
    protected EditText inputDescription;
    protected EditText inputDimensions;
    protected EditText inputMinPrice; */

    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    }

    @Override
    protected void onStart() {
        super.onStart();
        loadValues();
    }

    //make button for this
    public void deleteEntry(View view) {
        Context context = getApplicationContext();
        CharSequence text = "Art Deleted!";
        int duration = Toast.LENGTH_SHORT;
        ArtList.allArt.remove(pos);
        Toast.makeText(context, text, duration).show();
        //FuelTrackActivity.fills.removeAll(FuelTrackActivity.fills);
        saveInFile();
        finish();
    }

    protected void loadValues() {

        /* get values to be edited and fill boxes */

        EditText inputTitle = (EditText) findViewById(R.id.enterTitle);
        EditText inputArtist = (EditText) findViewById(R.id.enterArtist);
        EditText inputDescription = (EditText) findViewById(R.id.enterDescription);
        EditText inputMinPrice = (EditText) findViewById(R.id.enterMinPrice);
        EditText inputDimensions = (EditText) findViewById(R.id.enterDimensions);


        /* append data into EditText box */
        inputArtist.append(ArtList.allArt.get(pos).getArtist());
        inputDescription.append(ArtList.allArt.get(pos).getDescription());
        inputTitle.append(ArtList.allArt.get(pos).getTitle());
        inputMinPrice.append(Float.toString(ArtList.allArt.get(pos).getMinprice()));
        inputDimensions.append(ArtList.allArt.get(pos).getDimensions());


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


        float minprice;

        EditText inputTitle = (EditText) findViewById(R.id.enterTitle);
        EditText inputArtist = (EditText) findViewById(R.id.enterArtist);
        EditText inputDescription = (EditText) findViewById(R.id.enterDescription);
        EditText inputMinPrice = (EditText) findViewById(R.id.enterMinPrice);
        EditText inputDimensions = (EditText) findViewById(R.id.enterDimensions);


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
        ArtList.allArt.add(newestArt);

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

}
