package com.csci5115.group2.planmymeal;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.csci5115.group2.planmymeal.database.DataSourceManager;

public class HomeRowButtonOnClickListener implements OnClickListener {
	Cookable cookable;
	DataSourceManager datasource;
	
	public HomeRowButtonOnClickListener(Cookable cookable) {
        this.cookable = cookable;
	}
	
	@Override
	public void onClick(View v) {
		int viewId = v.getId();

		switch(viewId) {
				case R.id.row_meal_split_button_cook: {
				        onClickButtonCook(v, cookable);
				        break;
				}
				case R.id.row_meal_split_button_edit: {
				        onClickButtonEdit(v, cookable);
				        break;
				}
		}
		
	}
	
	private void onClickButtonCook(View v, Cookable cookable) {
		Context context = v.getContext();
        Intent intent = new Intent(context, CookActivity.class);
        intent.putExtra(HomeActivity.EXTRA_MEAL, cookable.getId());
        context.startActivity(intent);
	}
	
	private void onClickButtonEdit(View v, Cookable cookable) {
		Context context = v.getContext();
		Intent intent;
		
		String type = cookable.getType();
		if (type == "Meal") {
			intent = new Intent(context, EditMealActivity.class);
	        intent.putExtra(HomeActivity.EXTRA_MEAL, cookable.getId());
	        context.startActivity(intent);
		} else if (type == "Recipe") {
			intent = new Intent(context, EditRecipeActivity.class);
	        intent.putExtra(HomeActivity.EXTRA_RECIPE, cookable.getId());
	        context.startActivity(intent);
		}
	}
}