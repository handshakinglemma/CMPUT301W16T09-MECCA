package mecca.meccurator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Displays user's home page.
 * From home, user may access search,their own listings, their notifications, the items they have
 * borrowed, the items they have place bids on, or they can view/edit their profile
 */
public class HomeActivity extends AppCompatActivity {

    protected static final String ARTFILE = "artfile.sav";

    public String current_user;
    public String keyword;
    private EditText search;
    private int pos;
    boolean connected = false;

    private ArrayList<Art> allServerArt = new ArrayList<Art>();

    protected ArrayList<User> userList;
    protected ArrayList<Art> allArt;

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
        Button profile = (Button) findViewById(R.id.username2);
        Button search = (Button) findViewById(R.id.ViewSearchButtonID);
        Button logOut = (Button) findViewById(R.id.logOut);

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
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewMyProfileButton(v);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewSearchActivity(v);
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutButton(v);
            }
        });

        ElasticsearchUserController.GetUserListTask getUserListTask = new ElasticsearchUserController.GetUserListTask();
        getUserListTask.execute();

        try {
            userList = new ArrayList<User>();
            userList.addAll(getUserListTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        pos = 0;

        for(User user: userList){
            if (current_user.equals(user.getUsername())){
                break;
            }
            ++pos;
        }

        if(userList.get(pos).getNotificationFlag().equals("true")){
            notifications.setBackgroundColor(Color.MAGENTA);
        } else{
            notifications.setBackgroundResource(R.color.buttonColor);
        }

        // Pull all server art
        boolean success = false;
        while (!success){
            success = pullAllServerArt();
        }

        // Save all server art locally
        saveInFile();
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkIfConnected();
        Log.i("TODO", String.valueOf(ArtList.offLineArt));

        String art_id = "";
        if (connected && !ArtList.offLineArt.isEmpty()) {
            Log.i("TODO", "Add offLineArt to ElasticSearch");
            for (Art art : ArtList.offLineArt) {
                ElasticsearchArtController.AddArtTask addArtTask = new ElasticsearchArtController.AddArtTask();
                addArtTask.execute(art);

                ArtList.allArt.get(ArtList.allArt.size() - (1 + ArtList.offLineArt.size())).setId(art_id);

                ArtList.offLineArt.remove(art);
            }
        }
    }

    // Checks if we are connected to the internet or not.
    private boolean checkIfConnected() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }
        return connected;
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
        Button notifications = (Button) findViewById(R.id.ViewNotificationsButtonID);
        notifications.setBackgroundResource(R.color.buttonColor);
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

    public void logOutButton(View view){
        Intent intent = new Intent(this, ViewLoginActivity.class);
        startActivity(intent);
        finish(); // This destroys the HomeActivity
    }

    public boolean pullAllServerArt() {

        // Get ALL art from server
        ElasticsearchArtController.GetArtListTask getArtListTask = new ElasticsearchArtController.GetArtListTask();
        getArtListTask.execute("");
        try {
            allServerArt = new ArrayList<Art>();
            allServerArt.addAll(getArtListTask.get());
            return true;

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            try {
                Thread.sleep(1000); // Sleep for 1 sec
                Log.i("TODO", "Sleeping for one sec");
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            return false;
        }
    }

    protected void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(ARTFILE, 0);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(allServerArt, out);
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