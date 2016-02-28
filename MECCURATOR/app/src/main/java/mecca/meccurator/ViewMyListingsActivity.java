package mecca.meccurator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ViewMyListingsActivity extends AppCompatActivity {

    private ArtList itemlistinglist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_listings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // Click to create a new listing
    public void CreateNewListingButton(View view) {
        Intent intent = new Intent(this, ViewItemActivity.class);
        startActivity(intent);
    }

    public ArtList getItemlistinglist() {
        return itemlistinglist;
    }

    public void setItemlistinglist(ArtList itemlistinglist) {
        this.itemlistinglist = itemlistinglist;
    }

    public void seeListingItem() {

    }

    public void viewBorrowedItemsOnly() {

    }

    public void viewBidOnItemsOnly() {

    }

}
