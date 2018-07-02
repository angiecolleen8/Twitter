package com.codepath.apps.Twitter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client; //our Twitter client

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this); //says params aren't right, needs Context?
        populateTimeline();
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

        });
    }
}
