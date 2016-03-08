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

public class EditItemActivity extends AppCompatActivity {

    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
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

    // Click to view bids on this item
    public void ViewItemBidsButton(View view) {
        Intent intent = new Intent(this, ViewItemBidsActivity.class);
        startActivity(intent);
    }

    /*

    protected void loadValues() {

        get values to be edited and fill boxes
        EditText title = (EditText) findViewById(R.id.enterTitle);
        EditText artist = (EditText) findViewById(R.id.editStation);
        EditText grade = (EditText) findViewById(R.id.editGrade);
        EditText odometer = (EditText) findViewById(R.id.editOdometer);
        EditText amount = (EditText) findViewById(R.id.editAmount);
        EditText unit = (EditText) findViewById(R.id.editUnitCost);

        date.append(FuelTrackActivity.fills.get(pos).getDate());
        station.append(FuelTrackActivity.fills.get(pos).getStation());
        grade.append(FuelTrackActivity.fills.get(pos).getGrade());
        odometer.append(Float.toString(FuelTrackActivity.fills.get(pos).getOdometer()));
        amount.append(Float.toString(FuelTrackActivity.fills.get(pos).getAmount()));
        unit.append(Float.toString(FuelTrackActivity.fills.get(pos).getUnit()));

    }

    public void saveEntry(View view) {

        float odometer;
        float amount;
        float cost;
        float unit;

        EditText entryStation = (EditText) findViewById(R.id.editStation);
        EditText entryGrade = (EditText) findViewById(R.id.editGrade);
        EditText entryOdometer = (EditText) findViewById(R.id.editOdometer);
        EditText entryAmount = (EditText) findViewById(R.id.editAmount);
        EditText entryUnit = (EditText) findViewById(R.id.editUnitCost);

        String date = entryDate.getText().toString();
        String station = entryStation.getText().toString();
        String grade = entryGrade.getText().toString();

        try {
            odometer = Float.valueOf(entryOdometer.getText().toString());
        } catch(NumberFormatException wrong){
            entryOdometer.setError("Invalid Input...");
            return;
        }

        try {
            amount = Float.valueOf(entryAmount.getText().toString());
        } catch(NumberFormatException wrong){
            entryAmount.setError("Invalid Input...");
            return;
        }

        try {
            unit = Float.valueOf(entryUnit.getText().toString());
        } catch(NumberFormatException wrong){
            entryUnit.setError("Invalid Input...");
            return;
        }

        cost = (amount * unit)/100;

        FillUp newestLog = new LogItem(date, station, grade, odometer, amount, cost, unit);
        FuelTrackActivity.fills.remove(pos);
        FuelTrackActivity.fills.add(pos, newestLog);

        Context context = getApplicationContext();
        CharSequence text = "Log Saved!";
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, text, duration).show();

        saveInFile();
        finish();

    } */

}
