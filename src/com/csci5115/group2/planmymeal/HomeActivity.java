package com.csci5115.group2.planmymeal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
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
