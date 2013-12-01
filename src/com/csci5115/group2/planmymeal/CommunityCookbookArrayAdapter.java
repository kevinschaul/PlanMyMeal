package com.csci5115.group2.planmymeal;

import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CommunityCookbookArrayAdapter extends ArrayAdapter<Meal> {
	
	private final Context context;
	private final Meal[] values;

	public CommunityCookbookArrayAdapter(Context context, Meal[] values) {
		super(context, R.layout.row_community_cookbook_meal, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    Typeface fontAwesome = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf" );
	    View rowView = inflater.inflate(R.layout.row_community_cookbook_meal, parent, false);
	    TextView mealName = (TextView) rowView.findViewById(R.id.row_communityCookbookMeal_listMealName);
	    TextView mealTime = (TextView) rowView.findViewById(R.id.row_communityCookbookMeal_listMealTime);
	    
	    Meal meal = values[position];
	    mealName.setText(meal.getName());
	    mealTime.setText(Double.toString(meal.getTime()));
	    
	    return rowView;
	}
	private class RowButtonOnClickListener implements OnClickListener {
		Meal meal;
		
		public RowButtonOnClickListener(Meal meal) {
			this.meal = meal;
		}

		@Override
		public void onClick(View v) {
			int viewId = v.getId();

			switch(viewId) {
				/*case R.id.row_meal_buttonCook: {
					onClickButtonCook(v, meal);
					break;
				}
				case R.id.row_meal_buttonEdit: {
					onClickButtonEdit(v, meal);
					break;
				}
				case R.id.row_meal_buttonDelete: {
					onClickButtonDelete(v, meal);
					break;*/
				//}
				default: {
					Context context = v.getContext();
					CharSequence text = "Not yet implemented";
					Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		}
		
	}
	
	private void onClickButtonCook(View v, Meal meal) {
		/*
		Context context = v.getContext();
		Intent intent = new Intent(context, CookActivity.class);
		intent.putExtra(HomeActivity.EXTRA_MEAL, meal.getName());
		context.startActivity(intent);
		*/
	}
	
	private void onClickButtonEdit(View v, Meal meal) {
		/*
		Context context = v.getContext();
		Intent intent = new Intent(context, EditMealActivity.class);
		intent.putExtra(HomeActivity.EXTRA_MEAL, meal.getId());
		context.startActivity(intent);
		*/
	}
	
	private void onClickButtonDelete(View v, Meal meal) {
		Context context = v.getContext();
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Delete meal?");
		builder.setMessage("Delete " + meal.getName() + "?");
		builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				
			}
		});
		builder.setNegativeButton("Cancel", null);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
}
