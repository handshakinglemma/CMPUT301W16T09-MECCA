package mecca.meccurator;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Displays the item description and allows user to place a bid on this item.
 * Saves the bid to the item's bid list. Notifies the item owner of the new bid. Updates the item's
 * status from available to bidded.
 */
public class AddNewBidActivity extends AppCompatActivity {

    boolean connected;
    private int pos;
    private String current_user;
    private String owner;
    private BidList bids;
    //ArtList myBids; //= new ArtList();
    int ownerpos;
    protected ArrayList<User> userList;

    protected static final String TRUE = "true";


    //also make an add notification method


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_bid2);
        Intent newbid = getIntent();
        pos = newbid.getIntExtra("position", 0);
        current_user = newbid.getStringExtra("current_user");
        owner = newbid.getStringExtra("owner");

        ElasticsearchUserController.GetUserListTask getUserListTask = new ElasticsearchUserController.GetUserListTask();
        getUserListTask.execute();

        try {
            userList = new ArrayList<User>();
            userList.addAll(getUserListTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ownerpos = 0;

        for(User user: userList){
            if (owner.equals(user.getUsername())){
                break;
            }
            ++ownerpos;
        }

        Button userProfile = (Button) findViewById(R.id.view_owner);
        Button saveBid = (Button) findViewById(R.id.item_bids);

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUserProfileButton(v);
            }
        });
        saveBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBidButton(v);
            }
        });
    }

    @Override
    protected void onStart() {
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

        loadValues();

    }

    private void loadValues() {

        /* get values to be edited and fill boxes */
        TextView inputTitle = (TextView) findViewById(R.id.enterTitle);
        TextView inputArtist = (TextView) findViewById(R.id.enterArtist);
        TextView inputDescription = (TextView) findViewById(R.id.enterDescription);
        TextView inputMinPrice = (TextView) findViewById(R.id.enterMinPrice);
        TextView inputLengthDimensions = (TextView) findViewById(R.id.enterLengthDimensions);
        TextView inputWidthDimensions = (TextView) findViewById(R.id.enterWidthDimensions);
        TextView inputOwner = (TextView) findViewById(R.id.owner);


        /* append data into EditText box */
        Art art = ArtList.allArt.get(pos);
        inputArtist.setText(art.getArtist());
        inputDescription.setText(art.getDescription());
        inputTitle.setText(art.getTitle());
        inputMinPrice.setText(Float.toString(art.getMinprice()));
        inputLengthDimensions.setText(art.getLength());
        inputWidthDimensions.setText(art.getWidth());
        inputOwner.setText(art.getOwner());
    }

    private void saveInFile() {
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

    // click to view owner info
    public void ViewUserProfileButton(View view) {
        Intent intent = new Intent(this, ViewUserProfileActivity.class);
        intent.putExtra("owner", owner);
        startActivity(intent);
    }

    // TODO set notification flag on item and add notification to owner's notification list
    public void saveBidButton(View view){

        Art art =  ArtList.allArt.get(pos);

        // Delete item from server
        ElasticsearchArtController.RemoveArtTask removeArtTask = new ElasticsearchArtController.RemoveArtTask();
        removeArtTask.execute(art);

        //Delete user from server
        ElasticsearchUserController.RemoveUserTask removeUserTask = new ElasticsearchUserController.RemoveUserTask();
        removeUserTask.execute(UserList.users.get(ownerpos));


        //get user data
        ArrayList<String> ownerNotifs = UserList.users.get(ownerpos).getAllNotifications();
        String ownerEmail = UserList.users.get(ownerpos).getEmail();


        float rate;
        String status;
        //USE CURRENT USER

        EditText inputRate = (EditText) findViewById(R.id.enterRate);

        try {
            rate = Float.parseFloat(inputRate.getText().toString());
        } catch(NumberFormatException wrong){
            inputRate.setError("Invalid Input...");
            return;
        }

        if (rate <= art.getMinprice()){
            inputRate.setError("Bid too low...");
            return;
        }


        Bid bid = new Bid(current_user, rate);
        bids = art.getBidLists();
        bids.addBid(bid);
        art.setBids(bids);

        //after this SAVE to ur own bids
        //and send a notif to the owner
        //and change item status to bidded if not already done
        status = "bidded";

        art.setStatus(status);
        //also change the minimum bidding price
        art.setMinprice(rate);

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

        //Set user again w/ new notif
        String addNotif = String.format("New bid placed on %s by %s", art.getTitle(), current_user);
        ownerNotifs.add(0,addNotif);
        User addOwner = new User(owner,ownerEmail,ownerNotifs, TRUE);

        ElasticsearchUserController.AddUserTask addUserTask = new ElasticsearchUserController.AddUserTask();
        addUserTask.execute(addOwner);

        UserList.users.remove(ownerpos);

        UserList.users.add(ownerpos, addOwner);

        /* toast message */
        // new func: displayToast or something?
        Context context = getApplicationContext();
        CharSequence saved = "Bid Placed!";
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, saved, duration).show();

        /* end add activity */
        saveInFile();
        finish();
    }

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