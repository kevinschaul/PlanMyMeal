package com.csci5115.group2.planmymeal.database;

import java.util.LinkedList;

import com.csci5115.group2.planmymeal.Meal;
import com.csci5115.group2.planmymeal.Tag;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


	  private static final String DATABASE_NAME = "planmymeal.db";
	  private static final int DATABASE_VERSION = 1;
	  private static DataSourceManager datasource;

	  public DatabaseHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  // Method is called during creation of the database
	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    DataSourceManager.onCreate(database);
	  }
	  

	  // Method is called during an upgrade of the database,
	  // e.g. if you increase the database version
	  @Override
	  public void onUpgrade(SQLiteDatabase database, int oldVersion,
	      int newVersion) {
	    DataSourceManager.onUpgrade(database, oldVersion, newVersion);
	  }
}
