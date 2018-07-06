package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.Constants;
import com.codepath.apps.Twitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    //variables
    com.codepath.apps.restclienttemplate.TwitterClient client;
    EditText etComposeTweet;
    TextView tvComposeTweet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = com.codepath.apps.restclienttemplate.TwitterApp.getRestClient(this);
        etComposeTweet = (EditText) findViewById(R.id.etComposeTweet);
        tvComposeTweet = (TextView) findViewById(R.id.tvComposeTweet);
        //TextWatcher for running remaining char count in
        final TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                tvComposeTweet.setText(String.valueOf(140 - s.length()));
            }

            public void afterTextChanged(Editable s) {
            }
        };

        etComposeTweet.addTextChangedListener(mTextEditorWatcher); //set TextWatcher (for running char count) here

    }

    public void onSubmit(View v) {
        //Button sendButton = (Button) findViewById(R.id.sendButton); TODO - remove?
        final String composedTweet = etComposeTweet.getText().toString(); //get text from ComposedTweet. //TODO - start here. Getting null pointer
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() { //TODO -difference between JSONHttpResponseHandler and AsyncHttpResponseHandler - declaring and instantiating as different things?

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) { //successfully received Twitter JSONObject
                Toast.makeText(getApplicationContext(), composedTweet, Toast.LENGTH_LONG).show();
                try { //what to do once you have the response object, do this
                    Tweet tweet = Tweet.fromJSON(response); //make new tweet with fields from JSON object
                    Intent intent = new Intent();
                    intent.putExtra("composedTweet", Parcels.wrap(tweet)); //
                    setResult(Constants.RESULT_OK, intent);
                    finish();
                    // closes the activity, pass data to parent
                    //onActivityResult should be called automatically here
                } catch (JSONException e) { //failed to read JSONOBject, though object was received
                    e.printStackTrace();
                }
            }


            /*@Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable errorResponse) {   //failed to get Twitter JSONObject
                logError("Failed getting configuration", errorResponse, true);
            }*/
        };

        // Pass relevant data back as a result
        client.sendTweet(composedTweet,handler);
        // Activity finished ok, return the data to client. Then, in Timeline Activity onActivityResult, just call to repopulate the timeline
    }
}


