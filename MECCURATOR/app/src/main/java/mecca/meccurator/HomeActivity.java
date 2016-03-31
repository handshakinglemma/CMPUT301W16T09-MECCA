package mecca.meccurator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Displays user's home page.
 * From home, user may access search,their own listings, their notifications, the items they have
 * borrowed, the items they have place bids on, or they can view/edit their profile
 */
public class HomeActivity extends AppCompatActivity {

    public String current_user;
    public String keyword;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get username from ViewLoginActivity
        Intent intentRcvEdit = getIntent();
        current_user = intentRcvEdit.getStringExtra("current_user");


        // Idea of how to have button with changing text from here:
        // https://stackoverflow.com/questions/16806376/how-to-change-the-text-of-button-using-a-variable-or-return-value-from-function
        Button button = (Button)findViewById(R.id.username2);
        button.setText(current_user);

        Button listings = (Button) findViewById(R.id.ViewListingsButtonID);
        Button notifications = (Button) findViewById(R.id.ViewNotificationsButtonID);
        Button borrowed = (Button) findViewById(R.id.ViewBorrowedButtonID);
        Button bids = (Button) findViewById(R.id.ViewBidsButtonID);

        listings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {ViewListingsButton(v);
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewMyNotificationsButton(v);
            }
        });
        borrowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewMyBorrowedItemsButton(v);
            }
        });
        bids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewMyBidsButton(v);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Click to view art listings
    public void ViewListingsButton(View view) {
        Intent intent = new Intent(this, ViewMyListingsActivity.class);
        intent.putExtra("current_user", current_user);
        startActivity(intent);
    }

    // Click to view my profile
    public void ViewMyProfileButton(View view) {
        Intent intent = new Intent(this, EditUserActivity.class);
        intent.putExtra("current_user", current_user);
        startActivity(intent);
    }

    public void ViewMyBidsButton(View view) {
        Intent intent = new Intent(this, ViewMyBidsActivity.class);
        intent.putExtra("current_user", current_user);
        startActivity(intent);

    }

    public void ViewMyNotificationsButton(View view){
        Intent intent = new Intent(this, ViewNotificationsActivity.class);
        intent.putExtra("current_user", current_user);
        startActivity(intent);
    }

    public void ViewMyBorrowedItemsButton(View view){
        Intent intent = new Intent(this, BorrowedItemsActivity.class);
        intent.putExtra("current_user", current_user);
        startActivity(intent);
    }

    public void ViewSearchActivity(View view){
        search = (EditText) findViewById(R.id.editText2);
        keyword = search.getText().toString();

        Intent intent = new Intent(this, ViewSearchActivity.class);
        intent.putExtra("current_user", current_user);
        intent.putExtra("keyword", keyword);
        startActivity(intent);
    }

    public void logOut(View view){
        Intent intent = new Intent(this, ViewLoginActivity.class);
        startActivity(intent);
        finish(); // This destroys the HomeActivity
    }
}
