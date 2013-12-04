package com.csci5115.group2.planmymeal;

import java.util.List;

import android.text.format.Time;

public class RecipeStep {
	private long id;
	private String instructions;
	private double time;
	private boolean activeStep;
	private List<String> appliancesUsed;
	
	
	public RecipeStep(){
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
	
	

}
