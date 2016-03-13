package mecca.meccurator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    public String username;
    public String currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        // Get username from ViewLoginActivity
        Intent intentRcvEdit = getIntent();
        username = intentRcvEdit.getStringExtra("username");
        currentuser = username;

        // Idea of how to have button with changing text from here:
        // https://stackoverflow.com/questions/16806376/how-to-change-the-text-of-button-using-a-variable-or-return-value-from-function
        Button button = (Button)findViewById(R.id.username2);
        button.setText(username);



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
        startActivity(intent);
    }

    // Click to view my profile
    public void ViewMyProfileButton(View view) {
        Intent intent = new Intent(this, EditUserActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void ViewMyBidsButton(View view) {
        Intent intent = new Intent(this, ViewMyBidsActivity.class);
        startActivity(intent);

    }

    public void ViewMyNotificationsButton(View view){
        Intent intent = new Intent(this, ViewNotificationsActivity.class);
        startActivity(intent);
    }

    public void ViewMyBorrowedItemsButton(View view){
        Intent intent = new Intent(this, BorrowedItemsActivity.class);
        startActivity(intent);
    }
}
