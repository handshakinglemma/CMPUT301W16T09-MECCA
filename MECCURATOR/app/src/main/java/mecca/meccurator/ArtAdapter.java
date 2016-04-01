package mecca.meccurator;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cjvenhuis on 2016-04-01.
 */
public class ArtAdapter extends ArrayAdapter<Art> {
    public ArtAdapter(Context context, ArrayList<Art> collection) {
        super(context, 0, collection);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Art art = getItem(position);
        String title = art.getTitle();
        String description = art.getDescription();
        String status = art.getStatus();
        String ownerName = art.getOwner();
        //String borrowerName = art.getBorrower();
        // Bid

        // Check if an existing view is being reused, otherwise inflate the view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView artTitle = (TextView) convertView.findViewById(R.id.artTitle);
        TextView artDescription = (TextView) convertView.findViewById(R.id.artDescription);

        TextView artPerson = (TextView) convertView.findViewById(R.id.artPerson);
        TextView artStatus = (TextView) convertView.findViewById(R.id.artStatus);

        // Populate the data into the template view using the data object
        artTitle.setText(title);
        artDescription.setText(description);
        artStatus.setText(status);
        artPerson.setText(ownerName);

        return convertView;
    }
}
