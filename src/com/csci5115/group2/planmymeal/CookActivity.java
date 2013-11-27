package com.csci5115.group2.planmymeal;

import java.util.LinkedList;
import java.util.List;

import com.csci5115.group2.planmymeal.database.DataSourceManager;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class CookActivity extends Activity {
	
	// Databases
	private DataSourceManager datasource;
	
	public LinearLayout recipeContainer;
	public RelativeLayout currentRecipe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cook);
		
		Intent intent = getIntent();
		Long mealId = intent.getLongExtra(HomeActivity.EXTRA_MEAL, 0);
		
        // Database Creation
        datasource = new DataSourceManager(this);
        datasource.open();
		
        Meal meal = datasource.getMealById(mealId);
		
		List<Recipe> recipes = datasource.getMealRecipes(mealId);// meal.getRecipes();
		
		
		
		Recipe firstRecipe = recipes.get(0);
		firstRecipe.setIngredients(datasource.getRecipeIngredients(firstRecipe.id));
		firstRecipe.setSteps(datasource.getRecipeSteps(firstRecipe.id));
		Recipe secondRecipe = recipes.get(1); //new Recipe();
		secondRecipe.setIngredients(datasource.getRecipeIngredients(secondRecipe.id));
		secondRecipe.setSteps(datasource.getRecipeSteps(secondRecipe.id));
		//Recipe recipessss = recipes.get(1);
		//String recipeName = recipe.getName();
		//recipe.setName("Test Recipe");
		LinkedList<Ingredient> ingredients = new LinkedList<Ingredient>();
		
		
		Ingredient egg = new Ingredient("Egg", 10, "test");
		Ingredient bread = new Ingredient("Bread", 10, "test");
		Ingredient bacon = new Ingredient("Bacon", 10, "test");
		Ingredient potato = new Ingredient("Potato", 10, "test");
		Ingredient chicken = new Ingredient("Chicken", 10, "test");
		Ingredient beef = new Ingredient("Beef", 10, "test");
		Ingredient tomato = new Ingredient("Tomato", 10, "test");
		Ingredient pickles = new Ingredient("Pickle", 10, "test");
		ingredients.add(egg);
		ingredients.add(bread);
		ingredients.add(bacon);
		ingredients.add(potato);
		ingredients.add(chicken);
		ingredients.add(beef);
		ingredients.add(tomato);
		ingredients.add(pickles);
		
		RecipeStep one = new RecipeStep();
		one.setInstructions("Testererer");
		RecipeStep two = new RecipeStep();
		two.setInstructions("Test");
		RecipeStep three = new RecipeStep();
		three.setInstructions("Test");
		RecipeStep four = new RecipeStep();
		four.setInstructions("Test");
		RecipeStep five = new RecipeStep();
		five.setInstructions("Test");
		RecipeStep six = new RecipeStep();
		six.setInstructions("Test");
		RecipeStep seven = new RecipeStep();
		seven.setInstructions("Test");
		RecipeStep eight = new RecipeStep();
		eight.setInstructions("Testing");
		LinkedList<RecipeStep> recipeSteps = new LinkedList<RecipeStep>();
		recipeSteps.add(one);
		recipeSteps.add(two);
		recipeSteps.add(three);
		recipeSteps.add(four);
		recipeSteps.add(five);
		recipeSteps.add(six);
		recipeSteps.add(seven);
		recipeSteps.add(eight);
		//recipe.setSteps(recipeSteps);
		//recipe.setIngredients(ingredients);
		LinkedList<Recipe> recipeList = new LinkedList<Recipe>();
		recipeList.add(firstRecipe);
		recipeList.add(secondRecipe);
		
		recipeContainer = (LinearLayout)findViewById(R.id.cookMealRecipeHolder);
		
		//ListView recipeContainer = (ListView) findViewById(R.id.myListView);
		//CookMealRecipeArrayAdapter cMRArrayAdapter = new CookMealRecipeArrayAdapter(this, recipeList);
		//recipeContainersetAdapter(cMRArrayAdapter);
		
		//
		//recipeContainer.setAdapter(cMRArrayAdapter);
		//(LinkedList)recipe.getIngredients();
		//List<RecipeStep> steps = recipe.getSteps();
		//String[] stepsss = {"test", "More"};
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_meal);
		
		//Intent intent = getIntent();
		//String mealName = intent.getStringExtra(HomeActivity.EXTRA_MEAL);

		//LinearLayout myLayout = (LinearLayout)View.inflate(getApplicationContext(), R.id.myLinearLayout, null); //root)findViewById(R.id.myLinearLayout);
		//LinearLayout myContainer = (LinearLayout)findViewById(R.id.cookMealRecipeHolder);
		
		TextView myText = new TextView(this);
		//TextView CookName = (TextView)findViewById(R.id.recipeCookName);
		//myText.setLayoutParams(CookName.getLayoutParams());
		myText.setText(firstRecipe.getName());
		//TextView mySecondText = (TextView)findViewById(R.id.recipeName);
		//myText.setText("Recipe 1");
		//mySecondText.setText("Recipe 2");
		
		IngredientArrayAdapter ingredientsAdapter = new IngredientArrayAdapter(this, (LinkedList)firstRecipe.getIngredients());
		GridView recipeIngredients = new GridView(this);
	
		GridView cookIngredients = (GridView) findViewById(R.id.recipeCookIngredients);
		recipeIngredients.setNumColumns(2);
		recipeIngredients.setLayoutParams(cookIngredients.getLayoutParams());
		recipeIngredients.setAdapter(ingredientsAdapter);
		
		RecipeStepsArrayAdapter recipeStepAdapter = new RecipeStepsArrayAdapter(this, (LinkedList)firstRecipe.getSteps());
		ListView secondRecipeStepsListView = new ListView(this);
		ListView recipeStepsListView = new ListView(this);
		ListView cookSteps =(ListView) findViewById(R.id.recipeCookSteps);
		recipeStepsListView.setLayoutParams(cookSteps.getLayoutParams());
		recipeStepsListView.setAdapter(recipeStepAdapter);
		secondRecipeStepsListView.setLayoutParams(cookSteps.getLayoutParams());
		secondRecipeStepsListView.setAdapter(recipeStepAdapter);
		
		//currentRecipe = (RelativeLayout)findViewById(R.id.row_cook_recipe_container);
		//currentRecipe.addView(myText);
		//currentRecipe.addView(recipeIngredients);
		//currentRecipe.addView(recipeStepsListView);
		
		//FrameLayout curRecipe = (FrameLayout) findViewById(R.id.row_cook_recipe_container);
		
		recipeContainer.addView(myText);
		recipeContainer.addView(recipeIngredients);
		recipeContainer.addView(recipeStepsListView);
		
		//recipeContainer.addView(myText);
		//recipeContainer.addView(recipeIngredients);
		//recipeContainer.addView(recipeStepsListView);
		//recipeContainer.addView(secondRecipeStepsListView);
		//LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			//	LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//recipeContainer.addView(curRecipe);
		
		//IngredientArrayAdapter secondAdapter = new IngredientArrayAdapter(this, ingredients);
		//GridView secondRecipe = (GridView) findViewById(R.id.recipeCookIngredients);
		//secondRecipe.setAdapter(secondAdapter);
		
		
		//HorizontalScrollView hSV = (HorizontalScrollView) findViewById(R.id.myListView);
		//hSV.addView(myText, R.layout.row_cook_recipe_container);
		
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
	public void onDestroy() {
		super.onDestroy();
		
		datasource.close();
	}
}
