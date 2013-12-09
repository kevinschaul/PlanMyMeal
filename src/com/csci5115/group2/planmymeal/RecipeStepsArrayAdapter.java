package com.csci5115.group2.planmymeal;

import java.util.LinkedList;

import com.csci5115.group2.planmymeal.database.DataSourceManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RecipeStepsArrayAdapter extends ArrayAdapter<RecipeStep> {
	private final Context context;
	private final LinkedList<RecipeStep> values;
	public boolean isCurrentRecipe;
	public boolean isPreviousCompleted;
	public DataSourceManager datasource;
	public Uri notification;
	public Ringtone r;
	
	public RecipeStepsArrayAdapter(Context context, LinkedList<RecipeStep> recipeSteps) {
		super(context, R.layout.row_recipe_steps, recipeSteps);
		this.context = context;
		this.values = recipeSteps;
		this.isCurrentRecipe = false;
		this.isPreviousCompleted = false;
		
		datasource = new DataSourceManager(context);
        datasource.open();
        
        UserSettings settings = datasource.getUserSettings();
        int startSoundType = settings.getStartSoundAlarmValue();
        
        notification = RingtoneManager.getDefaultUri(startSoundType);
		r = RingtoneManager.getRingtone(context, notification);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	    Typeface fontAwesome = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf" );
	    View rowView = inflater.inflate(R.layout.row_recipe_steps, parent, false);
	    
	    TextView stepDescription = (TextView) rowView.findViewById(R.id.row_step_text);
	    
	    
	    RecipeStep recipeStep = values.get(position);
	    stepDescription.setText(recipeStep.getInstructions());
	    
	    
	    
	    
	    if(recipeStep.isCompleted() && recipeStep.isActiveStep())
	    {
	    	stepDescription.setTextColor(Color.GRAY);
	    	stepDescription.setPaintFlags(stepDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
	    	isPreviousCompleted = true;
	    }
	    else if(recipeStep.getToWatch() && !recipeStep.isActiveStep())
	    {
	    	long timeToSet;
	    	if(recipeStep.getTimer() == null)
	    	{
	    		timeToSet = (long)recipeStep.getTime()*60000;
	    	
		    	//recipeStep.setTimer(new StepTimer(timeToSet, stepDescription, stepDescription));
		    	
	    		//recipeStep.currentStepDescription = stepDescription;
	    		//recipeStep.currentStepTime = stepDescription;
	    		//recipeStep.timerSet = true;	
	    		//recipeStep.setTimer(new StepTimer(timeToSet, stepDescription, stepDescription));
	    		//timeToSet = (long)recipeStep.getTimer().timeTilFinished;
	    		//values.set(position, recipeStep);
	    		//recipeStep.setTimer(new StepTimer(timeToSet, stepDescription, stepDescription));
	    	}
	    	else
	    	{
	    		//recipeStep.getTimer().start();
	    		
	    		timeToSet = values.get(position).getTimer().timeTilFinished - 400;
	    		if(values.get(position).getTimer().isFinished)
	    		{
	    			timeToSet = timeToSet-60000;//(long)recipeStep.getTimer().timeTilFinished;
	    		}
		    	//recipeStep.getTimer().cancel();     		//recipeStep.getTimer().start();
	    		values.get(position).getTimer().setTextViews(stepDescription, stepDescription);
	    			    		//recipeStep.getTimer().timeTilFinished);//recipeStep.getTimer().start());
	    		//recipeStep.getTimer().start();
	    		//recipeStep.getTimer().cancel();
	    		
	    		//recipeStep.setTimer(new StepTimer(recipeStep.getTimer().timeTilFinished, stepDescription, stepDescription));

	    		//recipeStep.getTimer().start();
	    		//recipeStep.setTimer(createTimer((long)recipeStep.getTime(), recipeStep));
	    		//values.set(position, recipeStep);
	    	}
	    	
	    	//recipeStep.getTimer().cancel();
	    	values.get(position).setTimer(new StepTimer(timeToSet, stepDescription, stepDescription));
	    	// ecipeStep.getTimer().start();
	    	values.get(position).getTimer().start();
	    	//

	     	
    		//CountDownTimer cdT = recipeStep.getTimer().createTimer(timeToSet);//(long)recipeStep.getTimer().timeTilFinished);
    		//recipeStep.getTimer().setCountDownTimer(cdT);
	    	//recipeStep.getTimer().start();
    		values.set(position, recipeStep);
    		//recipeStep.getTimer().start();
	    	//recipeStep.getTimer().start();
	    	//CountDownTimer cdT = recipeStep.getTimer().createTimer(timeToSet);
    		//recipeStep.getTimer().setCountDownTimer(cdT);
    		//recipeStep.getTimer().start();
    		//recipeStep.currentStepTime = stepDescription;
    		//recipeStep.setTimer(createTimer(timeToSet, recipeStep).start());
	    	isPreviousCompleted = true;
	    	
    		//values.set(position, recipeStep);
	    	stepDescription.setBackgroundColor(CookActivity.GREEN);
	    }
	    else  if(position == 0 && isCurrentRecipe){
	    		//stepDescription.setBackgroundColor(Color.RED);
	    		stepDescription.setPaintFlags(stepDescription.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
	    }
	    else{
	    	if(isPreviousCompleted && !recipeStep.isCompleted() && isCurrentRecipe)
	    	{
	    		//stepDescription.setBackgroundColor(Color.RED);
	    		stepDescription.setPaintFlags(stepDescription.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
	    		isPreviousCompleted = false;
	    	}
	    }
	    
	    return rowView;
	}
	
}
