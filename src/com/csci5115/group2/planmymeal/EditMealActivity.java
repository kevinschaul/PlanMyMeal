package com.csci5115.group2.planmymeal;

import java.util.LinkedList;

import com.csci5115.group2.planmymeal.database.DataSourceManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class EditMealActivity extends Activity {
	
	public Meal meal;
	public EditMealActivity view;
	public LinearLayout tagContainer;
	private DataSourceManager datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_meal);
		
		view = this;
		
		// Database Creation
		datasource = new DataSourceManager(this);
		datasource.open();
		
		Intent intent = getIntent();
		long mealId = intent.getLongExtra(HomeActivity.EXTRA_MEAL, 0);
		
		meal = datasource.getMealById(mealId);
		
		EditText mealNameTextView = (EditText) findViewById(R.id.edit_meal_mealName);
		mealNameTextView.setText(meal.getName());
		
		// Set up recipes in meal list
		LinkedList<Recipe> recipes = (LinkedList<Recipe>) meal.getRecipes();
		
		MealRecipesArrayAdapter mealRecipesAdapter = new MealRecipesArrayAdapter(this, recipes);
		ListView mealListView = (ListView) findViewById(R.id.edit_meal_recipes_in_meal);
		mealListView.setAdapter(mealRecipesAdapter);

		// Set up all user recipes
		LinkedList<Recipe> allUserRecipesArray = GlobalData.userRecipes;
				
		RecipeArrayAdapter recipeAdapter = new RecipeArrayAdapter(this, allUserRecipesArray, meal, mealRecipesAdapter);
		ListView allUserListView = (ListView) findViewById(R.id.edit_meal_allUserRecipes);
		allUserListView.setAdapter(recipeAdapter);
		
		// Set up tags
		tagContainer = (LinearLayout)findViewById(R.id.edit_meal_tag_container);
		
		//Get tags for id
		
		for(Tag tag : datasource.getMealTags(mealId))
		{
			Button newTag = new Button(this);
			//newTag.setLayoutParams(params);
			newTag.setText(tag.getName());
			tagContainer.addView(newTag);
		}
		
		Button addTagButton = (Button) findViewById(R.id.addTagButton);
		addTagButton.setOnClickListener(new View.OnClickListener() {
			

			@Override
			public void onClick(View v)
			{
				EditText newTagText = (EditText) findViewById(R.id.edit_meal_newTagName);
				String tagText = newTagText.getText().toString();
				//Add tag to meal
				meal.tags.add(new Tag(tagText));
				
				
				Button newTag = new Button(view);
				//newTag.setLayoutParams(params);
				newTag.setText(tagText);
				tagContainer.addView(newTag);
			}
		}
		);
		
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
