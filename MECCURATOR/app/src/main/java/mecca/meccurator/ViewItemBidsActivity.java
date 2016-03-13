package mecca.meccurator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

// View bids on a particular item
// Only owner is able to get to this point since the "View Item Bids" is only visible when
// owner is editing their item?
public class ViewItemBidsActivity extends AppCompatActivity {

    private ListView itemBidListings;
    private ArrayAdapter<Bid> adapter;
    private ArrayList<Bid> oldItemBids = new ArrayList<>();
    private String current_user;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item_bids);


        itemBidListings = (ListView) findViewById(R.id.itemBidListings);

        Intent intent = getIntent();
        pos = intent.getIntExtra("position", 0);
        current_user = intent.getStringExtra("current_user");
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        loadFromFile();

        /* just a test
        Default = new User("default", "88");
        pos = 1;

        //also add bid to myBids eg. the borrowers
        ArtList myBids = new ArtList();
        Art myBid = ArtList.allArt.get(pos);
        myBids.addItem(myBid);
        Default.myBidsPlaced(myBids, ArtList.allArt.get(pos).getOwner());

        oldItemBids = ArtList.allArt.get(0).getBids(); */


        oldItemBids = ArtList.allArt.get(pos).getBids();

        adapter = new ArrayAdapter<Bid>(ViewItemBidsActivity.this,
                R.layout.list_item, oldItemBids);
        itemBidListings.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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
