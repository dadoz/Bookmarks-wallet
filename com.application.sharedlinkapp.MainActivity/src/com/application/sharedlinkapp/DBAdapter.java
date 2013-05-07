package com.application.sharedlinkapp;

//import android.os.Bundle;
//import android.app.Activity;
//import android.view.Menu;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

public class DBAdapter 
{
	public static final String KEY_ROWID="_id";
	public static final String KEY_USERNAME="username";
	public static final String KEY_USER_ICON="user_icon";
	public static final String KEY_ONLINE_FRIENDS="online_friends";
	public static final String KEY_CLOSER_FRIENDS="closer_friends";
	public static final String KEY_OTHERS_FRIENDS="others_friends";
	public static final String KEY_BLACK_LIST="black_list";
	public static final String KEY_HIDDEN_POSITION="hidden_position";
	public static final String TAG="DBAdapter";
	
	public static final String DATABASE_NAME="BookmarksWalletDb";
	public static final String USER_LIST_TABLE="LinksListTable_TMP";
	public static final String USER_PROFILE_DATA_TABLE="UserProfileDataTable";
	public static final int DATABASE_VERSION=1;
	
	public static final String DATABASE_CREATE=
			"create table UserListTable(_id integer primary key autoincrement,"
			+"username text not null," 
			+"user_icon text not null,"		
			+"online_friends BOOLEAN DEFAULT 'FALSE',"
			+"closer_friends BOOLEAN DEFAULT 'FALSE',"
			+"others_friends BOOLEAN DEFAULT 'FALSE',"
			+"black_list BOOLEAN DEFAULT 'FALSE',"
			+"hidden_position BOOLEAN DEFAULT 'FALSE');";
	private final Context context;
	
	private DatabaseHelper DBHelper;
	private static SQLiteDatabase db;
	
	public DBAdapter(Context ctx)
	{
		this.context=ctx;
		DBHelper=new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context)
		{
			super(context,DATABASE_NAME,null,DATABASE_VERSION);			
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.w(TAG,"Upgrading database from version" + oldVersion
					+ " to " 
					+ newVersion + ", wich will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS titles");
			onCreate(db);
		}
	}
	
	//--- opens the database ---
	public DBAdapter open() throws SQLException
	{
		db = DBHelper.getWritableDatabase();
		return this;
		
	}
	
	//--- close the database ---
	public void close()
	{
		DBHelper.close();
	}

	//--- insert a title into the database --- row of a db
	public long insertTitle(String username, String userIcon, boolean onlineFriends,boolean closerFriends,boolean othersFriends,boolean blackList,boolean hiddenPosition)
	{
		
		ContentValues initialValues = new ContentValues();
		//initialValues.put(KEY_ROWID, username);
		initialValues.put(KEY_USERNAME, username);
		initialValues.put(KEY_USER_ICON, userIcon);
		initialValues.put(KEY_ONLINE_FRIENDS, onlineFriends);
		initialValues.put(KEY_CLOSER_FRIENDS, closerFriends);
		initialValues.put(KEY_OTHERS_FRIENDS, othersFriends);
		initialValues.put(KEY_BLACK_LIST, blackList);
		initialValues.put(KEY_HIDDEN_POSITION, hiddenPosition);
		
		return db.insert(USER_LIST_TABLE,null, initialValues);

	}
	//delete all rows
	public void deleteAllRows()
	{
		//delete all db table
		db.delete(USER_LIST_TABLE, null, null);
	}
	
	//delete row by rowId
	public void deleteRowById(int dbRowId)
	{
		//delete one row from db - by rowId
		db.delete(USER_LIST_TABLE, KEY_ROWID + "=" + dbRowId, null);
	}


	public void dropDbTable()
	{
		try {
			  db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
			  db.execSQL("VACUUM");
		} 
		catch (Exception e){
			Log.d(TAG, "Failed to do : " + e.getMessage());
		}	
	}   
	//---retrieves all the titles---
    public Cursor getAllTitles() 
    {
        return db.query(USER_LIST_TABLE, new String[] {
        		KEY_ROWID,
        		KEY_USERNAME,
        		KEY_USER_ICON,
        		KEY_ONLINE_FRIENDS, 
        		KEY_CLOSER_FRIENDS, 
        		KEY_OTHERS_FRIENDS, 
        		KEY_BLACK_LIST, 
        		KEY_HIDDEN_POSITION}, 
                null, 
                null, 
                null, 
                null, 
                null);
    }

    //---retrieves a particular title---
    public Cursor getTitle(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, USER_LIST_TABLE, new String[] {
                		KEY_USERNAME,
                		KEY_USER_ICON,
                		KEY_ONLINE_FRIENDS, 
                		KEY_CLOSER_FRIENDS, 
                		KEY_OTHERS_FRIENDS, 
                		KEY_BLACK_LIST, 
                		KEY_HIDDEN_POSITION}, 
                		KEY_ROWID + "=" + rowId, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a title---
    public boolean updateTitle(long rowId, String username, String userIcon, boolean onlineFriends,boolean closerFriends,boolean othersFriends,boolean blackList,boolean hiddenPosition) 
    {
    	
        ContentValues args = new ContentValues();
		args.put(KEY_USERNAME, username);
		args.put(KEY_USER_ICON, userIcon);
		args.put(KEY_ONLINE_FRIENDS, onlineFriends);
		args.put(KEY_CLOSER_FRIENDS, closerFriends);
		args.put(KEY_OTHERS_FRIENDS, othersFriends);
		args.put(KEY_BLACK_LIST, blackList);
		args.put(KEY_HIDDEN_POSITION, hiddenPosition);

        return db.update(USER_LIST_TABLE, args, 
                         KEY_ROWID + "=" + rowId, null) > 0;
    }	
}
