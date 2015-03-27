package com.ukietux.pamobile;

import java.util.HashMap;

import com.ukietux.pamobile.Login;
import com.ukietux.pamobile.MainActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

@SuppressLint("CommitPrefEdits")
public class SessionManager {
	// Shared Preferences
	SharedPreferences pref,prefProfil;

	// Editor for Shared preferences
	Editor editor,editorProfil;

	// Context
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// nama sharepreference
	private static final String PREF_NAME = "Sesi";
	private static final String PREF_PROFIL = "Profil";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";
	public static final String KEY_NIM = "Nim";

	// Constructor
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

		prefProfil = _context.getSharedPreferences(PREF_PROFIL, 0);
		editor = pref.edit();
		editorProfil = prefProfil.edit();
		
	}

	/**
	 * Create login session
	 * */
	public void createLoginSession(String nim) {
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_NIM, nim);
		editor.commit();
	}

	/**
	 * Check login method wil check user login status If false it will redirect
	 * user to login page Else won't do anything
	 * */
	public void checkLogin() {
		// Check login status
		if (!this.isLoggedIn()) {
			Intent i = new Intent(_context, MainActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			_context.startActivity(i);
			// ((Activity)_context).finish();
		}

	}

	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();

		user.put(KEY_NIM, pref.getString(KEY_NIM, null));

		return user;
	}

	/**
	 * Clear session details
	 * */
	public void logoutUser() {
		// Clearing all data from Shared Preferences

		editor.clear().commit();
		editorProfil.clear().commit();

//		SharedPreferences.Editor edt = pref.edit();
//		edt.putString("profil_pic", "empty");
//		edt.commit();
//
//		String profil = pref.getString("profil_pic", "empty");
//		Log.d("Cekidot", profil);
		
		Intent i = new Intent(_context, Login.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		_context.startActivity(i);
	}

	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}
}
