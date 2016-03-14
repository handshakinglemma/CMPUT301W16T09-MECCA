package mecca.meccurator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Displays the item description and allows user to place a bid on this item
 * Saves the bid to the item's bid list, notifies the item owner of the new bid, updates the item's
 * status from available to bidded.
 */
public class AddNewBidActivity extends AppCompatActivity {

    int pos;
    String current_user;
    BidList bids;
    //ArtList myBids; //= new ArtList();
    //int userpos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_bid2);
        Intent newbid = getIntent();
        pos = newbid.getIntExtra("position", 0);
        current_user = newbid.getStringExtra("current_user");

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadValues();

    }

    protected void loadValues() {

        /* get values to be edited and fill boxes */
        EditText inputTitle = (EditText) findViewById(R.id.enterTitle);
        EditText inputArtist = (EditText) findViewById(R.id.enterArtist);
        EditText inputDescription = (EditText) findViewById(R.id.enterDescription);
        EditText inputMinPrice = (EditText) findViewById(R.id.enterMinPrice);
        EditText inputLengthDimensions = (EditText) findViewById(R.id.enterLengthDimensions);
        EditText inputWidthDimensions = (EditText) findViewById(R.id.enterWidthDimensions);

        /* append data into EditText box */
        inputArtist.append(ArtList.allArt.get(pos).getArtist());
        inputDescription.append(ArtList.allArt.get(pos).getDescription());
        inputTitle.append(ArtList.allArt.get(pos).getTitle());
        inputMinPrice.append(Float.toString(ArtList.allArt.get(pos).getMinprice()));
        inputLengthDimensions.append(ArtList.allArt.get(pos).getLength());
        inputWidthDimensions.append(ArtList.allArt.get(pos).getWidth());
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

    public void saveBidButton(View view){


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


        Bid bid = new Bid(current_user, rate);

        bids = ArtList.allArt.get(pos).getBidLists();

        bids.addBid(bid);

        ArtList.allArt.get(pos).setBids(bids);

        //after this SAVE to ur own bids
        //and send a notif to the owner
        //and change item status to bidded if not already done
        status = "bidded";

        ArtList.allArt.get(pos).setStatus(status);
        //also change the minimum bidding price
        ArtList.allArt.get(pos).setMinprice(rate);


        //also add bid to myBids eg. the borrowers

        //userpos = 0;

        //for(User user: UserList.users){
        //    if (current_user.equals(user.getUsername())){
        //        break;
        //    }
        //    ++userpos;
        //}

        //Art myBid = ArtList.allArt.get(pos);

        //myBids = UserList.users.get(userpos).getMyBids();
        //myBids.addItem(myBid);
        //UserList.users.get(userpos).myBidsPlaced(myBids, ArtList.allArt.get(pos).getOwner());


        /* toast message */
        // new func: displayToast or something?
        Context context = getApplicationContext();
        CharSequence saved = "Bid Placed!";
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, saved, duration).show();

        /* end add activity */
        saveInFile();
        //saveUserInFile();
        finish();
    }

    protected void saveUserInFile() {
        try {
            FileOutputStream fos = openFileOutput(AddNewUserActivity.USERFILE, 0);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(UserList.users, out);
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

}
