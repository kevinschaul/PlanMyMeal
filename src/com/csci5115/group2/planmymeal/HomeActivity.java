package com.csci5115.group2.planmymeal;

import java.util.LinkedList;
import java.util.List;

import com.csci5115.group2.planmymeal.database.DataSourceManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

// Samantha Oyen: This used to implement OnClickListener... had to take out.
public class HomeActivity extends FragmentActivity implements OnEditorActionListener, MealListFragment.Callbacks {
	
	public final static String EXTRA_MEAL = "com.csci5115.group2.planmymeal.MEAL";
	
	private final String TAG = "HomeActivity";
	
	// Databases
	private DataSourceManager datasource;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        // Database Creation
        datasource = new DataSourceManager(this);
        datasource.open();
        
     // List items should be given the 'activated' state when touched.
     		((MealListFragment) getSupportFragmentManager().findFragmentById(
     				R.id.home_meal_list)).setActivateOnItemClick(true);
        
        
        // add initial meals
        Meal meal1= datasource.createMeal("Sam's new Meal", 5.3, "My Description");
        Tag meal1Tag1 = datasource.createTag("Spicy");
        Tag meal1Tag2 = datasource.createTag("Yummy");
        datasource.createMealTag(meal1.getId(), meal1Tag1.getId());
        datasource.createMealTag(meal1.getId(), meal1Tag2.getId());
        
        Meal meal2 = datasource.createMeal("Sam's second Meal", 3.3, "Description 2");
        
        List<Meal> meals = datasource.getAllMeals();
        
        // TODO Samantha Oyen: Use meals list defined above for list on home page.
        //MealArrayAdapter mealAdapter = new MealArrayAdapter(this, meals);
		//ListView listView = (ListView) findViewById(R.id.home_mealListView);
		//listView.setAdapter(mealAdapter);
		
        // Register button listeners
        //Button settingsButton = (Button) findViewById(R.id.home_buttonSettings);
        //settingsButton.setOnClickListener(this);
        
        //Button cookbookButton = (Button) findViewById(R.id.home_buttonCookbook);
        //cookbookButton.setOnClickListener(this);
        
        //Button newRecipeButton = (Button) findViewById(R.id.home_buttonNewRecipe);
        //newRecipeButton.setOnClickListener(this);
        
        //Button newMealButton = (Button) findViewById(R.id.home_buttonNewMeal);
        //newMealButton.setOnClickListener(this);
        
        // Register text listener
		/*AutoCompleteTextView search = (AutoCompleteTextView) findViewById(R.id.home_search);
		String[] tags = getResources().getStringArray(R.array.tags_array);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tags);
		search.setAdapter(adapter);
		search.setOnEditorActionListener(this);*/
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
	
	private void onClickButtonSettings(View v) {	
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
	
	private void onClickButtonCookbook(View v) {	
		Intent intent = new Intent(this, CommunityCookbookActivity.class);
		startActivity(intent);
	}
	
	 @Override
	  protected void onResume() {
	    datasource.open();
	    super.onResume();
	  }

	  @Override
	  protected void onPause() {
		  datasource.close();
	    super.onPause();
	  }
}
