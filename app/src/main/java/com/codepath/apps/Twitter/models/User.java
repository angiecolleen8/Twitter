package com.codepath.apps.Twitter.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {

    //list attributes
    public String name;
    public long uid;
    public String screenName;
    public String profileImageURL;

    public User () {}

    //deserialize the JSON
    public static User fromJSON(JSONObject json) throws JSONException {

        User user = new User();

        //extract and fill values from JSON
        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.screenName = json.getString("screen_name");
        user.profileImageURL = json.getString("profile_image_url");
        return user;
    }
}
