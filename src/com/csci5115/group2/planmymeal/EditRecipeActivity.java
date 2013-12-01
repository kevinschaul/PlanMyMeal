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
	private DataSourceManager datasource;
	private Typeface fontAwesome;

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
		long recipeId = intent.getLongExtra(HomeActivity.EXTRA_MEAL, 0);

		recipe = datasource.getRecipeById(recipeId);

		EditText recipeNameTextView = (EditText) findViewById(R.id.edit_recipe_recipeName);
		recipeNameTextView.setText(recipe.getName());

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
				InputMethodManager imm = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
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
		LinearLayout ingredients_wrapper = (LinearLayout) findViewById(R.id.edit_recipe_ingredientsList);
		List<Ingredient> ingredients = datasource.getRecipeIngredients(recipe
				.getId());

		for (Ingredient ingredient : ingredients)
		{
			View ingredientView = getLayoutInflater().inflate(
					R.layout.ingredient, null);

			TextView ingredientName = (TextView) ingredientView
					.findViewById(R.id.ingredient_name);
			ingredientName.setText(ingredient.getName());

			ingredients_wrapper.addView(ingredientView);
		}

		// Add ingredient Button
		Button saveIngredient = (Button) findViewById(R.id.edit_recipe_ingredient_save);
		saveIngredient.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Check if valid

				// Hide keyboard
				InputMethodManager imm = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),      
			    InputMethodManager.HIDE_NOT_ALWAYS);
				
				// Save state
				String iName = ((EditText) findViewById(R.id.edit_recipe_ingredient_name))
						.getText().toString();
				String iAmount = ((EditText) findViewById(R.id.edit_recipe_ingredient_amount))
						.getText().toString();
				String iUnit = ((EditText) findViewById(R.id.edit_recipe_ingredient_unit))
						.getText().toString();
				long amountLong = Long.parseLong(iAmount);
				long recipeId = recipe.getId();
				// Save to db
				datasource.addIngredientToRecipe(iName, amountLong, iUnit,
						recipeId);

				// Toastr popup for user feedback
				Toast toast = Toast.makeText(getBaseContext(),
						"New Ingredient Saved", Toast.LENGTH_SHORT);
				toast.show();
				// Clear data
				((EditText) findViewById(R.id.edit_recipe_ingredient_name))
						.setText("");
				((EditText) findViewById(R.id.edit_recipe_ingredient_amount))
						.setText("");
				((EditText) findViewById(R.id.edit_recipe_ingredient_unit))
						.setText("");

			}
		});

		// Set save step button.
		Button saveStep = (Button) findViewById(R.id.edit_recipe_step_save);
		saveStep.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Check if valid
				
				// Hide keyboard
				InputMethodManager imm = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),      
			    InputMethodManager.HIDE_NOT_ALWAYS);
				
				// Save Values
				String iName = ((EditText) findViewById(R.id.edit_recipe_step_name))
						.getText().toString();
				String iTime = ((EditText) findViewById(R.id.edit_recipe_step_time))
						.getText().toString();
				LinkedList<String> appliancesUsed = new LinkedList();
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
				int selectedId = ((RadioGroup) findViewById(R.id.radio_step_active_group)).getCheckedRadioButtonId();
				boolean isActiveStep = selectedId == R.id.step_active;
				long timeLong = Long.parseLong(iTime);
				long recipeId = recipe.getId();
				// Save to db
				datasource.addStepToRecipe(iName, timeLong, isActiveStep, appliancesUsed, recipeId);

				// Toastr popup for user feedback
				Toast toast = Toast.makeText(getBaseContext(),
						"New Step Saved", Toast.LENGTH_SHORT);
				toast.show();
				// Clear data
				((EditText) findViewById(R.id.edit_recipe_step_name))
						.setText("");
				((EditText) findViewById(R.id.edit_recipe_step_time))
						.setText("");
				((CheckBox) findViewById(R.id.microwave_used_chk_box)).setChecked(false);
				((CheckBox) findViewById(R.id.oven_used_chk_box)).setChecked(false);
				((CheckBox) findViewById(R.id.burner_used_chk_box)).setChecked(false);
				((RadioButton) findViewById(R.id.step_active)).setSelected(true);
				((RadioButton) findViewById(R.id.step_inactive)).setSelected(false);
			}
		});

		// Set up recipe directions list
		LinearLayout steps_wrapper = (LinearLayout) findViewById(R.id.edit_recipe_directionsList);
		List<RecipeStep> steps = datasource.getRecipeSteps(recipe.getId());

		for (int i = 0; i < steps.size(); i++)
		{
			RecipeStep step = steps.get(i);
			View stepView = getLayoutInflater().inflate(R.layout.recipe_step,
					null);

			TextView stepName = (TextView) stepView
					.findViewById(R.id.step_instruction);
			stepName.setText(step.getInstructions());

			steps_wrapper.addView(stepView);
		}

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

}
