package com.codepath.apps.mytwitterapp;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

public class ComposeActivity extends Activity {
	
	EditText etCompose;
	TextView tvCharsLeft;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		
		etCompose = (EditText) findViewById(R.id.etCompose);
		tvCharsLeft = (TextView) findViewById(R.id.tvCharsLeft);
		
		etCompose.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				int length = etCompose.getText().length();
				tvCharsLeft.setText("Characters left: " + (140 - length));
				
				if (tvCharsLeft.length() < 30) {
					tvCharsLeft.setTextColor(Color.RED);
				} else {
					tvCharsLeft.setTextColor(Color.DKGRAY);
				}
				
				Log.d("DEBUG", "chars: " + count);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	/*@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		int length = etCompose.getText().length();
		tvCharsLeft.setText("Characters left: " + (140 - length));
		
		/*if (tvCharsLeft.length() < 0) {
			tvCharsLeft.setTextColor(Color.RED);
		} else {
			tvCharsLeft.setTextColor(Color.GRAY);
		}
		
		Log.d("DEBUG", "chars: " + length);
		return true;
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}
	
	public void onTweet(MenuItem mi) {

		String tweet = etCompose.getText().toString();
		MyTwitterApp.getRestClient().postTweet(tweet, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {
				Log.d("DEBUG", "Success");
				Toast.makeText(ComposeActivity.this, "Your tweet has been posted!", Toast.LENGTH_LONG).show();
				//setResult(RESULT_OK);
				ComposeActivity.this.finish();
				super.onSuccess(json);
			}
			@Override
			public void onFailure(Throwable arg0, JSONObject json) {
				Log.d("DEBUG", "Failed");
				super.onFailure(arg0, json);
			}
		});
	
	}
	
	/*public void onCompose(View v) {
		String tweet = etCompose.getText().toString();
		MyTwitterApp.getRestClient().postTweet(tweet, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {
				Log.d("DEBUG", "Success");
				Toast.makeText(ComposeActivity.this, "Hold on to your butts", Toast.LENGTH_LONG).show();
				//setResult(RESULT_OK);
				ComposeActivity.this.finish();
				super.onSuccess(json);
			}
			@Override
			public void onFailure(Throwable arg0, JSONObject json) {
				Log.d("DEBUG", "Failed");
				super.onFailure(arg0, json);
			}
		});
	}*/

}
