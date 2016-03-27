package mecca.meccurator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import java.sql.Blob;
import java.util.ArrayList;

/**
 * Displays editable form for user to update their profile
 */
public class EditUserActivity extends AppCompatActivity {

    public String current_user;
    private String email;
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

        loadFromFile();

        pos = 0;

        for(User user: UserList.users){
            if (current_user.equals(user.getUsername())){
                break;
            }
            ++pos;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadValues();
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

    protected void loadValues() {
        /* get values to be edited and fill boxes */
        EditText inputEmail = (EditText) findViewById(R.id.enterEmail);

        /* append data into EditText box */

        inputEmail.append(UserList.users.get(pos).getEmail());
    }

    protected void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(USEREDITFILE, 0);

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

    public void saveUser(View view){

        EditText inputEmail = (EditText) findViewById(R.id.enterEmail);

        /* get text from EditText */
        String email = inputEmail.getText().toString();

        /* add new entry to list of items */
        User newestUser = new User(current_user, email);

        ElasticsearchUserController.RemoveUserTask removeUserTask = new ElasticsearchUserController.RemoveUserTask();
        removeUserTask.execute(UserList.users.get(pos));
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
}
