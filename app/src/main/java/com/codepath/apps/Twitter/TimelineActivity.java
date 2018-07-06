package com.codepath.apps.Twitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.Twitter.models.Tweet;
import com.codepath.apps.restclienttemplate.ComposeActivity;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        setContentView(R.layout.activity_timeline); //sets the main layout as the timeline activity?

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
    @Override //TODO -Commented out @Override notation in several places...is that ok? Also commented it out in TweetAdapter, onClick
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //TODO - actually, I think the onClickListener goes in the main OnCreate
        return true;
    }

    public void onComposeAction(MenuItem menuItem) {
        //need an onClickListener, so that when "compose" button is clicked, takes user to ComposeActivity
        /*menuItem.setOnMenuItemClickListener(new View.OnClickListener) {

        };*/
        //new intent to go ComposeActivity, which is in launchComposeView
        //launchComposeView();
        //call onActivityResult - need "code" to take you to
    }

    //onActivityResult - method that takes you back to timeline, scrolls to top, fills top position with your new tweet

    public void launchComposeView() {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(this, ComposeActivity.class); //change activity names
        startActivity(i); // brings up the second activity
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
