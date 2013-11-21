package com.csci5115.group2.planmymeal;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class HomeActivity extends FragmentActivity implements
		MealListFragment.Callbacks, OnEditorActionListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		// List items should be given the 'activated' state when touched.
		((MealListFragment) getSupportFragmentManager().findFragmentById(
				R.id.home_meal_list)).setActivateOnItemClick(true);

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
		Intent intent;
	    switch (item.getItemId()) {
	        case R.id.action_community_cookbook:
	    		intent = new Intent(this, CommunityCookbookActivity.class);
	    		startActivity(intent);
	            return true;
	        case R.id.action_new_meal:
	    		intent = new Intent(this, EditMealActivity.class);
	    		startActivity(intent);
	            return true;
	        case R.id.action_new_recipe:
	    		intent = new Intent(this, EditMealActivity.class);
	    		startActivity(intent);
	            return true;
	        case R.id.action_settings:
	    		intent = new Intent(this, SettingsActivity.class);
	    		startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
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

	@Override
	public void onItemSelected(String id) {
		
		LinearLayout.LayoutParams params;
		LinearLayout homeColumn0 = (LinearLayout) findViewById(R.id.home_column_0);
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 2);
		homeColumn0.setLayoutParams(params);
		
		
		LinearLayout homeColumn1 = (LinearLayout) findViewById(R.id.home_column_1);
		params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 2);
		homeColumn1.setLayoutParams(params);
		
		// Show the detail view in this activity by adding or replacing the
		// detail fragment using a fragment transaction.
		Bundle arguments = new Bundle();
		arguments.putString(MealDetailFragment.ARG_ITEM_ID, id);
		MealDetailFragment fragment = new MealDetailFragment();
		fragment.setArguments(arguments);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.home_meal_detail_container, fragment).commit();
	}
}
