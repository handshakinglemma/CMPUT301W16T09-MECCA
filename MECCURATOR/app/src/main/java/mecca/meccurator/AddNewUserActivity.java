package mecca.meccurator;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

/**
 * Displays a form for the user to fill out to create a new account
 * Saves new user to users
 */
public class AddNewUserActivity extends AppCompatActivity {

    protected static final String USERFILE = "userfile.sav";

    /* initialize all input fields */
    private EditText inputUsername;
    private EditText inputEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);

        inputUsername = (EditText) findViewById(R.id.enterUsername);
        inputEmail = (EditText) findViewById(R.id.enterEmail);

        loadFromFile();
    }

    public void saveUser(View view) {

        inputUsername = (EditText) findViewById(R.id.enterUsername);
        inputEmail = (EditText) findViewById(R.id.enterEmail);

        /* get text from EditText */
        String username = inputUsername.getText().toString();
        String email = inputEmail.getText().toString();

        /* add new user to list of users */
        User newestUser = new User(username, email);

        // The following commented out code is for for testing only
        // You can use it to see how many users exist inside users (the user list)
        //String usernames = "";
        //for(User user: UserList.users) {
        //    usernames = usernames + " : " + user.getUsername();
        //}

        //Context testcontext = getApplicationContext();
        //CharSequence testsaved = usernames;
        //int testduration = Toast.LENGTH_LONG;
        //Toast.makeText(testcontext, testsaved, testduration).show();

        boolean user_bool = false;

        for(User user: UserList.users) {

            // If user alread exists in users
            if (username.equals(user.getUsername())) {
                user_bool = true;
                Context context = getApplicationContext();
                CharSequence saved = "Username already exists";
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(context, saved, duration).show();
                // Clear input boxes
                inputUsername.setText("", EditText.BufferType.EDITABLE);
                inputEmail.setText("", EditText.BufferType.EDITABLE);
                saveInFile();
            }
        }

        if (username.equals("")) {
            user_bool = true;
            Context context = getApplicationContext();
            CharSequence blank_mssg = "Username cannot be blank";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, blank_mssg, duration).show();
            // Clear input boxes
            inputUsername.setText("", EditText.BufferType.EDITABLE);
            inputEmail.setText("", EditText.BufferType.EDITABLE);
            saveInFile();
        }
        
        // If user does not already exist in users
        if (user_bool == false) {
            try {
                UserList.users.add(newestUser);
            } catch (NullPointerException e) {
                UserList users = new UserList();
                UserList.users.add(newestUser);
            }
            saveInFile();
            finish();
        }
    }

    protected void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(USERFILE, 0);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(UserList.users, out);
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
}
