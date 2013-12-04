package com.csci5115.group2.planmymeal;

import java.util.LinkedList;
import java.util.List;

import com.csci5115.group2.planmymeal.database.DataSourceManager;

import android.R.string;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Pair;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class CookActivity extends Activity implements OnClickListener {
	
	// Databases
	private DataSourceManager datasource;
	public HorizontalScrollView allRecipes;
	
	public LinearLayout recipeHolder0;
	public LinearLayout recipeHolder1;
	public LinearLayout recipeHolder2;
	public LinearLayout recipeHolder3;
	public Recipe recipe0;
	public Recipe recipe1;
	public Recipe recipe2;
	public Recipe recipe3;
	public LinkedList<Recipe> allRecipesList;
	RecipeStepsArrayAdapter recipeStepAdapter0;
	RecipeStepsArrayAdapter recipeStepAdapter1;
	RecipeStepsArrayAdapter recipeStepAdapter2;
	RecipeStepsArrayAdapter recipeStepAdapter3;
	
	public RecipeStep currentStep;
	public TextView currentStepDescription;
	public TextView currentStepTime;
	public int currentRecipe;
	public Button nextStep;
	public CountDownTimer currentStepTimer;
	
	public LinkedList<string> StepOrder;
	
	public int numRecipes;
	
	public long timeTilFinished;
	
	public Uri notification;
	public Ringtone r;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cook);
		allRecipes = (HorizontalScrollView) findViewById(R.id.myListView);
		Intent intent = getIntent();
		Long mealId = intent.getLongExtra(HomeActivity.EXTRA_MEAL, 0);
		
		notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		r = RingtoneManager.getRingtone(getApplicationContext(), notification);
		
		
        // Database Creation
        datasource = new DataSourceManager(this);
        datasource.open();
		
        numRecipes = 4;
        
        //Meal meal = datasource.getMealById(mealId);
		
		//List<Recipe> recipes = datasource.getMealRecipes(mealId);// meal.getRecipes();
		
		recipe0 = new Recipe();
		CreateFakeRecipes();	
		
		
		recipeHolder0 = (LinearLayout)findViewById(R.id.RecipeHolder0);
		recipeHolder1 = (LinearLayout)findViewById(R.id.RecipeHolder1);
		recipeHolder2 = (LinearLayout)findViewById(R.id.RecipeHolder2);
		recipeHolder3 = (LinearLayout)findViewById(R.id.RecipeHolder3);
		
		
		
		populateRecipes();
		//removeRecipeStep(0);
		//removeRecipeStep(1);
		
		
		currentStepDescription = (TextView)findViewById(R.id.currentStepDescription);
		currentStepTime = (TextView)findViewById(R.id.currentStepTime);
		nextStep = (Button)findViewById(R.id.button0);
		nextStep.setOnClickListener(this);	
		
		currentStep = new RecipeStep();
		currentStep.setTime(1);
		long time = (long) currentStep.getTime()*60000;
		currentStepTimer = createTimer(time);
		setCurrentStep();
		
		//currentStepTimer = createTimer();
		//currentStepTimer.start();
				
				/*) new CountDownTimer(60000, 1000) {
			public void onTick(long millisUntilFinished) {
				currentStepTime.setText(makeTimeString(millisUntilFinished));
			}

			public void onFinish() {
				currentStepTime.setText("done!");
				nextStep.callOnClick();
			}
		}.start();
		*/
	
		
		
	}
	
	public CountDownTimer createTimer(long time)
	{
		 CountDownTimer newTimer = new CountDownTimer(time, 1000) {
				public void onTick(long millisUntilFinished) {
					currentStepTime.setText(makeTimeString(millisUntilFinished));
					timeTilFinished = millisUntilFinished;
				}

				public void onFinish() {
					currentStepTime.setText("done!");
					r.play();
					nextStep.callOnClick();
				}
		 };
		return newTimer;		
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
	
	public void removeRecipeStep(int recipe){
		List<RecipeStep> newSteps = new LinkedList<RecipeStep>();
		
		switch(recipe){
			case 0:
				newSteps = recipe0.getSteps();
				newSteps.remove(0);
				if(newSteps.isEmpty())
				{
					hideRecipe(0);
				}
				else
				{
					recipe0.setSteps(newSteps);
					allRecipes.refreshDrawableState();
					recipeStepAdapter0.notifyDataSetChanged();
					recipeHolder0.refreshDrawableState();
				}
				break;
			case 1:
				newSteps = recipe1.getSteps();
				newSteps.remove(0);
				if(newSteps.isEmpty())
				{
					hideRecipe(1);
				}
				else
				{
					recipe1.setSteps(newSteps);
					allRecipes.refreshDrawableState();
					recipeStepAdapter1.notifyDataSetChanged();
					recipeHolder1.refreshDrawableState();
				}
				break;
			case 2:
				newSteps = recipe2.getSteps();
				newSteps.remove(0);
				if(newSteps.isEmpty())
				{
					hideRecipe(2);
				}
				else
				{
					recipe2.setSteps(newSteps);
					allRecipes.refreshDrawableState();
					recipeStepAdapter2.notifyDataSetChanged();
					recipeHolder2.refreshDrawableState();
				}
				break;
			case 3:
				newSteps = recipe3.getSteps();
				newSteps.remove(0);
				if(newSteps.isEmpty())
				{
					hideRecipe(3);
				}
				else
				{
					recipe3.setSteps(newSteps);
					allRecipes.refreshDrawableState();
					recipeStepAdapter3.notifyDataSetChanged();
					recipeHolder3.refreshDrawableState();
				}
				break;
		}
	}
	
	
	public void populateRecipes(){
		allRecipesList = new LinkedList<Recipe>();

		
		switch(numRecipes){
			case 1:
				populateFirstRecipe();
				allRecipesList.add(recipe0);
				break;
			case 2:
				populateFirstRecipe();
				populateSecondRecipe();
				allRecipesList.add(recipe0);
				allRecipesList.add(recipe1);
				break;
			case 3:
				populateFirstRecipe();
				populateSecondRecipe();
				populateThirdRecipe();
				allRecipesList.add(recipe0);
				allRecipesList.add(recipe1);
				allRecipesList.add(recipe2);
				break;
			case 4:
				populateFirstRecipe();
				populateSecondRecipe();
				populateThirdRecipe();
				populateFourthRecipe();
				allRecipesList.add(recipe0);
				allRecipesList.add(recipe1);
				allRecipesList.add(recipe2);
				allRecipesList.add(recipe3);
				break;
		}
	}
	
	public void hideRecipe(int currentRecipe){
		List<RecipeStep> newSteps = new LinkedList<RecipeStep>();
		switch(currentRecipe){
		case 0:
			recipeHolder0.setVisibility(View.GONE);
			newSteps = recipe0.getSteps();
			newSteps.clear();
			recipe0.setSteps(newSteps);
			allRecipes.refreshDrawableState();
        	recipeStepAdapter0.notifyDataSetChanged();
        	recipeHolder0.refreshDrawableState();
			break;
		case 1:
			recipeHolder1.setVisibility(View.GONE);
			recipeHolder1.setVisibility(View.GONE);
			newSteps = recipe1.getSteps();
			newSteps.clear();
			recipe1.setSteps(newSteps);
			allRecipes.refreshDrawableState();
        	recipeStepAdapter1.notifyDataSetChanged();
        	recipeHolder1.refreshDrawableState();
			break;
		case 2:
			recipeHolder2.setVisibility(View.GONE);
			recipeHolder2.setVisibility(View.GONE);
			newSteps = recipe2.getSteps();
			newSteps.clear();
			recipe2.setSteps(newSteps);
			allRecipes.refreshDrawableState();
        	recipeStepAdapter2.notifyDataSetChanged();
        	recipeHolder2.refreshDrawableState();
			break;
		case 3:
			recipeHolder3.setVisibility(View.GONE);
			recipeHolder3.setVisibility(View.GONE);
			newSteps = recipe3.getSteps();
			newSteps.clear();
			recipe3.setSteps(newSteps);
			allRecipes.refreshDrawableState();
        	recipeStepAdapter3.notifyDataSetChanged();
        	recipeHolder3.refreshDrawableState();
			break;
		}
		setCurrentStep();
	}
	
	@Override
	public void onClick(View v) {
			Context context = v.getContext();
			Intent intent;
			
		    switch (v.getId()) {
		        case R.id.button0:
		            PopupMenu popup = new PopupMenu(this, nextStep);
		            MenuInflater inflater = popup.getMenuInflater();
		            inflater.inflate(R.menu.activity_cook_popup_menu, popup.getMenu());
		            popup.show();
		            
		            
		            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
		                public boolean onMenuItemClick(MenuItem item) {  
		                	Intent intent;
		                	if(r.isPlaying())
            	        	{
            	        		r.stop();
            	        	}
		            	    switch (item.getItemId()) {
		            	        case R.id.nextStep:
		            	        	removeRecipeStep(currentRecipe);
		        		        	setCurrentStep();
		            	            break;
		            	        case R.id.addTime:
		            	        	
		            	        	currentStepTimer.cancel();
		            	        	currentStepTimer = createTimer(timeTilFinished + 30000);
		            	        	currentStepTimer.start();
		            	        	currentStepTime.refreshDrawableState();
		            	    		//intent = new Intent(this, EditMealActivity.class);
		            	    		//startActivity(intent);
		            	            break;
		            	        case R.id.scrapRecipe:
		            	        	hideRecipe(currentRecipe);
		            	    		//intent = new Intent(this, EditMealActivity.class);
		            	    		//startActivity(intent);
		            	            break;
		            	        default:
		            	        	removeRecipeStep(currentRecipe);
		        		        	setCurrentStep();
		        		        	break;
		            	    }
		                
		                 return true;  
		                }  
		               }); 
		            
		        	//removeRecipeStep(currentRecipe);
		        	//setCurrentStep();
		        	
		        	
		    		break;
		        default:
		            return;
		    }
	}
	
	
	public void setCurrentStep(){
		RecipeStep dummyStep = new RecipeStep();
		dummyStep.setTime(-1);
		RecipeStep curStep = new RecipeStep();
		RecipeStep MaxStep = dummyStep;
		int recipe = 0;
		
		for(Recipe r : allRecipesList){
			
			if(!r.getSteps().isEmpty()){	
				curStep = r.getSteps().get(0);
				if(MaxStep.getTime() <= curStep.getTime()){
					MaxStep = curStep;
					currentRecipe = recipe;
				}
			}
			
			recipe++;
		}
		
		currentStep = MaxStep;
		currentStepDescription.setText(currentStep.getInstructions());
		currentStepTime.setText(Double.toString(currentStep.getTime()));
		currentStepDescription.refreshDrawableState();
		currentStepTime.refreshDrawableState();


		currentStepTimer.cancel();
		long time = (long) currentStep.getTime()*60000;
		currentStepTimer = createTimer(time);//.onTick(currentStep.getTime()*60000);
		currentStepTimer.start();
	}
	
	public String makeTimeString(long millis){
		long second = (millis / 1000) % 60;
		long minute = (millis / (1000 * 60)) % 60;
		long hour = (millis / (1000 * 60 * 60)) % 24;

		String time = String.format("%02d:%02d:%02d", hour, minute, second);
		
		return time;
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
		recipeStepAdapter0 = new RecipeStepsArrayAdapter(this, (LinkedList)recipe0.getSteps());
		ListView recipeStepsListView0 = new ListView(this);
		ListView cookSteps =(ListView) findViewById(R.id.recipeCookSteps);
		recipeStepsListView0.setLayoutParams(cookSteps.getLayoutParams());
		recipeStepsListView0.setAdapter(recipeStepAdapter0);
		
		recipeHolder0.addView(myText0);
		recipeHolder0.addView(recipeIngredients0);
		recipeHolder0.addView(recipeStepsListView0);
	}
	
	public void calculateRecipeTime(Recipe recipe){
		List<RecipeStep> steps = recipe.getSteps();
		long totalTime = 0;
		for(RecipeStep r : steps){
			totalTime += r.getTime();
		}
		recipe.setTime(totalTime);
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
		recipeStepAdapter1= new RecipeStepsArrayAdapter(this, (LinkedList)recipe1.getSteps());
		ListView recipeStepsListView1 = new ListView(this);
		ListView cookSteps =(ListView) findViewById(R.id.recipeCookSteps);
		recipeStepsListView1.setLayoutParams(cookSteps.getLayoutParams());
		recipeStepsListView1.setAdapter(recipeStepAdapter1);
		
		recipeHolder1.addView(myText1);
		recipeHolder1.addView(recipeIngredients1);
		recipeHolder1.addView(recipeStepsListView1);
		recipeHolder1.setVisibility(View.VISIBLE);
		calculateRecipeTime(recipe0);
		calculateRecipeTime(recipe1);
		calculateRecipeTime(recipe2);
		calculateRecipeTime(recipe3);
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
		recipeStepAdapter2 = new RecipeStepsArrayAdapter(this, (LinkedList)recipe2.getSteps());
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
		recipeStepAdapter3 = new RecipeStepsArrayAdapter(this, (LinkedList)recipe3.getSteps());
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
		
		
		LinkedList<Ingredient> ingredients0 = new LinkedList<Ingredient>();
		LinkedList<RecipeStep> recipeSteps0 = new LinkedList<RecipeStep>();
		
		LinkedList<Ingredient> ingredients1 = new LinkedList<Ingredient>();
		LinkedList<RecipeStep> recipeSteps1 = new LinkedList<RecipeStep>();
		
		LinkedList<Ingredient> ingredients2 = new LinkedList<Ingredient>();
		LinkedList<RecipeStep> recipeSteps2 = new LinkedList<RecipeStep>();
		
		LinkedList<Ingredient> ingredients3 = new LinkedList<Ingredient>();
		LinkedList<RecipeStep> recipeSteps3 = new LinkedList<RecipeStep>();
		
		Ingredient brownSugar = new Ingredient("Brown Sugar", 1, "Cup");
		Ingredient Apples = new Ingredient("Apples", 2, "Whole");
		Ingredient sugar = new Ingredient("Sugar", 1, "Cup");
		Ingredient pieCrust = new Ingredient("Pie Crust", 1, "Whole");
		
		ingredients0.add(brownSugar);
		ingredients0.add(Apples);
		ingredients0.add(sugar);
		ingredients0.add(pieCrust);
		
		RecipeStep o = new RecipeStep();
		o.setInstructions("Preheat ove to 350F");
		o.setTime(10);
		RecipeStep tw = new RecipeStep();
		tw.setInstructions("Cut up Apples");
		tw.setTime(5);
		RecipeStep th = new RecipeStep();
		th.setInstructions("Mix up all ingredients");
		th.setTime(3);
		RecipeStep f = new RecipeStep();
		f.setInstructions("Bake for 30 minutes");
		f.setTime(30);
		
		recipeSteps0.add(o);
		recipeSteps0.add(tw);
		recipeSteps0.add(th);
		recipeSteps0.add(f);
		recipe0.setName("Apple Pie");
		recipe0.setIngredients(ingredients0);
		recipe0.setSteps(recipeSteps0);
		
		
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
		one.setTime(10);
		RecipeStep two = new RecipeStep();
		two.setTime(1);
		two.setInstructions("Remove the sage and thyme and set aside on a paper towel to drain");
		RecipeStep three = new RecipeStep();
		three.setInstructions("Season with salt and pepper");
		three.setTime(1);
		RecipeStep four = new RecipeStep();
		four.setInstructions("Remove onions from pan and add apples");
		four.setTime(2);
		RecipeStep five = new RecipeStep();
		five.setInstructions("Crush the pecans and add to the pan");
		five.setTime(5);
		RecipeStep six = new RecipeStep();
		six.setInstructions("Add more oil, if needed and season with salt and pepper");
		six.setTime(2);
		RecipeStep seven = new RecipeStep();
		seven.setInstructions("Gently saute until pecans are lightly toasted and apples are just cooked slightly");
		seven.setTime(10);
		RecipeStep eight = new RecipeStep();
		eight.setInstructions("whisk together egg, cream, chicken stock, and salt and pepper, to taste");
		eight.setTime(5);
		RecipeStep nine = new RecipeStep();
		nine.setInstructions("Add torn sourdough, caramelized onions, apples, pecans and chopped parsley");
		nine.setTime(5);
		RecipeStep ten = new RecipeStep();
		ten.setInstructions("Using a wooden spoon, mix the stuffing until well combined");
		ten.setTime(3);
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
		one.setTime(10);
		two = new RecipeStep();
		two.setInstructions("Melt the butter in a small saucepan");
		two.setTime(5);
		three = new RecipeStep();
		three.setInstructions("Add zest and juice of lemon and thyme leaves to butter mixture");
		three.setTime(3);
		four = new RecipeStep();
		four.setInstructions("Take giblets out of Turkey and wash Turkey");
		four.setTime(5);
		five = new RecipeStep();
		five.setInstructions("Clean and dry Turkey");
		five.setTime(5);
		six = new RecipeStep();
		six.setInstructions("Place Turkey in large roasting pan");
		six.setTime(1);
		seven = new RecipeStep();
		seven.setInstructions("Liberally apply Salt and Pepper");
		seven.setTime(1);
		eight = new RecipeStep();
		eight.setInstructions("Stuff the cavity with thyme, halved lemon, quartered onion, and garlic");
		eight.setTime(3);
		nine = new RecipeStep();
		nine.setInstructions("Brush outside of the turkey with butter mixed with salt and pepper");
		nine.setTime(3);
		ten = new RecipeStep();
		ten.setInstructions("Ties the legs of the turkey together with string");
		ten.setTime(1);
		RecipeStep eleven = new RecipeStep();
		eleven.setInstructions("Roast turky for 2 1/2 hours or until juices run clear");
		eleven.setTime(150);
		RecipeStep twelve = new RecipeStep();
		twelve.setInstructions("Remove Turkey and let it rest for 20 minutes before serving");
		twelve.setTime(20);
		
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
		one.setTime(3);
		two = new RecipeStep();
		two.setInstructions("Bake at 350 degrees F for 25 minutes");
		two.setTime(25);
		three = new RecipeStep();
		three.setInstructions("Stir Bean mixture and add remaining Onions");
		three.setTime(3);
		four = new RecipeStep();
		four.setInstructions("Bake for 5 more minutes");
		four.setTime(5);
		
		recipeSteps3.add(one);
		recipeSteps3.add(two);
		recipeSteps3.add(three);
		recipeSteps3.add(four);
		
		recipe3.setName("Green Bean Casserole");
		recipe3.setIngredients(ingredients3);
		recipe3.setSteps(recipeSteps3);
		
		calculateRecipeTime(recipe0);
		calculateRecipeTime(recipe1);
		calculateRecipeTime(recipe2);
		calculateRecipeTime(recipe3);
	}
}
