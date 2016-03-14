package mecca.meccurator;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.sql.Blob;


/** This activity is what someone will see when they view someone else's profile.
 * If user != username, then start this activity. Otherwise, start EditUserActivity.
 */
public class ViewUserActivity extends AppCompatActivity {

    private String username;
    private String name;
    private String phoneNumber;
    private Blob photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);
    }

}
