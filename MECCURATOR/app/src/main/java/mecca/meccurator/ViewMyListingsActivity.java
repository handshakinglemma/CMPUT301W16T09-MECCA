package mecca.meccurator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

public class ViewMyListingsActivity extends AppCompatActivity {


    private static final String FILENAME = "file.sav";
    //public static ArrayList<Art> artwork = new ArrayList<Art>();

    protected ListView oldListings;
    //private ArrayList<Art> itemlistinglist = new ArrayList<Art>();
    public ArrayAdapter<Art> adapter; // Adapter used for displaying the ListView items


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_listings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        oldListings = (ListView) findViewById(R.id.oldMyListings);


        oldListings.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Intent edit = new Intent(getApplicationContext(), EditMyItemActivity.class);
                int pos = position;
                edit.putExtra("position", pos);
                startActivity(edit);
                return true;


            }


        });

    }

    // Click to create a new listing
    public void CreateNewListingButton(View view) {
        Intent intent = new Intent(this, AddNewItemActivity.class);
        startActivity(intent);
    }


    // Code from https://github.com/joshua2ua/lonelyTwitter
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        loadFromFile();
        adapter = new ArrayAdapter<Art>(this, R.layout.list_item, ArtList.artwork);
        oldListings.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();

    }

    // Code from https://github.com/joshua2ua/lonelyTwitter
    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(AddNewItemActivity.ARTFILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            // took from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.htmlon Jan-20-2016
            Type listType = new TypeToken<ArrayList<Art>>() {}.getType();
            ArtList.artwork = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            ArtList.artwork = new ArrayList<Art>();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    /*
    // Code from https://github.com/joshua2ua/lonelyTwitter
    private void saveInFile() {
        try {
            adapter.notifyDataSetChanged();
            FileOutputStream fos = openFileOutput(AddNewItemActivity.ARTFILE,
                    0); // This file can be accessed by this application only, file will be filled with new stuff
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(artwork, out);
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


    public void seeListingItem() {

    }

    public void viewBorrowedItemsOnly() {

    }

    public void viewBidOnItemsOnly() {

    }



}
