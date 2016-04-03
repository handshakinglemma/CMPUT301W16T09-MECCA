package mecca.meccurator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
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


public class EditBidStatusActivity extends AppCompatActivity {

    protected int pos; //item position
    int bidpos; //bidlist pos
    String current_user;
    public String address;

    private User bidderProfile;
    private ArrayList<User> allServerUsers = new ArrayList<User>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("TODO", "OnCreate");
        setContentView(R.layout.activity_edit_bid_status);

        Intent intent = getIntent();
        pos = intent.getIntExtra("item_position", 0);
        bidpos = intent.getIntExtra("bid_position", 0);
        current_user = intent.getStringExtra("current_user");

        String bidder = ArtList.allArt.get(pos).getBidLists().getBid(bidpos).getBidder();
        Float bidOffer = ArtList.allArt.get(pos).getBidLists().getBid(bidpos).getRate();

        // pull all users from the server
        boolean success = false;
        while (!success){
            success = pullAllServerUsers();
        }

        saveInFile();
        loadFromFile();
        // /get intent from ItemBidsActivity w/ username and position

        setBidderProfile(allServerUsers);

        //loadValues();



        Button acceptButton = (Button) findViewById(R.id.acceptBidButton);
        Button declineButton = (Button) findViewById(R.id.declineBidButton);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptBidButton(v);
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineBidButton(v);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("TODO", "OnStart");
        Art art = ArtList.allArt.get(pos);
        if ( art.getLatLng()!= null){
            addItemToServer();
            Log.i("TODO", "Call home intent ");
            //after accepting the bid, go to edit item activity
            Intent edit = new Intent(getApplicationContext(), HomeActivity.class);
            edit.putExtra("position", pos);
            edit.putExtra("current_user", current_user);
            //finish();
            startActivity(edit);
        } else {
            loadValues();
        }

    }

    //so on screen u see the bidder + rate
    //and u have an accept decline buttons

    public void acceptBidButton(View view){

        Log.i("TODO", "Accept bid");

        //change variable names
        Art art = ArtList.allArt.get(pos);

        // Delete item from server
        ElasticsearchArtController.RemoveArtTask removeArtTask = new ElasticsearchArtController.RemoveArtTask();
        removeArtTask.execute(art);

        Log.i("TODO", "Call map intent ");
        // after accepting bid, go to place picker activity
        Intent map = new Intent(getApplicationContext(), PlacePickerActivity.class);
        map.putExtra("position", pos);
        //startActivityForResult(map, 2);
        startActivity(map);
    }

    public void addItemToServer(){
        //change variable names
        Art art = ArtList.allArt.get(pos);

        Bid currentbid = art.getBids().get(bidpos);

        //change status to borrowed and change borrower
        art.setStatus("borrowed");
        String borrower = currentbid.getBidder();
        art.setBorrower(borrower);

        //also need to delete bids from other users

        //use method to decline rest of the bids
        declineAllBids();

        // Add the art to Elasticsearch
        ElasticsearchArtController.AddArtTask addArtTask = new ElasticsearchArtController.AddArtTask();
        addArtTask.execute(art);

        String art_id = ""; // Initialize
        try {
            art_id = addArtTask.get();
            Log.i("adds art_id is", art_id);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        art.setId(art_id); // set id locally
        saveInFile();




    }

    public void setBidderProfile(ArrayList<User> userList) {
        Log.i("TODO", "bidderProfile function");
        Log.i("userList size", String.valueOf(userList.size()));
        Log.i("userList", String.valueOf(userList));
        for (User u : userList) {
            Log.i("user", String.valueOf(u));
            if( u.getUsername().equals(ArtList.allArt.get(pos).getBidLists().getBid(bidpos).getBidder())) {
                bidderProfile = u;
                Log.i("TODO", "match bidderProfile");
                break;
            }
        }
    }

    protected void loadValues(){
        Log.i("TODO", "loadValues");
        //load the bidder and rate into textviews
        TextView bidderUsername = (TextView) findViewById(R.id.bidder_username);
        TextView bidderEmail = (TextView) findViewById(R.id.bidder_email);
        TextView bidRate = (TextView) findViewById(R.id.bid_offer);

        bidderUsername.setText(bidderProfile.getUsername());
        bidderEmail.setText(bidderProfile.getEmail());
        bidRate.setText(Float.toString(ArtList.allArt.get(pos).getBidLists().getBid(bidpos).getRate()));
    }

    public void declineBidButton(View view){

        //change variable names
        Art art = ArtList.allArt.get(pos);

        // Delete item from server
        ElasticsearchArtController.RemoveArtTask removeArtTask = new ElasticsearchArtController.RemoveArtTask();
        removeArtTask.execute(art);

        //removes that bid from the BidList
        art.getBids().remove(bidpos);

        if(art.getBids() == null || art.getBids().isEmpty()){
            art.setStatus("available");
        } else{
            ArrayList<Bid> artbids;
            float minprice = art.getBids().get(0).getRate();

            artbids = art.getBids();
            for (int i = 0; i < artbids.size(); i ++) {
                if (artbids.get(i).getRate() > minprice) {
                    minprice = artbids.get(i).getRate();
                }
            }

            art.setMinprice(minprice);

        }

        // Add the art to Elasticsearch
        ElasticsearchArtController.AddArtTask addArtTask = new ElasticsearchArtController.AddArtTask();
        addArtTask.execute(art);

        String art_id = ""; // Initialize
        try {
            art_id = addArtTask.get();
            Log.i("adds art_id is", art_id);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        art.setId(art_id); // set id locally

        saveInFile();
        finish();
    }

    //put method into bidlist class
    public void declineAllBids(){

        //empty the BidList
        ArtList.allArt.get(pos).setBids(null);

    }

    protected void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(AddNewItemActivity.ARTFILE, 0);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(ArtList.allArt, out);
            out.flush();
            fos.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    // Code from https://github.com/joshua2ua/lonelyTwitter
    private void loadFromFile() {
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

        }
    }

    public boolean pullAllServerUsers() {

        // Get ALL art from server
        ElasticsearchUserController.GetUserListTask getUserListTask = new ElasticsearchUserController.GetUserListTask();
        getUserListTask.execute("");
        try {
            allServerUsers = new ArrayList<User>();
            allServerUsers.addAll(getUserListTask.get());
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
}
