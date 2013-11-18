package com.csci5115.group2.planmymeal;

import java.util.List;

public class Meal {
	private String name;
	// TODO Don't use String to represent time
	private String time;
	List<Tag> tags;
	List<Recipe> recipes;
	
	public Meal(){
		//default Constructor
	}
	
	public Meal(String name, String time){
		this.name = name;
		this.time = time;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public List<Tag> getTags() {
		return tags;
	}
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	public List<Recipe> getRecipes() {
		return recipes;
	}
	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}
	
	
	
}
