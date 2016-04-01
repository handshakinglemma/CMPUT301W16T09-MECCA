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
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

/**
 * This shows the user all art items that are not borrowed and that are not
 * owned by the current user
 */
// TODO need to make the search results filterable by keyword(s)
public class ViewSearchActivity extends AppCompatActivity {

    private static final String FILENAME = "file.sav";
    protected static final String ARTFILE = "artfile.sav";

    public String keyword;
    public String current_user;
    public String owner;

    private ListView oldSearchListings;
    private ArrayList<Art> selectedArt = new ArrayList<Art>();
    private ArrayList<Art> allServerArt = new ArrayList<Art>();

    private ArrayAdapter<Art> adapter; // Adapter used for displaying the ListView items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("TODO", "ON CREATE");

        setContentView(R.layout.activity_view_search);
        oldSearchListings = (ListView) findViewById(R.id.oldSearchListings);

        // Get username and keyword from HomeActivity
        Intent intentRcvEdit = getIntent();
        current_user = intentRcvEdit.getStringExtra("current_user");
        keyword = intentRcvEdit.getStringExtra("keyword").toLowerCase();

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

        oldSearchListings.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {

                // Need to find position of art in ArtList.allArt
                Log.i("clicked pos", String.valueOf(pos));
                Art art_clicked = adapter.getItem(pos);
                Log.i("clicked art", art_clicked.toString());
                Log.i("ID of clicked art", art_clicked.getId());
                int meta_position = ArtList.allArt.indexOf(art_clicked);
                Log.i("meta pos", String.valueOf(meta_position));
                owner = art_clicked.getOwner();

                Intent newbid = new Intent(getApplicationContext(), AddNewBidActivity.class);
                newbid.putExtra("position", meta_position);
                newbid.putExtra("current_user", current_user);
                newbid.putExtra("owner", owner);
                startActivity(newbid);
                return true;
            }
        });
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

        selectedArt = new ArrayList<>();
        // If no input, selected art is art not owned by current user and that
        // do not have status == "borrowed"
        // Split wouldn't work, so we replace everything that is not a character or
        // number with a space
        keyword = keyword.replaceAll("\\W", " ");
        ArrayList<String> keywords = new ArrayList(Arrays.asList(keyword.split(" ")));

        // NOTE: punctuation and spaces are excluded from the input and description
        // if nothing entered, search all
        if(keyword.equals("")) {
            try {
                for (Art a : artlist) {
                    if ((!(a.getOwner().toLowerCase().trim().equals(current_user.toLowerCase().trim()))) &&
                            (!(a.getStatus().toLowerCase().trim().equals("borrowed")))) {
                        selectedArt.add(a);
                    }
                }
            } catch (NullPointerException e) {
                selectedArt = new ArrayList<>();
            }
        } else {
            // Otherwise search for keywords in the description.
            try {
                for (Art a : artlist) {
                    if ((!(a.getOwner().toLowerCase().trim().equals(current_user.toLowerCase().trim()))) &&
                            (!(a.getStatus().toLowerCase().trim().equals("borrowed")))) {

                        String desc = a.getDescription().toLowerCase().trim().replaceAll("\\W", " ");
                        ArrayList<String> compare = new ArrayList(Arrays.asList(desc.split(" ")));
                        Log.i("TODO", "Split description:");

                        for (String c : compare) {
                            Log.i("TODO", "*" + c + "*");
                            if (keywords.contains(c)) {
                                selectedArt.add(a);
                                Log.i("TODO", a.getTitle());
                            }
                        }
                    }
                }
            }catch (NullPointerException e) {
                selectedArt = new ArrayList<>();
            }
        }

        Log.i("Size of selected Art", String.valueOf(selectedArt.size()));
        Log.i("Size of All art", String.valueOf(ArtList.allArt.size()));

        // Update adapter
        adapter = new ArtAdapter(ViewSearchActivity.this,
                selectedArt);
        oldSearchListings.setAdapter(adapter);
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

}
