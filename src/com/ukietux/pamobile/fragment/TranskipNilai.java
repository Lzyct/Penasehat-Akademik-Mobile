package com.ukietux.pamobile.fragment;

import com.ukietux.pamobile.R;
import com.ukietux.pamobile.database.DBController;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

@SuppressLint("InlinedApi")
public class TranskipNilai extends Fragment {
	SQLiteDatabase database, db;

	TableLayout tableLayout, tableIPK;
	TableRow row, rowIPK;
	TextView ColomnNim;
	TextView ColomnNama;
	TextView ColomnNamaMaKul;
	TextView ColomnNilaiHuruf;
	TextView ColomnSemester;
	TextView ColomnSKS;

	TextView NHuruf, SKSx, totIPK, totSKS;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater
				.inflate(R.layout.transkip, container, false);

		tableLayout = (TableLayout) view.findViewById(R.id.TBLdataMHS);

		Log.d("Skripsi", "memanggil DBController");
		DBController controler = new DBController(getActivity());
		Log.d("Skripsi", "mengakses database");
		db = controler.getWritableDatabase();
		displayDB();
		return view;
	}

	private TextView initPlainTextView() {
		TextView textView = new TextView(getActivity());

		return textView;
	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	private void displayDB() {
		Log.d("Skripsi", "query ke dataMHS");
		Cursor c = db.rawQuery(
				"SELECT * FROM DataMHS Where NilaiHuruf!='' ORDER BY Semester",
				null);
		Integer Nim = c.getColumnIndex("Nim");
		Integer Nama = c.getColumnIndex("Nama");
		Integer NamaMaKul = c.getColumnIndex("NamaMaKul");
		Integer NilaiHuruf = c.getColumnIndex("NilaiHuruf");
		Integer Semester = c.getColumnIndex("Semester");
		Integer SKS = c.getColumnIndex("SKS");
		Integer KodeMakul = c.getColumnIndex("KodeMaKul");

		
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				TableLayout.LayoutParams.FILL_PARENT,
				TableLayout.LayoutParams.FILL_PARENT);
		tableLayout.setLayoutParams(lp);
		tableLayout.setStretchAllColumns(true);
		TableLayout.LayoutParams rowLp = new TableLayout.LayoutParams(
				TableLayout.LayoutParams.FILL_PARENT,
				TableLayout.LayoutParams.FILL_PARENT, 1.0f);
		TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
				TableRow.LayoutParams.FILL_PARENT,
				TableRow.LayoutParams.FILL_PARENT, 1.0f);
		
		if (c.getCount() > 0) {
			// Scanning value field by raw Cursor
			c.moveToFirst();
			do {
				row = new TableRow(getActivity());
				row.setId(100);


				// Setting up the ColomnNamaMaKul parameters
				Log.d("Skripsi", "mengambil data colom NamaMakul");
				ColomnNamaMaKul = new TextView(getActivity());
				ColomnNamaMaKul.setText(c.getString(NamaMaKul));
				ColomnNamaMaKul.setTextColor(Color.BLACK);
				ColomnNamaMaKul.setTextSize(14);
				ColomnNamaMaKul.setPadding(20, 5, 20, 5);
				ColomnNamaMaKul.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnNamaMaKul, cellLp); // adding column to row
				Log.d("Skripsi", c.getString(NamaMaKul));
				Log.d("KodeMaKul", c.getString(KodeMakul));

				// Setting up ColomnNilaiHuruf parameters
				Log.d("Skripsi", "mengambil data colom NilaiHuruf");
				ColomnNilaiHuruf = new TextView(getActivity());
				ColomnNilaiHuruf.setText(c.getString(NilaiHuruf));
				ColomnNilaiHuruf.setTextColor(Color.BLACK);
				ColomnNilaiHuruf.setGravity(Gravity.CENTER);
				ColomnNilaiHuruf.setTextSize(14);
//				ColomnNilaiHuruf.setPadding(20, 5, 20, 5);
				ColomnNilaiHuruf.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnNilaiHuruf, cellLp);
				Log.d("Skripsi", c.getString(NilaiHuruf));

				// Setting up ColomnSemester parameters
				Log.d("Skripsi", "mengambil data colom Semester");
				ColomnSemester = new TextView(getActivity());
				ColomnSemester.setText(c.getString(Semester));
				ColomnSemester.setTextColor(Color.BLACK);
				ColomnSemester.setTextSize(14);
				ColomnSemester.setGravity(Gravity.CENTER);
//				ColomnSemester.setPadding(20, 5, 20, 5);
				ColomnSemester.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnSemester, cellLp);
				Log.d("Skripsi", c.getString(Semester));

				// Setting up ColomnSKS parameters
				Log.d("Skripsi", "mengambil data colom SKS");
				ColomnSKS = new TextView(getActivity());
				ColomnSKS.setText(c.getString(SKS));
				ColomnSKS.setTextColor(Color.BLACK);
				ColomnSKS.setGravity(Gravity.CENTER);
				ColomnSKS.setTextSize(14);
//				ColomnSKS.setPadding(20, 5, 20, 5);
				ColomnSKS.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnSKS, cellLp);
				Log.d("Skripsi", c.getString(SKS));
				tableLayout.addView(row, rowLp);
			} while (c.moveToNext());
			db.close();
		} else {
			// Toast.makeText(getActivity().getApplicationContext(),
			// "Event occurred.", Toast.LENGTH_LONG).show();
		}

	}

}