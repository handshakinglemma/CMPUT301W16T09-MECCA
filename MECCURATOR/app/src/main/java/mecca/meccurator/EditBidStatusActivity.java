package mecca.meccurator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

    int pos; //item position
    int bidpos; //bidlist pos
    String current_user;

    private User bidderProfile;
    private ArrayList<User> allServerUsers = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadValues();

    }

    //so on screen u see the bidder + rate
    //and u have an accept decline buttons

    public void acceptBidButton(View view){

        //change variable names
        Art art = ArtList.allArt.get(pos);

        // Delete item from server
        ElasticsearchArtController.RemoveArtTask removeArtTask = new ElasticsearchArtController.RemoveArtTask();
        removeArtTask.execute(art);

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
        finish();
    }

    public void setBidderProfile(ArrayList<User> userList) {
        for (User u : userList) {
            if( u.getUsername().equals(ArtList.allArt.get(pos).getBidLists().getBid(bidpos).getBidder())) {
                bidderProfile = u;
                break;
            }
        }
    }

    protected void loadValues(){
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
