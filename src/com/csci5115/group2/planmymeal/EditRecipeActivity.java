package com.csci5115.group2.planmymeal;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.csci5115.group2.planmymeal.database.DataSourceManager;

public class EditRecipeActivity extends Activity
{

	public Recipe recipe;
	public EditRecipeActivity view;
	public LinearLayout tagContainer;
	private DataSourceManager datasource;
	private Typeface fontAwesome;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_recipe);
		
		view = this;
		
		// Database Creation
		datasource = new DataSourceManager(this);
		datasource.open();
		
		this.fontAwesome = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf" );
		
		Intent intent = getIntent();
		long recipeId = intent.getLongExtra(HomeActivity.EXTRA_MEAL, 0);
		
		recipe = datasource.getRecipeById(recipeId);
		
		EditText recipeNameTextView = (EditText) findViewById(R.id.edit_recipe_recipeName);
		recipeNameTextView.setText(recipe.getName());
		
		
		LinearLayout tags_wrapper = (LinearLayout)findViewById(R.id.edit_recipe_tag_container);
		List<Tag> tags = datasource.getRecipeTags(recipe.getId());
		for (final Tag tag : tags) {
			final View tagView = getLayoutInflater().inflate(R.layout.tag, null);
			
			TextView tagName = (TextView) tagView.findViewById(R.id.tag_name);
			tagName.setText(tag.getName());
			
			Button tagDelete = (Button) tagView.findViewById(R.id.tag_button_delete);
			tagDelete.setTypeface(fontAwesome);
			
			tagDelete.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					tagView.setVisibility(View.GONE);
					datasource.deleteRecipeTag(tag, recipe.getId());
				}
			});
			
			tags_wrapper.addView(tagView);
		}
		
		Button addTagButton = (Button) findViewById(R.id.addTagButton);
		addTagButton.setOnClickListener(new View.OnClickListener() {
			

			@Override
			public void onClick(View v)
			{
				EditText newTagText = (EditText) findViewById(R.id.edit_recipe_newTagName);
				String tagText = newTagText.getText().toString();
				//Add tag to recipe
				recipe.tags.add(new Tag(tagText));
				
				final RelativeLayout newTagLayout = new RelativeLayout(view);
				
				// Defining the RelativeLayout layout parameters.
		        // In this case I want to fill its parent
		        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
		                RelativeLayout.LayoutParams.MATCH_PARENT,
		                RelativeLayout.LayoutParams.WRAP_CONTENT);
				
		        Button newTag = new Button(view);
				newTag.setText(tagText);
				newTag.append("              ");
				
				
				Button deleteTag = new Button(view);
				deleteTag.setText("X");
				
				// Defining the layout parameters of the tag Buttons
		        RelativeLayout.LayoutParams tagButtonLayout = new RelativeLayout.LayoutParams(
		                RelativeLayout.LayoutParams.MATCH_PARENT,
		                RelativeLayout.LayoutParams.WRAP_CONTENT);

		        tagButtonLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

				// Defining the layout parameters of the Delete tag Buttons
		        RelativeLayout.LayoutParams deleteButtonLayout = new RelativeLayout.LayoutParams(
		                RelativeLayout.LayoutParams.WRAP_CONTENT,
		                RelativeLayout.LayoutParams.WRAP_CONTENT);
		        deleteButtonLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		        //deleteButtonLayout.addRule(RelativeLayout.CENTER_VERTICAL);
				
				newTagLayout.addView(newTag, tagButtonLayout);
				newTagLayout.addView(deleteTag, deleteButtonLayout);
				tagContainer.addView(newTagLayout);
				deleteTag.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						newTagLayout.setVisibility(View.GONE);
					}
				}
				);
			}
		}
		);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.community_cookbook, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	        case R.id.action_settings:
	    		Intent intent = new Intent(this, SettingsActivity.class);
	    		startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
