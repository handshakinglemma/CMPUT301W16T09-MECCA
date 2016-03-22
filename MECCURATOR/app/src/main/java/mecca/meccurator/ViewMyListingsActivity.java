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
 */
public class ViewMyListingsActivity extends AppCompatActivity implements OnItemSelectedListener {


    private static final String FILENAME = "file.sav";
    private ListView oldArtListings;
    private ArrayAdapter<Art> adapter; // Adapter used for displaying the ListView items
    private ArrayList<Art> selectedArt = new ArrayList<Art>();
    private ArrayList<Art> allServerArt = new ArrayList<Art>();
    public String current_user;
    Integer CODE; // Use for return from AddNewItemActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_listings);


        // Get current_user from HomeActivity
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
        // Filter all art by spinner
        listingsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choiceSelected = parent.getItemAtPosition(position).toString().split(" ")[0];
                selectedArt = new ArrayList<Art>();

                if (choiceSelected.equals("All")) {
                    for (Art a : allServerArt) {
                        if (a.getOwner().toLowerCase().trim().equals(current_user.toLowerCase().trim())) {
                            selectedArt.add(a);
                        }
                    }


                } else {

                    for (Art a : allServerArt) {
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

    // Click to create a new listing
    public void CreateNewListingButton(View view) {
        CODE = 1;
        Intent intent = new Intent(this, AddNewItemActivity.class);
        intent.putExtra("current_user", current_user);
        //startActivity(intent);
        startActivityForResult(intent, CODE); // This way we can do something upon return from the AddNewItemActivity
    }

    // onActivityResult calls onResume(). This way adapter is updated after new item is added
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // If the request went well (OK) and the request was CODE
        if (resultCode == Activity.RESULT_OK && requestCode == CODE) {
            onResume();
        }
    }


    // Code from https://github.com/joshua2ua/lonelyTwitter
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        Log.i("TODO", "ON START!!!!");

        // TODO implement offline behavior
        //loadFromFile();

        // Get ALL art from server
        ElasticsearchArtController.GetArtListTask getTweetsTask = new ElasticsearchArtController.GetArtListTask();
        getTweetsTask.execute("");

        try {
            allServerArt = new ArrayList<Art>();
            allServerArt.addAll(getTweetsTask.get());
            int size = allServerArt.size();
            Log.i("SServer art size is", String.valueOf(size));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Filter all art by owner
        selectedArt = new ArrayList<>();
        for (Art a: allServerArt) {
            if (a.getOwner().toLowerCase().trim().equals(current_user.toLowerCase().trim())) {
                selectedArt.add(a);
            }
        }

        // Update adapter
        adapter = new ArrayAdapter<Art>(ViewMyListingsActivity.this,
                R.layout.list_item, selectedArt);
        oldArtListings.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("TODO", "ON RESUME!!!!");

        // Get ALL art from server
        ElasticsearchArtController.GetArtListTask getTweetsTask = new ElasticsearchArtController.GetArtListTask();
        getTweetsTask.execute("");

        try {
            allServerArt = new ArrayList<Art>();
            allServerArt.addAll(getTweetsTask.get());
            int size = allServerArt.size();
            Log.i("RServer art size is", String.valueOf(size));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

//      // Filter all art by owner
        selectedArt = new ArrayList<>();
        for (Art a: allServerArt) {
            if (a.getOwner().toLowerCase().trim().equals(current_user.toLowerCase().trim())) {
                selectedArt.add(a);
            }
        }

        // Update adapter
        adapter = new ArrayAdapter<Art>(ViewMyListingsActivity.this,
                R.layout.list_item, selectedArt);
        oldArtListings.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    // Not being used currently used. Will need this later for implementing offline behavior.
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

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
