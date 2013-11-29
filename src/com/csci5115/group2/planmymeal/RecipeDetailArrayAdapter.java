package com.csci5115.group2.planmymeal;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RecipeDetailArrayAdapter extends ArrayAdapter<Recipe> {
	
	private final Context context;
	private final List<Recipe> recipes;
	private final Typeface fontAwesome;

	public RecipeDetailArrayAdapter(Context context, List<Recipe> recipes) {
		super(context, R.layout.row_recipe_detail_array, recipes);
		this.context = context;
		this.recipes = recipes;
		this.fontAwesome = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf" );
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	    Recipe recipe = recipes.get(position);
	    
	    View rowView = inflater.inflate(R.layout.row_recipe_detail_array, parent, false);
	    TextView mealType = (TextView) rowView.findViewById(R.id.row_recipe_detail_array_recipeType);
	    TextView mealName = (TextView) rowView.findViewById(R.id.row_recipe_detail_array_recipeName);
	    TextView mealTime = (TextView) rowView.findViewById(R.id.row_recipe_detail_array_time);
	    
	    if (recipe != null) {
		    mealType.setText(context.getString(recipe.getTypeIconResource()));
	    	mealType.setTypeface(this.fontAwesome);
		    mealName.setText(recipe.getName());
		    mealTime.setText(recipe.getReadableTime());
	    }
	    
	    return rowView;
	}

}
