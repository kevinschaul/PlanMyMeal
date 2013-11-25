package com.csci5115.group2.planmymeal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
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

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Recipe recipe;
	
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
		
		Log.v(HomeActivity.TAG, "onCreate");

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			long id = getArguments().getLong(ARG_ITEM_ID);
			Log.v(HomeActivity.TAG, Long.toString(id));
			recipe = datasource.getRecipeById(id);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		datasource.close();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_recipe_detail,
				container, false);
		
		if (recipe != null) {
			Log.v(HomeActivity.TAG, "Recipe exists");
			TextView name = (TextView) rootView.findViewById(R.id.fragment_recipe_title);
			name.setText(recipe.getName());
			
			TextView time = (TextView) rootView.findViewById(R.id.fragment_recipe_total_time);
			time.setText(Double.toString(recipe.getTime()));
			
			// TODO This database call causes a crash
			/*
			TextView servings = (TextView) rootView.findViewById(R.id.fragment_recipe_number_of_servings);
			servings.setText(Integer.toString(recipe.getNumServings()));
			*/
			
			Button cook = (Button) rootView.findViewById(R.id.fragment_recipe_button_cook);
			cook.setOnClickListener(clickListener);
			
			Button edit = (Button) rootView.findViewById(R.id.fragment_recipe_button_edit);
			edit.setOnClickListener(clickListener);
			
			Button delete = (Button) rootView.findViewById(R.id.fragment_recipe_button_delete);
			delete.setOnClickListener(clickListener);
			
		} else {
			Log.v(HomeActivity.TAG, "Recipe does not exist.");
		}

		return rootView;
	}
	
	public View.OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Context context = v.getContext();
			Intent intent;

		    switch (v.getId()) {
		        case R.id.fragment_recipe_button_delete:
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("Delete meal?");
					builder.setMessage("Delete " + recipe.getName() + "?");
					builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {   
							datasource.deleteRecipe(recipe);
						}
					});
					builder.setNegativeButton("Cancel", null);
					AlertDialog dialog = builder.create();
					dialog.show();
		    		break;
		        case R.id.fragment_recipe_button_edit:
		        	// TODO Uncomment when this class exists
		        	/*
		    		intent = new Intent(context, EditRecipeActivity.class);
		    		intent.putExtra(EditRecipeActivity.EXTRA_RECIPE, recipe.getId());
		    		startActivity(intent);
		    		 */
		    		break;
		        case R.id.fragment_recipe_button_cook:
		    		intent = new Intent(context, CookActivity.class);
		    		intent.putExtra(HomeActivity.EXTRA_RECIPE, recipe.getId());
		    		startActivity(intent);
		    		break;
		        default:
		            return;
		    }
		}
	};
}
