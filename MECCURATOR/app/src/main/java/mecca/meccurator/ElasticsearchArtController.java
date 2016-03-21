package mecca.meccurator;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DeleteByQuery;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by emcdonald on 19/03/16.
 */

// Some queries : http://cmput301.softwareprocess.es:8080/testing/art/_search?q="owner"="Alanna"
    // returns all art owned by Alanna

public class ElasticsearchArtController {
    private static JestDroidClient client;

    public static class GetArtListTask extends AsyncTask<String,Void,ArrayList<Art>> {

        @Override
        protected ArrayList<Art> doInBackground(String... params) {
            verifyConfig();

            // Hold (eventually) the tweets that we get back from Elasticsearch
            ArrayList<Art> allArt = new ArrayList<Art>();

            // NOTE: A HUGE ASSUMPTION IS ABOUT TO BE MADE!
            // Assume that only one string is passed in.

            // The following gets the top "10000" tweets
            //String search_string = "{\"from\":0,\"size\":10000}";

            // The following searches for the top 10 tweets matching the string passed in (NOTE: HUGE ASSUMPTION PREVIOUSLY)
            //String search_string = "{\"query\":{\"match\":{\"message\":\"" + params[0] + "\"}}}";

            // The following orders the results by date
            //String search_string = "{\"sort\": { \"date\": { \"order\": \"desc\" }}}";

            /* NEW! */
            String search_string;
            if(params[0] == "") {
                search_string = "{\"from\":0,\"size\":10000}";
                //search_string = "{\"sort\": { \"date\": { \"order\": \"desc\" }}}";
            } else {
                // The following gets the top 10000 tweets matching the string passed in
                search_string = "{\"from\":0,\"size\":10000,\"query\":{\"match\":{\"message\":\"" + params[0] + "\"}}}";
            }

            Search search = new Search.Builder(search_string).addIndex("cats").addType("art").build();
            try {
                SearchResult execute = client.execute(search);
                if(execute.isSucceeded()) {
                    List<Art> foundTweets = execute.getSourceAsObjectList(Art.class);
                    allArt.addAll(foundTweets);
                    Log.i("TODO", "Search was SUCCESSFUL, do something!!!!");
                    int size = allArt.size();
                    Log.i("size is", String.valueOf(size));
                } else {
                    Log.i("TODO", "Search was unsuccessful, do something!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return allArt;
        }
    }

    public static class AddArtTask extends AsyncTask<Art,Void,Void> {

        @Override
        protected Void doInBackground(Art... params) {
            verifyConfig();

            for(Art art : params) {
                Index index = new Index.Builder(art).index("cats").type("art").build();

                try {
                    DocumentResult execute = client.execute(index);
                    if(execute.isSucceeded()) {
                        art.setId(execute.getId());
                    } else {
                        Log.e("TODO", "Our insert of art failed, oh no!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    // Code inspired by:
    ///https://github.com/searchbox-io/Jest/blob/master/jest/README.md
    public static class RemoveArtTask extends AsyncTask<Art,Void,Void> {

        @Override
        protected Void doInBackground(Art... params) {
            verifyConfig();

            // https://stackoverflow.com/questions/13939202/how-to-use-asynctask-class-input-params
            Art art_to_delete = params[0];

                try {
                    DocumentResult execute = client.execute(new Delete.Builder(art_to_delete.getId()).index("cats").type("art").build());;
                    if(execute.isSucceeded()) {
                        Log.i("TODO", "Delete was SUCCESSFUL?, do something!!!!");
                    } else {
                        Log.e("TODO", "Our insert of art failed, oh no!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            return null;
        }

    }

    // Code inspired by:
    ///https://github.com/searchbox-io/Jest/blob/master/jest/README.md
    public static class RemoveAllArtTask extends AsyncTask<Art,Void,Void> {

        @Override
        protected Void doInBackground(Art... params) {
            verifyConfig();

            for(Art art : params) {

                try {
                    DocumentResult execute = client.execute(new Delete.Builder(art.getId()).index("cats").type("art").build());;
                    if(execute.isSucceeded()) {
                        Log.i("TODO", "Delete was SUCCESSFUL?, do something!!!!");
                    } else {
                        Log.e("TODO", "Our insert of art failed, oh no!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }

    // If no client, add one
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