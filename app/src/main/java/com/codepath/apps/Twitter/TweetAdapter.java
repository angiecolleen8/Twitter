package com.codepath.apps.Twitter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.Twitter.models.GlideApp;
import com.codepath.apps.Twitter.models.Tweet;
import com.codepath.apps.restclienttemplate.ComposeActivity;
import com.codepath.apps.restclienttemplate.R;

import org.parceler.Parcels;

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
        holder.tvTimeStamp.setText(tweet.getRelativeTimeAgo(tweet.createdAt));//needs another param?

        //load images using Glide
        GlideApp.with(context)
                .load(tweet.user.profileImageURL) //imageURL is profileImageURL
                .into(holder.ivProfileImage);

    }

    public int getItemCount() {
        return mTweets.size(); //size of list or the number of tweets
    }

    //create ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder {     //Should I add "implements View.OnClickListener"? //No static?

        //declare the views that are inside our item_tweet
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvTimeStamp;

        public ViewHolder(View itemView) {
            super(itemView); //Why do we need to call the super-constructor?

            //do the findbyid lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            //itemView.setOnClickListener(this); //added this
        }

        //@Override
        public void onClick(View view) {
            //gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the tweet at the position, this is why class can't be static
                Tweet tweet = mTweets.get(position); //Why is it that when I take away "static" on the ViewHolder Class that mTweets is suddenly recognized?
                // create intent for new activity
                Intent intent = new Intent(context, ComposeActivity.class);
                // serialize movie using parceller, short name is key
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                // show the activity
                context.startActivity(intent);
            }
        }
    }
}
