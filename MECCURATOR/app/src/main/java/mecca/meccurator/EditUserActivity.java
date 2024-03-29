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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Displays editable form for user to update their profile
 */
public class EditUserActivity extends AppCompatActivity {

    boolean connected;
    private ArrayList<User> userList;
    private String current_user;
    private static final String USEREDITFILE = "userfile.sav";
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        // Get username from ViewLoginActivity
        Intent intentRcvEdit = getIntent();
        current_user = intentRcvEdit.getStringExtra("current_user");

        TextView textview = (TextView) findViewById(R.id.username_my_profile);
        textview.setText(current_user);

        ElasticsearchUserController.GetUserListTask getUserListTask = new ElasticsearchUserController.GetUserListTask();
        getUserListTask.execute();

        try {
            userList = new ArrayList<User>();
            userList.addAll(getUserListTask.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        pos = 0;

        for(User user: userList){
            if (current_user.equals(user.getUsername())){
                break;
            }
            ++pos;
        }

        final Button saveUser = (Button) findViewById(R.id.saveUser);

        saveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser(v);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        isConnected();
        if (!connected) {
            /* toast message */
            Context context = getApplicationContext();
            CharSequence saved = "Offline";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, saved, duration).show();
            finish();
        }

        loadValues();
    }

    // NOT IMPLEMENTED AS A BUTTON
    // NOT sure if having a delete user button is useful (it's definitely not necessary)
    public void deleteUser(View view) {
        Context context = getApplicationContext();
        CharSequence text = "User Deleted!";
        int duration = Toast.LENGTH_SHORT;
        UserList.users.remove(pos);
        Toast.makeText(context, text, duration).show();
        saveInFile();
        finish();
    }

    private void loadValues() {
        /* get values to be edited and fill boxes */
        EditText inputEmail = (EditText) findViewById(R.id.enterEmail);

        /* append data into EditText box */
        inputEmail.append(UserList.users.get(pos).getEmail());
    }

    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(USEREDITFILE, 0);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(UserList.users, out);
            out.flush();
            fos.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    public void saveUser(View view){

        User user = UserList.users.get(pos);

        ElasticsearchUserController.RemoveUserTask removeUserTask = new ElasticsearchUserController.RemoveUserTask();
        removeUserTask.execute(user);

        //get user data
        ArrayList<String> ownerNotifs = user.getAllNotifications();
        String ownerFlag = user.getNotificationFlag();
        ArrayList<String> ownerWatchList = user.getWatchList();

        EditText inputEmail = (EditText) findViewById(R.id.enterEmail);

        /* get text from EditText */
        String email = inputEmail.getText().toString();

        // if blank input given, give error
        if(email.equals("")){
            inputEmail.setError("Empty Field!");
            return;
        }

        /* add new entry to list of items */
        User newestUser = new User(current_user, email, ownerNotifs, ownerFlag, ownerWatchList);

        ElasticsearchUserController.AddUserTask addUserTask = new ElasticsearchUserController.AddUserTask();
        addUserTask.execute(newestUser);

        UserList.users.remove(pos);

        UserList.users.add(pos, newestUser);

        /* toast message */
        // new func: displayToast or something?
        Context context = getApplicationContext();
        CharSequence saved = "User Saved!";
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, saved, duration).show();

        /* end add activity */
        saveInFile();
        finish();
    }

    /**
     * isConnected checks whether we are connected to the internet through
     * wife or mobile networks
     */
    public void isConnected() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }
    }
}
