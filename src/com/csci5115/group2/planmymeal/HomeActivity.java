package com.csci5115.group2.planmymeal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class HomeActivity extends Activity implements OnClickListener, OnEditorActionListener {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        // Register button listeners
        Button settingsButton = (Button) findViewById(R.id.buttonSettings);
        settingsButton.setOnClickListener(this);
        
        Button cookbookButton = (Button) findViewById(R.id.buttonCookbook);
        cookbookButton.setOnClickListener(this);
        
        Button newRecipeButton = (Button) findViewById(R.id.buttonNewRecipe);
        newRecipeButton.setOnClickListener(this);
        
        Button newMealButton = (Button) findViewById(R.id.buttonNewMeal);
        newMealButton.setOnClickListener(this);
        
        // Register text listener
        EditText search = (EditText) findViewById(R.id.search);
		search.setOnEditorActionListener(this);
		
		// Set up list view
		String[] stringArray = {
			"Beef Stew",
			"Olivia's Favorite",
			"Beef Stew",
			"Olivia's Favorite",
			"Beef Stew",
			"Olivia's Favorite",
			"Beef Stew",
			"Olivia's Favorite",
			"Beef Stew",
			"Olivia's Favorite",
			"Beef Stew",
			"Olivia's Favorite",
		};
		ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringArray);
		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(adapter);
    }
    
	@Override
	public void onClick(View v) {
		int viewId = v.getId();

		switch(viewId) {
			case R.id.buttonSettings: {
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
