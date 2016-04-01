package mecca.meccurator;

import android.app.Activity;
import android.content.Context;
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
 * Created by cjvenhuis on 2016-04-01.
 */
public class ArtAdapter extends ArrayAdapter<Art> {
    LayoutInflater inflater;
    Context context;
    public ArtAdapter(Context context, ArrayList<Art> collection) {
        super(context, 0, collection);
        inflater = LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Art art = getItem(position);
        String title = art.getTitle();
        String description = art.getDescription();
        String status = art.getStatus();
        String ownerName = art.getOwner();
        String borrowerName = art.getBorrower();
        // Bid
        String empty = " ";

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

        String identifyContext;

        identifyContext = String.valueOf(context).replaceAll("\\W", " ");
        ArrayList<String> contextWords = new ArrayList(Arrays.asList(identifyContext.split(" ")));
        for( String s : contextWords) {
            if (s.equals("ViewSearchActivity")) {
                artStatus.setText(status);
                artPerson.setText(ownerName);
            }

            if (s.equals("ViewMyListingsActivity")) {
                if (status.toLowerCase().equals("borrowed")) {
                    artStatus.setText(empty);
                    artPerson.setText(borrowerName);
                } else {
                    artStatus.setText(status);
                    artPerson.setText(empty);
                }
            }

            if (s.equals("ViewMyBidsActivity")) {
                artStatus.setText("bid");
                artPerson.setText(ownerName);
            }

            if (s.equals("BorrowedItemsActivity")) {
                artStatus.setText(empty);
                artPerson.setText(ownerName);
            }
        }

        return convertView;
    }
}
