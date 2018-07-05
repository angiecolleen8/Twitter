package com.codepath.apps.Twitter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.Twitter.models.GlideApp;
import com.codepath.apps.Twitter.models.Tweet;
import com.codepath.apps.restclienttemplate.R;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    //pass in Tweets list in constructor
    private List<Tweet> mTweets;
    Context context;

    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }

    //for each row, inflate the layout and cache the references into ViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext(); //Context is a request to the OS to do something
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false); //look into this method
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder; //my original code - added the new return statement below

        /* return new ViewHolder(tweetView); */ //this is what MovieAdapter did, but I think it does the same thing
    }

    //bind values based on the position of the element in array

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get data according to position so that we know which tweet to populate
        Tweet tweet = mTweets.get(position);

        //populate the views according to this data
        holder.tvUserName.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);

        //load images using Glide
        GlideApp.with(context)
                .load(tweet.user.profileImageURL) //imageURL is profileImageURL
                .into(holder.ivProfileImage); //TODO - fix glide!
    }

    public int getItemCount() {
        return mTweets.size(); //size of list or the number of tweets
    }

    //create ViewHolder class

    public static class ViewHolder extends RecyclerView.ViewHolder {     //Should I add "implements View.OnClickListener"? //No static?

        //declare the views that are inside our item_tweet
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;

        public ViewHolder(View itemView) {
            super(itemView); //Why do we need to call the super-constructor?

            //do the findbyid lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);

            //itemView.setOnClickListener(this); //added this
        }
    }
}
