package com.csci5115.group2.planmymeal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class SettingsActivity extends Activity implements OnClickListener, OnEditorActionListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		 // Register button listeners
       Button saveButton = (Button) findViewById(R.id.save_settings_button);
       saveButton.setOnClickListener(this);
        
        // Register text listener
        //EditText search = (EditText) findViewById(R.id.home_search);
		//search.setOnEditorActionListener(this);
		
		// Set up list view
		// TODO Use a database call to populate this list view
		Tags[] tagArray = new Tags[6];
		
		for (int i = 0; i < 6; i++) {
			if (i % 2 == 0) {
				tagArray[i] = new Tags("Dairy");
			} else {
				tagArray[i] = new Tags("Olivia Likes");
			}
		}

		TagArrayAdapter tagAdapter = new TagArrayAdapter(this, tagArray);
		//ListView listView = (ListView) findViewById(R.id.settings_tags);
		//listView.setAdapter(tagAdapter);
    }

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId = v.getId();

		switch(viewId) {
			case R.id.save_settings_button: {
				onClickButtonSettings(v);
				Context context = getApplicationContext();
				CharSequence text = "Settings Saved";
				Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
				toast.show();
				break;
			}
			default: {
				Context context = getApplicationContext();
				CharSequence text = "Not yet implemented";
				Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
				toast.show();
			}
		}
	}
	
	private void onClickButtonSettings(View v) {	
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}

}
