package com.csci5115.group2.planmymeal.database;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.csci5115.group2.planmymeal.Meal;
import com.csci5115.group2.planmymeal.MealTag;
import com.csci5115.group2.planmymeal.Recipe;
import com.csci5115.group2.planmymeal.Tag;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataSourceManager
{
	//Database fields
	  private SQLiteDatabase database;
	  private DatabaseHelper dbHelper;
	  
	// MEAL DATABASE 
	  public static final String TABLE_MEAL = "meal";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_NAME = "name";
	  public static final String MEAL_COLUMN_TIME = "time";
	  public static final String MEAL_COLUMN_DESCRIPTION = "description";
	  private String[] allMealColumns = { COLUMN_ID, COLUMN_NAME, MEAL_COLUMN_TIME, MEAL_COLUMN_DESCRIPTION };
	  private static final String MEAL_DATABASE_CREATE = "create table " 
	      + TABLE_MEAL
	      + "(" 
	      + COLUMN_ID + " integer primary key autoincrement, " 
	      + COLUMN_NAME + " text not null, " 
	      + MEAL_COLUMN_TIME + " real not null," 
	      + MEAL_COLUMN_DESCRIPTION
	      + " text" 
	      + ");";
	  
	  //TAG DATABASE
		  public static final String TABLE_TAG = "tag";
		  private String[] allTagColumns = { COLUMN_ID, COLUMN_NAME };
		  
		// MEAL TAG REL DATABASE
		  public static final String TABLE_MEAL_TAG_REL = "mealTagRel";
		  public static final String COLUMN_MEAL_ID = "mealId";
		  public static final String COLUMN_TAG_ID = "tagId";
		  private String[] allMealTagColumns = { COLUMN_ID, COLUMN_MEAL_ID, COLUMN_TAG_ID};

		  // Database creation SQL statement
		  private static final String MEAL_TAG_DATABASE_CREATE = "create table " 
		      + TABLE_MEAL_TAG_REL
		      + "(" 
		      + COLUMN_ID + " integer primary key autoincrement, " 
		      + COLUMN_MEAL_ID + " integer not null, " 
		      + COLUMN_TAG_ID + " integer not null"
		      + ");";

		  // Database creation SQL statement
		  private static final String TAG_DATABASE_CREATE = "create table " 
		      + TABLE_TAG
		      + "(" 
		      + COLUMN_ID + " integer primary key autoincrement, " 
		      + COLUMN_NAME + " text not null" 
		      + ");";
	  
	  public DataSourceManager(Context context)
	  {
		  dbHelper = new DatabaseHelper(context);
	  }
	  
	  public void open() throws SQLException {
		    database = dbHelper.getWritableDatabase();
	  }

	  	public void close() {
	  		dbHelper.close();
	  	}
	  	
	  	// MEAL DATA ACCESS METHODS
	  	public Meal createMeal(String name, double time, String description) {
	  	    ContentValues values = new ContentValues();
	  	    values.put(COLUMN_NAME, name);
	  	  values.put(MEAL_COLUMN_TIME, time);
	  	values.put(MEAL_COLUMN_DESCRIPTION, description);
	  	    long insertId = database.insert(TABLE_MEAL, null,
	  	        values);
	  	    Cursor cursor = database.query(TABLE_MEAL,
	  	    		allMealColumns, COLUMN_ID + " = " + insertId, null,
	  	        null, null, null);
	  	    cursor.moveToFirst();
	  	    Meal newMeal = cursorToMeal(cursor);
	  	    cursor.close();
	  	    return newMeal;
	  	  }

	  	  public void deleteMeal(Meal meal) {
	  	    long id = meal.getId();
	  	    System.out.println("Meal deleted with id: " + id);
	  	    database.delete(TABLE_MEAL, COLUMN_ID
	  	        + " = " + id, null);
	  	  }

	  	  public List<Meal> getAllMeals() {
	  	    List<Meal> meals = new ArrayList<Meal>();

	  	    Cursor cursor = database.query(TABLE_MEAL,
	  	    		allMealColumns, null, null, null, null, null);

	  	    cursor.moveToFirst();
	  	    while (!cursor.isAfterLast()) {
	  	      Meal meal = cursorToMeal(cursor);
	  	      meals.add(meal);
	  	      cursor.moveToNext();
	  	    }
	  	    // make sure to close the cursor
	  	    cursor.close();
	  	    return meals;
	  	  }
	  	  
	  	  public Meal getMealById(long mealId)
	  	  {
	  		Meal meal = new Meal();

	  		Cursor cursor = database.query(TABLE_MEAL,
	  				allMealColumns, COLUMN_ID + " = " + mealId, null,
		  	        null, null, null);

	  	    cursor.moveToFirst();
	  	    meal = cursorToMeal(cursor);
	  	   
	  	    // make sure to close the cursor
	  	    cursor.close();
	  	    return meal;
	  	  }
	  	  private Meal cursorToMeal(Cursor cursor) {
	  	    Meal meal = new Meal();
	  	    meal.setId(cursor.getLong(0));
	  	    meal.setName(cursor.getString(1));
	  	    meal.setTime(cursor.getLong(2));
	  	    meal.setDescription(cursor.getString(3));
	  	    meal.setRecipes(new LinkedList<Recipe>());
	  	    meal.setTags(new LinkedList<Tag>());
	  	    return meal;
	  	  }
	  	  
	  	  // TAG DATA ACCESS METHODS
	  	// Data Access Methods
		  	public Tag createTag(String name) {
		  	    ContentValues values = new ContentValues();
		  	    values.put(COLUMN_NAME, name);
		  	    long insertId = database.insert(TABLE_TAG, null,
		  	        values);
		  	    Cursor cursor = database.query(TABLE_TAG,
		  	        allTagColumns, COLUMN_ID + " = " + insertId, null,
		  	        null, null, null);
		  	    cursor.moveToFirst();
		  	    Tag newTag = cursorToTag(cursor);
		  	    cursor.close();
		  	    return newTag;
		  	  }

		  	  public void deleteTag(Tag tag) {
		  	    long id = tag.getId();
		  	    System.out.println("Tag deleted with id: " + id);
		  	    database.delete(TABLE_TAG, COLUMN_ID
		  	        + " = " + id, null);
		  	  }

		  	  public List<Tag> getAllTags() {
		  	    List<Tag> tags = new ArrayList<Tag>();

		  	    Cursor cursor = database.query(TABLE_TAG,
		  	        allTagColumns, null, null, null, null, null);

		  	    cursor.moveToFirst();
		  	    while (!cursor.isAfterLast()) {
		  	      Tag tag = cursorToTag(cursor);
		  	      tags.add(tag);
		  	      cursor.moveToNext();
		  	    }
		  	    // make sure to close the cursor
		  	    cursor.close();
		  	    return tags;
		  	  }
		  	  
		  	  public List<Tag> getMealTags(long mealId)
		  	  {
		  		List<Long> tagIds = getTagIdsForMeal(mealId);
		  		
		  		List<Tag> tags = new LinkedList<Tag>();
		  		for(long tagId : tagIds)
		  		{
		  			Cursor cursor = database.query(TABLE_TAG,
				  	        allTagColumns, COLUMN_ID + " = " + tagId, null,
				  	        null, null, null);

			  	    cursor.moveToFirst();
			  	    tags.add(cursorToTag(cursor));
			  	    
			  	    // make sure to close the cursor
			  	    cursor.close();
		  		}
		  		
		  		  return tags;
		  	  }

		  	  private Tag cursorToTag(Cursor cursor) {
		  	    Tag tag = new Tag();
		  	    tag.setId(cursor.getLong(0));
		  	    tag.setName(cursor.getString(1));
		  	    return tag;
		  	  }

		  	  //MEALTAG METHODS
		  	// Data Access Methods
			  	public MealTag createMealTag(long mealId, long tagId) {
			  	    ContentValues values = new ContentValues();
			  	    values.put(COLUMN_MEAL_ID, mealId);
			  	  values.put(COLUMN_TAG_ID, tagId);
			  	    long insertId = database.insert(TABLE_MEAL_TAG_REL, null,
			  	        values);
			  	    Cursor cursor = database.query(TABLE_MEAL_TAG_REL,
			  	        allMealTagColumns, COLUMN_ID + " = " + insertId, null,
			  	        null, null, null);
			  	    cursor.moveToFirst();
			  	    MealTag newMealTag = cursorToMealTag(cursor);
			  	    cursor.close();
			  	    return newMealTag;
			  	  }

			  	  public void deleteMealTag(MealTag mealTag) {
			  	    long id = mealTag.getId();
			  	    System.out.println("Meal deleted with id: " + id);
			  	    database.delete(TABLE_MEAL_TAG_REL, COLUMN_ID
			  	        + " = " + id, null);
			  	  }

			  	  public List<MealTag> getAllMealTags() {
			  	    List<MealTag> mealTags = new ArrayList<MealTag>();

			  	    Cursor cursor = database.query(TABLE_MEAL_TAG_REL,
			  	        allMealTagColumns, null, null, null, null, null);

			  	    cursor.moveToFirst();
			  	    while (!cursor.isAfterLast()) {
			  	      MealTag mealTag = cursorToMealTag(cursor);
			  	      mealTags.add(mealTag);
			  	      cursor.moveToNext();
			  	    }
			  	    // make sure to close the cursor
			  	    cursor.close();
			  	    return mealTags;
			  	  }
			  	  
			  	public List<MealTag> getAllMealTagsForMeal(long mealId) {
			  	    List<MealTag> mealTags = new ArrayList<MealTag>();

			  	    Cursor cursor = database.query(TABLE_MEAL_TAG_REL,
				  	        allMealTagColumns, COLUMN_ID + " = " + mealId, null,
				  	        null, null, null);

			  	    cursor.moveToFirst();
			  	    while (!cursor.isAfterLast()) {
			  	      MealTag mealTag = cursorToMealTag(cursor);
			  	      mealTags.add(mealTag);
			  	      cursor.moveToNext();
			  	    }
			  	    // make sure to close the cursor
			  	    cursor.close();
			  	    return mealTags;
			  	  }
			  	
			  	public List<Long> getTagIdsForMeal(long mealId)
			  	{
			  		List<Long> mealTags = new ArrayList<Long>();

			  		Cursor cursor = database.query(TABLE_MEAL_TAG_REL,
				  	        allMealTagColumns, COLUMN_ID + " = " + mealId, null,
				  	        null, null, null);

			  	    cursor.moveToFirst();
			  	    while (!cursor.isAfterLast()) {
			  	      MealTag mealTag = cursorToMealTag(cursor);
			  	      mealTags.add(mealTag.getTagId());
			  	      cursor.moveToNext();
			  	    }
			  	    // make sure to close the cursor
			  	    cursor.close();
			  	    return mealTags;
			  	}

			  	  private MealTag cursorToMealTag(Cursor cursor) {
			  	    MealTag mealTag = new MealTag();
			  	    mealTag.setId(cursor.getLong(0));
			  	    mealTag.setMealId(cursor.getLong(1));
			  	    mealTag.setTagId(cursor.getLong(2));
			  	    return mealTag;
			  	  }
	  	  
	  	  // GENERIC DATABASE METHODS
	  public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(MEAL_DATABASE_CREATE);
	    database.execSQL(TAG_DATABASE_CREATE);
	    database.execSQL(MEAL_TAG_DATABASE_CREATE);
	  }

	  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
	      int newVersion) {
	    Log.w(DataSourceManager.class.getName(), "Upgrading database from version "
	        + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");
	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_MEAL);
	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_MEAL_TAG_REL);
	    onCreate(database);
	  }

}
