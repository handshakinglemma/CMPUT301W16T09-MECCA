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
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
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

public class ViewMyListingsActivity extends AppCompatActivity implements OnItemSelectedListener {


    private static final String FILENAME = "file.sav";
    private ListView oldArtListings;
    private ArrayAdapter<Art> adapter; // Adapter used for displaying the ListView items
    private ArrayList<Art> selectedArt = new ArrayList<Art>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_listings);

        oldArtListings = (ListView) findViewById(R.id.oldArtListings);

        oldArtListings.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Intent edit = new Intent(getApplicationContext(), EditItemActivity.class);

                int pos = position;
                edit.putExtra("position", pos);
                Toast.makeText(parent.getContext(), "Selected: if" + pos, Toast.LENGTH_LONG).show();
                startActivity(edit);
                return true;
            }


        });

        Spinner listingsSpinner = (Spinner) findViewById(R.id.listingTypesSpinner);
        //listingsSpinner.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        ArrayAdapter adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.listingChoices, android.R.layout.simple_spinner_item);
        ///// don't do the activity thing again just use for loop maybe and change adapter
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listingsSpinner.setAdapter(adapterSpinner);
        //String choiceSelected = listingsSpinner.getSelectedItem().toString().split(" ")[0];
        listingsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String choiceSelected = parent.getItemAtPosition(position).toString().split(" ")[0];
                //if (choiceSelected = "Available" ) {
                if (choiceSelected.equals("All")) {
                    // Do Nothing
                    //Toast.makeText(parent.getContext(), "Selected: if" + choiceSelected, Toast.LENGTH_LONG).show();
                    adapter = new ArrayAdapter<Art>(ViewMyListingsActivity.this,
                            R.layout.list_item, ArtList.allArt);
                    oldArtListings.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                } else {
                    selectedArt = new ArrayList<Art>();

                    for (Art a : ArtList.allArt) {
                        //Toast.makeText(parent.getContext(), a.getTitle().toLowerCase(), Toast.LENGTH_SHORT).show();
                        if (a.getStatus().toLowerCase().trim().equals(choiceSelected.toLowerCase().trim())) {
                            //Toast.makeText(parent.getContext(), "Selected: else" + choiceSelected, Toast.LENGTH_LONG).show();
                            selectedArt.add(a);
                            //Toast.makeText(parent.getContext(), selectedArt.size(), Toast.LENGTH_SHORT).show();


                        }
                    }

                    adapter = new ArrayAdapter<Art>(ViewMyListingsActivity.this,
                            R.layout.list_item, selectedArt);
                    oldArtListings.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                // Showing selected spinner item
                //Toast.makeText(parent.getContext(), "Selected: " + choiceSelected, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do Nothing
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

        adapter = new ArrayAdapter<Art>(ViewMyListingsActivity.this,
                R.layout.list_item, selectedArt);
        oldArtListings.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new ArrayAdapter<Art>(ViewMyListingsActivity.this,
               R.layout.list_item, ArtList.allArt);
        oldArtListings.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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
            // TODO Auto-generated catch block
            ArtList.allArt = new ArrayList<Art>();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    public void seeListingItem() {

    }

    public void viewBorrowedItemsOnly() {

    }

    public void viewBidOnItemsOnly() {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
