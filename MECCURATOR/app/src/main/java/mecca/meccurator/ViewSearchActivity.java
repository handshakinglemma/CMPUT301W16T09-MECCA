package mecca.meccurator;

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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * This shows the user all art items that are not borrowed and that are not
 * owned by the current user
 */
// TODO need to make the search results filterable by keyword(s)
public class ViewSearchActivity extends AppCompatActivity {

    private static final String FILENAME = "file.sav";
    public String keyword;
    private ListView oldAllArtListings;
    private ArrayAdapter<Art> adapter; // Adapter used for displaying the ListView items
    private ArrayList<Art> selectedArt = new ArrayList<Art>();
    public String current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_search);

        // Get username from ViewLoginActivity
        Intent intentRcvEdit = getIntent();
        current_user = intentRcvEdit.getStringExtra("current_user");

        // Get keyword from HomeActivity
        Intent intentRcvEdit2 = getIntent();
        keyword = intentRcvEdit.getStringExtra("keyword").toLowerCase();

        oldAllArtListings = (ListView) findViewById(R.id.oldAllArtListings);

        oldAllArtListings.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Intent newbid = new Intent(getApplicationContext(), AddNewBidActivity.class);
                newbid.putExtra("position", position);
                newbid.putExtra("current_user", current_user);
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
        loadFromFile();

        adapter = new ArrayAdapter<Art>(ViewSearchActivity.this,
                R.layout.list_item, selectedArt);
        oldAllArtListings.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedArt = new ArrayList<>();
        // If no input, selected art is art not owned by current user and that
        // do not have status == "borrowed"
        // Split wouldn't work, so we replace everything that is not a character or
        // number with a space
        keyword = keyword.replaceAll("\\W", " ");
        ArrayList<String> keywords = new ArrayList(Arrays.asList(keyword.split(" ")));
        for( String k: keywords) {
            Log.i("TODO", "*" + k + "*");
        }

        // TODO: What do we do if somebody enters in a bunch of spaces or other characters?
        if(keyword.equals("")) {
            try {
                for (Art a : ArtList.allArt) {
                    if ((!(a.getOwner().toLowerCase().trim().equals(current_user.toLowerCase().trim()))) &&
                            (!(a.getStatus().toLowerCase().trim().equals("borrowed")))) {
                        selectedArt.add(a);
                    }
                }
            } catch (NullPointerException e) {
                selectedArt = new ArrayList<>();
            }
        } else {
            // Otherwise it also has keywords from the user input within the descritption.
            try {
                for (Art a : ArtList.allArt) {
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


        adapter = new ArrayAdapter<Art>(ViewSearchActivity.this,
                R.layout.list_item, selectedArt);
        oldAllArtListings.setAdapter(adapter);
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



}
