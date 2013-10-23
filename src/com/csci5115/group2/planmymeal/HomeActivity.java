package com.csci5115.group2.planmymeal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;


public class HomeActivity extends Activity implements OnEditorActionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        TextView search = (TextView) findViewById(R.id.search);
		search.setOnEditorActionListener(this);
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
    
    public void openSettings(View view) {
    	Context context = getApplicationContext();
    	CharSequence text = "Settings is not yet implemented.";
    	int duration = Toast.LENGTH_SHORT;
    	
    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
    }
    
    public void openCookbook(View view) {
    	Context context = getApplicationContext();
    	CharSequence text = "Community Cookbook is not yet implemented.";
    	int duration = Toast.LENGTH_SHORT;
    	
    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
    }
    
    public void newRecipe(View view) {
    	Context context = getApplicationContext();
    	CharSequence text = "New recipe is not yet implemented.";
    	int duration = Toast.LENGTH_SHORT;
    	
    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
    }
    
    public void newMeal(View view) {
    	Context context = getApplicationContext();
    	CharSequence text = "New meal is not yet implemented.";
    	int duration = Toast.LENGTH_SHORT;
    	
    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
    }
    
}
