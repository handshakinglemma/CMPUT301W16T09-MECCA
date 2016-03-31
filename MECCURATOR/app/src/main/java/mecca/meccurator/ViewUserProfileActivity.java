package mecca.meccurator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import java.sql.Blob;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * This activity is what someone will see when they view someone else's profile.
 * If user != username, then start this activity. Otherwise, start EditUserActivity.
 * Has not been implemented yet. Is not called by anything yet.
 */
public class ViewUserProfileActivity extends AppCompatActivity {

    private static final String USERFILE = "userfile.sav";
    private String owner;
    private User ownerProfile;
    private ArrayList<User> allServerUsers = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        Intent viewInfo = getIntent();
        owner = viewInfo.getStringExtra("owner");

        // pull all users from the server
        boolean success = false;
        while (!success){
            success = pullAllServerUsers();
        }

        // save and reload it locally
        saveInFile();
        loadFromFile();

        setUserProfile(allServerUsers);

        // set displayed info
        TextView ownerName = (TextView) findViewById(R.id.owner_username);
        ownerName.setText(ownerProfile.getUsername());
        TextView ownerEmail = (TextView) findViewById(R.id.owner_email);
        ownerEmail.setText(ownerProfile.getEmail());
    }

    // Code from https://github.com/joshua2ua/lonelyTwitter
    @Override
    protected void onStart() {
        super.onStart();
        loadFromFile();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFromFile(); /// Need to update
    }

    public void setUserProfile(ArrayList<User> userList) {
        for (User u : userList) {
            if( u.getUsername().equals(owner)) {
                ownerProfile = u;
                break;
            }
        }
    }

    protected void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(USERFILE, 0);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(allServerUsers, out);
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

    // Code from https://github.com/joshua2ua/lonelyTwitter
    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(AddNewUserActivity.USERFILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            // took from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.htmlon Jan-20-2016

            Type listType = new TypeToken<ArrayList<User>>() {
            }.getType();
            UserList.users = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            UserList.users = new ArrayList<User>();

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public boolean pullAllServerUsers() {

        // Get ALL art from server
        ElasticsearchUserController.GetUserListTask getUserListTask = new ElasticsearchUserController.GetUserListTask();
        getUserListTask.execute("");
        try {
            allServerUsers = new ArrayList<User>();
            allServerUsers.addAll(getUserListTask.get());
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
}
