package mecca.meccurator;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * ElasticsearchArtController
 * Used to facilitate interaction between the local artlist and the server artlist
 * NOTE: use command: curl -XDELETE 'http://cmput301.softwareprocess.es:8080/cats/art' to clear server
 * TODO: Could implement a special return value that returns some error code ie. -1 when task Fails
 */
public class ElasticsearchArtController {

    // Changed hardcoded strings to variables
    private static final String serverIndex = "cats";
    private static final String type = "art";
    private static JestDroidClient client;


    // Input: Search string
    // Output: Returns all items in the server: allServerArt
    // Sometimes the server does not update as quickly as we would like (when items are added/deleted)
    public static class GetArtListTask extends AsyncTask<String,Void,ArrayList<Art>> {

        // Returns allServerArt
        @Override
        protected ArrayList<Art> doInBackground(String... params) {
            verifyConfig();

            ArrayList<Art> allServerArt = new ArrayList<Art>();
            String search_string;

            // TODO ? customize this so that it can be used by ViewSearchActivity (ie. add more if statements)
            if(params[0].equals("")) {
                // This always happens when this is called from ViewMyListingsActivity
                search_string = "{\"from\":0,\"size\":10000}";
            } else {
                // The following gets the top 10000 art listings matching the string passed in
                search_string = "{\"from\":0,\"size\":10000,\"query\":{\"match\":{\"message\":\"" + params[0] + "\"}}}";
            }

            Search search = new Search.Builder(search_string).addIndex(serverIndex).addType(type).build();
            try {
                SearchResult execute = client.execute(search);
                if (execute.isSucceeded()) {
                    List<Art> foundArt = execute.getSourceAsObjectList(Art.class);
                    allServerArt.addAll(foundArt);
                    Log.i("TODO", "Search was SUCCESSFUL");
                } else {
                    Log.i("TODO", "Search FAILED");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return allServerArt;
        }
    }

    // Input: newestArt
    // Output: Returns art_id generated by jest for the art item that was just added to the server
    public static class AddArtTask extends AsyncTask<Art,Void,String> {

        @Override
        protected String doInBackground(Art... params) {
            verifyConfig();

            String art_id = ""; // Initialize

            for(Art art : params) {
                Index index = new Index.Builder(art).index(serverIndex).type(type).build();

                try {
                    DocumentResult execute = client.execute(index);
                    if(execute.isSucceeded()) {
                        art.setId(execute.getId());
                        art_id = art.getId();
                        Log.i("TODO", "Add art was SUCCESSFUL");

                    } else {
                        Log.e("TODO", "Add art FAILED");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return art_id;
        }
    }

    // Input: art_to_delete
    // Output: None
    // Use of Delete.Builder inspired by: https://github.com/searchbox-io/Jest/blob/master/jest/README.md
    // Use of params inspired by: https://stackoverflow.com/questions/13939202/how-to-use-asynctask-class-input-params
    public static class RemoveArtTask extends AsyncTask<Art,Void,Void> {

        @Override
        protected Void doInBackground(Art... params) {
            verifyConfig();

            Art art_to_delete = params[0];

                try {
                    DocumentResult execute = client.execute(new Delete.Builder(art_to_delete.getId()).index(serverIndex).type(type).build());
                    if(execute.isSucceeded()) {
                        Log.i("TODO", "Delete art was SUCCESSFUL");
                    } else {
                        Log.e("TODO", "Delete art FAILED");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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

