package mecca.meccurator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.sql.Blob;

// Only accessible by owner of account
// "View my Profile"
public class EditUserActivity extends AppCompatActivity {
    public String username;
    private String name;
    private String phoneNumber;
    private Blob photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        // Get username from ViewLoginActivity
        Intent intentRcvEdit = getIntent();
        username = intentRcvEdit.getStringExtra("username");

        TextView textview = (TextView) findViewById(R.id.username_my_profile);
        textview.setText(username);
    }
}
