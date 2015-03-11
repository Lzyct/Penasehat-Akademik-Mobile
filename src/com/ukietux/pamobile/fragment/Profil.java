package com.ukietux.pamobile.fragment;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ukietux.pamobile.R;
import com.ukietux.pamobile.database.DBController;

public class Profil extends Fragment {
	SQLiteDatabase database, db;
	TextView Nimx, Namax, JumSKSx, NilaiIPKx;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.profil, container,
				false);
		Nimx = (TextView) v.findViewById(R.id.Nim);
		Namax = (TextView) v.findViewById(R.id.Nama);
		NilaiIPKx = (TextView) v.findViewById(R.id.IPK);
		JumSKSx = (TextView) v.findViewById(R.id.JumSKS);

		Log.d("Skripsi", "memanggil DBController");
		DBController controler = new DBController(getActivity());
		Log.d("Skripsi", "mengakses database");
		db = controler.getWritableDatabase();

		displayIPK();
		return v;

	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	private void displayIPK() {
		Log.d("Skripsix", "query ke dataMHS");
		String query = "SELECT Nama,Nim,NilaiHuruf,SKS,SUM(SKS) as JumSKS,SUM(CASE WHEN NilaiHuruf= 'A' THEN 4*SKS WHEN NilaiHuruf= 'B' THEN 3*SKS WHEN NilaiHuruf= 'C' THEN 2*SKS WHEN NilaiHuruf= 'D' THEN 1*SKS ELSE 0*SKS END)*1.0/SUM(SKS) AS IPK FROM DataMHS WHERE NilaiHuruf!=''";
		Cursor a = db.rawQuery(query, null);
		Integer Nim = a.getColumnIndex("Nim");
		Integer Nama = a.getColumnIndex("Nama");
		Integer JumSKS = a.getColumnIndex("JumSKS");
		Integer IPK = a.getColumnIndex("IPK");

		if (a.getCount() > 0) {
			// Scanning value field by raw Cursor
			a.moveToFirst();
			do {

				// Setting up the ColomnNim parameters
				Log.d("Skripsi", "mengambil data colom nim");
				Nimx.setText("Nim : " + a.getString(Nim));
				Nimx.setTextColor(Color.BLACK);
				NilaiIPKx.setGravity(Gravity.CENTER_HORIZONTAL);
				Log.d("Skripsix", a.getString(Nim));

				// Setting up the ColomnNama parameters
				Log.d("Skripsi", "mengambil data colom Nama");
				Namax.setText("Nama : " + a.getString(Nama));
				Namax.setTextColor(Color.BLACK);
				NilaiIPKx.setGravity(Gravity.CENTER_HORIZONTAL);

				Log.d("Skripsi", "mengambil data colom nim");
				NilaiIPKx.setText("IPK = " + a.getString(IPK));
				NilaiIPKx.setTextColor(Color.BLACK);
				NilaiIPKx.setGravity(Gravity.CENTER_HORIZONTAL);
				Log.d("Skripsix", a.getString(IPK));

				// Setting up the ColomnNama parameters
				Log.d("Skripsi", "mengambil data colom Nama");
				JumSKSx.setText("SKS Dilulusi : " + a.getString(JumSKS));
				JumSKSx.setTextColor(Color.BLACK);
				NilaiIPKx.setGravity(Gravity.CENTER_HORIZONTAL);
				Log.d("Skripsix", a.getString(JumSKS));

			} while (a.moveToNext());
			db.close();
		} else {
			// Toast.makeText(getActivity().getApplicationContext(),
			// "Event occurred.", Toast.LENGTH_LONG).show();
		}

	}
}
