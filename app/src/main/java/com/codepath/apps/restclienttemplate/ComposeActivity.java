package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.Constants;

public class ComposeActivity extends AppCompatActivity {

    //client

    com.codepath.apps.restclienttemplate.TwitterClient client = com.codepath.apps.restclienttemplate.TwitterApp.getRestClient(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
    }

    public void onSubmit(View v) {
        EditText etComposeTweet = (EditText) findViewById(R.id.etComposeTweet);
        //Button sendButton = (Button) findViewById(R.id.sendButton); TODO - remove?
        // Prepare data intent
        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("composeTweet", etComposeTweet.getText().toString());

        // Activity finished ok, return the data to client. Then, in Timeline Activity onActivityResult, just call to repopulate the itmeline

        client.post(apiUrl, etComposeTweet.getText().toString(), ); //getapiUrl, ;
        setResult(Constants.RESULT_OK, data); // set result code and bundle data for response

        finish();
        // closes the activity, pass data to parent
        //onActivityResult should be called automatically here

    }
}

