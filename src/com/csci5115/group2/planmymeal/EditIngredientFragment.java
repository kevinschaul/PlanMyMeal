package com.csci5115.group2.planmymeal;

import android.app.AlertDialog;
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
import android.widget.EditText;
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
public class EditIngredientFragment extends Fragment
{

	public static final String ARG_INGREDIENT_ID = "ingredient_id";
	public static final String ARG_RECIPE_ID = "recipe_id";
	private View rootView;
	private long recipeId;
	private Recipe recipe;
	private long ingredientId;
	private Ingredient ingredient;
	private boolean newIngredient;
	private Typeface fontAwesome;

	// Databases
	private DataSourceManager datasource;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public EditIngredientFragment()
	{
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.v("EditIngredientFragment", "onCreate");
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
		if (getArguments().containsKey(ARG_INGREDIENT_ID))
		{
			if (getArguments().getLong(ARG_INGREDIENT_ID) > 0)
			{
				ingredientId = getArguments().getLong(ARG_INGREDIENT_ID);
				ingredient = datasource.getIngredientById(ingredientId);
				newIngredient = false;
			} else
			{
				// Create new ingredient & add to recipe
				ingredient = datasource.addIngredientToRecipe("", 0.0, "",
						recipeId);
				ingredientId = ingredient.getId();
				newIngredient = true;
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflator, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		rootView = inflator.inflate(R.layout.edit_ingredient_fragment,
				container, false);

		Context context = this.getActivity().getApplicationContext();
		datasource = new DataSourceManager(context);
		datasource.open();

		// Initialize Ingredient fields
		((EditText) rootView.findViewById(R.id.edit_recipe_ingredient_name))
				.setText(ingredient.getName());
		((EditText) rootView.findViewById(R.id.edit_recipe_ingredient_amount))
				.setText(Double.toString(ingredient.getAmount()));
		((EditText) rootView.findViewById(R.id.edit_recipe_ingredient_unit))
				.setText(ingredient.getUnit());

		// Save ingredient Button
		Button saveIngredient = (Button) rootView
				.findViewById(R.id.edit_recipe_ingredient_save);
		saveIngredient.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Context iContext = v.getContext();
				// Save state
				final String iName = ((EditText) rootView
						.findViewById(R.id.edit_recipe_ingredient_name))
						.getText().toString();
				final String iAmount = ((EditText) rootView
						.findViewById(R.id.edit_recipe_ingredient_amount))
						.getText().toString();
				final String iUnit = ((EditText) rootView
						.findViewById(R.id.edit_recipe_ingredient_unit))
						.getText().toString();
				if (checkIfValidIngredient(iName, iAmount, iUnit, iContext))
				{
				double amountLong = Double.parseDouble(iAmount);
					long recipeId = recipe.getId();
					final Ingredient addedOrUpdatedIngredient;
					// Save to db
					addedOrUpdatedIngredient = datasource
							.updateRecipeIngredient(ingredientId, iName,
									amountLong, iUnit, recipeId);
					if (newIngredient)
					{
						Toast toast = Toast.makeText(rootView.getContext(),
								"New Ingredient Saved!", Toast.LENGTH_SHORT);
						toast.show();
					} else
					{
						Toast toast = Toast.makeText(rootView.getContext(),
								"Ingredient Updated!", Toast.LENGTH_SHORT);
						toast.show();
					}

					// Restart Edit Recipe Activity
					Intent intent = new Intent(rootView.getContext(),
							EditRecipeActivity.class);
					intent.putExtra(HomeActivity.EXTRA_RECIPE, recipeId);
					startActivity(intent);
				}
			}

			private boolean checkIfValidIngredient(String iName,
					String iAmount, String iUnit, Context iContext)
			{
				String errors = "";
				boolean error = false;
				if (iName == null || iName.isEmpty())
				{
					errors += "Must fill out Ingredient Name!\n\n";
					error = true;
				}
				if (iUnit == null || iUnit.isEmpty())
				{
					errors += "Must fill out Ingredient Unit!\n\n";
					error = true;
				}

				if (iAmount == null || iAmount.isEmpty())
				{
					errors += "Must fill out Ingredient Time!\n\n";
					error = true;
				}
				else
				{
					try
					{
						double num = Double.parseDouble(iAmount);
						if(num < 0.0)
						{
							errors += "Ingredient Amount must be greater than 0!  ";
							error = true;
						}
					}
					catch(NumberFormatException  e)
					{
						errors += "Must enter valid Ingredient Amount!  ";
						error = true;
					}
				}
				if(error)
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(iContext);
					builder.setTitle("Please fix form errors");
					builder.setMessage(errors);
					builder.setPositiveButton("OK", null);
					AlertDialog dialog = builder.create();
					dialog.show();
					return false;
				}
				return true;
			}
		});

		// Save ingredient Button
		Button cancelChanges = (Button) rootView
				.findViewById(R.id.edit_recipe_ingredient_cancel);
		cancelChanges.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (newIngredient)
				{
					datasource.deleteRecipeIngredient(ingredient, recipeId);
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
