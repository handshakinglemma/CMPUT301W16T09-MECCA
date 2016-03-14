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

/*
Displays the items the user has listed. These items can be filtered by clicking an option from
the dropdown menu. Filters include:
view all my items,
view only my items with bids,
view only my borrowed items, and
view only my available items
User may edit one of their item descriptions by long clicking on the item
User may add a new item listing by click the add new item button
 */
public class ViewMyListingsActivity extends AppCompatActivity implements OnItemSelectedListener {


    private static final String FILENAME = "file.sav";
    private ListView oldArtListings;
    private ArrayAdapter<Art> adapter; // Adapter used for displaying the ListView items
    private ArrayList<Art> selectedArt = new ArrayList<Art>();
    public String current_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_listings);


        // Get username from ViewLoginActivity
        Intent intentRcvEdit = getIntent();
        current_user = intentRcvEdit.getStringExtra("current_user");

        oldArtListings = (ListView) findViewById(R.id.oldArtListings);

        oldArtListings.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Intent edit = new Intent(getApplicationContext(), EditItemActivity.class);

                int pos = position;
                edit.putExtra("position", pos);
                edit.putExtra("current_user", current_user);
                Toast.makeText(parent.getContext(), "Selected: if" + pos, Toast.LENGTH_LONG).show();
                startActivity(edit);
                return true;
            }


        });

        //Setting up the spinner and the adapter. I followed the guidelines from http://www.survivingwithandroid.com/2012/10/android-listview-custom-filter-and.html
        Spinner listingsSpinner = (Spinner) findViewById(R.id.listingTypesSpinner);
        ArrayAdapter adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.listingChoices, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listingsSpinner.setAdapter(adapterSpinner);

        //This sets up what will be down on the option picked with the spinner.
        listingsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choiceSelected = parent.getItemAtPosition(position).toString().split(" ")[0];
                selectedArt = new ArrayList<Art>();

                if (choiceSelected.equals("All")) {
                    for (Art a: ArtList.allArt) {
                        if (a.getOwner().toLowerCase().trim().equals(current_user.toLowerCase().trim())) {
                            selectedArt.add(a);
                        }
                    }


                } else {

                    for (Art a : ArtList.allArt) {
                        if (a.getOwner().toLowerCase().trim().equals(current_user.toLowerCase().trim())) {
                            if (a.getStatus().toLowerCase().trim().equals(choiceSelected.toLowerCase().trim())) {
                                selectedArt.add(a);
                            }
                        }

                    }
                }

                adapter = new ArrayAdapter<Art>(ViewMyListingsActivity.this, R.layout.list_item, selectedArt);
                oldArtListings.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Toast.makeText(parent.getContext(), "Selected: " + choiceSelected, Toast.LENGTH_LONG).show();
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
        intent.putExtra("current_user", current_user);
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
        selectedArt = new ArrayList<>();
        for (Art a: ArtList.allArt) {
            if (a.getOwner().toLowerCase().trim().equals(current_user.toLowerCase().trim())) {
                selectedArt.add(a);
            }
        }
        adapter = new ArrayAdapter<Art>(ViewMyListingsActivity.this,
                R.layout.list_item, selectedArt);
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
