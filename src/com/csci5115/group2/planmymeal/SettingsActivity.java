package com.csci5115.group2.planmymeal;


import com.csci5115.group2.planmymeal.database.DataSourceManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends Activity implements OnClickListener {

		// Databases
		private DataSourceManager datasource;
		
		private UserSettings settings;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		setTitle("Settings");
		// Database Creation
        datasource = new DataSourceManager(this);
        datasource.open();
        
        settings = datasource.getUserSettings();
        
        // Initialize settings fields
       Spinner numMicrowaves = (Spinner) findViewById(R.id.my_kitchen_numMicrowaves);
       Spinner numOvens = (Spinner) findViewById(R.id.my_kitchen_numOvens);
       Spinner numBurners= (Spinner) findViewById(R.id.my_kitchen_numBurners);
       
       Spinner reminderTime = (Spinner) findViewById(R.id.cooking_alerts_reminderTime);
       Spinner reminderAlert = (Spinner) findViewById(R.id.cooking_alerts_reminderAlert);
       Spinner mainAlert= (Spinner) findViewById(R.id.cooking_alerts_mainAlert);
       
       reminderTime.setSelection(2);
       reminderAlert.setSelection(1);
       mainAlert.setSelection(0);
       
       
        
       numMicrowaves.setSelection(1);
		 // Register button listeners
       Button saveButton = (Button) findViewById(R.id.save_settings_button);
       saveButton.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		int viewId = v.getId();

		switch(viewId) {
			case R.id.save_settings_button: {
				// Save to db
				
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
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		datasource.close();
	}
	
	@Override
	  protected void onResume() {
	    datasource.open();
	    super.onResume();
	  }

	  @Override
	  protected void onPause() {
		datasource.close();
	    super.onPause();
	  }
	
	private void onClickButtonSettings(View v) {	
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}

}
