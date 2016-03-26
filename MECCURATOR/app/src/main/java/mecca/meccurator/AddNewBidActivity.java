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
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.ExecutionException;

/**
 * Displays the item description and allows user to place a bid on this item.
 * Saves the bid to the item's bid list. Notifies the item owner of the new bid. Updates the item's
 * status from available to bidded.
 */
public class AddNewBidActivity extends AppCompatActivity {

    int pos;
    String current_user;
    BidList bids;
    //ArtList myBids; //= new ArtList();
    //int userpos;]


    //also make an add notification method
    

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

    // TODO set notification flag on item and add notification to owner's notification list
    public void saveBidButton(View view){

        // Delete item from server
        ElasticsearchArtController.RemoveArtTask removeArtTask = new ElasticsearchArtController.RemoveArtTask();
        removeArtTask.execute(ArtList.allArt.get(pos));


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

        if (rate <= ArtList.allArt.get(pos).getMinprice()){
            inputRate.setError("Bid too low...");
            return;
        }

        Art art =  ArtList.allArt.get(pos);
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
}
