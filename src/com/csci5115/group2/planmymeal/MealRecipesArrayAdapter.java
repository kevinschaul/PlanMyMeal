package com.csci5115.group2.planmymeal;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.csci5115.group2.planmymeal.database.DataSourceManager;

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
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

public class MealRecipesArrayAdapter extends ArrayAdapter<Recipe>
{
	private final Context context;
	private final List<Recipe> recipeValues;
	private final List<Recipe> mealRecipeValues;
	private final Meal meal;
	private DataSourceManager datasource;
	private MealRecipeDetailArrayAdapter recipeDetailArrayAdapter;
	private Filter filter;
	private List<Cookable> allCookables;

	public MealRecipesArrayAdapter(Context context, List<Recipe> allUserRecipes, List<Recipe> mealRecipes, Meal currentMeal, MealRecipeDetailArrayAdapter recipeDetailAdapter) {
		super(context, R.layout.row_recipe, allUserRecipes);
		this.context = context;
		this.recipeValues = allUserRecipes;
		this.mealRecipeValues = mealRecipes;
		this.meal = currentMeal;
		this.recipeDetailArrayAdapter = recipeDetailAdapter;
		this.allCookables = new LinkedList<Cookable>();
		this.allCookables.addAll(this.recipeValues);
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	 // Database Creation
	 	datasource = new DataSourceManager(context);
	 	datasource.open();

	    View rowView = inflater.inflate(R.layout.row_recipe, parent, false);
	    TextView recipeName = (TextView) rowView.findViewById(R.id.row_recipe_recipeName);
	    TextView recipeTime = (TextView) rowView.findViewById(R.id.row_recipe_time);
	    
	    Recipe recipe = recipeValues.get(position);
	    recipeName.setText(recipe.getName());
	    recipeTime.setText(recipe.getReadableTime());
	    
		CheckBox addRecipeToMeal = (CheckBox) rowView.findViewById(R.id.add_recipe_to_meal);
		
		for(Recipe mealRecipe : mealRecipeValues){
			if(recipe.getName().equals(mealRecipe.getName())){
				addRecipeToMeal.setChecked(true);
			}
		}
		
		addRecipeToMeal.setOnClickListener(new RowAddRecipeToMealListener(recipe, meal));	
		
	    return rowView;
	}
	
	public class RowAddRecipeToMealListener implements OnClickListener {
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
				if((datasource.getMealRecipes(meal.getId())).contains(recipe))
				{
					//do nothing
				}
				else
				{
					datasource.addMealRecipe(meal.getId(), recipe.getId());
					recipeDetailArrayAdapter.add(recipe);
					recipeDetailArrayAdapter.notifyDataSetChanged();
				}
			}
			else
			{
				datasource.deleteMealRecipe(meal.getId(), recipe.getId());
				recipeDetailArrayAdapter.remove(recipe);
				recipeDetailArrayAdapter.notifyDataSetChanged();
			}
			//Intent intent = new Intent(context, EditMealActivity.class);
    		//context.startActivity(intent);
		}
	}
	
	@Override
	public Filter getFilter() {
		if (filter != null) {
			return filter;
		} else {
			filter = new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults results = new FilterResults();
					if (constraint == null || constraint.length() == 0) {
						results.values = allCookables;
						results.count = allCookables.size();
					} else {
						List<Cookable> filteredCookables = new ArrayList<Cookable>();
						for (Cookable c : allCookables) {
							if (c.getName().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
								filteredCookables.add(c);
							}
						}
						results.values = filteredCookables;
						results.count = filteredCookables.size();
					}
					return results;
				}
	
				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					if (results.count == 0) {
						notifyDataSetInvalidated();
					} else {
						List<Recipe> filterResults = (List<Recipe>) results.values;
						recipeValues.clear();
						recipeValues.addAll(filterResults);
						notifyDataSetChanged();
					}
				}
			};
			return filter;
		}
	}
}