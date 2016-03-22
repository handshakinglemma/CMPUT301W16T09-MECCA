package mecca.meccurator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

public class EditBidStatusActivity extends AppCompatActivity {

    int pos; //item position
    int bidpos; //bidlist pos
    String current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bid_status);

        //get intent from ItemBidsActivity w/ username and position
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadValues();

    }

    //so on screen u see the bidder + rate
    //and u have an accept decline buttons

    protected void acceptBidButton(){

        //change variable names
        Art editart = ArtList.allArt.get(pos);
        Bid currentbid = editart.getBids().get(bidpos);

        //change status to borrowed and change borrower
        editart.setStatus("borrowed");
        editart.setBorrower(currentbid.getBidder());

        //use method to decline rest of the bids
        declineAllBids();



    }

    protected void loadValues(){
        //load the bidder and rate into textviews
    }

    protected void declineBidButton(){

        //removes that bid from the BidList
        ArtList.allArt.get(pos).getBids().remove(bidpos);


    }

    protected void declineAllBids(){

        //empty the BidList
        ArtList.allArt.get(pos).setBids(null);

    }

}
