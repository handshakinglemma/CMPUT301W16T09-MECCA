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

    // Create a new user
    public void CreateNewUserButton(View view) {
        Intent intent = new Intent(this, AddNewUserActivity.class);
        startActivity(intent);
    }

    // View my home activity
    public void ViewHomeActivity(View view) {

        // Save username input as username_text
        username = (EditText)findViewById(R.id.username);
        username_text = username.getText().toString();

        boolean match = false;

        for(User user: UserList.users){
            if (username_text.equals(user.getUsername())){

                match = true;
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("username", username.getText().toString());
                startActivity(intent);
            }
        }

        if (match == false){
            // If username is incorrect display error message then clear the input
            Context context = getApplicationContext();
            CharSequence saved = "Invalid Username!";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, saved, duration).show();

            username = (EditText)findViewById(R.id.username);
            username.setText("", EditText.BufferType.EDITABLE);
        }
    }
}
