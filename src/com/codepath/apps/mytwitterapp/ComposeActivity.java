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
	int length;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);

		etCompose = (EditText) findViewById(R.id.etCompose);
		tvCharsLeft = (TextView) findViewById(R.id.tvCharsLeft);

		etCompose.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				length = etCompose.getText().length();
				
				if (length == 0)
					tvCharsLeft.setText("No input.");
				else if (length > 140)
					tvCharsLeft.setText("Exceeding maximum input for tweet. Please delete " + (-(140 - length)) + ((length == 141) ? " character." : " characters."));
				else
					tvCharsLeft.setText("Characters left: " + (140 - length));

				if (length > 120) {
					tvCharsLeft.setTextColor(Color.RED);
				} else {
					tvCharsLeft.setTextColor(Color.GRAY);
				}

				Log.d("DEBUG", "chars: " + length);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}

	public void onTweet(MenuItem mi) {

		String tweet = etCompose.getText().toString();
		
		if (length == 0) {
			Toast.makeText(this, "You don't have any inputs. In order to tweet, you must type a message.", Toast.LENGTH_LONG).show();
		}
		
		if (length < 141) {

			MyTwitterApp.getRestClient().postTweet(tweet,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONObject json) {
							Log.d("DEBUG", "Success");
							Toast.makeText(ComposeActivity.this,
									"Your tweet has been posted!",
									Toast.LENGTH_LONG).show();
							// setResult(RESULT_OK);
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
		
		if (length > 140) {
			Toast.makeText(this, "Your tweet exceeds the maximum amount of characters.", Toast.LENGTH_LONG).show();
		}
		

	}

}
