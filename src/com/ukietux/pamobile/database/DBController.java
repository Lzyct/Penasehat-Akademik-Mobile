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
		Log.d("Skripsi", "membuat database");
	}

	// Creates Table
	@Override
	public void onCreate(SQLiteDatabase database) {
		String DataMHS, NamaMaKul;
		DataMHS = "CREATE TABLE DataMHS (Nim TEXT, Nama TEXT, NamaMaKul TEXT, NilaiHuruf TEXT, Semester TEXT, SKS INT)";
		database.execSQL(DataMHS);

		NamaMaKul = "CREATE TABLE MataKuliah (NamaMaKul TEXT, SKSTeori INT, SKSPraktikum INT, PaketSemester TEXT, SifatMaKul TEXT, JKurikulum TXT)";
		database.execSQL(NamaMaKul);

		Log.d("Skripsi", "membuat table DataMHS");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int version_old,
			int current_version) {
		String DataMHS, NamaMaKul;
		DataMHS = "DROP TABLE IF EXISTS DataMHS";
		database.execSQL(DataMHS);
		NamaMaKul = "DROP TABLE IF EXISTS MataKuliah ";
		database.execSQL(NamaMaKul);

		onCreate(database);
	}


	/**
	 * Inserts DataMHS into SQLite DB
	 * 
	 * @param queryValues
	 */
	public void insertDataMHS(HashMap<String, String> queryValues) {
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
		Log.d("Skripsi", "Insert dataMHS ke SQLite");
	}
	
	/**
	 * Inserts User into SQLite DB
	 * 
	 * @param queryValues
	 */
	public void insertMataKuliah(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("NamaMaKul", queryValues.get("NamaMaKul"));
		values.put("SKSTeori", queryValues.get("SKSTeori"));
		values.put("SKSPraktikum", queryValues.get("SKSPraktikum"));
		values.put("PaketSemester", queryValues.get("PaketSemester"));
		values.put("SifatMaKul", queryValues.get("SifatMaKul"));
		values.put("JKurikulum", queryValues.get("JKurikulum"));
		database.insert("MataKuliah", null, values);
		database.close();
		Log.d("Skripsi", "Insert MataKuliah ke SQLite");
	}



	/*
	 * getAlldataMHS menampilkan data seluruh dataMHS ke listview
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
	
	/*
	 * getMataKuliah menampilkan data seluruh dataMHS ke listview
	 */
	public ArrayList<HashMap<String, String>> getMataKuliah() {
		ArrayList<HashMap<String, String>> dataMHS;
		dataMHS = new ArrayList<HashMap<String, String>>();
		String selectQuery = "select * from DataMHS";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("NamaMaKul", cursor.getString(0));
				map.put("SKSTeori", cursor.getString(1));
				map.put("SKSPraktikum", cursor.getString(2));
				map.put("PaketSemester", cursor.getString(3));
				map.put("SifatMaKul", cursor.getString(4));
				map.put("JKurikulum", cursor.getString(5));
				dataMHS.add(map);
			} while (cursor.moveToNext());
		}

		// return contact list
		return dataMHS;
	}

	// delete All
	public void deleteAll() {
		SQLiteDatabase database = this.getWritableDatabase();
		database.delete("DataMHS", null, null);
		database.delete("MataKuliah",null,null);
	}

}
