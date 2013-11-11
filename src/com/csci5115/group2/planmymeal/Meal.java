package com.csci5115.group2.planmymeal;

public class Meal {
	private String name;
	// TODO Don't use String to represent time
	private String time;
	// TODO meal needs many more fields
	
	public Meal(String name, String time) {
		this.name = name;
		this.time = time;
	}
	
	public String getName() {
		return name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setName(String name) {
		this.name = name;
	}
}
