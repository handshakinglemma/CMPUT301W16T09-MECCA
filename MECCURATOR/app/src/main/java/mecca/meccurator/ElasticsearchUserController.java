package mecca.meccurator;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by cjvenhuis on 2016-03-26.
 */
public class ElasticsearchUserController {

    // Changed hardcoded strings to variables
    private static final String INDEX = "kitties";
    private static final String TYPE = "user";
    private static JestDroidClient client;

    public static class GetUserListTask extends AsyncTask<String,Void,ArrayList<User>> {
        @Override
        protected ArrayList<User> doInBackground(String... params) {

            verifyConfig();

            ArrayList<User> allServerUsers = new ArrayList<User>();
            String search_string;

            search_string="{\"from\":0,\"size\":10000}";

            Search search = new Search.Builder(search_string).addIndex(INDEX).addType(TYPE).build();
            try

            {
                SearchResult execute = client.execute(search);
                if (execute.isSucceeded()) {
                    List<User> foundUsers = execute.getSourceAsObjectList(User.class);
                    allServerUsers.addAll(foundUsers);
                    Log.i("TODO", "Search was SUCCESSFUL");
                } else {
                    Log.i("TODO", "Search FAILED");
                }
            }

            catch(IOException e) {
                e.printStackTrace();
            }
            return allServerUsers;
        }
    }

    public static class AddUserTask extends AsyncTask<User,Void,ArrayList<User>> {
        @Override
        protected ArrayList<User> doInBackground(User... params) {
            verifyConfig();

            for(User user : params) {
                Index index = new Index.Builder(user).index(INDEX).type(TYPE).build();

                try {
                    DocumentResult execute = client.execute(index);
                    if (execute.isSucceeded()) {
                        user.setId(execute.getId());
                    } else {
                        Log.i("TODO", "Insert User failed");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    // If no client, add one
    // Code from escamera lab
    public static void verifyConfig() {
        if(client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }

}
