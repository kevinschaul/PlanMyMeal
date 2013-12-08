package com.csci5115.group2.planmymeal;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.csci5115.group2.planmymeal.database.DataSourceManager;

/**
 * A list fragment representing a list of Meals. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link MealDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class EditStepFragment extends Fragment
{

	public static final String ARG_STEP_ID = "step_id";
	public static final String ARG_RECIPE_ID = "recipe_id";
	private View rootView;
	private long recipeId;
	private Recipe recipe;
	private long stepId;
	private RecipeStep step;
	private boolean newStep;
	private Typeface fontAwesome;

	// Databases
	private DataSourceManager datasource;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public EditStepFragment()
	{
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.v("EditStepFragment", "onCreate");
		super.onCreate(savedInstanceState);

		Context context = this.getActivity().getApplicationContext();
		datasource = new DataSourceManager(context);
		datasource.open();

		this.fontAwesome = Typeface.createFromAsset(context.getAssets(),
				"fontawesome-webfont.ttf");

		// Initialize Recipe
		if (getArguments().containsKey(ARG_RECIPE_ID))
		{
			recipeId = getArguments().getLong(ARG_RECIPE_ID);
			recipe = datasource.getRecipeById(recipeId);
		}

		// Initialize Ingredient
		if (getArguments().containsKey(ARG_STEP_ID))
		{
			if (getArguments().getLong(ARG_STEP_ID) > 0)
			{
				stepId = getArguments().getLong(ARG_STEP_ID);
				step = datasource.getRecipeStepById(stepId);
				newStep = false;
			} else
			{
				// Create new ingredient & add to recipe
				step = datasource.addStepToRecipe("", 0.0, true,
						new LinkedList<String>(), recipeId);
				stepId = step.getId();
				newStep = true;
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflator, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		rootView = inflator.inflate(R.layout.edit_step_fragment, container,
				false);

		Context context = this.getActivity().getApplicationContext();
		datasource = new DataSourceManager(context);
		datasource.open();

		// Initialize Step fields
		((EditText) rootView.findViewById(R.id.edit_recipe_step_name))
				.setText(step.getInstructions());
		((EditText) rootView.findViewById(R.id.edit_recipe_step_time))
				.setText(Double.toString(step.getTime()));
		// initialize appliances and active status
		List<String> appliances = step.getAppliancesUsed();
		((CheckBox) rootView.findViewById(R.id.oven_used_chk_box))
				.setChecked(false);
		((CheckBox) rootView.findViewById(R.id.microwave_used_chk_box))
				.setChecked(false);
		((CheckBox) rootView.findViewById(R.id.burner_used_chk_box))
				.setChecked(false);
		((RadioButton) rootView.findViewById(R.id.step_active))
				.setChecked(true);
		if (appliances.contains("oven"))
		{
			((CheckBox) rootView.findViewById(R.id.oven_used_chk_box))
					.setChecked(true);
		}
		if (appliances.contains("microwave"))
		{
			((CheckBox) rootView.findViewById(R.id.microwave_used_chk_box))
					.setChecked(true);
		}
		if (appliances.contains("burner"))
		{
			((CheckBox) rootView.findViewById(R.id.burner_used_chk_box))
					.setChecked(true);
		}
		if (!step.isActiveStep())
		{
			((RadioButton) rootView.findViewById(R.id.step_inactive))
					.setChecked(true);
		}

		// Save Step Button
		Button saveStep = (Button) rootView
				.findViewById(R.id.edit_recipe_step_save);
		saveStep.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO: Check if valid

				// Save Values
				final String iName = ((EditText) rootView
						.findViewById(R.id.edit_recipe_step_name)).getText()
						.toString();
				final String iTime = ((EditText) rootView
						.findViewById(R.id.edit_recipe_step_time)).getText()
						.toString();
				final LinkedList<String> appliancesUsed = new LinkedList<String>();
				if (((CheckBox) rootView.findViewById(R.id.oven_used_chk_box))
						.isChecked())
				{
					appliancesUsed.add("oven");
				}
				if (((CheckBox) rootView
						.findViewById(R.id.microwave_used_chk_box)).isChecked())
				{
					appliancesUsed.add("microwave");
				}
				if (((CheckBox) rootView.findViewById(R.id.burner_used_chk_box))
						.isChecked())
				{
					appliancesUsed.add("burner");
				}
				// get selected radio button from radioGroup
				int selectedId = ((RadioGroup) rootView
						.findViewById(R.id.radio_step_active_group))
						.getCheckedRadioButtonId();
				final boolean isActiveStep = selectedId == R.id.step_active;
				double timeLong = Double.parseDouble(iTime);
				long recipeId = recipe.getId();
				// Save to db
				final RecipeStep addedOrUpdatedStep;
				addedOrUpdatedStep = datasource.updateRecipeStep(stepId, iName,
						timeLong, isActiveStep, appliancesUsed, recipeId);
				if(newStep)
				{
					Toast toast = Toast.makeText(rootView.getContext(),
							"New Step Saved!", Toast.LENGTH_SHORT);
					toast.show();
				}
				else
				{
					Toast toast = Toast.makeText(rootView.getContext(),
							"Recipe Step Updated!", Toast.LENGTH_SHORT);
					toast.show();
				}
				

				// Restart Edit Recipe Activity
				Intent intent = new Intent(rootView.getContext(),
						EditRecipeActivity.class);
				intent.putExtra(HomeActivity.EXTRA_RECIPE, recipeId);
				startActivity(intent);
			}
		});
		return rootView;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		datasource.close();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);

	}
}
