package com.csci5115.group2.planmymeal.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.csci5115.group2.planmymeal.Ingredient;
import com.csci5115.group2.planmymeal.Meal;
import com.csci5115.group2.planmymeal.Recipe;
import com.csci5115.group2.planmymeal.RecipeStep;
import com.csci5115.group2.planmymeal.Tag;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataSourceManager
{
	// Database fields
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;

	// MEAL DATABASE
	public static final String TABLE_MEAL = "meal";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_TIME = "time";
	public static final String COLUMN_DESCRIPTION = "description";
	private static String[] allMealColumns =
	{ COLUMN_ID, COLUMN_NAME, COLUMN_TIME, COLUMN_DESCRIPTION };
	private static final String MEAL_DATABASE_CREATE = "create table "
			+ TABLE_MEAL + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_NAME
			+ " text not null, " + COLUMN_TIME + " real not null,"
			+ COLUMN_DESCRIPTION + " text" + ");";

	// TAG DATABASE
	public static final String TABLE_TAG = "tag";
	private static String[] allTagColumns =
	{ COLUMN_ID, COLUMN_NAME };
	// Database creation
	private static final String TAG_DATABASE_CREATE = "create table "
			+ TABLE_TAG + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_NAME
			+ " text not null" + ");";

	// MEAL TAG REL DATABASE
	public static final String TABLE_MEAL_TAG_REL = "mealTagRel";
	public static final String COLUMN_MEAL_ID = "mealId";
	public static final String COLUMN_TAG_ID = "tagId";
	private static String[] allMealTagColumns =
	{ COLUMN_ID, COLUMN_MEAL_ID, COLUMN_TAG_ID };
	// Database creation
	private static final String MEAL_TAG_DATABASE_CREATE = "create table "
			+ TABLE_MEAL_TAG_REL + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_MEAL_ID
			+ " integer not null, " + COLUMN_TAG_ID + " integer not null"
			+ ");";

	// RECIPE DATABASE
	public static final String TABLE_RECIPE = "recipe";
	public static final String COLUMN_RECIPE_NUM_SERVINGS = "numServings";
	private static String[] allRecipeColumns =
	{ COLUMN_ID, COLUMN_NAME, COLUMN_TIME, COLUMN_DESCRIPTION };
	// Database creation
	private static final String RECIPE_DATABASE_CREATE = "create table "
			+ TABLE_RECIPE + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_NAME
			+ " text not null, " + COLUMN_TIME + " real not null,"
			+ COLUMN_DESCRIPTION + " text," + COLUMN_RECIPE_NUM_SERVINGS
			+ " integer not null" + ");";

	// MEAL RECIPE REL DATABASE
	public static final String TABLE_MEAL_RECIPE_REL = "mealRecipeRel";
	public static final String COLUMN_RECIPE_ID = "recipeId";
	private static String[] allMealRecipeColumns =
	{ COLUMN_ID, COLUMN_MEAL_ID, COLUMN_RECIPE_ID };
	// Database creation
	private static final String MEAL_RECIPE_DATABASE_CREATE = "create table "
			+ TABLE_MEAL_RECIPE_REL + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_MEAL_ID
			+ " integer not null, " + COLUMN_RECIPE_ID + " integer not null"
			+ ");";
	// INGREDIENT DATABASE
	public static final String TABLE_INGREDIENT = "ingredient";
	public static final String COLUMN_AMOUNT = "ingredientAmount";
	public static final String COLUMN_UNIT = "ingredientUnit";
	private static String[] allIngredientColumns =
	{ COLUMN_ID, COLUMN_NAME, COLUMN_AMOUNT, COLUMN_UNIT };
	// Database creation
	private static final String INGREDIENT_DATABASE_CREATE = "create table "
			+ TABLE_INGREDIENT + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_NAME
			+ " text not null, " + COLUMN_AMOUNT + " real not null,"
			+ COLUMN_UNIT + " text" + ");";

	// RECIPE INGREDIENT REL DATABASE
	public static final String TABLE_RECIPE_INGREDIENT_REL = "recipeIngredientRel";
	public static final String COLUMN_INGREDIENT_ID = "ingredientId";
	private static String[] allRecipeIngredientColumns =
	{ COLUMN_ID, COLUMN_RECIPE_ID, COLUMN_INGREDIENT_ID };
	// Database creation
	private static final String RECIPE_INGREDIENT_DATABASE_CREATE = "create table "
			+ TABLE_RECIPE_INGREDIENT_REL
			+ "("
			+ COLUMN_ID
			+ " integer primary key autoincrement, "
			+ COLUMN_RECIPE_ID
			+ " integer not null, "
			+ COLUMN_INGREDIENT_ID
			+ " integer not null" + ");";

	// RECIPE TAG REL DATABASE
	public static final String TABLE_RECIPE_TAG_REL = "recipeTagRel";
	private static String[] allRecipeTagColumns =
	{ COLUMN_ID, COLUMN_RECIPE_ID, COLUMN_TAG_ID };
	// Database creation
	private static final String RECIPE_TAG_DATABASE_CREATE = "create table "
			+ TABLE_RECIPE_TAG_REL + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_RECIPE_ID
			+ " integer not null, " + COLUMN_INGREDIENT_ID
			+ " integer not null" + ");";
	// RECIPE STEP DATABASE
	public static final String TABLE_RECIPE_STEP = "recipeStep";
		public static final String COLUMN_INSTRUCTIONS = "instructions";
		public static final String COLUMN_ACTIVE = "isActiveStep";
		public static final String COLUMN_APPLIANCES = "appliancesUsed";
		private static String[] allRecipeStepColumns =
		{ COLUMN_ID, COLUMN_INSTRUCTIONS, COLUMN_TIME, COLUMN_ACTIVE, COLUMN_APPLIANCES };
		// Database creation
		private static final String RECIPE_STEP_DATABASE_CREATE = "create table "
				+ TABLE_RECIPE_STEP + "(" + COLUMN_ID
				+ " integer primary key autoincrement, " + COLUMN_INSTRUCTIONS
				+ " text not null, " + COLUMN_ACTIVE + " integer not null,"
				+ COLUMN_APPLIANCES + " text" + ");";
		
		// RECIPE STEP REL DATABASE
		public static final String TABLE_RECIPE_STEP_REL = "recipeStepRel";
		public static final String COLUMN_RECIPE_STEP_ID = "recipeStepId";
		private static String[] allRecipeStepRelColumns =
		{ COLUMN_ID, COLUMN_RECIPE_ID, COLUMN_RECIPE_STEP_ID };
		// Database creation
		private static final String RECIPE_STEP_REL_DATABASE_CREATE = "create table "
				+ TABLE_RECIPE_STEP_REL + "(" + COLUMN_ID
				+ " integer primary key autoincrement, " + COLUMN_RECIPE_ID
				+ " integer not null, " + COLUMN_RECIPE_STEP_ID
				+ " integer not null" + ");";

	public DataSourceManager(Context context)
	{
		dbHelper = new DatabaseHelper(context);
	}

	public void open() throws SQLException
	{
		database = dbHelper.getWritableDatabase();
	}

	public void close()
	{
		dbHelper.close();
	}

	// MEAL DATA ACCESS METHODS
	public Meal createMeal(String name, double time, String description)
	{
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, name);
		values.put(COLUMN_TIME, time);
		values.put(COLUMN_DESCRIPTION, description);
		long insertId = database.insert(TABLE_MEAL, null, values);
		Cursor cursor = database.query(TABLE_MEAL, allMealColumns, COLUMN_ID
				+ " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Meal newMeal = cursorToMeal(cursor);
		cursor.close();
		return newMeal;
	}

	public void deleteMeal(Meal meal)
	{
		long id = meal.getId();
		System.out.println("Meal deleted with id: " + id);
		database.delete(TABLE_MEAL, COLUMN_ID + " = " + id, null);
	}

	public List<Meal> getAllMeals()
	{
		List<Meal> meals = new ArrayList<Meal>();

		Cursor cursor = database.query(TABLE_MEAL, allMealColumns, null, null,
				null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
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

		Cursor cursor = database.query(TABLE_MEAL, allMealColumns, COLUMN_ID
				+ " = " + mealId, null, null, null, null);

		cursor.moveToFirst();
		meal = cursorToMeal(cursor);

		// make sure to close the cursor
		cursor.close();
		return meal;
	}

	private Meal cursorToMeal(Cursor cursor)
	{
		Meal meal = new Meal();
		meal.setId(cursor.getLong(0));
		meal.setName(cursor.getString(1));
		meal.setTime(cursor.getLong(2));
		meal.setDescription(cursor.getString(3));
		meal.setType("Meal");
		meal.setRecipes(new LinkedList<Recipe>());
		meal.setTags(new LinkedList<Tag>());
		return meal;
	}

	// TAG DATA ACCESS METHODS
	// Data Access Methods
	public Tag createTag(String name)
	{
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, name);
		long insertId = database.insert(TABLE_TAG, null, values);
		Cursor cursor = database.query(TABLE_TAG, allTagColumns, COLUMN_ID
				+ " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Tag newTag = cursorToTag(cursor);
		cursor.close();
		return newTag;
	}

	public void deleteTag(Tag tag)
	{
		long id = tag.getId();
		System.out.println("Tag deleted with id: " + id);
		database.delete(TABLE_TAG, COLUMN_ID + " = " + id, null);
	}

	public List<Tag> getAllTags()
	{
		List<Tag> tags = new ArrayList<Tag>();

		Cursor cursor = database.query(TABLE_TAG, allTagColumns, null, null,
				null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
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
		for (long tagId : tagIds)
		{
			Cursor cursor = database.query(TABLE_TAG, allTagColumns, COLUMN_ID
					+ " = " + tagId, null, null, null, null);

			cursor.moveToFirst();
			tags.add(cursorToTag(cursor));

			// make sure to close the cursor
			cursor.close();
		}

		return tags;
	}

	private Tag cursorToTag(Cursor cursor)
	{
		Tag tag = new Tag();
		tag.setId(cursor.getLong(0));
		tag.setName(cursor.getString(1));
		return tag;
	}

	// MEALTAG METHODS
	// Data Access Methods
	public List<Long> getTagIdsForMeal(long mealId)
	{
		List<Long> mealTags = new ArrayList<Long>();

		Cursor cursor = database.query(TABLE_MEAL_TAG_REL, allMealTagColumns,
				COLUMN_MEAL_ID + " = " + mealId, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			long tagId = cursor.getLong(2);
			mealTags.add(tagId);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return mealTags;
	}

	// RECIPE DATA ACCESS METHODS
	public Recipe createRecipe(String name, double time, String description,
			Integer numServings)
	{
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, name);
		values.put(COLUMN_TIME, time);
		values.put(COLUMN_DESCRIPTION, description);
		values.put(COLUMN_RECIPE_NUM_SERVINGS, numServings);
		long insertId = database.insert(TABLE_RECIPE, null, values);
		Cursor cursor = database.query(TABLE_RECIPE, allRecipeColumns,
				COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Recipe newRecipe = cursorToRecipe(cursor);
		cursor.close();
		return newRecipe;
	}

	public void deleteRecipe(Recipe recipe)
	{
		long id = recipe.getId();
		System.out.println("Recipe deleted with id: " + id);
		database.delete(TABLE_RECIPE, COLUMN_ID + " = " + id, null);
	}

	public List<Recipe> getAllRecipes()
	{
		List<Recipe> recipes = new ArrayList<Recipe>();

		Cursor cursor = database.query(TABLE_RECIPE, allRecipeColumns, null,
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			Recipe recipe = cursorToRecipe(cursor);
			recipes.add(recipe);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return recipes;
	}

	public List<Recipe> getMealRecipes(long mealId)
	{
		List<Long> recipeIds = getRecipeIdsForMeal(mealId);

		List<Recipe> recipes = new LinkedList<Recipe>();
		for (long recipeId : recipeIds)
		{
			Cursor cursor = database.query(TABLE_RECIPE, allRecipeColumns,
					COLUMN_ID + " = " + recipeId, null, null, null, null);

			cursor.moveToFirst();
			recipes.add(cursorToRecipe(cursor));

			// make sure to close the cursor
			cursor.close();
		}

		return recipes;
	}

	public Recipe getRecipeById(long recipeId)
	{
		Recipe recipe = new Recipe();

		Cursor cursor = database.query(TABLE_RECIPE, allRecipeColumns,
				COLUMN_ID + " = " + recipeId, null, null, null, null);

		cursor.moveToFirst();
		recipe = cursorToRecipe(cursor);

		// make sure to close the cursor
		cursor.close();
		return recipe;
	}

	private Recipe cursorToRecipe(Cursor cursor)
	{
		Recipe recipe = new Recipe();
		recipe.setId(cursor.getLong(0));
		recipe.setName(cursor.getString(1));
		recipe.setTime(cursor.getLong(2));
		recipe.setDescription(cursor.getString(3));
		recipe.setType("Recipe");
		recipe.setSteps(new LinkedList<RecipeStep>());
		recipe.setIngredients(new LinkedList<Ingredient>());
		recipe.setTags(new LinkedList<Tag>());
		return recipe;
	}

	// MEALRECIPE METHODS
	// Data Access Methods
	public List<Long> getRecipeIdsForMeal(long mealId)
	{
		List<Long> mealRecipes = new ArrayList<Long>();

		Cursor cursor = database.query(TABLE_MEAL_RECIPE_REL,
				allMealRecipeColumns, COLUMN_MEAL_ID + " = " + mealId, null,
				null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			long tagId = cursor.getLong(2);
			mealRecipes.add(tagId);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return mealRecipes;
	}

	// INGREDIENT DATA ACCESS METHODS
	public Ingredient createIngredient(String name, double amount, String unit)
	{
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, name);
		values.put(COLUMN_AMOUNT, amount);
		values.put(COLUMN_UNIT, unit);
		long insertId = database.insert(TABLE_INGREDIENT, null, values);
		Cursor cursor = database.query(TABLE_INGREDIENT, allIngredientColumns,
				COLUMN_ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		Ingredient newIngredient = cursorToIngredient(cursor);
		cursor.close();
		return newIngredient;
	}

	public void deleteIngredient(Ingredient ingredient)
	{
		long id = ingredient.getId();
		System.out.println("Ingredient deleted with id: " + id);
		database.delete(TABLE_INGREDIENT, COLUMN_ID + " = " + id, null);
	}

	public List<Ingredient> getRecipeIngredients(long recipeId)
	{
		List<Long> ingredientIds = getIngredientIdsForRecipe(recipeId);

		List<Ingredient> ingredients = new LinkedList<Ingredient>();
		for (long ingredientId : ingredientIds)
		{
			Cursor cursor = database.query(TABLE_INGREDIENT,
					allIngredientColumns, COLUMN_ID + " = " + ingredientId,
					null, null, null, null);

			cursor.moveToFirst();
			ingredients.add(cursorToIngredient(cursor));

			// make sure to close the cursor
			cursor.close();
		}

		return ingredients;
	}

	private List<Long> getIngredientIdsForRecipe(long recipeId)
	{
		List<Long> recipeIngredients = new ArrayList<Long>();

		Cursor cursor = database.query(TABLE_RECIPE_INGREDIENT_REL,
				allRecipeIngredientColumns,
				COLUMN_RECIPE_ID + " = " + recipeId, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			long ingredientId = cursor.getLong(2);
			recipeIngredients.add(ingredientId);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return recipeIngredients;
	}

	public List<Tag> getRecipeTags(long recipeId)
	{
		List<Long> tagIds = getTagIdsForRecipe(recipeId);

		List<Tag> tags = new LinkedList<Tag>();
		for (long tagId : tagIds)
		{
			Cursor cursor = database.query(TABLE_TAG, allTagColumns, COLUMN_ID
					+ " = " + tagId, null, null, null, null);

			cursor.moveToFirst();
			tags.add(cursorToTag(cursor));

			// make sure to close the cursor
			cursor.close();
		}

		return tags;
	}

	private List<Long> getTagIdsForRecipe(long recipeId)
	{
		List<Long> recipeTags = new ArrayList<Long>();

		Cursor cursor = database.query(TABLE_RECIPE_TAG_REL,
				allRecipeTagColumns, COLUMN_RECIPE_ID + " = " + recipeId, null,
				null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			long tagId = cursor.getLong(2);
			recipeTags.add(tagId);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return recipeTags;
	}

	private Ingredient cursorToIngredient(Cursor cursor)
	{
		Ingredient ingredient = new Ingredient();
		ingredient.setId(cursor.getLong(0));
		ingredient.setName(cursor.getString(1));
		ingredient.setAmount(cursor.getLong(2));
		ingredient.setUnit(cursor.getString(3));
		return ingredient;
	}
	
	// RECIPE STEP DATA ACCESS METHODS
		public RecipeStep createRecipeStep(String instructions, long time, Boolean isActive, List<String> appliancesUsed)
		{
			ContentValues values = new ContentValues();
			values.put(COLUMN_INSTRUCTIONS, instructions);
			values.put(COLUMN_TIME, time);
			values.put(COLUMN_ACTIVE, isActive);
			values.put(COLUMN_APPLIANCES, createApplianceString(appliancesUsed));
			long insertId = database.insert(TABLE_RECIPE_STEP, null, values);
			Cursor cursor = database.query(TABLE_RECIPE_STEP, allRecipeStepColumns,
					COLUMN_ID + " = " + insertId, null, null, null, null);
			cursor.moveToFirst();
			RecipeStep newStep = cursorToRecipeStep(cursor);
			cursor.close();
			return newStep;
		}

		public void deleteRecipeStep(RecipeStep step)
		{
			long id = step.getId();
			System.out.println("Recipe Step deleted with id: " + id);
			database.delete(TABLE_RECIPE_STEP, COLUMN_ID + " = " + id, null);
		}

		public List<RecipeStep> getRecipeSteps(long recipeId)
		{
			List<Long> stepIds = getStepIdsForRecipe(recipeId);

			List<RecipeStep> steps = new LinkedList<RecipeStep>();
			for (long stepId : stepIds)
			{
				Cursor cursor = database.query(TABLE_RECIPE_STEP,
						allRecipeStepColumns, COLUMN_ID + " = " + stepId,
						null, null, null, null);

				cursor.moveToFirst();
				steps.add(cursorToRecipeStep(cursor));

				// make sure to close the cursor
				cursor.close();
			}

			return steps;
		}

		private List<Long> getStepIdsForRecipe(long recipeId)
		{
			List<Long> recipeSteps = new ArrayList<Long>();

			Cursor cursor = database.query(TABLE_RECIPE_STEP_REL,
					allRecipeStepRelColumns,
					COLUMN_RECIPE_ID + " = " + recipeId, null, null, null, null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				long stepId = cursor.getLong(2);
				recipeSteps.add(stepId);
				cursor.moveToNext();
			}
			// make sure to close the cursor
			cursor.close();
			return recipeSteps;
		}


		private RecipeStep cursorToRecipeStep(Cursor cursor)
		{
			RecipeStep step = new RecipeStep();
			step.setId(cursor.getLong(0));
			step.setInstructions(cursor.getString(1));
			step.setTime(cursor.getLong(2));
			step.setActiveStep(cursor.getInt(3) == 1);
			step.setAppliancesUsed(getAppliances(cursor.getString(4)));
			return step;
		}

	private List<String> getAppliances(String appliancesUsed)
		{
			if(appliancesUsed == null)
			{
				return new LinkedList<String>();
			}
			else
			{
				return Arrays.asList(appliancesUsed.split(";"));
			}
		}
	
	private String createApplianceString(List<String> appliances)
	{
		String applianceString = "";
		for(int i = 1; i < appliances.size(); i++)
		{
			applianceString += appliances.get(i) + ";";
		}
		applianceString += appliances.get(appliances.size() - 1);
		
		return applianceString;
	}

	// GENERIC DATABASE METHODS
	public static void onCreate(SQLiteDatabase database)
	{
		database.execSQL(MEAL_DATABASE_CREATE);
		database.execSQL(TAG_DATABASE_CREATE);
		database.execSQL(MEAL_TAG_DATABASE_CREATE);
		database.execSQL(RECIPE_DATABASE_CREATE);
		database.execSQL(MEAL_RECIPE_DATABASE_CREATE);
		database.execSQL(INGREDIENT_DATABASE_CREATE);
		database.execSQL(RECIPE_INGREDIENT_DATABASE_CREATE);
		database.execSQL(RECIPE_TAG_DATABASE_CREATE);
		database.execSQL(RECIPE_STEP_DATABASE_CREATE);
		database.execSQL(RECIPE_STEP_REL_DATABASE_CREATE);
		

		// Thanksgiving Meal
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME, "Thanksgiving");
		values.put(COLUMN_TIME, 5.5);
		values.put(COLUMN_DESCRIPTION, "Thanksgiving description");
		long thanksgivingId = database.insert(TABLE_MEAL, null, values);

		// Thanksgiving Tags
		values = new ContentValues();
		values.put(COLUMN_NAME, "Holiday");
		long holidayTagId = database.insert(TABLE_TAG, null, values);

		values = new ContentValues();
		values.put(COLUMN_NAME, "Turkey Meal");
		long turkeyMealTagId = database.insert(TABLE_TAG, null, values);

		values = new ContentValues();
		values.put(COLUMN_MEAL_ID, thanksgivingId);
		values.put(COLUMN_TAG_ID, holidayTagId);
		database.insert(TABLE_MEAL_TAG_REL, null, values);

		values = new ContentValues();
		values.put(COLUMN_MEAL_ID, thanksgivingId);
		values.put(COLUMN_TAG_ID, turkeyMealTagId);
		database.insert(TABLE_MEAL_TAG_REL, null, values);

		// Thanksgiving Recipes
		values = new ContentValues();
		values.put(COLUMN_NAME, "Apple Pie");
		values.put(COLUMN_TIME, 2.2);
		values.put(COLUMN_DESCRIPTION, "Grandma's Original Recipe");
		values.put(COLUMN_RECIPE_NUM_SERVINGS, 8);
		long applePieId = database.insert(TABLE_RECIPE, null, values);

		values = new ContentValues();
		values.put(COLUMN_NAME, "Mashed Potatoes");
		values.put(COLUMN_TIME, 3);
		values.put(COLUMN_DESCRIPTION, "Sah good!");
		values.put(COLUMN_RECIPE_NUM_SERVINGS, 6);
		long mashedPotatoesId = database.insert(TABLE_RECIPE, null, values);

		values = new ContentValues();
		values.put(COLUMN_MEAL_ID, thanksgivingId);
		values.put(COLUMN_RECIPE_ID, applePieId);
		database.insert(TABLE_MEAL_RECIPE_REL, null, values);

		values = new ContentValues();
		values.put(COLUMN_MEAL_ID, thanksgivingId);
		values.put(COLUMN_RECIPE_ID, mashedPotatoesId);
		database.insert(TABLE_MEAL_RECIPE_REL, null, values);

		// Mashed Potato Ingredients
		values = new ContentValues();
		values.put(COLUMN_NAME, "Potato");
		values.put(COLUMN_AMOUNT, 3);
		values.put(COLUMN_UNIT, "Pounds");
		long potatoId = database.insert(TABLE_INGREDIENT, null, values);

		values = new ContentValues();
		values.put(COLUMN_NAME, "Butter");
		values.put(COLUMN_AMOUNT, 1);
		values.put(COLUMN_UNIT, "Cup");
		long butterId = database.insert(TABLE_INGREDIENT, null, values);

		values = new ContentValues();
		values.put(COLUMN_NAME, "Cream");
		values.put(COLUMN_AMOUNT, 1.5);
		values.put(COLUMN_UNIT, "Cups");
		long creamId = database.insert(TABLE_INGREDIENT, null, values);

		values = new ContentValues();
		values.put(COLUMN_NAME, "Chives");
		values.put(COLUMN_AMOUNT, 4);
		values.put(COLUMN_UNIT, "Tablespoons");
		long chiveId = database.insert(TABLE_INGREDIENT, null, values);

		values = new ContentValues();
		values.put(COLUMN_RECIPE_ID, mashedPotatoesId);
		values.put(COLUMN_INGREDIENT_ID, potatoId);
		database.insert(TABLE_RECIPE_INGREDIENT_REL, null, values);

		values = new ContentValues();
		values.put(COLUMN_RECIPE_ID, mashedPotatoesId);
		values.put(COLUMN_INGREDIENT_ID, butterId);
		database.insert(TABLE_RECIPE_INGREDIENT_REL, null, values);

		values = new ContentValues();
		values.put(COLUMN_RECIPE_ID, mashedPotatoesId);
		values.put(COLUMN_INGREDIENT_ID, creamId);
		database.insert(TABLE_RECIPE_INGREDIENT_REL, null, values);

		values = new ContentValues();
		values.put(COLUMN_RECIPE_ID, mashedPotatoesId);
		values.put(COLUMN_INGREDIENT_ID, chiveId);
		database.insert(TABLE_RECIPE_INGREDIENT_REL, null, values);

		// Mashed Potato Ingredients
		values = new ContentValues();
		values.put(COLUMN_NAME, "Apples");
		values.put(COLUMN_AMOUNT, 2);
		values.put(COLUMN_UNIT, "Pounds");
		long appleId = database.insert(TABLE_INGREDIENT, null, values);

		values = new ContentValues();
		values.put(COLUMN_NAME, "Brown Sugar");
		values.put(COLUMN_AMOUNT, .3);
		values.put(COLUMN_UNIT, "Cup");
		long brownSugarId = database.insert(TABLE_INGREDIENT, null, values);

		values = new ContentValues();
		values.put(COLUMN_NAME, "Sugar");
		values.put(COLUMN_AMOUNT, .5);
		values.put(COLUMN_UNIT, "Cups");
		long sugarId = database.insert(TABLE_INGREDIENT, null, values);

		values = new ContentValues();
		values.put(COLUMN_NAME, "Pie Crust");
		values.put(COLUMN_AMOUNT, 1);
		values.put(COLUMN_UNIT, "9 inch");
		long pieCrustId = database.insert(TABLE_INGREDIENT, null, values);

		values = new ContentValues();
		values.put(COLUMN_RECIPE_ID, applePieId);
		values.put(COLUMN_INGREDIENT_ID, brownSugarId);
		database.insert(TABLE_RECIPE_INGREDIENT_REL, null, values);

		values = new ContentValues();
		values.put(COLUMN_RECIPE_ID, applePieId);
		values.put(COLUMN_INGREDIENT_ID, appleId);
		database.insert(TABLE_RECIPE_INGREDIENT_REL, null, values);

		values = new ContentValues();
		values.put(COLUMN_RECIPE_ID, applePieId);
		values.put(COLUMN_INGREDIENT_ID, sugarId);
		database.insert(TABLE_RECIPE_INGREDIENT_REL, null, values);

		values = new ContentValues();
		values.put(COLUMN_RECIPE_ID, applePieId);
		values.put(COLUMN_INGREDIENT_ID, pieCrustId);
		database.insert(TABLE_RECIPE_INGREDIENT_REL, null, values);

		// Meal 2
		values = new ContentValues();
		values.put(COLUMN_NAME, "Sam Initial Meal 2");
		values.put(COLUMN_TIME, 6.8);
		values.put(COLUMN_DESCRIPTION, "Initial Description 2");
		long meal2Id = database.insert(TABLE_MEAL, null, values);

	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion)
	{
		Log.w(DataSourceManager.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_MEAL);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_MEAL_TAG_REL);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_MEAL_RECIPE_REL);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENT);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENT_REL);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_TAG_REL);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_STEP);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_STEP_REL);
		onCreate(database);
	}

}
