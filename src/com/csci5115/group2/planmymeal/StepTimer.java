package com.csci5115.group2.planmymeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class StepTimer implements OnClickListener {
	public CountDownTimer newTimer;
	public long timeTilFinished;
	public TextView currentStepTime;
	public TextView currentStepDescription;
	public String currentStepText;
	public Ringtone ring;
	public boolean isFinished;
	
	public StepTimer(long time, TextView cD, TextView cST){
		currentStepDescription = cD;
		currentStepTime = cST;
		currentStepText = (String) cD.getText();
		timeTilFinished = time;
		isFinished = false;
		newTimer = createTimer(time);
		newTimer.start();
		//ring = r;
	 }
	public String makeTimeString(long millis){
		long second = (millis / 1000) % 60;
		long minute = (millis / (1000 * 60)) % 60;
		long hour = (millis / (1000 * 60 * 60)) % 24;

		String time = String.format("%02d:%02d:%02d", hour, minute, second);
		
		return time;
	}
	public void cancel() {
		newTimer.cancel();
	}
	public void start() {
		newTimer.start();		
	}
	public void add30Seconds(){
		newTimer.cancel();
    	newTimer = createTimer(timeTilFinished + 30000);
    	newTimer.start();
	}
	
	public CountDownTimer createTimer(long time)
	{
		CountDownTimer timer = new CountDownTimer(time, 1000) {
			public void onTick(long millisUntilFinished) {
				if(currentStepTime.equals(currentStepDescription))
				{
					currentStepDescription.setText(currentStepText + " (" + makeTimeString(millisUntilFinished) + ')');
					currentStepDescription.refreshDrawableState();
					
				}
				else
				{
					currentStepTime.setText(makeTimeString(millisUntilFinished));
				
				}
				
				timeTilFinished = millisUntilFinished;
				currentStepTime.setBackgroundColor(getColor(millisUntilFinished));
				currentStepDescription.setBackgroundColor(getColor(millisUntilFinished));				
			}
			
			public int getColor(long millisUntilFinished) {
				if(millisUntilFinished <= 30000)
				{
					return Color.RED;
				}
				else if(millisUntilFinished <= 60000)
				{
					return Color.YELLOW;
				}
				
				return Color.BLUE;
			}

			@Override
			public void onFinish() {
				currentStepDescription.setText(currentStepText);
				currentStepDescription.setBackgroundColor(Color.TRANSPARENT);
				currentStepDescription.setTextColor(Color.GRAY);
		    	currentStepDescription.setPaintFlags(currentStepDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		    	isFinished = true;
			}
						
		};
	 return timer;
	}
	public StepTimer setTextViews(TextView stepDescription, TextView stepTime) {
		currentStepTime = stepTime;
		currentStepDescription = stepDescription;
		return null;
	}
	
	public void setCountDownTimer(CountDownTimer c)
	{
		newTimer = c;
		newTimer.start();
	}
	@Override
	public void onClick(final View v) {	
		//s//etContentView(R.layout.activity_cook);
	}

}
