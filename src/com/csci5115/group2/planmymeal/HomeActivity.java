package com.csci5115.group2.planmymeal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class HomeActivity extends Activity implements OnClickListener, OnEditorActionListener {
	
	public final static String EXTRA_MEAL = "com.csci5115.group2.planmymeal.MEAL";
	
	private final String TAG = "HomeActivity";

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        
        // Register button listeners
        Button settingsButton = (Button) findViewById(R.id.home_buttonSettings);
        settingsButton.setOnClickListener(this);
        
        Button cookbookButton = (Button) findViewById(R.id.home_buttonCookbook);
        cookbookButton.setOnClickListener(this);
        
        Button newRecipeButton = (Button) findViewById(R.id.home_buttonNewRecipe);
        newRecipeButton.setOnClickListener(this);
        
        Button newMealButton = (Button) findViewById(R.id.home_buttonNewMeal);
        newMealButton.setOnClickListener(this);
        
        // Register text listener
        EditText search = (EditText) findViewById(R.id.home_search);
		search.setOnEditorActionListener(this);
		
		// Set up list view
		// TODO Use a database call to populate this list view
		Meal[] mealArray = new Meal[10];
		
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				mealArray[i] = new Meal("Beef Stew", "1:18:00");
			} else {
				mealArray[i] = new Meal("Olivia's Favorite", "1:40:22");
			}
		}

		MealArrayAdapter mealAdapter = new MealArrayAdapter(this, mealArray);
		ListView listView = (ListView) findViewById(R.id.home_mealListView);
		listView.setAdapter(mealAdapter);
    }
    
	@Override
	public void onClick(View v) {
		int viewId = v.getId();

		switch(viewId) {
			case R.id.home_buttonSettings: {
				onClickButtonSettings(v);
				break;
			}
			default: {
				Context context = getApplicationContext();
				CharSequence text = "Not yet implemented";
				Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
				toast.show();
			}
		}
	}
    
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		boolean handled = false;
		Log.i("onEditorAction()", Integer.toString(actionId));
		if (actionId == EditorInfo.IME_ACTION_SEARCH) {
	    	Context context = getApplicationContext();
	    	CharSequence text = v.getText();
	    	int duration = Toast.LENGTH_SHORT;
	    	
	    	Toast toast = Toast.makeText(context, text, duration);
	    	toast.show();
		}
		
		return handled;
	}
	
	private void onClickButtonSettings(View v) {	
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

}
