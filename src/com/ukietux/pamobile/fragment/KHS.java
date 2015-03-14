package com.ukietux.pamobile.fragment;

import com.ukietux.pamobile.Login;
import com.ukietux.pamobile.MainActivity;
import com.ukietux.pamobile.R;
import com.ukietux.pamobile.SessionManager;
import com.ukietux.pamobile.database.DBController;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class KHS extends Fragment {

	SQLiteDatabase db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.keluar, container, false);

		return v;
	}

}