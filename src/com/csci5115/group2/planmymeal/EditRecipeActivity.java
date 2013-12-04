package com.csci5115.group2.planmymeal;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csci5115.group2.planmymeal.database.DataSourceManager;

public class EditRecipeActivity extends Activity
{

	public Recipe recipe;
	public EditRecipeActivity view;
	public LinearLayout tags_wrapper;
	public LinearLayout ingredients_wrapper;
	public LinearLayout steps_wrapper;
	private DataSourceManager datasource;
	private Typeface fontAwesome;
	private boolean newIngredient = true;
	private long currentIngredientId = 0;
	private View currentIngredientView = null;
	private boolean newStep = true;
	private long currentStepId = 0;
	private View currentStepView = null;

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
		if(recipeId > 0)
		{
			recipe = datasource.getRecipeById(recipeId);
		}
		else
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
			ingredientName.setText(ingredient.getName());

			Button editButton = (Button) ingredientView
					.findViewById(R.id.row_edit_ingredient_buttonEdit);
			editButton.setTypeface(fontAwesome);
			editButton.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					// Populate ingredient shtuff
					((EditText) findViewById(R.id.edit_recipe_ingredient_name))
							.setText(ingredient.getName());
					((EditText) findViewById(R.id.edit_recipe_ingredient_amount))
							.setText(Double.toString(ingredient.getAmount()));
					((EditText) findViewById(R.id.edit_recipe_ingredient_unit))
							.setText(ingredient.getUnit());
					newIngredient = false;
					currentIngredientId = ingredient.getId();
					currentIngredientView = ingredientView;
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
					ingredientView.setVisibility(View.GONE);
					datasource.deleteRecipeIngredient(ingredient,
							recipe.getId());
					if (ingredient.getId() == currentIngredientId)
					{
						newIngredient = true;
						currentIngredientId = 0;
						currentIngredientView = null;
					}
				}
			});

			ingredients_wrapper.addView(ingredientView);
		}

		// Save ingredient Button
		Button saveIngredient = (Button) findViewById(R.id.edit_recipe_ingredient_save);
		saveIngredient.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO: Check if valid

				// Hide keyboard
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

				// Save state
				final String iName = ((EditText) findViewById(R.id.edit_recipe_ingredient_name))
						.getText().toString();
				final String iAmount = ((EditText) findViewById(R.id.edit_recipe_ingredient_amount))
						.getText().toString();
				final String iUnit = ((EditText) findViewById(R.id.edit_recipe_ingredient_unit))
						.getText().toString();
				long amountLong = Long.parseLong(iAmount);
				long recipeId = recipe.getId();
				final Ingredient addedOrUpdatedIngredient;
				// Save to db
				if (newIngredient)
				{
					addedOrUpdatedIngredient = datasource
							.addIngredientToRecipe(iName, amountLong, iUnit,
									recipeId);
					newIngredient = true;
					currentIngredientId = 0;
					currentIngredientView = null;
					Toast toast = Toast.makeText(getBaseContext(),
							"New Ingredient Saved", Toast.LENGTH_SHORT);
					toast.show();
				} else
				{
					addedOrUpdatedIngredient = datasource
							.updateRecipeIngredient(currentIngredientId, iName,
									amountLong, iUnit, recipeId);
					currentIngredientView.setVisibility(View.GONE);
					newIngredient = true;
					currentIngredientId = 0;
					currentIngredientView = null;
					Toast toast = Toast.makeText(getBaseContext(),
							"Ingredient Updated", Toast.LENGTH_SHORT);
					toast.show();
				}

				// Clear data
				((EditText) findViewById(R.id.edit_recipe_ingredient_name))
						.setText("");
				((EditText) findViewById(R.id.edit_recipe_ingredient_amount))
						.setText("");
				((EditText) findViewById(R.id.edit_recipe_ingredient_unit))
						.setText("");

				final View ingredientView = getLayoutInflater().inflate(
						R.layout.row_edit_ingredient, null);

				TextView ingredientName = (TextView) ingredientView
						.findViewById(R.id.row_edit_ingredient_name);
				ingredientName.setText(iName);

				Button editButton = (Button) ingredientView
						.findViewById(R.id.row_edit_ingredient_buttonEdit);
				editButton.setTypeface(fontAwesome);
				editButton.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						// Populate ingredient shtuff
						((EditText) findViewById(R.id.edit_recipe_ingredient_name))
								.setText(iName);
						((EditText) findViewById(R.id.edit_recipe_ingredient_amount))
								.setText(iAmount);
						((EditText) findViewById(R.id.edit_recipe_ingredient_unit))
								.setText(iUnit);

						newIngredient = false;
						currentIngredientId = addedOrUpdatedIngredient.getId();
						currentIngredientView = ingredientView;
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
						ingredientView.setVisibility(View.GONE);
						datasource.deleteRecipeIngredient(
								addedOrUpdatedIngredient, recipe.getId());
						if (addedOrUpdatedIngredient.getId() == currentIngredientId)
						{
							newIngredient = true;
							currentIngredientId = 0;
							currentIngredientView = null;
						}
					}
				});
				ingredients_wrapper.addView(ingredientView);
			}
		});

		// Save step button.
		Button saveStep = (Button) findViewById(R.id.edit_recipe_step_save);
		saveStep.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO: Check if valid

				// Hide keyboard
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);

				// Save Values
				final String iName = ((EditText) findViewById(R.id.edit_recipe_step_name))
						.getText().toString();
				final String iTime = ((EditText) findViewById(R.id.edit_recipe_step_time))
						.getText().toString();
				final LinkedList<String> appliancesUsed = new LinkedList<String>();
				if (((CheckBox) findViewById(R.id.oven_used_chk_box))
						.isChecked())
				{
					appliancesUsed.add("oven");
				}
				if (((CheckBox) findViewById(R.id.microwave_used_chk_box))
						.isChecked())
				{
					appliancesUsed.add("microwave");
				}
				if (((CheckBox) findViewById(R.id.burner_used_chk_box))
						.isChecked())
				{
					appliancesUsed.add("burner");
				}
				// get selected radio button from radioGroup
				int selectedId = ((RadioGroup) findViewById(R.id.radio_step_active_group))
						.getCheckedRadioButtonId();
				final boolean isActiveStep = selectedId == R.id.step_active;
				long timeLong = Long.parseLong(iTime);
				long recipeId = recipe.getId();
				// Save to db
				final RecipeStep addedOrUpdatedStep;
				if (newStep)
				{
					addedOrUpdatedStep = datasource.addStepToRecipe(iName,
							timeLong, isActiveStep, appliancesUsed, recipeId);
					newStep = true;
					currentStepId = 0;
					currentStepView = null;
					Toast toast = Toast.makeText(getBaseContext(),
							"New Recipe Step Saved", Toast.LENGTH_SHORT);
					toast.show();
				} else
				{
					addedOrUpdatedStep = datasource.updateRecipeStep(
							currentStepId, iName, timeLong, isActiveStep,
							appliancesUsed, recipeId);
					currentStepView.setVisibility(View.GONE);
					newStep = true;
					currentStepId = 0;
					currentStepView = null;
					Toast toast = Toast.makeText(getBaseContext(),
							"Recipe Step Updated", Toast.LENGTH_SHORT);
					toast.show();
				}

				// Clear data
				((EditText) findViewById(R.id.edit_recipe_step_name))
						.setText("");
				((EditText) findViewById(R.id.edit_recipe_step_time))
						.setText("");
				((CheckBox) findViewById(R.id.microwave_used_chk_box))
						.setChecked(false);
				((CheckBox) findViewById(R.id.oven_used_chk_box))
						.setChecked(false);
				((CheckBox) findViewById(R.id.burner_used_chk_box))
						.setChecked(false);
				((RadioButton) findViewById(R.id.step_active)).setChecked(true);

				final View stepView = getLayoutInflater().inflate(
						R.layout.row_edit_step, null);

				TextView stepName = (TextView) stepView
						.findViewById(R.id.row_edit_step_name);
				stepName.setText(iName);

				Button editButton = (Button) stepView
						.findViewById(R.id.row_edit_step_buttonEdit);
				editButton.setTypeface(fontAwesome);
				editButton.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						// Populate step shtuff
						((EditText) findViewById(R.id.edit_recipe_step_name))
								.setText(iName);
						((EditText) findViewById(R.id.edit_recipe_step_time))
								.setText(iTime);
						// initialize appliances and active status
						List<String> appliances = appliancesUsed;
						((CheckBox) findViewById(R.id.oven_used_chk_box))
								.setChecked(false);
						((CheckBox) findViewById(R.id.microwave_used_chk_box))
								.setChecked(false);
						((CheckBox) findViewById(R.id.burner_used_chk_box))
								.setChecked(false);
						((RadioButton) findViewById(R.id.step_active))
								.setChecked(true);
						if (appliances.contains("oven"))
						{
							((CheckBox) findViewById(R.id.oven_used_chk_box))
									.setChecked(true);
						}
						if (appliances.contains("microwave"))
						{
							((CheckBox) findViewById(R.id.microwave_used_chk_box))
									.setChecked(true);
						}
						if (appliances.contains("burner"))
						{
							((CheckBox) findViewById(R.id.burner_used_chk_box))
									.setChecked(true);
						}
						if (!isActiveStep)
						{
							((RadioButton) findViewById(R.id.step_inactive))
									.setChecked(true);
						}

						newStep = false;
						currentStepId = addedOrUpdatedStep.getId();
						currentStepView = stepView;
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
						stepView.setVisibility(View.GONE);
						datasource.deleteRecipeStep(addedOrUpdatedStep);
						if (addedOrUpdatedStep.getId() == currentStepId)
						{
							newStep = true;
							currentStepId = 0;
							currentStepView = null;
						}
					}
				});

				steps_wrapper.addView(stepView);
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
					// Populate step shtuff
					((EditText) findViewById(R.id.edit_recipe_step_name))
							.setText(step.getInstructions());
					((EditText) findViewById(R.id.edit_recipe_step_time))
							.setText(Double.toString(step.getTime()));
					// initialize appliances and active status
					List<String> appliances = step.getAppliancesUsed();
					((CheckBox) findViewById(R.id.oven_used_chk_box))
							.setChecked(false);
					((CheckBox) findViewById(R.id.microwave_used_chk_box))
							.setChecked(false);
					((CheckBox) findViewById(R.id.burner_used_chk_box))
							.setChecked(false);
					((RadioButton) findViewById(R.id.step_active))
							.setChecked(true);
					if (appliances.contains("oven"))
					{
						((CheckBox) findViewById(R.id.oven_used_chk_box))
								.setChecked(true);
					}
					if (appliances.contains("microwave"))
					{
						((CheckBox) findViewById(R.id.microwave_used_chk_box))
								.setChecked(true);
					}
					if (appliances.contains("burner"))
					{
						((CheckBox) findViewById(R.id.burner_used_chk_box))
								.setChecked(true);
					}
					if (!step.isActiveStep())
					{
						((RadioButton) findViewById(R.id.step_inactive))
								.setChecked(true);
					}

					newStep = false;
					currentStepId = step.getId();
					currentStepView = stepView;
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
					stepView.setVisibility(View.GONE);
					datasource.deleteRecipeStep(step);
					if (step.getId() == currentStepId)
					{
						newStep = true;
						currentStepId = 0;
						currentStepView = null;
					}
				}
			});

			steps_wrapper.addView(stepView);
		}
		
		Button saveRecipeButton = (Button) findViewById(R.id.saveRecipeButton);
		saveRecipeButton.setTypeface(fontAwesome);
		saveRecipeButton.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// Check if input is valid
				String recipeName = ((EditText) findViewById(R.id.edit_recipe_recipeName)).getText().toString();
				int numServings = Integer.parseInt(((EditText) findViewById(R.id.edit_recipe_numServings)).getText().toString());
				String recipeDescription = "";
				double recipeTime = 5.5;
				
				
				// Update in db
				datasource.updateRecipe(recipe.getId(), recipeName, recipeTime,
						recipeDescription, numServings);
				// Pop up
				Toast toast = Toast.makeText(getBaseContext(),
						"Recipe Saved", Toast.LENGTH_SHORT);
				toast.show();
				// Go to home activity
				Intent intent = new Intent(getBaseContext(), HomeActivity.class);
	    		startActivity(intent);
			}
		});
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
	public void onDestroy() {
		super.onDestroy();
		HomeActivity.updateData();
	}

}
