package com.codepath.apps.mytwitterapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.activeandroid.ActiveAndroid;
import com.codepath.apps.mytwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends Activity {

	PullToRefreshListView lvTweets;
	TweetsAdapter adapter;
	ArrayList<Tweet> tweets;
	List<Tweet> myList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

		lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);

		if (!isInternetAvailable(this)) {
			myList = Tweet.getAll();
			adapter = new TweetsAdapter(getBaseContext(), myList);
			lvTweets.setAdapter(adapter);

		} else {
			MyTwitterApp.getRestClient().getHomeTimeline(
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONArray jsonTweets) {
							tweets = Tweet.fromJson(jsonTweets);

							// Log.d("DEBUG", tweets.toString());

							adapter = new TweetsAdapter(getBaseContext(),
									tweets);
							lvTweets.setAdapter(adapter);

							ActiveAndroid.beginTransaction();
							try {
								for (Tweet tweetInstance : tweets) {
									tweetInstance.getUser().save();
									tweetInstance.save();
								}
								ActiveAndroid.setTransactionSuccessful();
							} finally {
								ActiveAndroid.endTransaction();
							}
							// super.onSuccess(arg0);
						}
					});

			lvTweets.setOnRefreshListener(new OnRefreshListener() {

				@Override
				public void onRefresh() {
					// TODO Auto-generated method stub
					fetchTimelineAsync(0);
				}
			});

			if (isInternetAvailable(this)) {
				lvTweets.setOnScrollListener((new EndlessScrollListener() {
					@Override
					public void onLoadMore(int page, int totalItemsCount) {
						// Triggered only when new data needs to be appended to
						// the
						// list
						// Add whatever code is needed to append new items to
						// your
						// AdapterView
						customLoadMoreDataFromApi(totalItemsCount);
						// or customLoadMoreDataFromApi(totalItemsCount);
					}
				}));
			}
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	public static boolean isInternetAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}

	protected void customLoadMoreDataFromApi(int totalItemsCount) {
		// TODO Auto-generated method stub

		Tweet lastTweet = (Tweet) lvTweets
				.getItemAtPosition(totalItemsCount - 1);

		MyTwitterApp.getRestClient().getAdditionalHomeTimeline(
				lastTweet.getUid() - 1, new JsonHttpResponseHandler() {

					public void onSuccess(JSONArray json) {
						Log.d("DEBUG", json.toString());
						ArrayList<Tweet> tweets = Tweet.fromJson(json);
						adapter.addAll(tweets);
						adapter.notifyDataSetChanged();

						ActiveAndroid.beginTransaction();
						try {
							for (Tweet tweetInstance : tweets) {
								tweetInstance.getUser().save();
								tweetInstance.save();
							}
							ActiveAndroid.setTransactionSuccessful();
						} finally {
							ActiveAndroid.endTransaction();
						}
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
						// super.onSuccess(jsonTweets);
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
