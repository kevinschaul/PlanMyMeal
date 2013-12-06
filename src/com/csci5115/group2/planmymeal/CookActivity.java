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
import android.graphics.Color;
import android.graphics.Typeface;
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
	public Button nextStepButton;
	public Button add30SecondsButton;
	public Button timelineButton;
	public CountDownTimer currentStepTimer;
	
	public LinkedList<string> allRecipeStepsRecipeOrder;
	
	public int numRecipes;
	
	public long timeTilFinished;
	
	public Uri notification;
	public Ringtone r;
	
	public LinkedList<RecipeStep> allRecipeSteps;
	public int allRecipeStepsCurrent;
	public int currentRecipeStep;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cook);
		allRecipes = (HorizontalScrollView) findViewById(R.id.myListView);
		Intent intent = getIntent();
		
		notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		r = RingtoneManager.getRingtone(getApplicationContext(), notification);
		
		
        // Database Creation
        datasource = new DataSourceManager(this);
        datasource.open();
		
        
        
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
		currentStepTimer = createTimer(time);
		allRecipeSteps = new LinkedList<RecipeStep>();
		setStepOrder();
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
		allRecipes.scrollBy(-100, -100);
		allRecipes.refreshDrawableState();
		//allRecipes.scrollBy(100, 100);
		allRecipes.setScrollbarFadingEnabled(false);
		//allRecipes.setScrollX(-25);
		allRecipes.refreshDrawableState();
		
		
	
		
		
	}
	
	public CountDownTimer createTimer(long time)
	{
		 CountDownTimer newTimer = new CountDownTimer(time, 1000) {
				public void onTick(long millisUntilFinished) {
					currentStepTime.setText(makeTimeString(millisUntilFinished));
					timeTilFinished = millisUntilFinished;
					currentStepTime.setBackgroundColor(getColor(millisUntilFinished));
					currentStepDescription.setBackgroundColor(getColor(millisUntilFinished));
				}

				public void onFinish() {
					currentStepTime.setText("done!");
					r.play();
					
				   
					currentStepTime.callOnClick();
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
		if(allRecipeStepsCurrent < allRecipeSteps.size())
		{
			currentStep = allRecipeSteps.get(allRecipeStepsCurrent);
		switch(recipe){
			case 0:
				recipe0.completeStepInRecipe();
				allRecipes.refreshDrawableState();
				recipeStepAdapter0.notifyDataSetChanged();
				recipeHolder0.refreshDrawableState();
				allRecipeStepsCurrent++;
				break;
			case 1:
				recipe1.completeStepInRecipe();
				allRecipes.refreshDrawableState();
				recipeStepAdapter1.notifyDataSetChanged();
				recipeHolder1.refreshDrawableState();
				allRecipeStepsCurrent++;
				break;
			case 2:
				recipe2.completeStepInRecipe();
				allRecipes.refreshDrawableState();
				recipeStepAdapter2.notifyDataSetChanged();
				recipeHolder2.refreshDrawableState();
				allRecipeStepsCurrent++;
				break;
			case 3:
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
	
	public void hideRecipe(int currentRecipe){		
		List<RecipeStep> newSteps = new LinkedList<RecipeStep>();
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
		        	builder.setTitle("Times Up!");
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
								hideRecipe(currentRecipe);
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
    	        	currentStepTimer = createTimer(timeTilFinished + 30000);
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
	
	public void resetStepOrder(Recipe recipe){
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

	
	
	public boolean allRecipesComplete(LinkedList<Recipe> recipes)
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
	
	public void setCurrentStep(){
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
		currentStepTime.setText(Double.toString(currentStep.getTime()));
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
		currentStepTimer = createTimer(time);//.onTick(currentStep.getTime()*60000);
		currentStepTimer.start();
		}
		else
		{
			currentStepDescription.setText("Meal Complete!");
			currentStepDescription.refreshDrawableState();
			currentStepTimer.cancel();
		}
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
	
	
	public void CreateFakeRecipes(){
		
		LinkedList<Ingredient> ingredients0 = new LinkedList<Ingredient>();
		LinkedList<RecipeStep> recipeSteps0 = new LinkedList<RecipeStep>();
		
		LinkedList<Ingredient> ingredients1 = new LinkedList<Ingredient>();
		LinkedList<RecipeStep> recipeSteps1 = new LinkedList<RecipeStep>();
		
		LinkedList<Ingredient> ingredients2 = new LinkedList<Ingredient>();
		LinkedList<RecipeStep> recipeSteps2 = new LinkedList<RecipeStep>();
		
		LinkedList<Ingredient> ingredients3 = new LinkedList<Ingredient>();
		LinkedList<RecipeStep> recipeSteps3 = new LinkedList<RecipeStep>();
		
		Ingredient crust = new Ingredient("Graham Cracker Crust", 1, "Whole");
		Ingredient tofu = new Ingredient("Extra Firm Silk Tofu", 25, "Ounces");
		Ingredient keyLime = new Ingredient("Key Lime Juice", 1, "Fourth Cup");
		Ingredient sugar = new Ingredient("Sugar", 1, "Cup");
		Ingredient salt = new Ingredient("Salt", 1, "Tsp");
		Ingredient cornstarch = new Ingredient("Cornstarch", 1, "Tsp.");
		
		ingredients0.add(crust);
		ingredients0.add(tofu);
		ingredients0.add(sugar);
		ingredients0.add(keyLime);
		ingredients0.add(salt);
		ingredients0.add(cornstarch);
		
		RecipeStep o = new RecipeStep();
		o.setInstructions("Place graham cracker crust in 9 inch pie plate");
		o.setTime(1);
		RecipeStep tw = new RecipeStep();
		tw.setInstructions("In blender, combine tofu, lime juice, sugar, salt and cornstarch.");
		tw.setTime(5);
		RecipeStep th = new RecipeStep();
		th.setInstructions("Process until liquid is smooth and thick");
		th.setTime(3);
		RecipeStep f = new RecipeStep();
		f.setInstructions("Pour into crust and place in freezer");
		f.setTime(120);
		
		recipeSteps0.add(o);
		recipeSteps0.add(tw);
		recipeSteps0.add(th);
		recipeSteps0.add(f);
		recipe0.setName("Key Lime Pie");
		recipe0.setIngredients(ingredients0);
		recipe0.setSteps(recipeSteps0);
		
		
		Ingredient cabbage = new Ingredient("Shredded Cabbage", 1, "Lb.");
		Ingredient carrots = new Ingredient("Carrots", 1, "Lb.");
		Ingredient tahini = new Ingredient("Tahini", 1, "Cup");
		Ingredient mapleSyrup = new Ingredient("Maple Syrup", 1, "Cup");
		Ingredient ciderVinegar = new Ingredient("Cider Vinegar", 1, "Cup");
		Ingredient vegenaise = new Ingredient("Vegenaise", 3, "Tbsp");
		Ingredient hempSeed = new Ingredient("Hemp Seed", 1, "Cup");
		Ingredient onion = new Ingredient("Onion", 1, "Small");
		Ingredient greenApple = new Ingredient("Green Apple", 1, "Small");
		
		
		ingredients1.add(cabbage);
		ingredients1.add(carrots);
		ingredients1.add(tahini);
		ingredients1.add(mapleSyrup);
		ingredients1.add(ciderVinegar);
		ingredients1.add(vegenaise);
		ingredients1.add(hempSeed);
		ingredients1.add(onion);
		ingredients1.add(greenApple);
		
		RecipeStep one = new RecipeStep();
		one.setInstructions("Add all ingredients into large mixing bowl");
		one.setTime(3);
		RecipeStep two = new RecipeStep();
		two.setTime(3);
		two.setInstructions("Start tossing ingredients with a large spoon");
		RecipeStep three = new RecipeStep();
		three.setInstructions("Place in Fridge, cover and chill");
		three.setTime(120);
		
		recipeSteps1.add(one);
		recipeSteps1.add(two);
		recipeSteps1.add(three);
		
		recipe1 = new Recipe();
		recipe2 = new Recipe();
		recipe3 = new Recipe();
		
		recipe1.setName("Vegan Summer Slaw");
		recipe1.setIngredients(ingredients1);
		recipe1.setSteps(recipeSteps1);
		
		Ingredient cornMeal = new Ingredient("Cornmeal", 2, "Cups");
		Ingredient flour = new Ingredient("Flour", 1, "Cup");
		Ingredient bakingPowder = new Ingredient("Baking Powder", 2, "Tsp");
		Ingredient canolaOil = new Ingredient("Canola Oil", 1, "Cup");
		mapleSyrup = new Ingredient("Maple Syrup", 2, "Tbsp");
		Ingredient soyMilk = new Ingredient("Soy Milk", 2, "Cups");
		Ingredient vinegar = new Ingredient("Apple Cider Vinegar", 2, "Tsp");
		salt = new Ingredient("Salt", 1, "Tsp");

		ingredients2.add(cornMeal);
		ingredients2.add(flour);
		ingredients2.add(bakingPowder);
		ingredients2.add(canolaOil);
		ingredients2.add(canolaOil);
		ingredients2.add(mapleSyrup);
		ingredients2.add(soyMilk);
		ingredients2.add(vinegar);
		ingredients2.add(salt);
		
		
		one = new RecipeStep();
		one.setInstructions("Preheat the oven to 350F");
		one.setTime(10);
		two = new RecipeStep();
		two.setInstructions("Line baking pan with parchmentpaper");
		two.setTime(2);
		three = new RecipeStep();
		three.setInstructions("Wisk together soymilk and vinegar in medium bowl");
		three.setTime(3);
		RecipeStep four = new RecipeStep();
		four.setInstructions("In a seperate Large bowl, combine all dry ingredients");
		four.setTime(3);
		RecipeStep five = new RecipeStep();
		five.setInstructions("Add oil and maple syrup to soymilk mixture, wisk until foamy");
		five.setTime(3);
		RecipeStep six = new RecipeStep();
		six.setInstructions("Pour wet ingredients into dry ingredients and mix");
		six.setTime(1);
		RecipeStep seven = new RecipeStep();
		seven.setInstructions("Pour batter into baking pan, bake for 35 minutes");
		seven.setTime(35);
		
		recipeSteps2.add(one);
		recipeSteps2.add(two);
		recipeSteps2.add(three);
		recipeSteps2.add(four);
		recipeSteps2.add(five);
		recipeSteps2.add(six);
		recipeSteps2.add(seven);
		
		recipe2.setName("Vegan Cornbread");
		recipe2.setSteps(recipeSteps2);
		recipe2.setIngredients(ingredients2);
		
		Ingredient oil = new Ingredient("Oil", 1, "tbsp");
		Ingredient garlic = new Ingredient("Garlic", 3, "Cloves");
		Ingredient bellPepper = new Ingredient("Bell Pepper", 2, "Whole");
		onion = new Ingredient("Onion", 1, "Whole");
		Ingredient greenBeans = new Ingredient("Green Beans", 4, "Cup");
		Ingredient friedOnions = new Ingredient("FrenchFried Onions", 1, "Cup");
		Ingredient cumin = new Ingredient("Cumin", 1, "tbsp");
		Ingredient chiliPowder = new Ingredient("Chili Powder", 3, "tbsp");
		Ingredient cayenne = new Ingredient("Cayenne", 1, "Pinch");
		Ingredient beans = new Ingredient("Beans", 4, "Cans");
		Ingredient tomato = new Ingredient("Tomato", 1, "Whole");
		Ingredient oregeno = new Ingredient("Oregano", 2, "tsp");
		Ingredient mushrooms = new Ingredient("Mushrooms", 1, "Can");
		Ingredient cocoaPowder = new Ingredient("Cocoa Powder", 1, "tbsp");
		
		
		
		ingredients3.add(oil);
		ingredients3.add(garlic);
		ingredients3.add(bellPepper);
		ingredients3.add(onion);
		ingredients3.add(salt);
		ingredients3.add(keyLime);
		ingredients3.add(greenBeans);
		ingredients3.add(friedOnions);
		ingredients3.add(cumin);
		ingredients3.add(chiliPowder);
		ingredients3.add(cayenne);
		ingredients3.add(beans);
		ingredients3.add(tomato);
		ingredients3.add(oregeno);
		ingredients3.add(mushrooms);
		ingredients3.add(cocoaPowder);
		
		one = new RecipeStep();
		one.setInstructions("Heat oil in a large pot over medium");
		one.setTime(2);
		two = new RecipeStep();
		two.setInstructions("Add garlic, peppers, onion, carrot and saute until soft");
		two.setTime(6);
		three = new RecipeStep();
		three.setInstructions("Add the rest of the ingredients and cover");
		three.setTime(3);
		four = new RecipeStep();
		four.setInstructions("Cook for 40 minutes");
		four.setTime(40);
		
		recipeSteps3.add(one);
		recipeSteps3.add(two);
		recipeSteps3.add(three);
		recipeSteps3.add(four);
		
		recipe3.setName("Vegan Chili");
		recipe3.setIngredients(ingredients3);
		recipe3.setSteps(recipeSteps3);
		
		calculateRecipeTime(recipe0);
		calculateRecipeTime(recipe1);
		calculateRecipeTime(recipe2);
		calculateRecipeTime(recipe3);
		
		
		/*
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
		*/
	}	
	
}
