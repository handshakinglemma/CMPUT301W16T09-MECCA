package mecca.meccurator;

import android.content.Context;
import android.content.Intent;
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

/**
 * Displays editable form for user to update their item listing
 * The list of bids place on the item may be view from this page
 */
public class EditItemActivity extends AppCompatActivity {

    int pos;
    public String current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Intent edit = getIntent();
        pos = edit.getIntExtra("position", 0);
        current_user = edit.getStringExtra("current_user");

        loadValues();
    }

    //make button for this
    public void deleteEntry(View view) {
        Context context = getApplicationContext();
        CharSequence text = "Art Deleted!";
        int duration = Toast.LENGTH_SHORT;
        ArtList.allArt.remove(pos);
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

        /* append data into EditText box */
        inputArtist.append(ArtList.allArt.get(pos).getArtist());
        inputDescription.append(ArtList.allArt.get(pos).getDescription());
        inputTitle.append(ArtList.allArt.get(pos).getTitle());
        inputMinPrice.append(Float.toString(ArtList.allArt.get(pos).getMinprice()));
        inputLengthDimensions.append(ArtList.allArt.get(pos).getLength());
        inputWidthDimensions.append(ArtList.allArt.get(pos).getWidth());
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
        EditText inputLengthDimensions = (EditText) findViewById(R.id.enterLengthDimensions);
        EditText inputWidthDimensions = (EditText) findViewById(R.id.enterWidthDimensions);

        /* get text from EditText */
        String title = inputTitle.getText().toString();
        String artist = inputArtist.getText().toString();
        String description = inputDescription.getText().toString();
        String dimensionsLength = inputLengthDimensions.getText().toString();
        String dimensionsWidth = inputWidthDimensions.getText().toString();
        String dimensions = dimensionsLength + "x" + dimensionsWidth;
        String status = "available";
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
        ArtList.allArt.remove(pos);
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

    // Click to view bids on this item
    public void ViewItemBidsButton(View view) {
        Intent intent = new Intent(this, ViewItemBidsActivity.class);
        intent.putExtra("current_user", current_user);
        intent.putExtra("position", pos);

        startActivity(intent);
    }


}
