package com.csci5115.group2.planmymeal;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class MealRecipesArrayAdapter extends ArrayAdapter<Recipe>
{
	private final Context context;
	private LinkedList<Recipe> values;

	public MealRecipesArrayAdapter(Context context, LinkedList<Recipe> recipes) {
		super(context, R.layout.row_recipe_in_meal, recipes);
		this.context = context;
		this.values = recipes;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	    View rowView = inflater.inflate(R.layout.row_recipe_in_meal, parent, false);
	    TextView recipeName = (TextView) rowView.findViewById(R.id.row_recipe_recipeName);
	    TextView recipeTime = (TextView) rowView.findViewById(R.id.row_recipe_time);
	    
	    Recipe recipe = values.get(position);
	    recipeName.setText(recipe.getName());
	    recipeTime.setText(Double.toString(recipe.getTime()));

	    return rowView;
	}
	
	public void addRecipe(Recipe recipe)
	{
		values.add(recipe);
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
				}
			}
		}
	}

}
