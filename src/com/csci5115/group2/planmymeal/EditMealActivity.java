package com.csci5115.group2.planmymeal;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.csci5115.group2.planmymeal.database.DataSourceManager;

public class EditMealActivity extends Activity implements TextWatcher, OnFocusChangeListener{
	
	public final static String EXTRA_MEAL = "com.csci5115.group2.planmymeal.MEAL";
	public final static String EXTRA_RECIPE = "com.csci5115.group2.planmymeal.RECIPE";
	public final static String BUNDLE_SHOWMEALS = "com.csci5115.group2.planmymeal.BUNDLE_SHOWMEALS";
	public final static String BUNDLE_SHOWRECIPES = "com.csci5115.group2.planmymeal.BUNDLE_SHOWRECIPES";
	
	public Meal meal;
	public EditMealActivity view;
	public Context context;
	public LinearLayout tagContainer;
	private DataSourceManager datasource;
	private RecipeArrayAdapter recipeAdapter; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_meal);
        setTitle("Edit Meal");
		
		// Database Creation
		datasource = new DataSourceManager(this);
		datasource.open();
		
		Intent intent = getIntent();
		long mealId = intent.getLongExtra(HomeActivity.EXTRA_MEAL, 0);
		meal = datasource.getMealById(mealId);
		context = this;
		
		EditText editMealNameView = (EditText) findViewById(R.id.edit_meal_mealName);
		editMealNameView.setText(meal.getName());
		//TODO: Have Meal Name actually change
		Button saveButton = (Button) findViewById(R.id.edit_meal_save_button);
		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				//TODO: save new name of meal
				EditText nameText = (EditText) findViewById(R.id.edit_meal_mealName);
				String mealNameText = nameText.getText().toString();
				datasource.renameMeal(meal.getId(), mealNameText);//NOt working
				//notifyDataSetChanged:not sure how to do this for prev page
				finish();
			}
		}
		);
		        
		// Set up recipes in meal list
		LinkedList<Recipe> recipes = (LinkedList<Recipe>) datasource.getMealRecipes(meal.getId());
						
		RecipeDetailArrayAdapter mealRecipesAdapter = new RecipeDetailArrayAdapter(this, recipes);
		ListView mealListView = (ListView) findViewById(R.id.edit_meal_recipes_in_meal);
		mealListView.setAdapter(mealRecipesAdapter);

		// Set up all user recipes
		List<Recipe> allUserRecipesList = datasource.getAllRecipes();
				
		recipeAdapter = new RecipeArrayAdapter(this, allUserRecipesList, meal, mealRecipesAdapter);
		ListView allUserListView = (ListView) findViewById(R.id.edit_meal_all_recipes);
		allUserListView.setAdapter(recipeAdapter);
				
		// Register text listener
        List<Tag> allTags = datasource.getAllTags();
        List<Recipe> allRecipes = datasource.getAllUserRecipes();
        
        String[] autocompleteStrings = new String[allTags.size() + allRecipes.size()];
        int i = 0;
        int j = 0;
        for (j = 0; j < allRecipes.size(); i++, j++) {
        	autocompleteStrings[i] = allRecipes.get(j).getName();
        }
        for (j = 0; j < allTags.size(); i++, j++) {
        	autocompleteStrings[i] = allTags.get(j).getName();
        }
        
		EditText search = (EditText) findViewById(R.id.edit_meal_search);
		search.addTextChangedListener(this);
		search.setOnFocusChangeListener(this);
		
		
			
		// Set up tags
		tagContainer = (LinearLayout)findViewById(R.id.edit_meal_tag_container);
		
		//Get tags for id
		
		for(final Tag tag : datasource.getMealTags(mealId))
		{
			final RelativeLayout newTagLayout = new RelativeLayout(this);
			
			// Defining the RelativeLayout layout parameters.			
			Button newTag = new Button(this);
			newTag.setText(tag.getName());
			newTag.append("              ");

			Button deleteTag = new Button(this);
			deleteTag.setText("X");
			
			// Defining the layout parameters of the tag Buttons
	        RelativeLayout.LayoutParams tagButtonLayout = new RelativeLayout.LayoutParams(
	                RelativeLayout.LayoutParams.MATCH_PARENT,
	                RelativeLayout.LayoutParams.WRAP_CONTENT);

	        tagButtonLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

			// Defining the layout parameters of the Delete tag Buttons
	        RelativeLayout.LayoutParams deleteButtonLayout = new RelativeLayout.LayoutParams(
	                RelativeLayout.LayoutParams.WRAP_CONTENT,
	                RelativeLayout.LayoutParams.WRAP_CONTENT);
	        deleteButtonLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			
			newTagLayout.addView(newTag, tagButtonLayout);
			newTagLayout.addView(deleteTag, deleteButtonLayout);
			tagContainer.addView(newTagLayout);
			deleteTag.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					newTagLayout.setVisibility(View.GONE);
					datasource.deleteMealTag(tag, meal.getId());
				}
			}
			);
		}
		
		Button addTagButton = (Button) findViewById(R.id.addTagButton);
		addTagButton.setOnClickListener(new View.OnClickListener() {
			

			@Override
			public void onClick(View v)
			{
				final RelativeLayout newTagLayout = new RelativeLayout(context); 
				
				EditText newTagText = (EditText) findViewById(R.id.edit_meal_newTagName);
				String tagText = newTagText.getText().toString();
				newTagText.setText("");
				newTagText.setHint("New Tag Name");
				
				//Add tag to meal
				meal.tags.add(new Tag(tagText));
				final Tag tag = datasource.createTag(tagText);
				datasource.addTagToMeal(tagText, meal.getId());
				
		        Button newTag = new Button(context);
				newTag.setText(tagText);
				newTag.append("              ");
				
				
				Button deleteTag = new Button(context);
				deleteTag.setText("X");
				
				// Defining the layout parameters of the tag Buttons
		        RelativeLayout.LayoutParams tagButtonLayout = new RelativeLayout.LayoutParams(
		                RelativeLayout.LayoutParams.MATCH_PARENT,
		                RelativeLayout.LayoutParams.WRAP_CONTENT);

		        tagButtonLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

				// Defining the layout parameters of the Delete tag Buttons
		        RelativeLayout.LayoutParams deleteButtonLayout = new RelativeLayout.LayoutParams(
		                RelativeLayout.LayoutParams.WRAP_CONTENT,
		                RelativeLayout.LayoutParams.WRAP_CONTENT);
		        deleteButtonLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				
				newTagLayout.addView(newTag, tagButtonLayout);
				newTagLayout.addView(deleteTag, deleteButtonLayout);
				tagContainer.addView(newTagLayout);
				deleteTag.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						newTagLayout.setVisibility(View.GONE);
						datasource.deleteMealTag(tag, meal.getId());
						
					}
				}
				);
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

	@Override
	public void afterTextChanged(Editable arg0) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		recipeAdapter.getFilter().filter(s);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
	}

}