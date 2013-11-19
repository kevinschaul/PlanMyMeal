package com.csci5115.group2.planmymeal;

import java.util.LinkedList;

public class GlobalData
{
	public static LinkedList<Meal> communityCookbookMeals;
	public static LinkedList<Recipe> communityCookbookRecipes;
	
	public static LinkedList<Meal> userMeals = new LinkedList<Meal>() {{
		add(new Meal("Beef Stew", 1.23, new LinkedList<Recipe>() {{
			add(new Recipe("MealRecipe1 Name", 3.3));
			add(new Recipe("MealRecipe2 Name", 2.2));
			
		}}));
		add(new Meal("Thanksgiving Dinner", 4.5));
		add(new Meal("Birthday Special", 4.2));
	}};
	
	public static LinkedList<Recipe> userRecipes = new LinkedList<Recipe>() {{
		add(new Recipe("Recipe1 Name", 9.0));
		add(new Recipe("Recipe2 Name", 3.4));
		add(new Recipe("Recipe3 Name", 2.1));
	}};
	
	public static Meal findUserMealByName(String mealName)
	{
		for(Meal meal : userMeals)
		{
			if(meal != null && meal.getName() != null && meal.getName().equals(mealName))
			{
				return meal;
			}
		}
		
		return null;
	}
	
}
