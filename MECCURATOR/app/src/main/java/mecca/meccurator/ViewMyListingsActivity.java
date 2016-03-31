package mecca.meccurator;

import android.app.Activity;
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
import java.util.concurrent.ExecutionException;

/** Displays the items the user has listed. These items can be filtered by clicking an option from
 * the dropdown menu. Filters include:
 * <ul>
 *     <li>View all my items.</li>
 *     <li>View only my itmes with bids.</li>
 *     <li>View only my borrowed items.</li>
 *     <li>View only my available items.</li>
 * </ul>
 * User may edit one of their item descriptions by long clicking on the item.
 * User may add a new item listing by click the add new item button.
 * TODO implement offline behavior
 */
public class ViewMyListingsActivity extends AppCompatActivity implements OnItemSelectedListener {

    protected static final String ARTFILE = "artfile.sav";
    private ListView oldArtListings;
    private ArrayAdapter<Art> adapter; // Adapter used for displaying the ListView items
    private ArrayList<Art> selectedArt = new ArrayList<Art>();
    private ArrayList<Art> allServerArt = new ArrayList<Art>();
    public String current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i("TODO", "ON CREATE");

        setContentView(R.layout.activity_view_my_listings);

        // Get current_user from HomeActivity
        Intent intentRcvEdit = getIntent();
        current_user = intentRcvEdit.getStringExtra("current_user");
        oldArtListings = (ListView) findViewById(R.id.oldArtListings);

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

        // When item in listview is clicked launch EditItem Activity
        oldArtListings.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {

                // Need to find position of art in ArtList.allArt
                Log.i("clicked pos", String.valueOf(pos));
                Art art_clicked = adapter.getItem(pos);
                Log.i("clicked art", art_clicked.toString());
                Log.i("ID of clicked art", art_clicked.getId());
                int meta_position = ArtList.allArt.indexOf(art_clicked);
                Log.i("meta pos", String.valueOf(meta_position));

                Intent edit = new Intent(getApplicationContext(), EditItemActivity.class);
                edit.putExtra("position", meta_position);
                edit.putExtra("current_user", current_user);
                startActivity(edit);

                return true;
            }

        });

        // NOTE: Spinner uses local data only.
        //Setting up the spinner and the adapter.
        // I followed the guidelines from http://www.survivingwithandroid.com/2012/10/android-listview-custom-filter-and.html
        Spinner listingsSpinner = (Spinner) findViewById(R.id.listingTypesSpinner);
        ArrayAdapter adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.listingChoices, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listingsSpinner.setAdapter(adapterSpinner);

        //This sets up what will be down on the option picked with the spinner.
        // Filter all art by spinner
        listingsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choiceSelected = parent.getItemAtPosition(position).toString().split(" ")[0];

                selectedArt = new ArrayList<Art>();

                if (choiceSelected.equals("All")) {
                    for (Art a : ArtList.allArt) {
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

                // Update adapter
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

    @Override
    protected void onStart() {

        super.onStart();
        Log.i("TODO", "ON START");
        // Sets variable selectedArt and updates adapter
        setSelectedArt(ArtList.allArt);

    }

    // Launches AddNewItemActivity
    public void CreateNewListingButton(View view) {
        Intent intent = new Intent(this, AddNewItemActivity.class);
        intent.putExtra("current_user", current_user);
        startActivity(intent);
    }

    // Sets variable selectedArt and updates adapter
    public void setSelectedArt (ArrayList<Art> artlist){

        // Filter all art from server by owner
        selectedArt = new ArrayList<Art>();
        for (Art a: artlist) {
            if (a.getOwner().toLowerCase().trim().equals(current_user.toLowerCase().trim())) {
                selectedArt.add(a);
            }
        }

        Log.i("Size of selected art", String.valueOf(selectedArt.size()));
        Log.i("Size of All art", String.valueOf(ArtList.allArt.size()));

        // Update adapter
        adapter = new ArrayAdapter<Art>(ViewMyListingsActivity.this,
                R.layout.list_item, selectedArt);
        oldArtListings.setAdapter(adapter);
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
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

}
