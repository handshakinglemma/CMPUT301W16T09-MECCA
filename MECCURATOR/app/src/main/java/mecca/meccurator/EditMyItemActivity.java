package mecca.meccurator;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class EditMyItemActivity extends AppCompatActivity {

    /* position of entry to be edited */
    int pos;

    ArtList artwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    protected void onStart() {
        super.onStart();
        //loadValues();
    }

    public void deleteEntry(View view) {
        Context context = getApplicationContext();
        CharSequence text = "Log Deleted!";
        int duration = Toast.LENGTH_SHORT;

        artwork.remove(pos);
        Toast.makeText(context, text, duration).show();
        //FuelTrackActivity.fills.removeAll(FuelTrackActivity.fills);
        //saveInFile();
        finish();
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


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_child, menu);
        return true;
    }

    protected void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FuelTrackActivity.FILENAME, 0);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(FuelTrackActivity.fills, out);
            out.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    } */

}
