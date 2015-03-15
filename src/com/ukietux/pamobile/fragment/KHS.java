package com.ukietux.pamobile.fragment;

import java.util.ArrayList;

import com.ukietux.pamobile.R;
import com.ukietux.pamobile.database.DBController;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class KHS extends Fragment {

	SQLiteDatabase db;
	TextView ColomnNamaMaKul;
	TextView ColomnNilaiHuruf;
	TextView ColomnSKS;
	TextView IPSx;
	TableRow row, rowIPK;
	TableLayout tableLayout, tableIPK;
	String Semester;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LayoutInflater x = getActivity().getLayoutInflater();
		View v = x.inflate(R.layout.khs, container, false);

		DBController controler = new DBController(getActivity());
		db = controler.getWritableDatabase();

		tableLayout = (TableLayout) v.findViewById(R.id.khs);

		IPSx = (TextView) v.findViewById(R.id.IPS);

		ArrayList<String> my_array = new ArrayList<String>();
		my_array = getTableValues();

		Spinner SP_Semester = (Spinner) v.findViewById(R.id.Spinner_Semester);
		SP_Semester.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				DBController controler = new DBController(getActivity());
				Semester = parent.getItemAtPosition(pos).toString();
				Log.d("semester", Semester);
				// clean all before add view
				tableLayout.removeAllViews();
				TotNilai();
				displayDataMHS();
				Log.d("semester", "Masuk ke displayDataMHS");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
		ArrayAdapter my_Adapter = new ArrayAdapter(getActivity(),
				R.layout.semester, my_array);
		SP_Semester.setAdapter(my_Adapter);
		return v;
	}

	private void TotNilai() {
		Log.d("Skripsix", "query ke dataMHS");
		String query = "SELECT Nama,Nim,NilaiHuruf,SKS,SUM(SKS) as JumSKS,"
				+ "SUM(CASE WHEN NilaiHuruf= 'A' THEN 4*SKS "
				+ "WHEN NilaiHuruf= 'B' THEN 3*SKS "
				+ "WHEN NilaiHuruf= 'C' THEN 2*SKS "
				+ "WHEN NilaiHuruf= 'D' THEN 1*SKS "
				+ "ELSE 0*SKS END)*1.0/SUM(SKS)*1.0 "
				+ "AS IPS FROM DataMHS "
				+ "WHERE NilaiHuruf!='' and Semester=" + Semester;
		Cursor a = db.rawQuery(query, null);
		Integer IPS = a.getColumnIndex("IPS");

		if (a.getCount() > 0) {
			// Scanning value field by raw Cursor
			a.moveToFirst();
			do {

				Log.d("Skripsi", "mengambil data colom Nama");
				IPSx.setText("IPS : " + a.getString(IPS));
				IPSx.setTextColor(Color.BLACK);
				IPSx.setPadding(20, 5, 20, 5);
				IPSx.setGravity(Gravity.CENTER_HORIZONTAL);
				Log.d("Skripsix", a.getString(IPS));

			} while (a.moveToNext());
//			db.close();
		} else {
			// Toast.makeText(getActivity().getApplicationContext(),
			// "Event occurred.", Toast.LENGTH_LONG).show();
		}
	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	private void displayDataMHS() {
		Log.d("skripsi", "query ke dataMHS");
		Cursor c = db.rawQuery(
				"SELECT * FROM DataMHS Where NilaiHuruf!='' and Semester="
						+ Semester + " ORDER BY Semester", null);
		Integer NamaMaKul = c.getColumnIndex("NamaMaKul");
		Integer NilaiHuruf = c.getColumnIndex("NilaiHuruf");
		Integer SKS = c.getColumnIndex("SKS");

		// tableLayout = new TableLayout(getActivity());
		// Java. You succeed!
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

				// Clean All View before display dataMHS

				// Setting up the ColomnNamaMaKul parameters
				Log.d("Skripsi", "mengambil data colom NamaMakul");
				ColomnNamaMaKul = new TextView(getActivity());
				ColomnNamaMaKul.setText(c.getString(NamaMaKul));
				ColomnNamaMaKul.setTextColor(Color.BLACK);
				ColomnNamaMaKul.setTextSize(14);
				ColomnNamaMaKul.setPadding(20, 5, 20, 5);
				ColomnNamaMaKul.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnNamaMaKul, cellLp); // adding column to row
				Log.d("skripsi", c.getString(NamaMaKul));

				// Setting up ColomnNilaiHuruf parameters
				Log.d("Skripsi", "mengambil data colom NilaiHuruf");
				ColomnNilaiHuruf = new TextView(getActivity());
				ColomnNilaiHuruf.setText(c.getString(NilaiHuruf));
				ColomnNilaiHuruf.setTextColor(Color.BLACK);
				ColomnNilaiHuruf.setTextSize(14);
				ColomnNilaiHuruf.setGravity(Gravity.CENTER);
//				ColomnNilaiHuruf.setPadding(20, 5, 20, 5);
				ColomnNilaiHuruf.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnNilaiHuruf, cellLp);
				Log.d("skripsi", c.getString(NilaiHuruf));

				// Setting up ColomnSKS parameters
				Log.d("Skripsi", "mengambil data colom SKS");
				ColomnSKS = new TextView(getActivity());
				ColomnSKS.setText(c.getString(SKS));
				ColomnSKS.setTextColor(Color.BLACK);
				ColomnSKS.setTextSize(14);
				ColomnSKS.setGravity(Gravity.CENTER);
//				ColomnSKS.setPadding(20, 5, 20, 5);
				ColomnSKS.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnSKS, cellLp);
				Log.d("skripsi", c.getString(SKS));
				tableLayout.addView(row, rowLp);
			} while (c.moveToNext());
			// db.close();
		} else {
			// Toast.makeText(getActivity().getApplicationContext(),
			// "Event occurred.", Toast.LENGTH_LONG).show();
		}

	}

	// THIS FUNCTION SHOWS DATA FROM THE DATABASE
	public ArrayList<String> getTableValues() {

		ArrayList<String> my_array = new ArrayList<String>();
		try {

			Cursor allrows = db
					.rawQuery(
							"SELECT DISTINCT Semester from DataMHS where NilaiHuruf!='' ORDER BY Semester ",
							null);
			System.out.println("COUNT : " + allrows.getCount());

			if (allrows.moveToFirst()) {
				do {
					String NAME = allrows.getString(0);
					my_array.add(NAME);

				} while (allrows.moveToNext());
			}
			allrows.close();
			// db.close();
		} catch (Exception e) {
		}
		return my_array;
	}

}