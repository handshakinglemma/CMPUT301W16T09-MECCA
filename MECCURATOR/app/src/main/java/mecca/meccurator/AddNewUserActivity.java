package mecca.meccurator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
 * Displays a form for the user to fill out to create a new account.
 * Saves new user to users.
 */
public class AddNewUserActivity extends AppCompatActivity {

    /* file that users are saved in */
    protected static final String USERFILE = "userfile.sav";
    private ArrayList<User> userList;

    /* initialize all input fields */
    private EditText inputUsername;
    private EditText inputEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);

        inputUsername = (EditText) findViewById(R.id.enterUsername);
        inputEmail = (EditText) findViewById(R.id.enterEmail);

        final Button saveUser = (Button) findViewById(R.id.saveUser);

        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser(v);

            }
        });
        loadFromFile();
    }

    public void saveUser(View view) {

        inputUsername = (EditText) findViewById(R.id.enterUsername);
        inputEmail = (EditText) findViewById(R.id.enterEmail);

        /* get text from EditText */
        String username = inputUsername.getText().toString();
        String email = inputEmail.getText().toString();

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

        if(username.equals("")){
            inputUsername.setError("Empty Field!");
            return;
        }

        if(email.equals("")){
            inputEmail.setError("Empty Field!");
            return;
        }

        // get user list and check if the username has been taken
        ElasticsearchUserController.GetUserListTask getUserListTask = new ElasticsearchUserController.GetUserListTask();
        getUserListTask.execute("");

        try {
            userList = new ArrayList<>();
            userList.addAll(getUserListTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for(User user: userList) {

            /* check if username already exists; don't allow duplicate user names */
            if (username.equals(user.getUsername())) {
                inputUsername.setError("Username taken!");
                return;
            }
        }

        // Everything checks out, add username and email to userlist
        User newestUser = new User(username, email, null, "false");

        ElasticsearchUserController.AddUserTask addUserTask = new ElasticsearchUserController.AddUserTask();
        addUserTask.execute(newestUser);
        try {
            UserList.users.add(newestUser);
        } catch (NullPointerException e) {
            UserList users = new UserList();
            UserList.users.add(newestUser);
        }

        saveInFile();
        finish();


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

    /* Code from https://github.com/joshua2ua/lonelyTwitter */
    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(AddNewUserActivity.USERFILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            /* took from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.htmlon Jan-20-2016 */

            Type listType = new TypeToken<ArrayList<User>>() {
            }.getType();
            UserList.users = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            UserList.users = new ArrayList<>();

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
