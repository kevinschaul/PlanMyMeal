package com.csci5115.group2.planmymeal;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.format.Time;
import android.widget.TextView;

public class RecipeStep {
	private long id;
	private String instructions;
	private double time;
	private boolean activeStep;
	private List<String> appliancesUsed;
	private boolean completed;
	private boolean toWatch;
	private StepTimer stepTimer;
	public boolean timerSet;
	public TextView currentStepTime;
	public TextView currentStepDescription;
	
	public RecipeStep(){
		completed = false;
		stepTimer = null;
		timerSet = false;
		//default Constructor
	}

	public long getId()
	{
		return id;
	}
	public void setId(long id)
	{
		this.id = id;
	}
	public String getInstructions() {
		return instructions;
	}
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double d) {
		this.time = d;
	}
	public List<String> getAppliancesUsed() {
		return appliancesUsed;
	}
	public void setAppliancesUsed(List<String> appliancesUsed) {
		this.appliancesUsed = appliancesUsed;
	}
	public boolean isActiveStep() {
		return activeStep;
	}
	public void setActiveStep(boolean activeStep) {
		this.activeStep = activeStep;
	}
	public void setCompleted(){
		this.completed = true;
	}
	
	public boolean isCompleted(){
		return this.completed;
	}
	
	public void setTimer(StepTimer stepTimer2)
	{
		this.stepTimer = stepTimer2;
		//this.stepTimer.newTimer.start();
	}
	
	public StepTimer getTimer()
	{
		return this.stepTimer;	
	}
	
	public void setToWatch(boolean b)
	{
		toWatch = b;
	}
	
	public boolean getToWatch(){
		return toWatch;
	}

	
	
	public String makeTimeString(long millis){
		long second = (millis / 1000) % 60;
		long minute = (millis / (1000 * 60)) % 60;
		long hour = (millis / (1000 * 60 * 60)) % 24;

		String time = String.format("%02d:%02d:%02d", hour, minute, second);
		
		return time;
	}

}
