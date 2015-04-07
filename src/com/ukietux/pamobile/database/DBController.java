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
		super(applicationcontext, "PAMobile.db", null, 1);
		Log.d("Skripsi", "membuat database");
	}

	// Creates Table
	@Override
	public void onCreate(SQLiteDatabase database) {
		String DataMHS, NamaMaKul,PenyetMaKul;
		DataMHS = "CREATE TABLE TRANSKIP (Nim TEXT, Nama TEXT, KodeMaKul TEXT, NamaMaKul TEXT, NilaiHuruf TEXT, Semester TEXT, SKS INT)";
		database.execSQL(DataMHS);

		NamaMaKul = "CREATE TABLE KRS (KodeMaKul TEXT,NamaMaKul TEXT, SKSTeori INT, SKSPraktikum INT, PaketSemester TEXT, SifatMaKul TEXT, JKurikulum TXT)";
		database.execSQL(NamaMaKul);
		
		PenyetMaKul = "CREATE TABLE Penyetaraan (KodeMKBaru TEXT,KodeMKLama TEXT)";
		database.execSQL(PenyetMaKul);

		Log.d("Skripsi", "membuat table DataMHS");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int version_old,
			int current_version) {
		String DataMHS, NamaMaKul,PenyetMaKul;
		DataMHS = "DROP TABLE IF EXISTS TRANSKIP";
		database.execSQL(DataMHS);
		NamaMaKul = "DROP TABLE IF EXISTS KRS ";
		database.execSQL(NamaMaKul);
		PenyetMaKul = "DROP TABLE IF EXISTS Penyetaraan";
		database.execSQL(PenyetMaKul);

		onCreate(database);
	}

	/**
	 * Inserts DataMHS into SQLite DB
	 * 
	 * @param queryValues
	 */
	public void insertTRANSKIP(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Nama", queryValues.get("Nama"));
		values.put("Nim", queryValues.get("Nim"));
		values.put("KodeMaKul", queryValues.get("KodeMaKul"));
		values.put("NamaMaKul", queryValues.get("NamaMaKul"));
		values.put("NilaiHuruf", queryValues.get("NilaiHuruf"));
		values.put("Semester", queryValues.get("Semester"));
		values.put("SKS", queryValues.get("SKS"));
		database.insert("TRANSKIP", null, values);
		database.close();
		Log.d("Skripsi", "Insert TRANSKIP ke SQLite");
	}

	/**
	 * Inserts User into SQLite DB
	 * 
	 * @param queryValues
	 */
	public void insertKRS(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("KodeMaKul", queryValues.get("KodeMaKul"));
		values.put("NamaMaKul", queryValues.get("NamaMaKul"));
		values.put("SKSTeori", queryValues.get("SKSTeori"));
		values.put("SKSPraktikum", queryValues.get("SKSPraktikum"));
		values.put("PaketSemester", queryValues.get("PaketSemester"));
		values.put("SifatMaKul", queryValues.get("SifatMaKul"));
		values.put("JKurikulum", queryValues.get("JKurikulum"));
		database.insert("KRS", null, values);
		database.close();
		Log.d("Skripsi", "Insert KRS ke SQLite");
	}
	
	/**
	 * Inserts User into SQLite DB
	 * 
	 * @param queryValues
	 */
	public void insertPenyetaraan(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("KodeMKBaru", queryValues.get("KodeMKBaru"));
		values.put("KodeMKLama", queryValues.get("KodeMKLama"));
		database.insert("Penyetaraan", null, values);
		database.close();
		Log.d("Skripsi", "Insert Penyetaraan ke SQLite");
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
				map.put("KodeMaKul", cursor.getString(2));
				map.put("NamaMaKul", cursor.getString(3));
				map.put("NilaiHuruf", cursor.getString(4));
				map.put("Semester", cursor.getString(5));
				map.put("SKS", cursor.getString(6));
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
				map.put("KodeMaKul", cursor.getString(0));
				map.put("NamaMaKul", cursor.getString(1));
				map.put("SKSTeori", cursor.getString(2));
				map.put("SKSPraktikum", cursor.getString(3));
				map.put("PaketSemester", cursor.getString(4));
				map.put("SifatMaKul", cursor.getString(5));
				map.put("JKurikulum", cursor.getString(6));
				dataMHS.add(map);
			} while (cursor.moveToNext());
		}

		// return contact list
		return dataMHS;
	}

	// delete All
	public void deleteAll() {
		SQLiteDatabase database = this.getWritableDatabase();
		database.delete("TRANSKIP", null, null);
		database.delete("KRS", null, null);
		database.delete("Penyetaraan", null, null);
	}

}
