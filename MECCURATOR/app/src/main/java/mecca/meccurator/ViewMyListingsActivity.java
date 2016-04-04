package mecca.meccurator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
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
 *     <li>View only my items with bids.</li>
 *     <li>View only my borrowed items.</li>
 *     <li>View only my available items.</li>
 * </ul>
 * User may edit one of their item descriptions by long clicking on the item.
 * User may add a new item listing by click the add new item button.
 * TODO implement offline behavior
 */
public class ViewMyListingsActivity extends AppCompatActivity implements OnItemSelectedListener {

    protected static final String ARTFILE = "artfile.sav";

    public String current_user;

    private ListView oldArtListings;
    private ArrayList<Art> selectedArt = new ArrayList<Art>();
    private ArrayList<Art> allServerArt = new ArrayList<Art>();
    private ArrayAdapter<Art> adapter; // Adapter used for displaying the ListView items

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i("TODO", "ON CREATE");

        setContentView(R.layout.activity_view_my_listings);

        // Get current_user from HomeActivity
        Intent intentRcvEdit = getIntent();
        current_user = intentRcvEdit.getStringExtra("current_user");
        oldArtListings = (ListView) findViewById(R.id.oldArtListings);

        // Load locally saved art
        // Only need to do this once for the lifetime of this activity
        loadFromFile();

        // Sets variable selectedArt and updates adapter
        setSelectedArt(allServerArt);

        final Button addNewItem = (Button) findViewById(R.id.additembutton);

        addNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewListingButton(v);

            }
        });

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
                adapter = new ArtAdapter(ViewMyListingsActivity.this, selectedArt, current_user);
                oldArtListings.setAdapter(adapter);
                adapter.notifyDataSetChanged();
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
        adapter = new ArtAdapter(ViewMyListingsActivity.this, selectedArt, current_user);
        oldArtListings.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
