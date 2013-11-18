package com.csci5115.group2.planmymeal;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class EditMealActivity extends Activity {
	
	public Meal meal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_meal);
		
		Intent intent = getIntent();
		String mealName = intent.getStringExtra(HomeActivity.EXTRA_MEAL);
		
		meal = GlobalData.findUserMealByName(mealName);
		
		EditText mealNameTextView = (EditText) findViewById(R.id.edit_meal_mealName);
		mealNameTextView.setText(mealName);
		
		// Set up recipes in meal list
		LinkedList<Recipe> recipes = (LinkedList<Recipe>) meal.getRecipes();
		
		MealRecipesArrayAdapter mealRecipesAdapter = new MealRecipesArrayAdapter(this, recipes);
		ListView mealListView = (ListView) findViewById(R.id.edit_meal_recipes_in_meal);
		mealListView.setAdapter(mealRecipesAdapter);

		// Set up all user recipes
		LinkedList<Recipe> allUserRecipesArray = GlobalData.userRecipes;
				
		RecipeArrayAdapter recipeAdapter = new RecipeArrayAdapter(this, allUserRecipesArray, meal, mealRecipesAdapter);
		ListView allUserListView = (ListView) findViewById(R.id.edit_meal_allUserRecipes);
		allUserListView.setAdapter(recipeAdapter);
	}

}
