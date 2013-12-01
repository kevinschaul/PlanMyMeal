package com.csci5115.group2.planmymeal;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.csci5115.group2.planmymeal.database.DataSourceManager;

// Samantha Oyen: This used to implement OnClickListener... had to take out.
public class CommunityCookbookActivity extends FragmentActivity implements CookableListFragment.Callbacks, TextWatcher, OnFocusChangeListener {
	
	public final static String EXTRA_MEAL = "com.csci5115.group2.planmymeal.MEAL";
	public final static String EXTRA_RECIPE = "com.csci5115.group2.planmymeal.RECIPE";
	public final static String BUNDLE_SHOWMEALS = "com.csci5115.group2.planmymeal.BUNDLE_SHOWMEALS";
	public final static String BUNDLE_SHOWRECIPES = "com.csci5115.group2.planmymeal.BUNDLE_SHOWRECIPES";
	public final static String TAG = "CommunityCookbookActivity";
	
	private ArrayAdapter<Cookable> adapter;
	// Databases
	private DataSourceManager datasource;
	
	private static LinearLayout CommColumn0;
	private static LinearLayout CommColumn1;
	private static LinearLayout CommColumn2;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_cookbook); ////////////////////// Was activity_home
        setTitle("Community Cookbook");
        // Database Creation
        datasource = new DataSourceManager(this);
        datasource.open();
        
        // List items should be given the 'activated' state when touched.
 		/*((CCListFragment) getSupportFragmentManager().findFragmentById(
 				R.id.CC_cookable_list)).setActivateOnItemClick(true);     ///////// change?
*/        
        CCListFragment fragment = (CCListFragment) getSupportFragmentManager().findFragmentById(
 				R.id.CC_cookable_list);
        fragment.setActivateOnItemClick(true);
        adapter = (ArrayAdapter<Cookable>) fragment.getListAdapter();
        
        // Register text listener
		/*AutoCompleteTextView search = (AutoCompleteTextView) findViewById(R.id.CC_search);  /// was home_search
		String[] tags = getResources().getStringArray(R.array.tags_array);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tags);
		search.setAdapter(adapter);
		search.setOnEditorActionListener(this);*/
        
        List<Tag> tags = datasource.getAllTags();
        List<Meal> meals = datasource.getAllUserMeals();
        List<Recipe> recipes = datasource.getAllUserRecipes();
        
        String[] autocompleteStrings = new String[tags.size() + meals.size() + recipes.size()];
        int i = 0;
        int j = 0;
        for (j = 0; j < meals.size(); i++, j++) {
        	autocompleteStrings[i] = meals.get(j).getName();
        }
        for (j = 0; j < recipes.size(); i++, j++) {
        	autocompleteStrings[i] = recipes.get(j).getName();
        }
        for (j = 0; j < tags.size(); i++, j++) {
        	autocompleteStrings[i] = tags.get(j).getName();
        }
        
		EditText search = (EditText) findViewById(R.id.CC_search);
		search.addTextChangedListener(this);
		search.setOnFocusChangeListener(this);
		
		CommColumn0 = (LinearLayout) findViewById(R.id.CC_column_0); ///////// in xml???
		CommColumn1 = (LinearLayout) findViewById(R.id.CC_column_1);
		CommColumn2 = (LinearLayout) findViewById(R.id.CC_column_2);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		datasource.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.community_cookbook, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
		Intent intent;
	    switch (item.getItemId()) {
	    case R.id.action_settings:
    		intent = new Intent(this, SettingsActivity.class);
    		startActivity(intent);
            return true;
	        
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	/*@Override
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
	}*/
	
	@Override
	public void onItemSelected(String type, long id) {
		
		// No matter which type of item is selected, we want to split the view
		// evenly between col0 and col1.
		showColumns(2);
		
		if (type == "Meal") {
			Log.v(CommunityCookbookActivity.TAG, "Meal selected");
			Bundle arguments = new Bundle();
			arguments.putLong(CCMealDetailFragment.ARG_ITEM_ID, id);
			CCMealDetailFragment fragment = new CCMealDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.CC_col1_container, fragment).commit();  /// changed home to comm
		} else if (type == "Recipe") {
			Log.v(CommunityCookbookActivity.TAG, "Recipe selected");
			Bundle arguments = new Bundle();
			arguments.putLong(CCRecipeDetailFragment.ARG_ITEM_ID, id);
			CCRecipeDetailFragment fragment = new CCRecipeDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.CC_col1_container, fragment).commit();  ///// Same
		}
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
	  
	  
	  public static void showColumns(int numberOfColumns) {
		  
		  if (CommColumn0 != null && CommColumn1 != null && CommColumn2 != null) {
			  LinearLayout.LayoutParams params0;
			  LinearLayout.LayoutParams params1;
			  LinearLayout.LayoutParams params2;
			  
			  switch (numberOfColumns) {
		        case 1:
		        	params0 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 6);
		        	params1 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0);
		        	params2 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0);
		        	CommColumn0.setLayoutParams(params0);
		        	CommColumn1.setLayoutParams(params1);
		        	CommColumn1.setVisibility(View.GONE);
		        	CommColumn2.setLayoutParams(params2);
		        	CommColumn2.setVisibility(View.GONE);
		            break;
		        case 2:
		        	params0 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 3);
		        	params1 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 3);
		        	params2 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 0);
		        	CommColumn0.setLayoutParams(params0);
		        	CommColumn1.setLayoutParams(params1);
		        	CommColumn1.setVisibility(View.VISIBLE);
		        	CommColumn2.setLayoutParams(params2);
		        	CommColumn2.setVisibility(View.GONE);
		        	break;
		        case 3:
		        	params0 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 2);
		        	params1 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 2);
		        	params2 = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 2);
		        	CommColumn0.setLayoutParams(params0);
		        	CommColumn1.setLayoutParams(params1);
		        	CommColumn1.setVisibility(View.VISIBLE);
		        	CommColumn2.setLayoutParams(params2);
		        	CommColumn2.setVisibility(View.VISIBLE);
		        	break;
		        default:
		            break;
			  }
		  }
	  }
	  
	  @Override
		public void afterTextChanged(Editable arg0) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			adapter.getFilter().filter(s);
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			switch(v.getId()) {
			case R.id.CC_search:
				if (hasFocus) {
					showColumns(1);
				}
				break;
			}
		}
}



/*package com.csci5115.group2.planmymeal;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class CommunityCookbookActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_cookbook);
		
		// Set up list view
		// TODO Use a database call to populate this list view
		Meal[] mealArray = new Meal[10];
		
		for (int i = 0; i < 10; i++) {
			if (i % 2 == 0) {
				mealArray[i] = new Meal("Beef Stew", 1.18, new LinkedList<Tag>());
			} else {
				mealArray[i] = new Meal("Olivia's Favorite", 1.40, new LinkedList<Tag>());
			}
		}

		CommunityCookbookArrayAdapter communityCookbookAdapter = new CommunityCookbookArrayAdapter(this, mealArray);
		ListView listView = (ListView) findViewById(R.id.communityCookbook_mealListView);
		listView.setAdapter(communityCookbookAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				  Meal meal = (Meal) parent.getItemAtPosition(position);
			    Toast.makeText(getApplicationContext(),
			      meal.getName(), Toast.LENGTH_LONG)
			      .show();
			  }
			}); 
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

}
*/