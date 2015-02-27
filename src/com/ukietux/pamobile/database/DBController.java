package com.ukietux.pamobile.database;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBController extends SQLiteOpenHelper {
	
	public DBController(Context applicationcontext) {
        super(applicationcontext, "Skripsi.db", null, 1);
        Log.d("Skripsi","membuat database");
    }
	//Creates Table
	@Override
	public void onCreate(SQLiteDatabase database) {
		String query;
		query = "CREATE TABLE DataMHS (Nim TEXT, Nama TEXT, NamaMaKul TEXT, NilaiHuruf TEXT, Semester TEXT, SKS TXT)";
        database.execSQL(query);
        Log.d("Skripsi","membuat table DataMHS");
	}
	@Override
	public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
		String query;
		query = "DROP TABLE IF EXISTS DataMHS";
		database.execSQL(query);
        onCreate(database);
	}
	
	/**
	 * Inserts User into SQLite DB
	 * @param queryValues
	 */
	/**
	 * Inserts User into SQLite DB
	 * @param queryValues
	 */
	public void insertUser(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Nama", queryValues.get("Nama"));
		values.put("Nim", queryValues.get("Nim"));
		values.put("NamaMaKul", queryValues.get("NamaMaKul"));
		values.put("NilaiHuruf", queryValues.get("NilaiHuruf"));
		values.put("Semester", queryValues.get("Semester"));
		values.put("SKS", queryValues.get("SKS"));
		database.insert("DataMHS", null, values);
		database.close();
		Log.d("Skripsi","Insert dataMHS ke SQLite");
	}
	
	/**
	 * Get list of Users from SQLite DB as Array List
	 * @return
	 */
//	public String ambilDataMHS(String Nim) {
//		String i = "Error";
//		try {
//			Cursor c = null;
//			c = mDb.rawQuery("select * from DataMHS where Nim=" + Nim, null);
//			c.moveToFirst();
//			i = c.getString(c.getColumnIndex("soal"));
//			c.close();
//			return i;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return i;
//	}
	
	/* getAlldataMHS
	 * menampilkan data seluruh dataMHS ke listview 
	 *   
	 */
	  public ArrayList<HashMap<String, String>> getdataMHS() {
	    ArrayList<HashMap<String, String>> dataMHS;
	    dataMHS = new ArrayList<HashMap<String, String>>();
	    String selectQuery = "select * from DataMHS";
	      SQLiteDatabase database = this.getWritableDatabase();
	      Cursor cursor = database.rawQuery(selectQuery, null);
	      if (cursor.moveToFirst()) {
	          do {
	            HashMap<String, String> map = new HashMap<String, String>();
	            map.put("Nim", cursor.getString(0));
	            map.put("Nama", cursor.getString(1));
	            map.put("NamaMaKul", cursor.getString(2));
	            map.put("NilaiHuruf", cursor.getString(3));
	            map.put("Semester", cursor.getString(4));
	            map.put("SKS", cursor.getString(5));
	                dataMHS.add(map);
	          } while (cursor.moveToNext());
	      }
	    
	      // return contact list
	      return dataMHS;
	  }

}
