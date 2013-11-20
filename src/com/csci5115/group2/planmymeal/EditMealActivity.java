package com.csci5115.group2.planmymeal;

import java.util.LinkedList;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_meal);
		
		view = this;
		
		Intent intent = getIntent();
		String mealName = intent.getStringExtra(HomeActivity.EXTRA_MEAL);
		
		meal = GlobalData.findUserMealByName(mealName);
		
		EditText mealNameTextView = (EditText) findViewById(R.id.edit_meal_mealName);
		mealNameTextView.setText(mealName);
		
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
		
		for(Tag tag : meal.getAllMealTags())
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
