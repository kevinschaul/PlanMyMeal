package com.csci5115.group2.planmymeal;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RecipeStepsArrayAdapter extends ArrayAdapter<RecipeStep> {
	private final Context context;
	private final LinkedList<RecipeStep> values;
	
	public RecipeStepsArrayAdapter(Context context, LinkedList<RecipeStep> recipeSteps) {
		super(context, R.layout.row_recipe_steps, recipeSteps);
		this.context = context;
		this.values = recipeSteps;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	    Typeface fontAwesome = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf" );
	    View rowView = inflater.inflate(R.layout.row_recipe_steps, parent, false);
	    
	    TextView ingredientName = (TextView) rowView.findViewById(R.id.row_step_text);
	    
	    RecipeStep recipeStep = values.get(position);
	    ingredientName.setText(recipeStep.getInstructions());
	    
	    return rowView;
	}
}
