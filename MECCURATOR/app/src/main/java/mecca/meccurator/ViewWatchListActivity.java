package mecca.meccurator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.app.PendingIntent.getActivity;

public class ViewWatchListActivity extends AppCompatActivity {

    private ArrayList<User> userList;
    protected String current_user;
    private ListView oldWatchList;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> watchList = new ArrayList<>();
    private static final String USEREDITFILE = "userfile.sav";
    private int userpos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_watch_list);

        Intent intentRcvEdit = getIntent();
        current_user = intentRcvEdit.getStringExtra("current_user");

        oldWatchList = (ListView) findViewById(R.id.oldWatchList);

        ElasticsearchUserController.GetUserListTask getUserListTask = new ElasticsearchUserController.GetUserListTask();
        getUserListTask.execute();

        try {
            userList = new ArrayList<User>();
            userList.addAll(getUserListTask.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        userpos = 0;

        for(User user: userList){
            if (current_user.equals(user.getUsername())){
                break;
            }
            ++userpos;
        }

        watchList = UserList.users.get(userpos).getWatchList();


        Button save = (Button) findViewById(R.id.saveArtist);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(v);

            }
        });



        oldWatchList.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                watchList.remove(position);
                adapter.notifyDataSetChanged();
                /* toast message */
                // new func: displayToast or something?
                Context context = getApplicationContext();
                CharSequence saved = "Artist Deleted!";
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(context, saved, duration).show();

                return true;
            }

        });



    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        adapter = new ArrayAdapter<String>(ViewWatchListActivity.this,
                R.layout.bid_item, watchList);
        oldWatchList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    private void save(View v) {


        EditText newArtist = (EditText) findViewById(R.id.editArtist);

        String artist = newArtist.getText().toString();

        User user = UserList.users.get(userpos);

        ElasticsearchUserController.RemoveUserTask removeUserTask = new ElasticsearchUserController.RemoveUserTask();
        removeUserTask.execute(user);

        //get user data
        ArrayList<String> ownerNotifs = user.getAllNotifications();
        String ownerFlag = user.getNotificationFlag();
        String email = user.getEmail();
        String username = user.getUsername();

        if(artist.equals("")){
            newArtist.setError("Empty Field!");
            return;
        }

        watchList.add(artist);

        /* add new entry to list of items */
        User newestUser = new User(username, email, ownerNotifs, ownerFlag, watchList);

        ElasticsearchUserController.AddUserTask addUserTask = new ElasticsearchUserController.AddUserTask();
        addUserTask.execute(newestUser);

        UserList.users.remove(userpos);

        UserList.users.add(userpos, newestUser);

        /* toast message */
        // new func: displayToast or something?
        Context context = getApplicationContext();
        CharSequence saved = "Artist Saved!";
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, saved, duration).show();
        newArtist.getText().clear();

        /* end add activity */
        saveInFile();
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
