package mecca.meccurator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
<<<<<<< HEAD
import android.widget.Button;
=======
import android.view.Window;
import android.view.WindowManager;
>>>>>>> Marcy

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

<<<<<<< HEAD
        Button viewArtListingButton = (Button) findViewById(R.id.viewMyArtListings);

        viewArtListingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);

                Intent intent = new Intent(HomeActivity.this, ViewArtListingsActivity.class);
                startActivity(intent);
            }
        });


=======
>>>>>>> dcdd937c509c227d9db7c4ac63de3d3cc089b7bd
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
}
