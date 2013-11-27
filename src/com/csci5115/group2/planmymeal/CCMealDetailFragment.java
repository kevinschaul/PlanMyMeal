package com.csci5115.group2.planmymeal;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.csci5115.group2.planmymeal.database.DataSourceManager;

/**
 * A fragment representing a single Meal detail screen. This fragment is either
 * contained in a {@link MealListActivity} in two-pane mode (on tablets) or a
 * {@link MealDetailActivity} on handsets.
 */
public class CCMealDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Meal meal;
	
	// Databases
	private DataSourceManager datasource;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public CCMealDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Context context = this.getActivity().getApplicationContext();
		datasource = new DataSourceManager(context);
		datasource.open();

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			long id = getArguments().getLong(ARG_ITEM_ID);
			meal = datasource.getMealById(id);
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
		View rootView = inflater.inflate(R.layout.cc_fragment_meal_detail,
				container, false);
		
		Context context = rootView.getContext();

		if (meal != null) {
			((TextView) rootView.findViewById(R.id.cc_fragment_meal_title))
					.setText(meal.getName());
			
			ListView recipeListView = (ListView) rootView.findViewById(R.id.cc_fragment_meal_recipe_list);
			List<Recipe> recipes = datasource.getMealRecipes(meal.getId());

			RecipeDetailArrayAdapter adapter = new RecipeDetailArrayAdapter(context, recipes);
			recipeListView.setAdapter(adapter);
			recipeListView.setOnItemClickListener(itemClickListener);
			
			Button cook = (Button) rootView.findViewById(R.id.cc_fragment_meal_button_cook);
			cook.setOnClickListener(clickListener);
			
			
			
		}

		return rootView;
	}
	
	public AdapterView.OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			// Item selected must be a recipe, so split the view evenly between all columns.
			CommunityCookbookActivity.showColumns(3);
			
			Recipe recipe = (Recipe) parent.getItemAtPosition(position);
			Bundle arguments = new Bundle();
			arguments.putLong(CCRecipeDetailFragment.ARG_ITEM_ID, recipe.getId());   //////new RecipeDetailFragment
			CCRecipeDetailFragment fragment = new CCRecipeDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
					.replace(R.id.CC_col2_container, fragment).commit();
		}
		
	};
	
	public View.OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Context context = v.getContext();
			Intent intent;
		    switch (v.getId()) {
		        case R.id.cc_fragment_meal_button_cook:
		    		intent = new Intent(context, CookActivity.class);
		    		intent.putExtra(CommunityCookbookActivity.EXTRA_MEAL, meal.getId());
		    		startActivity(intent);
		    		break;
		        default:
		            return;
		    }
		}
	};
}