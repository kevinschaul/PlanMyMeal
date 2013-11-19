package com.csci5115.group2.planmymeal;

import java.util.List;

import android.text.format.Time;

public class RecipeStep {
	
	private String instructions;
	private Time time;
	private List<String> appliancesUsed;
	private boolean activeStep;
	
	public RecipeStep(){
		//default Constructor
	}
	
	public String getInstructions() {
		return instructions;
	}
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	public Time getTime() {
		return time;
	}
	public void setTime(Time time) {
		this.time = time;
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
	
	

}
