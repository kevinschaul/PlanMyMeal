package com.csci5115.group2.planmymeal;

public class Cookable {
	protected long id;
	protected String name;
	protected double time;
	protected String type;
	
	public Cookable() {
		
	}
	
	public Cookable(String name, double time) {
		this.name = name;
		this.time = time;
	}
	
	public String getType() {
		return type;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}

}
