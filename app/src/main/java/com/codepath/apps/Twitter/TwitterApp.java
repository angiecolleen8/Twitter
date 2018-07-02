package com.codepath.apps.restclienttemplate;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.facebook.stetho.Stetho;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     RestClient client = RestApplication.getRestClient(Context context);
 *     // use client to send requests to API
 *
 */
public class TwitterApp extends Application {

    com.codepath.apps.restclienttemplate.MyDatabase myDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        // when upgrading versions, kill the original tables by using
		// fallbackToDestructiveMigration()
        myDatabase = Room.databaseBuilder(this, com.codepath.apps.restclienttemplate.MyDatabase.class,
                com.codepath.apps.restclienttemplate.MyDatabase.NAME).fallbackToDestructiveMigration().build();

        // use chrome://inspect to inspect your SQL database
        Stetho.initializeWithDefaults(this);
    }

    public static com.codepath.apps.restclienttemplate.TwitterClient getRestClient(Context context) {
        //used by all of the activities to get access to an instance of the RestClient
        return (com.codepath.apps.restclienttemplate.TwitterClient) com.codepath.apps.restclienttemplate.TwitterClient.getInstance(com.codepath.apps.restclienttemplate.TwitterClient.class, context);
    }

    public com.codepath.apps.restclienttemplate.MyDatabase getMyDatabase() {
        return myDatabase;
    }
}