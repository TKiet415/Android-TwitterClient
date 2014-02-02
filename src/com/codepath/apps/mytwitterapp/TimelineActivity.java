package com.codepath.apps.mytwitterapp;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.mytwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends Activity {
	
	PullToRefreshListView lvTweets;
	TweetsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
		
		MyTwitterApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				// TODO Auto-generated method stub
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				
				adapter = new TweetsAdapter(getBaseContext(), tweets);
				lvTweets.setAdapter(adapter);
				//super.onSuccess(arg0);
			}
		});
		
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				fetchTimelineAsync(0);
			}
		});
		
		lvTweets.setOnScrollListener((new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
				customLoadMoreDataFromApi(totalItemsCount); 
                // or customLoadMoreDataFromApi(totalItemsCount); 
			}
        	})
        );
		
	}
	
	protected void customLoadMoreDataFromApi(int totalItemsCount) {
		// TODO Auto-generated method stub
		
		Tweet lastTweet = (Tweet) lvTweets.getItemAtPosition(totalItemsCount - 1);
		
		MyTwitterApp.getRestClient().getAdditionalHomeTimeline(lastTweet.getId(), new JsonHttpResponseHandler() {
			
			public void onSuccess(JSONArray json) {
				Log.d("DEBUG", json.toString());
				ArrayList<Tweet> tweets = Tweet.fromJson(json);
				adapter.addAll(tweets);
				adapter.notifyDataSetChanged();
			}
			
			public void onFailure(Throwable e) {
				Log.d("DEBUG", "Fetch timeline error: " + e.toString());
			}
		});
	}
	
	protected void fetchTimelineAsync(int i) {
		MyTwitterApp.getRestClient().getHomeTimeline(
				new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				Log.d("DEBUG", jsonTweets.toString());
				ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);
				
				adapter = new TweetsAdapter(getBaseContext(), tweets);
				lvTweets.setAdapter(adapter);
				lvTweets.onRefreshComplete();
//				super.onSuccess(jsonTweets); 
			}
			
		});		
	}

	public void onCompose(MenuItem mi) {
		Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
		startActivityForResult(i, 2);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		fetchTimelineAsync(0);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

}
