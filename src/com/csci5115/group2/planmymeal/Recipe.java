package com.csci5115.group2.planmymeal;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;

public class Recipe extends Cookable {
	private Integer numServings;
	private List<Ingredient> ingredients;
	List<Tag> tags;
	private List<RecipeStep> steps;
	public CountDownTimer stepTimer;
	// TODO Don't use String to represent time
	
	public Recipe(){
		//default Constructor
		this.description = "";
		this.tags = new LinkedList<Tag>();
		this.ingredients = new LinkedList<Ingredient>();
		this.steps = new LinkedList<RecipeStep>();
		this.type = "Recipe";
		
	}
	
	public Recipe(String name, double time){
		this.description = "";
		this.name = name;
		this.time = time;
		this.tags = new LinkedList<Tag>();
		this.ingredients = new LinkedList<Ingredient>();
		this.steps = new LinkedList<RecipeStep>();
		this.type = "Recipe";
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
	
	public RecipeStep getFirstUncompletedStep()
	{
		RecipeStep prev = new RecipeStep();
		RecipeStep toReturn = null;
		
		for(int i = 0; i < this.steps.size(); i++){
			RecipeStep recipeStep = steps.get(i);
			if(recipeStep.isCompleted())
			{
				prev = recipeStep;
			}
			if(i == 0){
				if(recipeStep.isCompleted())
				{
					prev = recipeStep;
				}
				else
				{
					toReturn = recipeStep;
				}
			}
			else
			{
				if(!recipeStep.isCompleted() && prev.isCompleted())
				{
					toReturn = recipeStep;
					break;
				}
				
			}
		}
		
		return toReturn;
	}
	
	public void completeStepInRecipe()
	{
		List<RecipeStep> newSteps = steps; //r.getSteps();
		for(int i = 0; i < newSteps.size(); i++)
		{
			RecipeStep curStep =  newSteps.get(i);
			if(!curStep.isCompleted())
			{
				curStep.setCompleted();
				newSteps.set(i, curStep);
				this.setSteps(newSteps);
				this.setTime(time - curStep.getTime());
				break;
			}
		}
	}
	
	public void timeBasedOnUnCompletedSteps()
	{
		this.time = 0;
		for(RecipeStep rs: steps)
		{
			if(!rs.isCompleted())
			{
				this.time += rs.getTime();
			}
		}
	}
	
	public LinkedList<RecipeStep> cloneSteps()
	{
		LinkedList<RecipeStep> clones = new LinkedList<RecipeStep>();
		for(RecipeStep rs: this.steps){
			RecipeStep curStep = new RecipeStep();
			curStep.setInstructions(rs.getInstructions());
			curStep.setTime(rs.getTime());
			if(rs.isCompleted())
			{
				curStep.setCompleted();
			}
			clones.add(curStep);
		}
		return clones;
	}
	
	public void setStepTimer(StepTimer s){
		//stepTimer = s;
	}

	public void setCurrentStepToWatch() {
		RecipeStep rs = getFirstUncompletedStep();
		if(!rs.isActiveStep())
		{
			rs.setToWatch(true);
		}
	}

}
