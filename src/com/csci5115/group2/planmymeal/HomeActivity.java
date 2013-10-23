package com.csci5115.group2.planmymeal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;


public class HomeActivity extends Activity implements OnClickListener, OnEditorActionListener {

    private Context context;

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
    }
    
	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		CharSequence text = null;
		Context context = getApplicationContext();

		switch(viewId) {
			case R.id.buttonSettings: {
				text = "Settings is not yet implemented.";
				break;
			}
			case R.id.buttonCookbook: {
				text = "Community Cookbook is not yet implemented.";
				break;
			}
			case R.id.buttonNewRecipe: {
				text = "New recipe is not yet implemented.";
				break;
			}
			case R.id.buttonNewMeal: {
				text = "New meal is not yet implemented.";
				break;
			}
			default: {
				text = "Unknown button";
			}
		}
		
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.show();
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

}
