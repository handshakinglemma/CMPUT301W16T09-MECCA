package mecca.meccurator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
    int pos;
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        pos = 0;
        Log.i("userList", String.valueOf(userList));
        for(User user: userList){
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

        //limit size of notification list to keep removing old elements
        while (notificationList.size() > 10){
            notificationList.remove(notificationList.size() - 1);
        }

        ElasticsearchUserController.RemoveUserTask removeUserTask = new ElasticsearchUserController.RemoveUserTask();
        removeUserTask.execute(UserList.users.get(pos));

        //set off notification flag
        String email = UserList.users.get(pos).getEmail();
        String flag = "false";
        ArrayList<String> notifs = UserList.users.get(pos).getAllNotifications();
        ArrayList<String> watchlist = UserList.users.get(pos).getWatchList();
        

         /* add new entry to list of items */
        User newestUser = new User(UserList.users.get(pos).getUsername(), email, notifs, flag, watchlist);

        ElasticsearchUserController.AddUserTask addUserTask = new ElasticsearchUserController.AddUserTask();
        addUserTask.execute(newestUser);

        UserList.users.remove(pos);

        UserList.users.add(pos, newestUser);

        Button viewWatchList = (Button) findViewById(R.id.watchList);
        viewWatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewWatchList(v);

            }
        });


    }

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


}