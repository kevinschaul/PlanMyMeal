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
public class MealDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";
	private static final String STATE_ACTIVATED_POSITION = "meal_deatil_activated_position";
	private int mActivatedPosition = ListView.INVALID_POSITION;

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Meal meal;
	private Typeface fontAwesome;
	
	// Databases
	private DataSourceManager datasource;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MealDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Context context = this.getActivity().getApplicationContext();
		datasource = new DataSourceManager(context);
		datasource.open();
		
		this.fontAwesome = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf" );

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
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_meal_detail,
				container, false);
		
		Log.v("MealDetailFragment", "onCreateView()");
		
		Context context = rootView.getContext();
		
		ListView recipeListView = (ListView) rootView.findViewById(R.id.fragment_meal_recipe_list);
		recipeListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		recipeListView.setSelector(R.drawable.cookable_selector);
		
		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(recipeListView, savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
		}
		
		recipeListView.setItemChecked(1, true);

		if (meal != null) {
			TextView name = (TextView) rootView.findViewById(R.id.fragment_meal_title);
			name.setText(meal.getName());
			
			TextView time = (TextView) rootView.findViewById(R.id.fragment_meal_time);
			time.setText(meal.getReadableTime());
			
			List<Recipe> recipes = datasource.getMealRecipes(meal.getId());

			RecipeDetailArrayAdapter adapter = new RecipeDetailArrayAdapter(context, recipes);
			recipeListView.setAdapter(adapter);
			recipeListView.setOnItemClickListener(itemClickListener);
			
			Button cook = (Button) rootView.findViewById(R.id.fragment_meal_button_cook);
			cook.setOnClickListener(clickListener);
			
			Button edit = (Button) rootView.findViewById(R.id.fragment_meal_button_edit);
			edit.setOnClickListener(clickListener);
			
			Button delete = (Button) rootView.findViewById(R.id.fragment_meal_button_delete);
			delete.setOnClickListener(clickListener);
			
			LinearLayout tags_wrapper = (LinearLayout) rootView.findViewById(R.id.fragment_meal_tags_wrapper);
			List<Tag> tags = datasource.getMealTags(meal.getId());
			for (Tag tag : tags) {
				View tagView = inflater.inflate(R.layout.tag, null);
				
				TextView tagName = (TextView) tagView.findViewById(R.id.tag_name);
				tagName.setText(tag.getName());
				
				Button tagDelete = (Button) tagView.findViewById(R.id.tag_button_delete);
				tagDelete.setTypeface(fontAwesome);
				
				tags_wrapper.addView(tagView);
			}
		}

		return rootView;
	}
	
	public AdapterView.OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			// Item selected must be a recipe, so split the view evenly between all columns.
			HomeActivity.showColumns(3);
			
			ListView listView = (ListView) parent;
			setActivatedPosition(listView, position);
			
			Recipe recipe = (Recipe) parent.getItemAtPosition(position);
			Bundle arguments = new Bundle();
			arguments.putLong(RecipeDetailFragment.ARG_ITEM_ID, recipe.getId());
			RecipeDetailFragment fragment = new RecipeDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
					.replace(R.id.home_col2_container, fragment).commit();
		}
		
	};
	
	public View.OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Context context = v.getContext();
			Intent intent;
		    switch (v.getId()) {
		        case R.id.fragment_meal_button_delete:
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("Delete meal?");
					builder.setMessage("Delete " + meal.getName() + "?");
					builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {   
							datasource.deleteMeal(meal);
						}
					});
					builder.setNegativeButton("Cancel", null);
					AlertDialog dialog = builder.create();
					dialog.show();
		    		break;
		        case R.id.fragment_meal_button_edit:
		    		intent = new Intent(context, EditMealActivity.class);
		    		intent.putExtra(HomeActivity.EXTRA_MEAL, meal.getId());
		    		startActivity(intent);
		    		break;
		        case R.id.fragment_meal_button_cook:
		    		intent = new Intent(context, CookActivity.class);
		    		intent.putExtra(HomeActivity.EXTRA_MEAL, meal.getId());
		    		startActivity(intent);
		    		break;
		        default:
		            return;
		    }
		}
	};
	
	private void setActivatedPosition(ListView listView, int position) {
		Log.v("HI", "HERE");
		if (position == ListView.INVALID_POSITION) {
			Log.v("HI", "INVALID");
			listView.setItemChecked(mActivatedPosition, false);
		} else {
			Log.v("HI", "VALID");
			listView.setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
}
