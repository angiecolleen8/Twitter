package com.codepath.apps.Twitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.Constants;
import com.codepath.apps.Twitter.models.Tweet;
import com.codepath.apps.restclienttemplate.ComposeActivity;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    //variables
    TwitterClient client; //our Twitter client
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline); //sets the main layout as the timeline activity

        client = TwitterApp.getRestClient(this); //says params aren't right, needs Context?

        //find RecyclerView
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        //initiate our dataset, the arraylist
        tweets = new ArrayList<>();
        //construct the adapter from datasource
        tweetAdapter = new TweetAdapter(tweets);
        //setup the Recycler view - this includes setting up the Layout manager and using the adapter
        rvTweets.setAdapter(tweetAdapter);
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        //set onClickListener for Menu Item
        populateTimeline();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onComposeAction(MenuItem menuItem) {
        launchComposeView();
    }

    public void launchComposeView() {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(this, ComposeActivity.class); //"I intend to go from this activity to ComposeActivity
        startActivityForResult(i, Constants.REQUEST_CODE_COMPOSE); // brings up the second activity
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        //onActivityResult - method that takes you back to timeline, scrolls to top, fills top position with your new tweet
        if (resultCode == Constants.RESULT_OK && requestCode == Constants.REQUEST_CODE_COMPOSE) {
            //unwrap tweet from parsels - use same name as when you parcelled it
            Tweet newTweet = Parcels.unwrap(data.getParcelableExtra("composedTweet"));
            //add new tweet to arrayList
            tweets.add(0, newTweet);
            //notify adapter that you inserted a new tweet item at top of list
            tweetAdapter.notifyItemInserted(0);
            //scroll to top of recycler view
            rvTweets.scrollToPosition(0);
            // Toast the name to display temporarily on screen
            Toast.makeText(this, "Successfully composed tweet", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Compose failed", Toast.LENGTH_LONG).show();
        }
    }

    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Twitter client", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("Twitter client", response.toString());
                //iterate through JSONArray
                //deserialize the JSONObject for each entry
                for (int i = 0; i < response.length(); i++) {
                    //convert each object to a Tweet model
                    Tweet tweet = null;
                    try {
                        tweet = Tweet.fromJSON(response.getJSONObject(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //add that Tweet model to our data source
                    tweets.add(tweet);
                    //notify adapter that we have added an item
                    tweetAdapter.notifyItemInserted(tweets.size() - 1); //the last item added will be at the last index of the arrayList
                    //-- With a recyclerView, we just notify the adapter of the things that have changed. With ListView, we have to notify the adapter that the entire data set has changed.
                    //i.e., with a ListView, I think you would use notifyDataSetChanged
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("Twitter client", responseString);
                throwable.printStackTrace();
            }

            //compiler was complaining about @Override notation, so I took it out
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable, JSONArray errorResponse) { //What is the difference between this onFailure and the one below? I think one of the parameters should be different
                Log.d("Twitter client", errorResponse.toString());
                throwable.printStackTrace();
            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable, JSONObject errorResponse) {
                Log.d("Twitter client", errorResponse.toString());
                throwable.printStackTrace();
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("Twitter client", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }
}
