package com.csci5115.group2.planmymeal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MealArrayAdapter extends ArrayAdapter<Meal> {
	private final Context context;
	private final Meal[] values;

	public MealArrayAdapter(Context context, Meal[] values) {
		super(context, R.layout.row_meal, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    View rowView = inflater.inflate(R.layout.row_meal, parent, false);
	    TextView mealName = (TextView) rowView.findViewById(R.id.row_meal_listMealName);
	    TextView mealTime = (TextView) rowView.findViewById(R.id.row_meal_listMealTime);
	    
	    Meal meal = values[position];
	    mealName.setText(meal.getName());
	    mealTime.setText(meal.getTime());

	    return rowView;
	}

}
