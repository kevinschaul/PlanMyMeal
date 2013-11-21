package com.csci5115.group2.planmymeal;

import java.util.LinkedList;
import java.util.List;

import com.csci5115.group2.planmymeal.database.DataSourceManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class HomeActivity extends Activity implements OnClickListener, OnEditorActionListener {
	
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
        
        
        // add initial meals
        Meal meal1= datasource.createMeal("Sam's new Meal", 5.3, "My Description");
        Tag meal1Tag1 = datasource.createTag("Spicy");
        Tag meal1Tag2 = datasource.createTag("Yummy");
        datasource.createMealTag(meal1.getId(), meal1Tag1.getId());
        datasource.createMealTag(meal1.getId(), meal1Tag2.getId());
        
        Meal meal2 = datasource.createMeal("Sam's second Meal", 3.3, "Description 2");
        
        List<Meal> meals = datasource.getAllMeals();
        
        MealArrayAdapter mealAdapter = new MealArrayAdapter(this, meals);
		ListView listView = (ListView) findViewById(R.id.home_mealListView);
		listView.setAdapter(mealAdapter);
		
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
		/*AutoCompleteTextView search = (AutoCompleteTextView) findViewById(R.id.home_search);
		String[] tags = getResources().getStringArray(R.array.tags_array);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tags);
		search.setAdapter(adapter);
		search.setOnEditorActionListener(this);*/
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.community_cookbook, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	        case R.id.action_settings:
	    		Intent intent = new Intent(this, SettingsActivity.class);
	    		startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
    
	@Override
	public void onClick(View v) {
		int viewId = v.getId();

		switch(viewId) {
			case R.id.home_buttonSettings: {
				onClickButtonSettings(v);
				break;
			}
			case R.id.home_buttonCookbook: {
				onClickButtonCookbook(v);
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
