package mecca.meccurator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import java.util.List;

/**
 * Displays the items that the user has placed bids on
 */
public class ViewMyBidsActivity extends AppCompatActivity {

    private ListView oldBidsPlaced;
    private ArrayAdapter<Art> adapter;
    private ArrayList<Art> oldBids = new ArrayList<>();
    int pos;
    int userpos;
    String current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_bids);

        oldBidsPlaced = (ListView) findViewById(R.id.oldBidsPlaced);

        Intent intent = getIntent();
        current_user = intent.getStringExtra("current_user");

        //userpos = -1;
        //userpos = 0;

        //for(User user: UserList.users){
        //    if (current_user.equals(user.getUsername())){
        //        break;
        //    }
        //    else {
        //        ++userpos;
        //    }
        //}


        //oldBids = UserList.users.get(userpos).getMyBidsPlaced();

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        loadFromFile();
        loadUserFromFile();

        adapter = new ArrayAdapter<Art>(ViewMyBidsActivity.this,
                R.layout.list_item, oldBids);
        oldBidsPlaced.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();

        oldBids = new ArrayList<>();
        ArrayList<Bid> artbids;

        for(Art art: ArtList.allArt){
            artbids = art.getBids();
            for (Bid bid: artbids) {
                if (current_user.equals(bid.getBidder())) {
                    oldBids.add(art);
                    Context context = getApplicationContext();
                    CharSequence saved = bid.getBidder();
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(context, saved, duration).show();
                    break;
                } else {
                    Context context = getApplicationContext();
                    CharSequence saved = "Nothing!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(context, saved, duration).show();
                }
            }
        }

        adapter = new ArrayAdapter<Art>(ViewMyBidsActivity.this,
                R.layout.list_item, oldBids);
        oldBidsPlaced.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void loadUserFromFile() {
        try {
            FileInputStream fis = openFileInput(AddNewUserActivity.USERFILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            // took from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.htmlon Jan-20-2016

            Type listType = new TypeToken<ArrayList<User>>() {
            }.getType();
            UserList.users = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            UserList.users = new ArrayList<User>();

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
            // TODO Auto-generated catch block
            ArtList.allArt = new ArrayList<Art>();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }



}
