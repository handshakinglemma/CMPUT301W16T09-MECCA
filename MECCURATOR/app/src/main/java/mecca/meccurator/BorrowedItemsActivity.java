package mecca.meccurator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Displays a list of all items currently being borrowed by the user
 */
public class BorrowedItemsActivity extends AppCompatActivity {

    protected static final String ARTFILE = "artfile.sav";

    public String current_user;

    private ListView oldBorrowedItems;
    private ArrayList<Art> allServerArt = new ArrayList<Art>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("TODO", "ON CREATE");

        setContentView(R.layout.activity_borrowed_items);

        // Get username from HomeActivity
        Intent intentRcvEdit = getIntent();
        current_user = intentRcvEdit.getStringExtra("current_user");

        oldBorrowedItems = (ListView) findViewById(R.id.oldBorrowedItems);

        // Pull all server art
        boolean success = false;
        while (!success){
            success = pullAllServerArt();
        }

        // Save all server art locally
        saveInFile();

        // Load locally saved art
        // Only need to do this once for the lifetime of this activity
        loadFromFile();

        // Sets variable selectedArt and updates adapter
        setSelectedArt(allServerArt);
    }

    // Code from https://github.com/joshua2ua/lonelyTwitter
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Log.i("TODO", "ON START");

        // Sets variable selectedArt and updates adapter
        setSelectedArt(ArtList.allArt);
    }

    // Sets variable selectedArt and updates adapter
    public void setSelectedArt (ArrayList<Art> artlist){

        // Filter all art by if user is borrowing item
        ArrayList<Art> borrowedArt = new ArrayList<>();
        // Selected art is only those items that the current_user is a borrower
        for (Art a: artlist) {
            if ((a.getBorrower().toLowerCase().trim().equals(current_user.toLowerCase().trim()))) {
                borrowedArt.add(a);
            }
        }

        Log.i("Size of borrowed art", String.valueOf(borrowedArt.size()));
        Log.i("Size of All art", String.valueOf(ArtList.allArt.size()));

        // Update adapter
        ArtAdapter adapter = new ArtAdapter(BorrowedItemsActivity.this, borrowedArt, current_user);
        oldBorrowedItems.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public boolean pullAllServerArt() {

        // Get ALL art from server
        ElasticsearchArtController.GetArtListTask getArtListTask = new ElasticsearchArtController.GetArtListTask();
        getArtListTask.execute("");
        try {
            allServerArt = new ArrayList<Art>();
            allServerArt.addAll(getArtListTask.get());
            return true;

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            try {
                Thread.sleep(1000); // Sleep for 1 sec
                Log.i("TODO", "Sleeping for one sec");
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            return false;
        }
    }

    protected void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(ARTFILE, 0);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(allServerArt, out);
            out.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    // Code from https://github.com/joshua2ua/lonelyTwitter
    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(AddNewItemActivity.ARTFILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            // took from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.htmlon Jan-20-2016

            Type listType = new TypeToken<ArrayList<Art>>() {
            }.getType();
            ArtList.allArt = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            ArtList.allArt = new ArrayList<Art>();
        }
    }

}
