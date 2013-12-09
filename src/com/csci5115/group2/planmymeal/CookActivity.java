package com.csci5115.group2.planmymeal;

import java.util.LinkedList;
import java.util.List;

import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.csci5115.group2.planmymeal.database.DataSourceManager;

public class CookActivity extends Activity implements OnClickListener {
	
	// Databases
	private DataSourceManager datasource;
	public static HorizontalScrollView allRecipes;
	
	public static LinearLayout recipeHolder0;
	public static LinearLayout recipeHolder1;
	public static LinearLayout recipeHolder2;
	public static LinearLayout recipeHolder3;
	public static Recipe recipe0;
	public static Recipe recipe1;
	public static Recipe recipe2;
	public static Recipe recipe3;
	public static LinkedList<Recipe> allRecipesList;
	
	
	static RecipeStepsArrayAdapter recipeStepAdapter0;
	static RecipeStepsArrayAdapter recipeStepAdapter1;
	static RecipeStepsArrayAdapter recipeStepAdapter2;
	static RecipeStepsArrayAdapter recipeStepAdapter3;
	
	public static RecipeStep currentStep;
	public static TextView currentStepDescription;
	public static TextView currentStepTime;
	public static int currentRecipe;
	public static Button nextStepButton;
	public static Button add30SecondsButton;
	public Button timelineButton;
	public static CountDownTimer currentStepTimer;
	
	public LinkedList<string> allRecipeStepsRecipeOrder;
	
	public static int numRecipes;
	
	public static long timeTilFinished;
	
	public Uri notification;
	public static Ringtone r;
	
	public static LinkedList<RecipeStep> allRecipeSteps;
	public static int allRecipeStepsCurrent;
	public int currentRecipeStep;
	public LinkedList<RecipeStep> allInactiveSteps;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cook);
		setTitle("Cook");
		allRecipes = (HorizontalScrollView) findViewById(R.id.myListView);
		Intent intent = getIntent();
		allInactiveSteps = new LinkedList<RecipeStep>();
		
		
		
        // Database Creation
        datasource = new DataSourceManager(this);
        datasource.open();
        
        UserSettings settings = datasource.getUserSettings();
        int startSoundType = settings.getStartSoundAlarmValue();
        notification = RingtoneManager.getDefaultUri(startSoundType);
		r = RingtoneManager.getRingtone(getApplicationContext(), notification);
		
        
        
        //Meal meal = datasource.getMealById(mealId);
		
		//List<Recipe> recipes = datasource.getMealRecipes(mealId);// meal.getRecipes();
		
		recipe0 = new Recipe();
		recipe1 = new Recipe();
		recipe2 = new Recipe();
		recipe3 = new Recipe();
		recipeStepAdapter0 = new RecipeStepsArrayAdapter(this, (LinkedList)recipe0.getSteps());
		recipeStepAdapter1 = new RecipeStepsArrayAdapter(this, (LinkedList)recipe0.getSteps());
		recipeStepAdapter2 = new RecipeStepsArrayAdapter(this, (LinkedList)recipe0.getSteps());
		recipeStepAdapter3 = new RecipeStepsArrayAdapter(this, (LinkedList)recipe0.getSteps());
		
		Long mealId = intent.getLongExtra(HomeActivity.EXTRA_MEAL, 0);
		//String test = intent.getClass().toString();
		//Long mealId = intent.getLongExtra(RecipeDetailFragment.ARG_ITEM_ID, 0);
		String type = intent.getStringExtra(HomeActivity.TAG);
		if(type.equals("Meal"))
		{
			List<Long> recipes = datasource.getRecipeIdsForMeal(mealId);
			numRecipes = recipes.size();
			LinkedList<Recipe> recipesToFill = new LinkedList<Recipe>();
			for(Long l: recipes)
			{
				recipesToFill.add(datasource.getRecipeById(l));
			}
			for(int i = 0; i < recipesToFill.size(); i++)
			{
				switch(i){
				case 0:
					recipe0 = recipesToFill.get(i);
					recipe0.setSteps(datasource.getRecipeSteps(recipe0.getId()));
					recipe0.setIngredients(datasource.getRecipeIngredients(recipe0.id));
					break;
				case 1:
					recipe1 = recipesToFill.get(i);
					recipe1.setSteps(datasource.getRecipeSteps(recipe1.getId()));
					recipe1.setIngredients(datasource.getRecipeIngredients(recipe1.id));
					break;
				case 2:
					recipe2 = recipesToFill.get(i);
					recipe2.setSteps(datasource.getRecipeSteps(recipe2.getId()));
					recipe2.setIngredients(datasource.getRecipeIngredients(recipe2.id));
					break;
				case 3:
					recipe3 = recipesToFill.get(i);
					recipe3.setSteps(datasource.getRecipeSteps(recipe3.getId()));
					recipe3.setIngredients(datasource.getRecipeIngredients(recipe3.id));
					break;
				}	
			}
		}
		else
		{
			numRecipes = 1;
			//List<Long> recipes = datasource.getRecipeIdsForMeal(mealId);
			Recipe recipestext = datasource.getRecipeById(mealId);
			recipestext.setSteps(datasource.getRecipeSteps(mealId));
			recipestext.setIngredients(datasource.getRecipeIngredients(mealId));
			recipe0 = recipestext;
			//allRecipesList.add(recipe0);
		}
		
		//List<Long> recipes = datasource.getRecipeIdsForMeal(mealId);
		//Recipe recipestext = datasource.getRecipeById(mealId);
		
		
		//CreateFakeRecipes();	
		
		
		recipeHolder0 = (LinearLayout)findViewById(R.id.RecipeHolder0);
		recipeHolder1 = (LinearLayout)findViewById(R.id.RecipeHolder1);
		recipeHolder2 = (LinearLayout)findViewById(R.id.RecipeHolder2);
		recipeHolder3 = (LinearLayout)findViewById(R.id.RecipeHolder3);
		
		
		populateRecipes();
		//removeRecipeStep(0);
		//removeRecipeStep(1);
		
		
		currentStepDescription = (TextView)findViewById(R.id.currentStepDescription);
		currentStepTime = (TextView)findViewById(R.id.currentStepTime);
		currentStepTime.setOnClickListener(this);
		currentStepTime.setClickable(false);
		nextStepButton = (Button)findViewById(R.id.nextStepButton);
		nextStepButton.setOnClickListener(this);	
		
		timelineButton = (Button)findViewById(R.id.timelineButton);
		timelineButton.setOnClickListener(this);
		
		add30SecondsButton = (Button)findViewById(R.id.addTime);
		add30SecondsButton.setOnClickListener(this);
		
		currentStep = new RecipeStep();
		currentStep.setTime(1);
		long time = (long) currentStep.getTime()*60000;
		currentStepTimer = createTimerHome(time); //new StepTimer(time, currentStepDescription, currentStepTime);
		allRecipeSteps = new LinkedList<RecipeStep>();
		setStepOrder();
		setCurrentStep();
		
		//currentStepTimer = createTimer();
		//currentStepTimer.start()			
				/*) new CountDownTimer(60000, 1000) {
			public void onTick(long millisUntilFinished) {
				currentStepTime.setText(makeTimeString(millisUntilFinished));
			}

			public void onFinish() {
				currentStepTime.setText("done!");
				nextStep.call
();
			}
		}.start();
		*/
		allRecipes.scrollBy(-100, -100);
		allRecipes.refreshDrawableState();
		//allRecipes.scrollBy(100, 100);
		allRecipes.setScrollbarFadingEnabled(false);
		//allRecipes.setScrollX(-25);
		allRecipes.refreshDrawableState();
		
		
	
		
		
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
		r.stop();
		datasource.close();
		currentStepTimer.cancel();
	}
	
	public void removeRecipeStep(int recipe){
		if(allRecipeStepsCurrent < allRecipeSteps.size())
		{
			currentStep = allRecipeSteps.get(allRecipeStepsCurrent);
		switch(recipe){
			case 0:
				recipe0.setCurrentStepToWatch(); //((long) currentStep.getTime()*60000);
				recipe0.completeStepInRecipe();
				allRecipes.refreshDrawableState();
				recipeStepAdapter0.notifyDataSetChanged();
				recipeHolder0.refreshDrawableState();
				allRecipeStepsCurrent++;
				break;
			case 1:
				recipe1.setCurrentStepToWatch();
				recipe1.completeStepInRecipe();
				allRecipes.refreshDrawableState();
				recipeStepAdapter1.notifyDataSetChanged();
				recipeHolder1.refreshDrawableState();
				allRecipeStepsCurrent++;
				break;
			case 2:
				recipe2.setCurrentStepToWatch();
				recipe2.completeStepInRecipe();
				allRecipes.refreshDrawableState();
				recipeStepAdapter2.notifyDataSetChanged();
				recipeHolder2.refreshDrawableState();
				allRecipeStepsCurrent++;
				break;
			case 3:
				recipe3.setCurrentStepToWatch();
				recipe3.completeStepInRecipe();
				allRecipes.refreshDrawableState();
				recipeStepAdapter3.notifyDataSetChanged();
				recipeHolder3.refreshDrawableState();
				allRecipeStepsCurrent++;
				break;
		}
		}
		else
		{
			currentStepTimer.cancel();
			currentStepDescription.setText("Meal Complete!");
			currentStepDescription.refreshDrawableState();	
			currentStepTime.setText("");
			currentStepTime.refreshDrawableState();
			allRecipeStepsCurrent++;
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
	
	public static void hideRecipe(){		
		List<RecipeStep> newSteps = new LinkedList<RecipeStep>();
		int test = currentRecipe;
		switch(currentRecipe){
		case 0:
			recipeHolder0.setVisibility(View.GONE);
			newSteps = recipe0.getSteps();
			newSteps.clear();
			recipe0.setSteps(newSteps);
			if(!allRecipesComplete(allRecipesList))
			{
				resetStepOrder(recipe0);
				allRecipes.refreshDrawableState();
	        	recipeStepAdapter0.notifyDataSetChanged();
	        	recipeHolder0.refreshDrawableState();
			}
			break;
		case 1:
			recipeHolder1.setVisibility(View.GONE);
			recipeHolder1.setVisibility(View.GONE);
			newSteps = recipe1.getSteps();
			newSteps.clear();
			recipe1.setSteps(newSteps);
			if(!allRecipesComplete(allRecipesList))
			{
			resetStepOrder(recipe1);
			allRecipes.refreshDrawableState();
        	recipeStepAdapter1.notifyDataSetChanged();
        	recipeHolder1.refreshDrawableState();
			}
			break;
		case 2:
			recipeHolder2.setVisibility(View.GONE);
			recipeHolder2.setVisibility(View.GONE);
			newSteps = recipe2.getSteps();
			newSteps.clear();
			recipe2.setSteps(newSteps);
			if(!allRecipesComplete(allRecipesList))
			{
			resetStepOrder(recipe2);
			allRecipes.refreshDrawableState();
        	recipeStepAdapter2.notifyDataSetChanged();
        	recipeHolder2.refreshDrawableState();
			}
			break;
		case 3:
			recipeHolder3.setVisibility(View.GONE);
			recipeHolder3.setVisibility(View.GONE);
			newSteps = recipe3.getSteps();
			newSteps.clear();
			recipe3.setSteps(newSteps);
			if(!allRecipesComplete(allRecipesList))
			{
			resetStepOrder(recipe3);
			allRecipes.refreshDrawableState();
        	recipeStepAdapter3.notifyDataSetChanged();
        	recipeHolder3.refreshDrawableState();
			}
			break;
		}
		setCurrentStep();
	}
	
	public void findFirstNonRecipeStep(Recipe r)
	{
		for(int i = allRecipeStepsCurrent; i < allRecipeSteps.size(); i++)
		{
			for(RecipeStep rs: r.getSteps())
			{
				if(rs.getInstructions() == currentStep.getInstructions())
				{
					
				}
			}
		}
	}
	
	@Override
	public void onClick(View v) {
			Context context = v.getContext();
			Intent intent;
			
		    switch (v.getId()) {
		        case R.id.nextStepButton:
		        		removeRecipeStep(currentRecipe);
		        		if(!allRecipesComplete(allRecipesList))
						{
							setCurrentStep();					
						}
						else
						{
							currentStepDescription.setText("Meal Complete!");
							currentStepDescription.refreshDrawableState();
							currentStepTime.setText("");
							currentStepTime.refreshDrawableState();
							currentStepTimer.cancel();
						}
		        		//setCurrentStep();
		        	break;
		        case R.id.timelineButton:
		        	AlertDialog.Builder timeLine = new AlertDialog.Builder(context);
		        	timeLine.setTitle("Timeline!");        	
		        	String[] itemsOther = new String[allRecipeSteps.size()];
		        	for(int i = 0; i < allRecipeSteps.size(); i++)
		        	{
		        		
		        		itemsOther[i] = allRecipeSteps.get(i).getInstructions() + "\n Done in: " + makeTimeString((long) allRecipeSteps.get(i).getTime()*60000);
		        	}
		        	
		        	
		        	timeLine.setItems(itemsOther,  new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							//datasource.deleteMealRecipe(mealId, recipe.getId());
							
							}
		        	});
					//builder.setNegativeButton("Cancel", null);
					AlertDialog dialog = timeLine.create();
					dialog.show();
		        	break;
		        case R.id.currentStepTime:
		        	r.play();
		        	AlertDialog.Builder builder = new AlertDialog.Builder(context);
		        	builder.setTitle(currentStepDescription.getText());
		        	builder.setCancelable(false);
		        	String[] items = { "Add 30 Seconds", "Go To Next Step", "Scrap Recipe" };
		        	builder.setItems(items,  new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							r.stop();
							//datasource.deleteMealRecipe(mealId, recipe.getId());
							switch(id){
							case 0:
								add30SecondsButton.callOnClick();
								break;
							case 1:
								nextStepButton.callOnClick();
								break;
							case 2:
								hideRecipe();
								if(!allRecipesComplete(allRecipesList))
								{
									setCurrentStep();					
								}
								else
								{
									currentStepDescription.setText("Meal Complete!");
									currentStepDescription.refreshDrawableState();
									currentStepTimer.cancel();
								}
								break;
							}
						}
					});
					//builder.setNegativeButton("Cancel", null);
					AlertDialog dialog1 = builder.create();
					dialog1.show();
					break;
		        case R.id.addTime:
		        	currentStepTime.setBackgroundColor(Color.WHITE);
    	        	currentStepDescription.setBackgroundColor(Color.WHITE);
    	        	//currentStepTime.setEnabled(false);
    	        	
    	        	currentStepTimer.cancel();
    	        	currentStepTimer = createTimerHome(timeTilFinished + 30000);//new StepTimer(currentStepTimer.timeTilFinished + 30000, currentStepDescription, currentStepTime);
    	        	currentStepTimer.start();
    	        	currentStepTime.refreshDrawableState();
    	    		//intent = new Intent(this, EditMealActivity.class);
    	    		//startActivity(intent);
    	            break;
    	        default:
    	        	return;
		    }
	}
		        	
		        	
	
	public void setStepOrder(){
		allRecipeSteps.clear();
		LinkedList<Recipe> dummyRecipes = new LinkedList<Recipe>();
		for(Recipe r: allRecipesList)
		{
			Recipe dummyRecipe = new Recipe();
			dummyRecipe.setSteps(r.cloneSteps());
			dummyRecipe.timeBasedOnUnCompletedSteps();
			dummyRecipes.add(dummyRecipe);
		}
		
		while(!allRecipesComplete(dummyRecipes))
		{
			Recipe next = dummyRecipes.get(0);
			for(Recipe r: dummyRecipes)
			{
				if(r.getTime() >= next.getTime())
				{
					next = r;
				}
			}
			allRecipeSteps.add(next.getFirstUncompletedStep());
			next.completeStepInRecipe();
		}	
		allRecipeStepsCurrent = 0;
		currentStep = allRecipeSteps.get(allRecipeStepsCurrent);
	}
	
	public static void resetStepOrder(Recipe recipe){
		RecipeStep test = currentStep;
		allRecipeSteps.clear();
		LinkedList<Recipe> dummyRecipes = new LinkedList<Recipe>();
		for(Recipe r: allRecipesList)
		{
			if(!recipe.equals(r))
			{
				Recipe dummyRecipe = new Recipe();
				dummyRecipe.setSteps(r.cloneSteps());
				dummyRecipe.timeBasedOnUnCompletedSteps();
				dummyRecipes.add(dummyRecipe);
			}
		}
		int i = 0;
		allRecipeStepsCurrent = 0;
		RecipeStep nextStep = new RecipeStep();
		for(Recipe r: dummyRecipes)
		{
			for(RecipeStep rs: r.getSteps())
			{
				if(rs.isCompleted())
				{
					allRecipeSteps.add(rs);
					allRecipeStepsCurrent++;
				}
			}
		}
		while(!allRecipesComplete(dummyRecipes))
		{
			Recipe next = dummyRecipes.get(0);
			for(Recipe r: dummyRecipes)
			{
				if(r.getTime() >= next.getTime())
				{
					next = r;
				}
			}
			nextStep = next.getFirstUncompletedStep();
			if(nextStep.getInstructions() == currentStep.getInstructions())
			{
				
			}
			allRecipeSteps.add(next.getFirstUncompletedStep());
			next.completeStepInRecipe();
			i++;
		}	
		currentStep = allRecipeSteps.get(allRecipeStepsCurrent);
	}

	
	
	public static boolean allRecipesComplete(LinkedList<Recipe> recipes)
	{
		for(Recipe r: recipes)
		{
			if(r.getFirstUncompletedStep() != null)
			{
				return false;
			}
		}
		
		return true;
	}
	
	public static void setCurrentStep(){
		currentStep = allRecipeSteps.get(allRecipeStepsCurrent);
		if(!allRecipesComplete(allRecipesList))
		{
		
		RecipeStep dummyStep = new RecipeStep();
		dummyStep.setTime(-1);
		RecipeStep curStep = new RecipeStep();
		RecipeStep MaxStep = dummyStep;
		int recipe = 0;
		for(Recipe r : allRecipesList){		
			if(r.getFirstUncompletedStep() != null){	
				if((r.getFirstUncompletedStep().getInstructions() == currentStep.getInstructions()) && (r.getFirstUncompletedStep().getTime() == currentStep.getTime())){					
					currentStep = r.getFirstUncompletedStep();
					currentRecipe = recipe;
				}
			}	
			recipe++;
		}
		
		
		currentStepDescription.setText(currentStep.getInstructions());
		//currentStepTime.setText(Double.toString(currentStep.getTime()));
		//currentStepTime.setEnabled(false);
		//currentStepTime.setClickable(false);
		currentStepDescription.refreshDrawableState();
		currentStepTime.refreshDrawableState();
		Recipe curRecipe = allRecipesList.get(currentRecipe);
		int recipeCol = 0;
		for(Recipe r: allRecipesList)
		{
			for(RecipeStep rs: r.getSteps())
			{
				if(rs.getInstructions().equals(currentStep.getInstructions()) && (rs.getTime() == currentStep.getTime())){
					currentRecipe = recipeCol;
				}
			}
			recipeCol++;
		}
		switch(currentRecipe){
			case 0:
				recipeStepAdapter0.isCurrentRecipe = true;
				recipeStepAdapter1.isCurrentRecipe = false;
				recipeStepAdapter2.isCurrentRecipe = false;
				recipeStepAdapter3.isCurrentRecipe = false;
				break;
			case 1:
				recipeStepAdapter0.isCurrentRecipe = false;
				recipeStepAdapter1.isCurrentRecipe = true;
				recipeStepAdapter2.isCurrentRecipe = false;
				recipeStepAdapter3.isCurrentRecipe = false;
				break;
			case 2:
				recipeStepAdapter0.isCurrentRecipe = false;
				recipeStepAdapter1.isCurrentRecipe = false;
				recipeStepAdapter2.isCurrentRecipe = true;
				recipeStepAdapter3.isCurrentRecipe = false;
				break;
			case 3:
				recipeStepAdapter0.isCurrentRecipe = false;
				recipeStepAdapter1.isCurrentRecipe = false;
				recipeStepAdapter2.isCurrentRecipe = false;
				recipeStepAdapter3.isCurrentRecipe = true;
				break;
		}
		
		recipeStepAdapter0.notifyDataSetChanged();
    	recipeHolder0.refreshDrawableState();
    	recipeStepAdapter1.notifyDataSetChanged();
    	recipeHolder1.refreshDrawableState();
    	recipeStepAdapter2.notifyDataSetChanged();
    	recipeHolder2.refreshDrawableState();
    	recipeStepAdapter3.notifyDataSetChanged();
    	recipeHolder3.refreshDrawableState();

		currentStepTimer.cancel();
		long time = (long) currentStep.getTime()*60000;
		currentStepTimer = createTimerHome(time);//, currentStepDescription, currentStepTime);//.onTick(currentStep.getTime()*60000);
		currentStepTimer.start();
		}
		else
		{
			currentStepDescription.setText("Meal Complete!");
			currentStepDescription.refreshDrawableState();
			currentStepTimer.cancel();
		}
	}
	
	
	
	
	
	public void populateFirstRecipe(){
		TextView myText0 = new TextView(this);
		myText0.setTypeface(Typeface.DEFAULT_BOLD);
		myText0.setTextSize(34);
		myText0.setTextColor(Color.BLACK);
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
		myText0.setTextSize(25);
		recipeHolder0.addView(myText0);
		recipeHolder0.addView(recipeIngredients0);
		recipeHolder0.addView(recipeStepsListView0);
		recipeStepsListView0.setScrollBarDefaultDelayBeforeFade(30);
		recipeStepsListView0.setScrollBarFadeDuration(30);
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
		myText1.setTypeface(Typeface.DEFAULT_BOLD);
		myText1.setTextSize(34);
		myText1.setTextColor(Color.BLACK);
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
		recipeStepsListView1.setScrollBarDefaultDelayBeforeFade(5);
		recipeStepsListView1.setScrollBarFadeDuration(30);
		
		myText1.setTextSize(25);
		recipeHolder1.addView(myText1);
		recipeHolder1.addView(recipeIngredients1);
		recipeHolder1.addView(recipeStepsListView1);
		recipeHolder1.setVisibility(View.VISIBLE);
		calculateRecipeTime(recipe0);
		calculateRecipeTime(recipe1);
		calculateRecipeTime(recipe2);
		calculateRecipeTime(recipe3);
	}

	
	public static String makeTimeString(long millis){
		long second = (millis / 1000) % 60;
		long minute = (millis / (1000 * 60)) % 60;
		long hour = (millis / (1000 * 60 * 60)) % 24;

		String time = String.format("%02d:%02d:%02d", hour, minute, second);
		
		return time;
	}
	
	public void populateThirdRecipe(){
		TextView myText2 = new TextView(this);
		myText2.setTypeface(Typeface.DEFAULT_BOLD);
		myText2.setTextSize(34);
		myText2.setTextColor(Color.BLACK);
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
		recipeStepsListView2.setScrollBarDefaultDelayBeforeFade(5);
		recipeStepsListView2.setScrollBarFadeDuration(30);
		
		myText2.setTextSize(25);
		recipeHolder2.addView(myText2);
		recipeHolder2.addView(recipeIngredients2);
		recipeHolder2.addView(recipeStepsListView2);
		recipeHolder2.setVisibility(View.VISIBLE);
	}
	
	public void populateFourthRecipe(){
		TextView myText3 = new TextView(this);
		myText3.setTypeface(Typeface.DEFAULT_BOLD);
		myText3.setTextSize(34);
		myText3.setTextColor(Color.BLACK);
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
		recipeStepsListView3.setScrollBarDefaultDelayBeforeFade(5);
		recipeStepsListView3.setScrollBarFadeDuration(30);
		
		recipeHolder3.addView(myText3);
		recipeHolder3.addView(recipeIngredients3);
		recipeHolder3.addView(recipeStepsListView3);
		recipeHolder3.setVisibility(View.VISIBLE);
		myText3.setTextSize(25);
	}

public static CountDownTimer createTimerHome(long time)
{
	CountDownTimer timer = new CountDownTimer(time, 1000) {
		public void onTick(long millisUntilFinished) {
			if(currentStepTime.equals(currentStepDescription))
			{
				currentStepDescription.setText(currentStep.getInstructions().toString());// + '\n' + makeTimeString(millisUntilFinished));
				currentStepDescription.refreshDrawableState();
				
			}
			else
			{
				currentStepDescription.setText(currentStep.getInstructions().toString());// + makeTimeString(millisUntilFinished));
				currentStepDescription.refreshDrawableState();
				currentStepTime.setText(makeTimeString(millisUntilFinished));
			
			}
			
			timeTilFinished = millisUntilFinished;
			//currentStepTime.setBackgroundColor(getColor(millisUntilFinished));
			currentStepTime.setBackgroundColor(getColor(millisUntilFinished));				
		}
		
		public int getColor(long millisUntilFinished) {
			if(millisUntilFinished <= 30000)
			{
				return Color.RED;
			}
			else if(millisUntilFinished <= 60000)
			{
				return Color.YELLOW;
			}
			
			return Color.GREEN;
		}

		@Override
		public void onFinish() {
			currentStepTime.callOnClick();
		}
					
	};
 return timer;
}

}
