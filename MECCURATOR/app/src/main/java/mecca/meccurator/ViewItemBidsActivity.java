package mecca.meccurator;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Displays bids place on the item. Note: if no bids have been placed, nothing is displayed.
 */
public class ViewItemBidsActivity extends AppCompatActivity {

    boolean connected;
    private ListView itemBidListings;
    private ArrayAdapter<Bid> adapter;
    private ArrayList<Bid> oldItemBids = new ArrayList<>();
    private String current_user;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item_bids);

        itemBidListings = (ListView) findViewById(R.id.itemBidListings);

        Intent intent = getIntent();
        pos = intent.getIntExtra("position", 0);
        current_user = intent.getStringExtra("current_user");

        oldItemBids = ArtList.allArt.get(pos).getBids();

        itemBidListings.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int meta_position, long id) {

                Intent bidStatus = new Intent(getApplicationContext(), EditBidStatusActivity.class);
                bidStatus.putExtra("item_position", pos);
                bidStatus.putExtra("bid_position", meta_position);
                bidStatus.putExtra("current_user", current_user);
                finish();
                startActivity(bidStatus);

            }
        });
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        isConnected();
        if (!connected) {
            /* toast message */
            Context context = getApplicationContext();
            CharSequence saved = "Offline";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, saved, duration).show();
            finish();
        }

        loadFromFile();

        adapter = new ArrayAdapter<Bid>(ViewItemBidsActivity.this,
                R.layout.bid_item, oldItemBids);
        itemBidListings.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(oldItemBids == null){
            adapter.notifyDataSetChanged();
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onPause() {
        super.onPause();
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

    /**
     * isConnected checks whether we are connected to the internet through
     * wife or mobile networks
     */
    public void isConnected() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }
    }

}
