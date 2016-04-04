package mecca.meccurator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Adapter for Art.
 * Created by cjvenhuis on 2016-04-01.
 */
public class ArtAdapter extends ArrayAdapter<Art> {
    LayoutInflater inflater;
    Context context;
    String currentUser;
    public ArtAdapter(Context context, ArrayList<Art> collection, String currentUser) {
        super(context, 0, collection);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.currentUser = currentUser;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Art art = getItem(position);
        // Set variables
        String title = art.getTitle();
        String description = art.getDescription();
        String status = art.getStatus();
        String ownerName = art.getOwner();
        String empty = " ";

        String borrowerName;
        String bidRate = new String();

        // Check if an existing view is being reused, otherwise inflate the view.
        if (convertView == null) {
            convertView = inflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView artTitle = (TextView) convertView.findViewById(R.id.artTitle);
        TextView artDescription = (TextView) convertView.findViewById(R.id.artDescription);
        TextView artPerson = (TextView) convertView.findViewById(R.id.artPerson);
        TextView artStatus = (TextView) convertView.findViewById(R.id.artStatus);

        // Populate the data into the template view using the data object
        artTitle.setText(title);
        artDescription.setText(description);

        // If ViewSearchActivity, list_item shows owner and status
        if (context instanceof ViewSearchActivity) {
            // Show status and ownerName
            artStatus.setText(status);
            artPerson.setText(ownerName);
        }

        // If ViewMyListings, list_item shows available/bidded status or borrower
        if (context instanceof ViewMyListingsActivity) {
            if (status.toLowerCase().equals("borrowed")) {
                // Set borrower
                borrowerName = art.getBorrower();
                // no status, show borrowerName
                artStatus.setText(empty);
                artPerson.setText(borrowerName);
            } else {
                // show status, no name
                artStatus.setText(status);
                artPerson.setText(empty);
            }
        }

        // If ViewMyBids, list_item shows owner and your bid
        if (context instanceof ViewMyBidsActivity) {
            // Find your bid
            ArrayList<Bid> bids = art.getBids();
            for (Bid bid : bids) {
                if (bid.getBidder().equals(currentUser)) {
                    bidRate = "$" + String.valueOf(bid.getRate());
                }
            }
            // show bidRate and ownerName
            artStatus.setText(bidRate);
            artPerson.setText(ownerName);
        }

        // If BorrowedItems, list_item shows owner
        if (context instanceof BorrowedItemsActivity) {
            // no status, show ownerName
            artStatus.setText(empty);
            artPerson.setText(ownerName);
        }

        return convertView;
    }
}
