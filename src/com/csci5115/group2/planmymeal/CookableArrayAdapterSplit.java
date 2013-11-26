package com.csci5115.group2.planmymeal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

public class CookableArrayAdapterSplit extends ArrayAdapter<Cookable> {
	
	private Context context;
	private List<Cookable> cookables;
	private List<Cookable> allCookables;
	private Filter filter;

	public CookableArrayAdapterSplit(Context context, List<Cookable> cookables) {
		super(context, R.layout.row_cookable_split, cookables);
		this.context = context;
		this.cookables = cookables;
		this.allCookables = new LinkedList<Cookable>();
		this.allCookables.addAll(this.cookables);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	    View rowView = inflater.inflate(R.layout.row_cookable_split, parent, false);
	    TextView mealName = (TextView) rowView.findViewById(R.id.row_meal_split_listCookableName);
	    TextView mealTime = (TextView) rowView.findViewById(R.id.row_meal_split_listCookableTime);

    	Cookable cookable = cookables.get(position);
	    mealName.setText("[" + cookable.getType() + "] " + cookable.getName());
	    mealTime.setText(Double.toString(cookable.getTime()));
	    
	    return rowView;
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
						List<Cookable> filterResults = (List<Cookable>) results.values;
						cookables.clear();
						cookables.addAll(filterResults);
						notifyDataSetChanged();
					}
				}
			};
			return filter;
		}
	}
}
