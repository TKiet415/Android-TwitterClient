package com.codepath.apps.mytwitterapp;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

public class ComposeActivity extends Activity {
	
	EditText etCompose;
	Button bCompose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		
		etCompose = (EditText) findViewById(R.id.etCompose);
		bCompose = (Button) findViewById(R.id.bCompose);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}
	
	public void onCompose(View v) {
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
	}

}
