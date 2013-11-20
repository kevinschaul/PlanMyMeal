package com.csci5115.group2.planmymeal;

import java.util.LinkedList;
import java.util.List;

public class Recipe {
	private String name;
	private Integer numServings;
	private List<Ingredient> ingredients;
	private List<Tag> tags;
	private List<RecipeStep> steps;
	private double time;
	// TODO Don't use String to represent time
	
	public Recipe(){
		//default Constructor
	}
	
	public Recipe(String name, double time){
		this.name = name;
		this.time = time;
		this.tags = new LinkedList<Tag>();
		this.ingredients = new LinkedList<Ingredient>();
		this.steps = new LinkedList<RecipeStep>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getNumServings() {
		return numServings;
	}
	public void setNumServings(Integer numServings) {
		this.numServings = numServings;
	}
	public List<Ingredient> getIngredients() {
		return ingredients;
	}
	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}
	public List<Tag> getTags() {
		return tags;
	}
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	public List<RecipeStep> getSteps() {
		return steps;
	}
	public void setSteps(List<RecipeStep> steps) {
		this.steps = steps;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	
	

	

}
