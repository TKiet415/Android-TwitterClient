package com.codepath.apps.mytwitterapp.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ParseException;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Tweet")
public class Tweet extends Model {
	
	@Column(name = "body", index = true)
	public String body;
	
	@Column(name = "uid", index = true)
	public long uid;
	
	@Column(name = "favorited", index = true)
	public boolean favorited;

	@Column(name = "retweeted", index = true)
	public boolean retweeted;

	@Column(name = "user", index = true)
    public User user;

	@Column(name = "date", index = true)
    public String date;
	
	public Tweet() {
		super();
	}
	
    public Tweet(String body, long uid, boolean favorited, boolean retweeted,
			User user, String date) {
		super();
		this.body = body;
		this.uid = uid;
		this.favorited = favorited;
		this.retweeted = retweeted;
		this.user = user;
		this.date = date;
	}

	public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }
    
    public String getDate() {
    	return date;
    }
    
    public Date getCreatedAt() throws ParseException, java.text.ParseException {
		String formattedDate = getDate();

		final String TWITTER = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(TWITTER, Locale.ENGLISH);
		sdf.setLenient(true);

		Date now = sdf.parse(formattedDate);

		return now;

	}

    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
        	tweet.body = jsonObject.getString("text");
        	tweet.uid = jsonObject.getLong("id");
        	tweet.favorited = jsonObject.getBoolean("favorited");
        	tweet.retweeted = jsonObject.getBoolean("retweeted");
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
            tweet.date = jsonObject.getString("created_at");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return tweet;
    }
    
	public static List<Tweet> getAll() {
		return new Select().from(Tweet.class).execute();
	}

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = Tweet.fromJson(tweetJson);
            if (tweet != null) {
                tweets.add(tweet);
            }
        }

        return tweets;
    }
}