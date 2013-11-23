package com.csci5115.group2.planmymeal;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CookableArrayAdapterSplit extends ArrayAdapter<Cookable> {
	
	private final Context context;
	private final List<Cookable> cookables;

	public CookableArrayAdapterSplit(Context context, List<Cookable> cookables) {
		super(context, R.layout.row_cookable_split, cookables);
		this.context = context;
		this.cookables = cookables;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	    View rowView = inflater.inflate(R.layout.row_cookable_split, parent, false);
	    TextView mealName = (TextView) rowView.findViewById(R.id.row_meal_split_listCookableName);
	    TextView mealTime = (TextView) rowView.findViewById(R.id.row_meal_split_listCookableTime);
	    
	    Cookable cookable = cookables.get(position);
	    mealName.setText(cookable.getName());
	    mealTime.setText(Double.toString(cookable.getTime()));
	    
	    return rowView;
	}
}
