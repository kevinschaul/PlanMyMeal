package com.csci5115.group2.planmymeal;

import com.csci5115.group2.planmymeal.database.DataSourceManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends Activity implements OnClickListener
{

	// Databases
	private DataSourceManager datasource;
	private UserSettings settings;
	private boolean initialSelectionReminderAlert = true;
	private boolean initialSelectionStartAlert = true;
	private Ringtone reminderRingtone;
	private Ringtone startRingtone;
	private Uri reminderUri;
	private Uri startUri;
	private int defaultAlarmType = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		setTitle("Settings");
		// Database Creation
		datasource = new DataSourceManager(this);
		datasource.open();

		settings = datasource.getUserSettings();

		reminderUri = RingtoneManager.getDefaultUri(defaultAlarmType);
		reminderRingtone = RingtoneManager.getRingtone(getApplicationContext(),
				reminderUri);

		startUri = RingtoneManager.getDefaultUri(defaultAlarmType);
		startRingtone = RingtoneManager.getRingtone(getApplicationContext(),
				reminderUri);

		// Initialize settings fields
		Spinner numMicrowaves = (Spinner) findViewById(R.id.my_kitchen_numMicrowaves);
		Spinner numOvens = (Spinner) findViewById(R.id.my_kitchen_numOvens);
		Spinner numBurners = (Spinner) findViewById(R.id.my_kitchen_numBurners);

		Spinner reminderTime = (Spinner) findViewById(R.id.cooking_alerts_reminderTime);
		Spinner reminderAlert = (Spinner) findViewById(R.id.cooking_alerts_reminderAlert);
		reminderAlert.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id)
			{
				if (reminderRingtone.isPlaying())
				{
					reminderRingtone.stop();
				}
				if (startRingtone.isPlaying())
				{
					startRingtone.stop();
				}
				
				if (initialSelectionReminderAlert)
				{ // If spinner initializes
					initialSelectionReminderAlert = false;
				} else
				{
					// If user manually select item
					int alarmType = UserSettings.indexToAlarmType(position);
					reminderUri = RingtoneManager.getDefaultUri(alarmType);
					reminderRingtone = RingtoneManager.getRingtone(
							getApplicationContext(), reminderUri);
					reminderRingtone.play();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView)
			{
				// your code here
			}

		});
		Spinner mainAlert = (Spinner) findViewById(R.id.cooking_alerts_mainAlert);
		mainAlert.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id)
			{
				if (startRingtone.isPlaying())
				{
					startRingtone.stop();
				}
				if (reminderRingtone.isPlaying())
				{
					reminderRingtone.stop();
				}
				if (initialSelectionStartAlert)
				{ // If spinner initializes
					initialSelectionStartAlert = false;
				} else
				{
					// If user manually select item
					int alarmType = UserSettings.indexToAlarmType(position);
					startUri = RingtoneManager.getDefaultUri(alarmType);
					startRingtone = RingtoneManager.getRingtone(
							getApplicationContext(), startUri);
					startRingtone.play();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView)
			{
				// your code here
			}

		});

		numMicrowaves.setSelection(settings.getNumMicrowaves());
		numOvens.setSelection(settings.getNumOvens());
		numBurners.setSelection(settings.getNumBurners());
		reminderTime.setSelection(settings.getReminderTime());
		reminderAlert.setSelection(settings.getReminderSound());
		mainAlert.setSelection(settings.getStartSound());

		// Register button listeners
		Button saveButton = (Button) findViewById(R.id.save_settings_button);
		saveButton.setOnClickListener(this);
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
		if (reminderRingtone.isPlaying())
		{
			reminderRingtone.stop();
		}
		if (startRingtone.isPlaying())
		{
			startRingtone.stop();
		}
		return true;
    }
	
	@Override
	public void onClick(View v)
	{
		
		int viewId = v.getId();

		switch (viewId)
		{
		case R.id.save_settings_button:
		{
			// Save to db
			int numMicrowaves = ((Spinner) findViewById(R.id.my_kitchen_numMicrowaves))
					.getSelectedItemPosition();
			int numOvens = ((Spinner) findViewById(R.id.my_kitchen_numOvens))
					.getSelectedItemPosition();
			int numBurners = ((Spinner) findViewById(R.id.my_kitchen_numBurners))
					.getSelectedItemPosition();

			int reminderTime = ((Spinner) findViewById(R.id.cooking_alerts_reminderTime))
					.getSelectedItemPosition();
			int reminderSound = ((Spinner) findViewById(R.id.cooking_alerts_reminderAlert))
					.getSelectedItemPosition();
			int startSound = ((Spinner) findViewById(R.id.cooking_alerts_mainAlert))
					.getSelectedItemPosition();
			datasource.updateUserSettings(settings.getId(), reminderTime,
					reminderSound, startSound, numOvens, numMicrowaves,
					numBurners);

			onClickButtonSettings(v);
			Context context = getApplicationContext();
			CharSequence text = "Settings Saved";
			Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			toast.show();
			break;
		}
		default:
		{
			Context context = getApplicationContext();
			CharSequence text = "Not yet implemented";
			Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			toast.show();
		}
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		datasource.close();
	}

	@Override
	protected void onResume()
	{
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		datasource.close();
		super.onPause();
	}

	private void onClickButtonSettings(View v)
	{
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}

}
