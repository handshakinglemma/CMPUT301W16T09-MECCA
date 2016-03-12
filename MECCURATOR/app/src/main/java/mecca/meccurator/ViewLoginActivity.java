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

public class ViewLoginActivity extends AppCompatActivity {
    private static final String USERFILENAME = "userfile.sav";
    private EditText username;
    private String username_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_login);

        //username = (EditText)findViewById(R.id.username);
        //username_text = username.getText().toString();
    }

    // Code from https://github.com/joshua2ua/lonelyTwitter
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        loadFromFile();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //username = (EditText)findViewById(R.id.username);
        //username_text = username.getText().toString();

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
            // TODO Auto-generated catch block
            UserList.users = new ArrayList<User>();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    // Click to create a new listing
    public void CreateNewUserButton(View view) {

        Intent intent = new Intent(this, AddNewUserActivity.class);
        startActivity(intent);
    }

    // Click to view my profile
    public void ViewHomeActivity(View view) {

        username = (EditText)findViewById(R.id.username);
        username_text = username.getText().toString();

        boolean match = false;


        for(User user: UserList.users){

            Context context = getApplicationContext();
            //CharSequence saved = user.getUsername();
            CharSequence saved = username_text;
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, saved, duration).show();

            if (username_text.equals(user.getUsername())){

                match = true;
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("username", username.getText().toString());
                startActivity(intent);
            }
        }

        if (match == false){
            // TODO Incorrect username, relaunch activity
        }


    }


}
