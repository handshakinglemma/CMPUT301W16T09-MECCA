package mecca.meccurator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class EditBidStatusActivity extends AppCompatActivity {

    int pos; //item position
    int bidpos; //bidlist pos
    String current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bid_status);

        Intent intent = getIntent();
        pos = intent.getIntExtra("item_position", 0);
        bidpos = intent.getIntExtra("bid_position", 0);
        current_user = intent.getStringExtra("current_user");

        //get intent from ItemBidsActivity w/ username and position
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
        Art editart = ArtList.allArt.get(pos);
        Bid currentbid = editart.getBids().get(bidpos);

        //change status to borrowed and change borrower
        editart.setStatus("borrowed");
        String borrower = currentbid.getBidder();
        editart.setBorrower(borrower);
        editart.setBids(null);

        //also need to delete bids from other users






        //use method to decline rest of the bids
        declineAllBids();
        saveInFile();
        finish();



    }

    protected void loadValues(){
        //load the bidder and rate into textviews
    }

    public void declineBidButton(View view){

        //removes that bid from the BidList
        ArtList.allArt.get(pos).getBids().remove(bidpos);
        finish();
        saveInFile();


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

}
