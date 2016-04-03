package mecca.meccurator;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * Displays the login bar. User can choose to login or signup. User must login to proceed to home
 */
public class ViewLoginActivity extends AppCompatActivity {

    private static final String USERFILENAME = "userfile.sav";

    boolean connected;
    private EditText username;
    private String username_text;

    private ArrayList<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_login);

        Button mEmailSignInButton = (Button) findViewById(R.id.newUser);
        Button viewHomeActivityButton = (Button) findViewById(R.id.login);

        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddNewUserActivity.class);
                startActivity(intent);
            }
        });

        viewHomeActivityButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Save username input as username_text
                EditText inputUser = (EditText)findViewById(R.id.username);
                String username = inputUser.getText().toString();

                boolean match = false;

                isConnected();
                if(!connected) {
                    // If username is incorrect display error message then clear the input
                    Context context = getApplicationContext();
                    CharSequence saved = "Offline";
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(context, saved, duration).show();

                    //EditText inputUser = (EditText) findViewById(R.id.username);
                    inputUser.setText("", EditText.BufferType.EDITABLE);
                } else {
                    ElasticsearchUserController.GetUserListTask getUserListTask = new ElasticsearchUserController.GetUserListTask();
                    getUserListTask.execute("");
                    try {
                        userList = new ArrayList<>();
                        userList.addAll(getUserListTask.get());
                        UserList.users = userList;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    for (User user : UserList.users) {
                        if (username.equals(user.getUsername())) {
                            match = true;

                            Intent intent = new Intent(view.getContext(), HomeActivity.class);
                            intent.putExtra("current_user", username);
                            startActivity(intent);
                        }
                    }


                    if (!match) {  // Simplified boolean expression
                        // If username is incorrect display error message then clear the input
                        Context context = getApplicationContext();
                        CharSequence saved = "Invalid Username!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(context, saved, duration).show();

                        //EditText inputUser = (EditText) findViewById(R.id.username);
                        inputUser.setText("", EditText.BufferType.EDITABLE);
                    }
                }
            }
        });
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ViewLoginActivity.class);
        startActivity(intent);
        finish(); // This destroys the HomeActivity
        //what it should do on back
    }

    public void isConnected() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }
    }

    /*// Create a new user
    public void CreateNewUserButton(View view) {
        Intent intent = new Intent(this, AddNewUserActivity.class);
        startActivity(intent);
    }*/

    /*// View my home activity
    public void ViewHomeActivity(View view) {

        // Save username input as username_text
        EditText inputUser = (EditText)findViewById(R.id.username);
        String username = inputUser.getText().toString();

        boolean match = false;

        ElasticsearchUserController.GetUserListTask getUserListTask = new ElasticsearchUserController.GetUserListTask();
        getUserListTask.execute("");
        try {
            userList = new ArrayList<>();
            userList.addAll(getUserListTask.get());
            UserList.users = userList;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for(User user: UserList.users){
            if (username.equals(user.getUsername())){
                match = true;

                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("current_user", username);
                startActivity(intent);
            }
        }

        if (!match){  // Simplified boolean expression
            // If username is incorrect display error message then clear the input
            Context context = getApplicationContext();
            CharSequence saved = "Invalid Username!";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, saved, duration).show();

            //EditText inputUser = (EditText) findViewById(R.id.username);
            inputUser.setText("", EditText.BufferType.EDITABLE);
        }
    }*/
}
