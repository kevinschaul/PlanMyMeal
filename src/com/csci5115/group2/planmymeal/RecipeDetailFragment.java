package com.csci5115.group2.planmymeal;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csci5115.group2.planmymeal.database.DataSourceManager;

/**
 * A fragment representing a single Meal detail screen. This fragment is either
 * contained in a {@link MealListActivity} in two-pane mode (on tablets) or a
 * {@link MealDetailActivity} on handsets.
 */
public class RecipeDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";
	public static final String ARG_FROM_MEAL = "from_meal";
	public static final String ARG_FROM_MEAL_ID = "from_meal_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Recipe recipe;
	private Typeface fontAwesome;
	private boolean fromMeal = false;
	private long mealId;
	private View rootView;
	private LayoutInflater inflater;
	private long recipeId;
	
	// Databases
	private DataSourceManager datasource;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public RecipeDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Context context = this.getActivity().getApplicationContext();
		datasource = new DataSourceManager(context);
		datasource.open();
		
		this.fontAwesome = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf" );
		
		Log.v(HomeActivity.TAG, "onCreate");
		
		Bundle arguments = getArguments();

		if (arguments.containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			recipeId = arguments.getLong(ARG_ITEM_ID);
			recipe = datasource.getRecipeById(recipeId);
		}
		
		if (arguments.containsKey(ARG_FROM_MEAL) && arguments.containsKey(ARG_FROM_MEAL_ID)) {
			fromMeal = getArguments().getBoolean(ARG_FROM_MEAL);
			mealId = arguments.getLong(ARG_FROM_MEAL_ID);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		datasource.close();
	}

	@Override
	public View onCreateView(LayoutInflater _inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = _inflater.inflate(R.layout.fragment_recipe_detail,
				container, false);
		
		inflater = _inflater;

		fillInMealData();

		return rootView;
	}
	
	private void fillInMealData() {
		//recipe = datasource.getRecipeById(recipeId);
		
		if (recipe != null) {
			TextView name = (TextView) rootView.findViewById(R.id.fragment_recipe_title);
			name.setText(recipe.getName());
			
			TextView time = (TextView) rootView.findViewById(R.id.fragment_recipe_time);
			time.setText(recipe.getReadableTime());
			
			Button cook = (Button) rootView.findViewById(R.id.fragment_recipe_button_Cook);
			cook.setOnClickListener(clickListener);
			
			Button edit = (Button) rootView.findViewById(R.id.fragment_recipe_button_edit);
			edit.setOnClickListener(clickListener);
			
			Button delete = (Button) rootView.findViewById(R.id.fragment_recipe_button_delete);
			delete.setOnClickListener(clickListener);
			
			if (fromMeal) {
				delete.setText("Remove from meal");
			}
			
			LinearLayout tags_wrapper = (LinearLayout) rootView.findViewById(R.id.fragment_recipe_tags_wrapper);
			List<Tag> tags = recipe.getTags();
			for (Tag tag : tags) {
				final View tagView = inflater.inflate(R.layout.tag, null);
				
				TextView tagName = (TextView) tagView.findViewById(R.id.tag_name);
				tagName.setText(tag.getName());
				
				Button tagDelete = (Button) tagView.findViewById(R.id.tag_button_delete);
				tagDelete.setTypeface(fontAwesome);
				
				tagDelete.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						tagView.setVisibility(View.GONE);
					}
				});
				
				tags_wrapper.addView(tagView);
			}
			
			// TODO Include unit and amount
			// TODO Fix layout to accommodate all fields
			LinearLayout ingredients_wrapper = (LinearLayout) rootView.findViewById(R.id.fragment_recipe_ingredients_wrapper);
			List<Ingredient> ingredients = datasource.getRecipeIngredients(recipeId);
			
			for (Ingredient ingredient : ingredients) {
				View ingredientView = inflater.inflate(R.layout.ingredient, null);
				
				TextView ingredientName = (TextView) ingredientView.findViewById(R.id.ingredient_name);
				ingredientName.setText(ingredient.getName());
				
				ingredients_wrapper.addView(ingredientView);
			}
			
			LinearLayout steps_wrapper = (LinearLayout) rootView.findViewById(R.id.fragment_recipe_steps_wrapper);
			List<RecipeStep> steps = datasource.getRecipeSteps(recipeId);
			
			for (int i = 0; i < steps.size(); i++) {
				RecipeStep step = steps.get(i);
				View stepView = inflater.inflate(R.layout.recipe_step, null);
				
				TextView stepName = (TextView) stepView.findViewById(R.id.step_instruction);
				stepName.setText(step.getInstructions());
				
				steps_wrapper.addView(stepView);
			}
		}
	}
	
	public void updateData() {
		fillInMealData();
	}
	
	public View.OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Context context = v.getContext();
			Intent intent;
			
		    switch (v.getId()) {
		        case R.id.fragment_recipe_button_delete:
		        	if (fromMeal) {
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setTitle("Remove recipe?");
						builder.setMessage("Remove " + recipe.getName() + " from meal?");
						builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								datasource.deleteMealRecipe(mealId, recipe.getId());
								HomeActivity.showColumns(2);
								HomeActivity.updateData();
							}
						});
						builder.setNegativeButton("Cancel", null);
						AlertDialog dialog = builder.create();
						dialog.show();
		        	} else {
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setTitle("Delete recipe?");
						builder.setMessage("Delete " + recipe.getName() + "?");
						builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {   
								datasource.deleteRecipe(recipe);
								HomeActivity.showColumns(1);
								HomeActivity.updateData();
							}
						});
						builder.setNegativeButton("Cancel", null);
						AlertDialog dialog = builder.create();
						dialog.show();
		        	}
		    		break;
		        case R.id.fragment_recipe_button_edit:
		    		intent = new Intent(context, EditRecipeActivity.class);
		    		intent.putExtra(HomeActivity.EXTRA_RECIPE, recipe.getId());
		    		startActivity(intent);
		    		break;
		        case R.id.fragment_recipe_button_Cook:
		    		intent = new Intent(context, CookActivity.class);
		    		intent.putExtra(HomeActivity.EXTRA_MEAL, recipe.getId());
		    		intent.putExtra(HomeActivity.TAG, "Recipe");
		    		startActivity(intent);
		    		break;
		        default:
		            return;
		    }
		}
	};
}
