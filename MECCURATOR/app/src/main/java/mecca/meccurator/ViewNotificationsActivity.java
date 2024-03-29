package mecca.meccurator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Displays a list of user notifications such as:
 * user item has recieved a new bid, and
 * user's bid on an item has been accepted.
 */
public class ViewNotificationsActivity extends AppCompatActivity {

    protected ArrayList<User> userList;
    protected ListView oldNotificationListing;
    protected ArrayAdapter<String> adapter;
    public String current_user;
    protected String email;
    protected static final String USEREDITFILE = "userfile.sav";
    private int pos;
    protected ArrayList<String> notificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notifications);

        oldNotificationListing = (ListView) findViewById(R.id.oldNotifications);

        // Get username from ViewLoginActivity
        Intent intent = getIntent();
        current_user = intent.getStringExtra("current_user");

        ElasticsearchUserController.GetUserListTask getUserListTask = new ElasticsearchUserController.GetUserListTask();
        getUserListTask.execute();

        try {
            userList = new ArrayList<User>();
            userList.addAll(getUserListTask.get());
            UserList.users = userList;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        pos = 0;
        Log.i("userList", String.valueOf(userList));
        for(User user: UserList.users){
            if (current_user.equals(user.getUsername())){
                break;
            }
            ++pos;
        }


        Log.i("pos", String.valueOf(pos));

        Log.i("user", String.valueOf(UserList.users.get(pos)));

        if(UserList.users.get(pos).getAllNotifications() == null){
            notificationList = new ArrayList<String>();
        } else{
            notificationList = UserList.users.get(pos).getAllNotifications();
        }

        // Limit size of notification list to keep removing old elements
        while (notificationList.size() > 10){
            notificationList.remove(notificationList.size() - 1);

        }

        // Set off notification flag
        String email = UserList.users.get(pos).getEmail();
        String flag = "false";
        ArrayList<String> notifs = UserList.users.get(pos).getAllNotifications();
        ArrayList<String> watchlist = UserList.users.get(pos).getWatchList();

         /* add new entry to list of items */
        User newestUser = new User(UserList.users.get(pos).getUsername(), email, notifs, flag, watchlist);

        // Remove user
        ElasticsearchUserController.RemoveUserTask removeUserTask = new ElasticsearchUserController.RemoveUserTask();
        removeUserTask.execute(UserList.users.get(pos));

        UserList.users.remove(pos);

        // Add edited user
        ElasticsearchUserController.AddUserTask addUserTask = new ElasticsearchUserController.AddUserTask();
        addUserTask.execute(newestUser);

        UserList.users.add(pos, newestUser);

        // Set Notification button
        Button viewWatchList = (Button) findViewById(R.id.watchList);
        viewWatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewWatchList(v);

            }
        });

        saveInFile();
    }

    /**
     * Takes the user to ViewWatchListActivity
     * @param v
     */
    private void viewWatchList(View v) {
        Intent intent = new Intent(this, ViewWatchListActivity.class);
        intent.putExtra("current_user", current_user);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Update adapter
        adapter = new ArrayAdapter<String>(ViewNotificationsActivity.this,
                R.layout.notification_item, notificationList);

        oldNotificationListing.setAdapter(adapter);

        adapter.notifyDataSetChanged();
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


}