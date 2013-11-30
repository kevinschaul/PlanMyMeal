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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class CookActivity extends Activity {
	
	// Databases
	private DataSourceManager datasource;
	
	public LinearLayout recipeHolder0;
	public LinearLayout recipeHolder1;
	public LinearLayout recipeHolder2;
	public LinearLayout recipeHolder3;
	public Recipe recipe0;
	public Recipe recipe1;
	public Recipe recipe2;
	public Recipe recipe3;
	
	public int numRecipes;
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
		
        numRecipes = 2;
        
        Meal meal = datasource.getMealById(mealId);
		
		List<Recipe> recipes = datasource.getMealRecipes(mealId);// meal.getRecipes();
		
		CreateFakeRecipes();
		 
		recipe0 = recipes.get(0);
		recipe0.setIngredients(datasource.getRecipeIngredients(recipe0.id));
		recipe0.setSteps(datasource.getRecipeSteps(recipe0.id));
		//Recipe secondRecipe = recipes.get(1); //new Recipe();
		//secondRecipe.setIngredients(datasource.getRecipeIngredients(secondRecipe.id));
		//secondRecipe.setSteps(datasource.getRecipeSteps(secondRecipe.id));
		
		
		
		recipeHolder0 = (LinearLayout)findViewById(R.id.RecipeHolder0);
		recipeHolder1 = (LinearLayout)findViewById(R.id.RecipeHolder1);
		recipeHolder2 = (LinearLayout)findViewById(R.id.RecipeHolder2);
		recipeHolder3 = (LinearLayout)findViewById(R.id.RecipeHolder3);
		
		
		
		populateRecipes();
		List<RecipeStep> newSteps = recipe0.getSteps();
		newSteps.remove(0);
		recipe0.setSteps(newSteps);

		
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
	
	public void populateRecipes(){
		switch(numRecipes){
			case 1:
				populateFirstRecipe();
				break;
			case 2:
				populateFirstRecipe();
				populateSecondRecipe();
				break;
			case 3:
				populateFirstRecipe();
				populateSecondRecipe();
				populateThirdRecipe();
				break;
			case 4:
				populateFirstRecipe();
				populateSecondRecipe();
				populateThirdRecipe();
				populateFourthRecipe();
				break;
		}
	}
	
	public void populateFirstRecipe(){
		TextView myText0 = new TextView(this);
		myText0.setText(recipe0.getName());
		IngredientArrayAdapter ingredientsAdapter0 = new IngredientArrayAdapter(this, (LinkedList)recipe0.getIngredients());
		GridView recipeIngredients0 = new GridView(this);
		GridView cookIngredients = (GridView) findViewById(R.id.recipeCookIngredients);
		recipeIngredients0.setNumColumns(2);
		recipeIngredients0.setLayoutParams(cookIngredients.getLayoutParams());
		recipeIngredients0.setAdapter(ingredientsAdapter0);
		RecipeStepsArrayAdapter recipeStepAdapter0 = new RecipeStepsArrayAdapter(this, (LinkedList)recipe0.getSteps());
		ListView recipeStepsListView0 = new ListView(this);
		ListView cookSteps =(ListView) findViewById(R.id.recipeCookSteps);
		recipeStepsListView0.setLayoutParams(cookSteps.getLayoutParams());
		recipeStepsListView0.setAdapter(recipeStepAdapter0);
		
		recipeHolder0.addView(myText0);
		recipeHolder0.addView(recipeIngredients0);
		recipeHolder0.addView(recipeStepsListView0);
	}
	
	public void populateSecondRecipe(){
		TextView myText1 = new TextView(this);
		myText1.setText(recipe1.getName());
		IngredientArrayAdapter ingredientsAdapter1 = new IngredientArrayAdapter(this, (LinkedList)recipe1.getIngredients());
		GridView recipeIngredients1 = new GridView(this);
		GridView cookIngredients = (GridView) findViewById(R.id.recipeCookIngredients);
		recipeIngredients1.setNumColumns(2);
		recipeIngredients1.setLayoutParams(cookIngredients.getLayoutParams());
		recipeIngredients1.setAdapter(ingredientsAdapter1);
		RecipeStepsArrayAdapter recipeStepAdapter1= new RecipeStepsArrayAdapter(this, (LinkedList)recipe1.getSteps());
		ListView recipeStepsListView1 = new ListView(this);
		ListView cookSteps =(ListView) findViewById(R.id.recipeCookSteps);
		recipeStepsListView1.setLayoutParams(cookSteps.getLayoutParams());
		recipeStepsListView1.setAdapter(recipeStepAdapter1);
		
		recipeHolder1.addView(myText1);
		recipeHolder1.addView(recipeIngredients1);
		recipeHolder1.addView(recipeStepsListView1);
		recipeHolder1.setVisibility(View.VISIBLE);
	}

	public void populateThirdRecipe(){
		TextView myText2 = new TextView(this);
		myText2.setText(recipe2.getName());
		IngredientArrayAdapter ingredientsAdapter2 = new IngredientArrayAdapter(this, (LinkedList)recipe2.getIngredients());
		GridView recipeIngredients2 = new GridView(this);
		GridView cookIngredients = (GridView) findViewById(R.id.recipeCookIngredients);
		recipeIngredients2.setNumColumns(2);
		recipeIngredients2.setLayoutParams(cookIngredients.getLayoutParams());
		recipeIngredients2.setAdapter(ingredientsAdapter2);
		RecipeStepsArrayAdapter recipeStepAdapter2 = new RecipeStepsArrayAdapter(this, (LinkedList)recipe2.getSteps());
		ListView recipeStepsListView2 = new ListView(this);
		ListView cookSteps =(ListView) findViewById(R.id.recipeCookSteps);
		recipeStepsListView2.setLayoutParams(cookSteps.getLayoutParams());
		recipeStepsListView2.setAdapter(recipeStepAdapter2);
		
		recipeHolder2.addView(myText2);
		recipeHolder2.addView(recipeIngredients2);
		recipeHolder2.addView(recipeStepsListView2);
		recipeHolder2.setVisibility(View.VISIBLE);
	}
	
	public void populateFourthRecipe(){
		TextView myText3 = new TextView(this);
		myText3.setText(recipe3.getName());
		IngredientArrayAdapter ingredientsAdapter3 = new IngredientArrayAdapter(this, (LinkedList)recipe3.getIngredients());
		GridView recipeIngredients3 = new GridView(this);
		GridView cookIngredients = (GridView) findViewById(R.id.recipeCookIngredients);
		recipeIngredients3.setNumColumns(2);
		recipeIngredients3.setLayoutParams(cookIngredients.getLayoutParams());
		recipeIngredients3.setAdapter(ingredientsAdapter3);
		RecipeStepsArrayAdapter recipeStepAdapter3 = new RecipeStepsArrayAdapter(this, (LinkedList)recipe3.getSteps());
		ListView recipeStepsListView3 = new ListView(this);
		ListView cookSteps =(ListView) findViewById(R.id.recipeCookSteps);
		recipeStepsListView3.setLayoutParams(cookSteps.getLayoutParams());
		recipeStepsListView3.setAdapter(recipeStepAdapter3);	
		
		recipeHolder3.addView(myText3);
		recipeHolder3.addView(recipeIngredients3);
		recipeHolder3.addView(recipeStepsListView3);
		recipeHolder3.setVisibility(View.VISIBLE);
	}
	
	public void CreateFakeRecipes(){
		
		LinkedList<Ingredient> ingredients1 = new LinkedList<Ingredient>();
		LinkedList<RecipeStep> recipeSteps1 = new LinkedList<RecipeStep>();
		
		LinkedList<Ingredient> ingredients2 = new LinkedList<Ingredient>();
		LinkedList<RecipeStep> recipeSteps2 = new LinkedList<RecipeStep>();
		
		LinkedList<Ingredient> ingredients3 = new LinkedList<Ingredient>();
		LinkedList<RecipeStep> recipeSteps3 = new LinkedList<RecipeStep>();
		
		Ingredient oliveOil = new Ingredient("Olive Oil", 3, "tblsp");
		Ingredient sage = new Ingredient("Sage", 1, "Bunch");
		Ingredient thyme = new Ingredient("Thyme", 1, "Bunch");
		Ingredient onion = new Ingredient("Onoin", 1, "Large");
		Ingredient apples = new Ingredient("Apples", 3, "Whole");
		Ingredient pecans = new Ingredient("Pecans", 1, "Cup");
		Ingredient eggs = new Ingredient("Eggs", 2, "Whole");
		Ingredient cream = new Ingredient("Cream", 1, "Cup");
		Ingredient chickenStock = new Ingredient("Chicken Stock", 2, "Cup");
		Ingredient sourdoughBread = new Ingredient("Sourdough Bread", 5, "Cups");
		
		
		ingredients1.add(oliveOil);
		ingredients1.add(sage);
		ingredients1.add(thyme);
		ingredients1.add(onion);
		ingredients1.add(apples);
		ingredients1.add(pecans);
		ingredients1.add(eggs);
		ingredients1.add(cream);
		ingredients1.add(chickenStock);
		ingredients1.add(sourdoughBread);
		
		RecipeStep one = new RecipeStep();
		one.setInstructions("Set a large saute pan over medium heat and add olive oil, sage and thyme sprigs");
		RecipeStep two = new RecipeStep();
		two.setInstructions("Remove the sage and thyme and set aside on a paper towel to drain");
		RecipeStep three = new RecipeStep();
		three.setInstructions("Season with salt and pepper");
		RecipeStep four = new RecipeStep();
		four.setInstructions("Remove onions from pan and add apples");
		RecipeStep five = new RecipeStep();
		five.setInstructions("Crush the pecans and add to the pan");
		RecipeStep six = new RecipeStep();
		six.setInstructions("Add more oil, if needed and season with salt and pepper");
		RecipeStep seven = new RecipeStep();
		seven.setInstructions("Gently saute until pecans are lightly toasted and apples are just cooked slightly");
		RecipeStep eight = new RecipeStep();
		eight.setInstructions("whisk together egg, cream, chicken stock, and salt and pepper, to taste");
		RecipeStep nine = new RecipeStep();
		nine.setInstructions("Add torn sourdough, caramelized onions, apples, pecans and chopped parsley");
		RecipeStep ten = new RecipeStep();
		ten.setInstructions("Using a wooden spoon, mix the stuffing until well combined");
		recipeSteps1.add(one);
		recipeSteps1.add(two);
		recipeSteps1.add(three);
		recipeSteps1.add(four);
		recipeSteps1.add(five);
		recipeSteps1.add(six);
		recipeSteps1.add(seven);
		recipeSteps1.add(eight);
		recipeSteps1.add(nine);
		recipeSteps1.add(ten);
		
		
		recipe1 = new Recipe();
		recipe2 = new Recipe();
		recipe3 = new Recipe();
		
		recipe1.setName("Apple Pecan Stuffing");
		recipe1.setIngredients(ingredients1);
		recipe1.setSteps(recipeSteps1);
		
		Ingredient butter = new Ingredient("Butter", 1, "Stick");
		Ingredient lemon = new Ingredient("Lemon", 1, "Whole");
		thyme = new Ingredient("Thyme", 1, "tsp");
		Ingredient turkey = new Ingredient("Turkey", 1, "Whole");
		Ingredient salt = new Ingredient("Kosher Salt", 1, "tsp");
		Ingredient blackPepper = new Ingredient("Black Pepper", 1, "tsp");
		thyme = new Ingredient("Thyme", 1, "Bunch");
		lemon = new Ingredient("Lemon", 1, "Whole");
		onion = new Ingredient("Onion", 1, "Large");
		Ingredient garlic = new Ingredient("Garlic", 1, "Head");

		ingredients2.add(butter);
		ingredients2.add(lemon);
		ingredients2.add(thyme);
		ingredients2.add(turkey);
		ingredients2.add(salt);
		ingredients2.add(blackPepper);
		ingredients2.add(thyme);
		ingredients2.add(lemon);
		ingredients2.add(onion);
		ingredients2.add(garlic);
		
		
		one = new RecipeStep();
		one.setInstructions("Preheat the oven to 350 degrees F");
		two = new RecipeStep();
		two.setInstructions("Melt the butter in a small saucepan");
		three = new RecipeStep();
		three.setInstructions("Add zest and juice of lemon and thyme leaves to butter mixture");
		four = new RecipeStep();
		four.setInstructions("Take giblets out of Turkey and wash Turkey");
		five = new RecipeStep();
		five.setInstructions("Clean and dry Turkey");
		six = new RecipeStep();
		six.setInstructions("Place Turkey in large roasting pan");
		seven = new RecipeStep();
		seven.setInstructions("Liverally apply Salt and Pepper");
		eight = new RecipeStep();
		eight.setInstructions("Stuff the cavity with thyme, halved lemon, quartered onion, and garlic");
		nine = new RecipeStep();
		nine.setInstructions("Brush outside of the turkey with butter mixed with salt and pepper");
		ten = new RecipeStep();
		ten.setInstructions("Ties the legs of the turkey together with string");
		RecipeStep eleven = new RecipeStep();
		eleven.setInstructions("Roast turky for 2 1/2 hours or until juices run clear");
		RecipeStep twelve = new RecipeStep();
		twelve.setInstructions("Remove Turkey and let it rest for 20 minutes before serving");
		
		recipeSteps2.add(one);
		recipeSteps2.add(two);
		recipeSteps2.add(three);
		recipeSteps2.add(four);
		recipeSteps2.add(five);
		recipeSteps2.add(six);
		recipeSteps2.add(seven);
		recipeSteps2.add(eight);
		recipeSteps2.add(nine);
		recipeSteps2.add(ten);
		recipeSteps2.add(eleven);
		recipeSteps2.add(twelve);
		
		recipe2.setName("Roast Turkey");
		recipe2.setSteps(recipeSteps2);
		recipe2.setIngredients(ingredients2);
		
		Ingredient creamOfMushroomSoup = new Ingredient("Cream of Mushroom Soup", 10, "Ounces");
		Ingredient milk = new Ingredient("Milk", 1, "Cup");
		Ingredient soySauce = new Ingredient("Soy Sauce", 1, "tsp");
		blackPepper = new Ingredient("Black Pepper", 1, "Dash");
		Ingredient greenBeans = new Ingredient("Green Beans", 4, "Cup");
		Ingredient friedOnions = new Ingredient("FrenchFried Onions", 1, "Cup");
		
		ingredients3.add(creamOfMushroomSoup);
		ingredients3.add(milk);
		ingredients3.add(soySauce);
		ingredients3.add(blackPepper);
		ingredients3.add(greenBeans);
		ingredients3.add(friedOnions);
		
		one = new RecipeStep();
		one.setInstructions("Stir Soup, Milk, Soy Sauce, Black Pepper, Beans and 2/3 of the Onions in casserole pan");
		two = new RecipeStep();
		two.setInstructions("Bake at 350 degrees F for 25 minutes");
		three.setInstructions("Stir Bean mixture and add remaining Onions");
		four.setInstructions("Bake for 5 more minutes");
		
		recipeSteps3.add(one);
		recipeSteps3.add(two);
		recipeSteps3.add(three);
		recipeSteps3.add(four);
		
		recipe3.setName("Green Bean Casserole");
		recipe3.setIngredients(ingredients3);
		recipe3.setSteps(recipeSteps3);
		
	}
}
