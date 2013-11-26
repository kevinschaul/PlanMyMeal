package com.csci5115.group2.planmymeal;


import java.util.LinkedList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class RecipeArrayAdapter extends ArrayAdapter<Recipe>
{
	private final Context context;
	private final LinkedList<Recipe> values;
	private final Meal meal;
	private final MealRecipesArrayAdapter mealRecipesArrayAdapter;

	public RecipeArrayAdapter(Context context, LinkedList<Recipe> allUserRecipes, Meal currentMeal, MealRecipesArrayAdapter mealRecipesAdapter) {
		super(context, R.layout.row_recipe, allUserRecipes);
		this.context = context;
		this.values = allUserRecipes;
		this.meal = currentMeal;
		this.mealRecipesArrayAdapter = mealRecipesAdapter;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	    View rowView = inflater.inflate(R.layout.row_recipe, parent, false);
	    TextView recipeName = (TextView) rowView.findViewById(R.id.row_recipe_recipeName);
	    TextView recipeTime = (TextView) rowView.findViewById(R.id.row_recipe_time);
	    
	    Recipe recipe = values.get(position);
	    recipeName.setText(recipe.getName());
	    recipeTime.setText(recipe.getReadableTime());
	    
		CheckBox addRecipeToMeal = (CheckBox) rowView.findViewById(R.id.add_recipe_to_meal);
		addRecipeToMeal.setOnClickListener(new RowAddRecipeToMealListener(recipe, meal));

	    return rowView;
	}
	
	private class RowAddRecipeToMealListener implements OnClickListener {
		Recipe recipe;
		Meal meal;
		
		public RowAddRecipeToMealListener(Recipe recipe, Meal currentMeal) {
			this.recipe = recipe;
			this.meal = currentMeal;
		}

		@Override
		public void onClick(View v) {

			if(((CheckBox) v).isChecked())
			{
				if(meal.isRecipeInMeal(recipe))
				{
					//do nothing
				}
				else
				{
					meal.addRecipeToMeal(recipe);
					mealRecipesArrayAdapter.notifyDataSetChanged();
				}
			}
			else
			{
				meal.deleteRecipeToMeal(recipe);
				mealRecipesArrayAdapter.notifyDataSetChanged();
			}
		}
	}
}
