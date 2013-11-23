package com.csci5115.group2.planmymeal;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class CookMealRecipeArrayAdapter extends ArrayAdapter<Recipe> {
	private final Context context;
	private final LinkedList<Recipe> values;
	
	public CookMealRecipeArrayAdapter(Context context, LinkedList<Recipe> recipes) {
		super(context, R.layout.row_cook_recipe_container, recipes);
		this.context = context;
		this.values = recipes;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    //setContentView(R.layout.row_cook_recipe_container);
	    
	    Typeface fontAwesome = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf" );
	    View rowView = inflater.inflate(R.layout.row_cook_recipe_container, parent, false);
	    
	    //TextView ingredientName = (TextView) rowView.findViewById(R.id.row_ingredient_name);
	    
	    Recipe recipe = values.get(position);
	    //Name.setText(recipe.getName());
	    TextView myText = (TextView) rowView.findViewById(R.id.recipeCookName);
	    String test = recipe.getName();
		myText.setText(recipe.getName());

		@SuppressWarnings("unchecked")
		LinkedList<Ingredient> recipIngred = (LinkedList)recipe.getIngredients();
		
		IngredientArrayAdapter ingredientsAdapter = new IngredientArrayAdapter(this.context, recipIngred);
		//GridView recipeIngredients = (GridView) rowView.findViewById(R.id.recipeCookIngredients);
		//recipeIngredients.setAdapter(ingredientsAdapter);
		
		
		@SuppressWarnings("unchecked")
		LinkedList<RecipeStep> recipeSteps = (LinkedList) recipe.getSteps();
		RecipeStepsArrayAdapter recipeStepAdapter = new RecipeStepsArrayAdapter(this.context, recipeSteps);
		ListView recipeStepsListView = (ListView) rowView.findViewById(R.id.recipeCookSteps);
		recipeStepsListView.setAdapter(recipeStepAdapter);
	    
	    return rowView;
	}
}
