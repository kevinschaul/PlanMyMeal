package com.csci5115.group2.planmymeal;

import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.csci5115.group2.planmymeal.database.DataSourceManager;

public class EditRecipeActivity extends FragmentActivity
{

	public Recipe recipe;
	public EditRecipeActivity view;
	public LinearLayout tags_wrapper;
	public LinearLayout ingredients_wrapper;
	public LinearLayout steps_wrapper;
	private DataSourceManager datasource;
	private Typeface fontAwesome;
	private boolean showingIngredient = false;
	private boolean showingStep = false;
	private long selectedIngredient = 0;
	private long selectedStep = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_recipe);

		view = this;

		// Database Creation
		datasource = new DataSourceManager(this);
		datasource.open();

		this.fontAwesome = Typeface.createFromAsset(getAssets(),
				"fontawesome-webfont.ttf");

		Intent intent = getIntent();
		long recipeId = intent.getLongExtra(HomeActivity.EXTRA_RECIPE, 0);
		if (recipeId > 0)
		{
			recipe = datasource.getRecipeById(recipeId);
		} else
		{
			// Create new recipe entry
			recipe = datasource.createNewUserRecipe("", 0, "", 0);
		}

		EditText recipeNameTextView = (EditText) findViewById(R.id.edit_recipe_recipeName);
		recipeNameTextView.setText(recipe.getName());

		EditText recipeNumServings = (EditText) findViewById(R.id.edit_recipe_numServings);
		recipeNumServings.setText(Long.toString(recipe.getNumServings()));

		EditText newTagText = (EditText) findViewById(R.id.edit_recipe_newTagName);
		newTagText.setInputType(newTagText.getInputType()
				| EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS
				| EditorInfo.TYPE_TEXT_VARIATION_FILTER);

		tags_wrapper = (LinearLayout) findViewById(R.id.edit_recipe_tag_container);
		List<Tag> tags = datasource.getRecipeTags(recipe.getId());
		for (final Tag tag : tags)
		{
			final View tagView = getLayoutInflater()
					.inflate(R.layout.tag, null);

			TextView tagName = (TextView) tagView.findViewById(R.id.tag_name);
			tagName.setText(tag.getName());

			Button tagDelete = (Button) tagView
					.findViewById(R.id.tag_button_delete);
			tagDelete.setTypeface(fontAwesome);

			tagDelete.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					tagView.setVisibility(View.GONE);
					datasource.deleteRecipeTag(tag, recipe.getId());
				}
			});

			tags_wrapper.addView(tagView);
		}

		Button addTagButton = (Button) findViewById(R.id.addTagButton);
		addTagButton.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				final View tagView = getLayoutInflater().inflate(R.layout.tag,
						null);

				// Hide keyboard
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

				EditText newTagText = (EditText) findViewById(R.id.edit_recipe_newTagName);

				String tagText = newTagText.getText().toString();
				newTagText.setText("");
				newTagText.setHint("New Tag Name");

				// Add tag to recipe
				recipe.tags.add(new Tag(tagText));
				final Tag tag = datasource.addTagToRecipe(tagText,
						recipe.getId());

				TextView tagName = (TextView) tagView
						.findViewById(R.id.tag_name);
				tagName.setText(tag.getName());

				Button tagDelete = (Button) tagView
						.findViewById(R.id.tag_button_delete);
				tagDelete.setTypeface(fontAwesome);

				tagDelete.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						tagView.setVisibility(View.GONE);
						datasource.deleteRecipeTag(tag, recipe.getId());
					}
				});

				tags_wrapper.addView(tagView);
			}
		});

		// Set up Ingredients List
		ingredients_wrapper = (LinearLayout) findViewById(R.id.edit_recipe_ingredientsList);
		List<Ingredient> ingredients = datasource.getRecipeIngredients(recipe
				.getId());

		for (final Ingredient ingredient : ingredients)
		{
			final View ingredientView = getLayoutInflater().inflate(
					R.layout.row_edit_ingredient, null);

			TextView ingredientName = (TextView) ingredientView
					.findViewById(R.id.row_edit_ingredient_name);
			
			ingredientName.setText(ingredient.getName() + "   ( " + ingredient.getAmount() + " - " + ingredient.getUnit() + " )");

			Button editButton = (Button) ingredientView
					.findViewById(R.id.row_edit_ingredient_buttonEdit);
			editButton.setTypeface(fontAwesome);
			editButton.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					if (showingStep)
					{
						// clear edit step form
						FrameLayout fm = ((FrameLayout) findViewById(R.id.edit_recipe_new_step_container));
						fm.removeAllViews();
					}
					showingIngredient = true;
					selectedIngredient = ingredient.getId();
					Bundle arguments = new Bundle();
					arguments.putLong(EditIngredientFragment.ARG_INGREDIENT_ID,
							ingredient.getId());
					arguments.putLong(EditIngredientFragment.ARG_RECIPE_ID,
							recipe.getId());
					EditIngredientFragment fragment = new EditIngredientFragment();
					fragment.setArguments(arguments);
					getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.edit_recipe_new_i_container, fragment)
							.commit();
				}
			});

			Button deleteButton = (Button) ingredientView
					.findViewById(R.id.row_edit_ingredient_buttonDelete);
			deleteButton.setTypeface(fontAwesome);
			deleteButton.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					if (selectedIngredient != 0
							&& selectedIngredient == ingredient.getId())
					{
						// clear edit ingredient form
						FrameLayout fm = ((FrameLayout) findViewById(R.id.edit_recipe_new_i_container));
						fm.removeAllViews();
					}

					ingredientView.setVisibility(View.GONE);
					datasource.deleteRecipeIngredient(ingredient,
							recipe.getId());
					// TODO Set the fragment to the original one.
					// if (ingredient.getId() == currentIngredientId)
					// {
					// newIngredient = true;
					// currentIngredientId = 0;
					// currentIngredientView = null;
					// }
				}
			});

			ingredients_wrapper.addView(ingredientView);
		}

		Button newIngredientButton = (Button) findViewById(R.id.addIngredientButton);
		newIngredientButton.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (showingStep)
				{
					// Dialog, would you like to save ingredient save changes
					// Yes - Save ; No - Undo Changes

					// clear edit step form
					FrameLayout fm = ((FrameLayout) findViewById(R.id.edit_recipe_new_step_container));
					fm.removeAllViews();
				}
				showingIngredient = true;
				Bundle arguments = new Bundle();
				arguments.putLong(EditIngredientFragment.ARG_INGREDIENT_ID, 0);
				arguments.putLong(EditIngredientFragment.ARG_RECIPE_ID,
						recipe.getId());
				EditIngredientFragment fragment = new EditIngredientFragment();
				fragment.setArguments(arguments);
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.edit_recipe_new_i_container, fragment)
						.commit();
			}
		});

		// Set up recipe directions list
		steps_wrapper = (LinearLayout) findViewById(R.id.edit_recipe_directionsList);
		List<RecipeStep> steps = datasource.getRecipeSteps(recipe.getId());

		for (final RecipeStep step : steps)
		{
			final View stepView = getLayoutInflater().inflate(
					R.layout.row_edit_step, null);

			TextView stepName = (TextView) stepView
					.findViewById(R.id.row_edit_step_name);
			stepName.setText(step.getInstructions());

			Button editButton = (Button) stepView
					.findViewById(R.id.row_edit_step_buttonEdit);
			editButton.setTypeface(fontAwesome);
			editButton.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					if (showingIngredient)
					{
						// Dialog, would you like to save ingredient save
						// changes
						// Yes - Save ; No - Undo Changes

						// clear edit ingredient form
						FrameLayout fm = ((FrameLayout) findViewById(R.id.edit_recipe_new_i_container));
						fm.removeAllViews();
					}
					showingStep = true;
					selectedStep = step.getId();
					// Start fragment
					Bundle arguments = new Bundle();
					arguments.putLong(EditStepFragment.ARG_STEP_ID,
							step.getId());
					arguments.putLong(EditStepFragment.ARG_RECIPE_ID,
							recipe.getId());
					EditStepFragment fragment = new EditStepFragment();
					fragment.setArguments(arguments);
					getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.edit_recipe_new_step_container,
									fragment).commit();
				}
			});

			Button deleteButton = (Button) stepView
					.findViewById(R.id.row_edit_step_buttonDelete);
			deleteButton.setTypeface(fontAwesome);
			deleteButton.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					if (selectedStep != 0 && selectedStep == step.getId())
					{
						// clear edit step form
						FrameLayout fm = ((FrameLayout) findViewById(R.id.edit_recipe_new_step_container));
						fm.removeAllViews();
					}
					stepView.setVisibility(View.GONE);
					datasource.deleteRecipeStep(step);
				}
			});

			steps_wrapper.addView(stepView);
		}

		Button newStepButton = (Button) findViewById(R.id.addStepButton);
		newStepButton.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (showingIngredient)
				{
					// Dialog, would you like to save ingredient save changes
					// Yes - Save ; No - Undo Changes

					// clear edit ingredient form
					FrameLayout fm = ((FrameLayout) findViewById(R.id.edit_recipe_new_i_container));
					fm.removeAllViews();
				}
				showingStep = true;
				Bundle arguments = new Bundle();
				arguments.putLong(EditStepFragment.ARG_STEP_ID, 0);
				arguments.putLong(EditStepFragment.ARG_RECIPE_ID,
						recipe.getId());
				EditStepFragment fragment = new EditStepFragment();
				fragment.setArguments(arguments);
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.edit_recipe_new_step_container, fragment)
						.commit();
			}
		});

		Button saveRecipeButton = (Button) findViewById(R.id.saveRecipeButton);
		saveRecipeButton.setTypeface(fontAwesome);
		saveRecipeButton.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Context iContext = v.getContext();
				// Check if input is valid
				String recipeName = ((EditText) findViewById(R.id.edit_recipe_recipeName))
						.getText().toString();
				String numServingsString = ((EditText) findViewById(R.id.edit_recipe_numServings))
				.getText().toString();
				if(checkIfValidRecipe(recipeName, numServingsString, iContext))
				{
				int numServings = Integer
						.parseInt(numServingsString);
				String recipeDescription = "";
				double recipeTime = 5.5;
				
					// Update in db
					datasource.updateRecipe(recipe.getId(), recipeName, recipeTime,
							recipeDescription, numServings);
					// Pop up
					Toast toast = Toast.makeText(getBaseContext(), "Recipe Saved",
							Toast.LENGTH_SHORT);
					toast.show();
					// Go to home activity
					Intent intent = new Intent(getBaseContext(), HomeActivity.class);
					startActivity(intent);
				}
			}
		});
	}
	
	private boolean checkIfValidRecipe(String iName, String numServings, Context icontext)
	{
		String errors = "";
		boolean error = false;
		if(iName == null || iName.isEmpty())
		{
			errors += "Must fill out Recipe Name!\n\n";
			error = true;
		}
		if(numServings == null || numServings.isEmpty())
		{
			errors += "Must fill out Number of Servings!\n\n";
			error = true;
		}
		else
		{
			try
			{
				int num = Integer.parseInt(numServings);
				if(num < 1)
				{
					errors += "Number of Servings must be at least 1!  ";
					error = true;
				}
			}
			catch(NumberFormatException  e)
			{
				errors += "Must enter valid Number of Servings!  ";
				error = true;
			}
		}
		
		if(error)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(icontext);
			builder.setTitle("Please fix form errors");
			builder.setMessage(errors);
			builder.setPositiveButton("OK", null);
			AlertDialog dialog = builder.create();
			dialog.show();
			return false;
		}
		return true;
	}

	@Override
	protected void onResume()
	{
		datasource.open();
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		datasource.close();
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.community_cookbook, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		HomeActivity.updateData();
	}
}
