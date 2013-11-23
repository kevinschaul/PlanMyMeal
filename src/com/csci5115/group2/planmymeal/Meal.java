package com.csci5115.group2.planmymeal;

import java.util.LinkedList;
import java.util.List;

public class Meal extends Cookable
{
	private String description;
	
	// Need to generate using Tag_Meal_Lookup table
	List<Tag> tags;
	// Need to generate using Tag_Meal_Lookup table
	List<Recipe> recipes;

	public Meal()
	{
		// default Constructor
		this.tags = new LinkedList<Tag>();
		this.recipes = new LinkedList<Recipe>();
		this.type = "Meal";
	}

	public Meal(String name, double time, LinkedList<Tag> tags)
	{
		this.name = name;
		this.time = time;
		this.tags = tags;
		this.recipes = new LinkedList<Recipe>();
		this.type = "Meal";
	}

	public Meal(String name, double time, LinkedList<Tag> tags,
			LinkedList<Recipe> recipes)
	{
		this.name = name;
		this.time = time;
		this.tags = tags;
		this.recipes = recipes;
		this.type = "Meal";
	}

	public List<Tag> getTags()
	{
		return tags;
	}

	public void setTags(List<Tag> tags)
	{
		this.tags = tags;
	}

	public List<Recipe> getRecipes()
	{
		return recipes;
	}

	public void setRecipes(List<Recipe> recipes)
	{
		this.recipes = recipes;
	}
	
	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void addRecipeToMeal(Recipe recipe)
	{
		this.recipes.add(recipe);
	}

	public void deleteRecipeToMeal(Recipe recipe)
	{
		if (this.recipes.contains(recipe))
		{
			this.recipes.remove(recipe);
		}
	}

	public List<Tag> getAllMealTags()
	{
		LinkedList<Tag> allMealTags = new LinkedList<Tag>();
		allMealTags.addAll(this.tags);

		for (Recipe recipe : this.recipes)
		{
			for (Tag tag : recipe.getTags())
			{
				if (allMealTags.contains(tag))
				{
					// do nothing
				} else
				{
					allMealTags.add(tag);
				}
			}
		}

		return allMealTags;
	}

	public boolean isRecipeInMeal(Recipe recipe)
	{
		if (this.recipes.contains(recipe))
		{
			return true;
		}
		return false;
	}
}
