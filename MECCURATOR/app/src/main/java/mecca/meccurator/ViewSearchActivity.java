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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ViewSearchActivity extends AppCompatActivity {

    private static final String FILENAME = "file.sav";
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

        oldAllArtListings = (ListView) findViewById(R.id.oldAllArtListings);

        // Update this!!!!
        // Want when item is clicked to see the view item page where you can add a bid.
        // I think this is the AddNewBidActivity
        oldAllArtListings.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {

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
        // Selected art is only those items that are not owner by current user and that have
        // status == "available"
        for (Art a: ArtList.allArt) {
            if ((!(a.getOwner().toLowerCase().trim().equals(current_user.toLowerCase().trim()))) &&
                    (!(a.getStatus().toLowerCase().trim().equals("borrowed")))){
                selectedArt.add(a);
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
