package com.csci5115.group2.planmymeal;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MealArrayAdapterSplit extends ArrayAdapter<Meal> {
	
	private final Context context;
	private final LinkedList<Meal> values;

	public MealArrayAdapterSplit(Context context, LinkedList<Meal> values) {
		super(context, R.layout.row_meal_split, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	    View rowView = inflater.inflate(R.layout.row_meal_split, parent, false);
	    TextView mealName = (TextView) rowView.findViewById(R.id.row_meal_split_listMealName);
	    TextView mealTime = (TextView) rowView.findViewById(R.id.row_meal_split_listMealTime);
	    
	    Meal meal = values.get(position);
	    mealName.setText(meal.getName());
	    mealTime.setText(Double.toString(meal.getTime()));
	    
	    return rowView;
	}
}
