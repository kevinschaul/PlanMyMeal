package com.csci5115.group2.planmymeal;

import java.util.LinkedList;

public class GlobalData
{
	public static LinkedList<Meal> communityCookbookMeals;
	public static LinkedList<Recipe> communityCookbookRecipes;
	
	public static LinkedList<Tag> userTags = new LinkedList<Tag>() {{
		add(new Tag("Fish"));
		add(new Tag("Sweet"));
		add(new Tag("Spicy"));
		add(new Tag("Breakfast"));
		add(new Tag("Lunch"));
		add(new Tag("Dinner"));
		add(new Tag("adsaskdhal"));
		add(new Tag("Dinsdahasner"));
	}};
	
	public static LinkedList<Meal> userMeals = new LinkedList<Meal>() {{
		add(new Meal("Beef Stew Meal", 1.23,userTags, new LinkedList<Recipe>() {{
			add(new Recipe("MealRecipe1 Name", 3.3));
			add(new Recipe("MealRecipe2 Name", 2.2));
		}}));
		add(new Meal("Thanksgiving Dinner", 4.5, userTags));
		add(new Meal("Birthday Special", 4.2, userTags));
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
