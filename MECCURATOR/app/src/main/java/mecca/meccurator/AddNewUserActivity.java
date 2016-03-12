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

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class AddNewUserActivity extends AppCompatActivity {
    protected static final String USERFILE = "userfile.sav";

    /* initialize all input fields */
    private EditText inputUsername;
    private EditText inputName;
    private EditText inputEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);

        inputUsername = (EditText) findViewById(R.id.enterUsername);
        inputName = (EditText) findViewById(R.id.enterName);
        inputEmail = (EditText) findViewById(R.id.enterEmail);

    }

    public void saveUser(View view) {

        /* get text from EditText */
        String username = inputUsername.getText().toString();
        String name = inputName.getText().toString();
        String email = inputEmail.getText().toString();

        /* add new entry to list of items */
        //TODO: add owner and other attributes by pulling from lists also PHOTO
        User newestUser = new User(username, name, email);

        //so this should be artwork.add(newestArt), when artwork is instantiated publicly
        UserList.users = new ArrayList<User>();

        try {
            UserList.users.add(newestUser);
        } catch (NullPointerException e) {
            UserList users = new UserList();
            UserList.users.add(newestUser);
        }

                /* toast message */
        // new func: displayToast or something?
        //Context context = getApplicationContext();
        //CharSequence saved = "User Saved!";
        //int duration = Toast.LENGTH_SHORT;
        //Toast.makeText(context, saved, duration).show();

        boolean boool = UserList.users.contains(newestUser);
        String bool_str = "False";
        if (boool == true){
            bool_str = "True";
        }

        Context context = getApplicationContext();
        CharSequence saved = bool_str;
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, saved, duration).show();

        /* end add activity */
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

}
