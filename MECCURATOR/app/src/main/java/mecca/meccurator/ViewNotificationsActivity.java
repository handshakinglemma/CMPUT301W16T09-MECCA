package mecca.meccurator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
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

        for(User user: userList){
            if (current_user.equals(user.getUsername())){
                break;
            }
            ++pos;
        }

        if(UserList.users.get(pos).getAllNotifications() == null){
            notificationList = new ArrayList<String>();
        } else{
            notificationList = UserList.users.get(pos).getAllNotifications();
        }

        ElasticsearchUserController.RemoveUserTask removeUserTask = new ElasticsearchUserController.RemoveUserTask();
        removeUserTask.execute(UserList.users.get(pos));

        //set off notification flag
        String email = UserList.users.get(pos).getEmail();
        String flag = "false";
        ArrayList<String> notifs = UserList.users.get(pos).getAllNotifications();

         /* add new entry to list of items */
        User newestUser = new User(current_user, email, notifs, flag);

        ElasticsearchUserController.AddUserTask addUserTask = new ElasticsearchUserController.AddUserTask();
        addUserTask.execute(newestUser);

        UserList.users.remove(pos);

        UserList.users.add(pos, newestUser);





    }

    @Override
    protected void onStart() {
        super.onStart();
        // Update adapter
        adapter = new ArrayAdapter<String>(ViewNotificationsActivity.this,
                R.layout.list_item, notificationList);

        oldNotificationListing.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    /*
    rn = someone places a bid, notification sent to owner
    max size = 10
    once they're looked at notification button colour changes (in oncreate)
    "Bidder placed a $10 bid on item name"
    clicking on it takes u to the accept/decline page



     */

}
