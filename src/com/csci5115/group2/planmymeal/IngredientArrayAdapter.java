package com.csci5115.group2.planmymeal;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class IngredientArrayAdapter extends ArrayAdapter<Ingredient>{
	
	private final Context context;
	private final LinkedList<Ingredient> values;

	public IngredientArrayAdapter(Context context, LinkedList<Ingredient> ingredients) {
		super(context, R.layout.row_ingredient, ingredients);
		this.context = context;
		this.values = ingredients;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	    Typeface fontAwesome = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf" );
	    View rowView = inflater.inflate(R.layout.row_ingredient, parent, false);
	    
	    TextView ingredientName = (TextView) rowView.findViewById(R.id.row_ingredient_name);
	    
	    Ingredient ingred = values.get(position);
	    ingredientName.setText(ingred.getName());
	    
	    return rowView;
	}
}
