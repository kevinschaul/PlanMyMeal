package com.csci5115.group2.planmymeal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class EditMealActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_meal);
		
		Intent intent = getIntent();
		String mealName = intent.getStringExtra(HomeActivity.EXTRA_MEAL);
		
		TextView mealNameTextView = (TextView) findViewById(R.id.edit_meal_mealName);
		mealNameTextView.setText(mealName);
	}

}
