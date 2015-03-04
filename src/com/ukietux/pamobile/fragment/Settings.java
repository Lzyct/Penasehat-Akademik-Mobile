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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Settings extends Fragment {

	SQLiteDatabase db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.keluar, container, false);

		Keluar();
		return v;
	}

	public void Keluar() {

		final DBController controler = new DBController(getActivity());
		Log.d("Skripsi", "mengakses database");
		db = controler.getWritableDatabase();

		final SessionManager session = new SessionManager(getActivity());
		Context context = this.getActivity();
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false)
				.setTitle("Peringatan!")
				.setMessage("Yakin ingin keluar")
				.setIcon(R.drawable.ic_warning)
				.setCancelable(false)
				.setPositiveButton("Keluar",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity
								Log.d("Skripsi","delete table");
								controler.deleteAll();
								Log.d("Skripsi","hapus session");
								session.logoutUser();
								Log.d("Skripsi","berhasil di hapus dan memulai kembali ke login");
								Intent intent = new Intent(getActivity(),
										Login.class);
								startActivity(intent);
							}
						})
				.setNegativeButton("Batal",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();

	}
}