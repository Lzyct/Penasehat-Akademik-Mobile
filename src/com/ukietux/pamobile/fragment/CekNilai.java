package com.ukietux.pamobile.fragment;

import com.ukietux.pamobile.R;
import com.ukietux.pamobile.database.DBController;

import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

@SuppressLint("InlinedApi")
public class CekNilai extends Fragment {
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
				.inflate(R.layout.cek_nilai, container, false);

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

		if (c.getCount() > 0) {
			// Scanning value field by raw Cursor
			c.moveToFirst();
			do {
				row = new TableRow(getActivity());
				row.setId(100);
				row.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));

				// Setting up the ColomnNim parameters
				Log.d("Skripsi", "mengambil data colom nim");
				ColomnNim = new TextView(getActivity());
				ColomnNim.setText(c.getString(Nim));
				ColomnNim.setTextSize(14);
				ColomnNim.setTextColor(Color.BLACK);
				ColomnNim.setVisibility(View.GONE);
				ColomnNim.setPadding(5, 5, 5, 5);
				ColomnNim.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnNim);
				Log.d("Skripsi", c.getString(Nim));

				// Setting up the ColomnNama parameters
				Log.d("Skripsi", "mengambil data colom Nama");
				ColomnNama = new TextView(getActivity());
				ColomnNama.setText(c.getString(Nama));
				ColomnNama.setTextColor(Color.BLACK);
				ColomnNama.setTextSize(14);
				ColomnNama.setPadding(5, 5, 5, 5);
				ColomnNama.setVisibility(View.GONE);
				ColomnNama.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnNama); // adding coloumn to row
				Log.d("Skripsi", c.getString(Nama));

				// Setting up the ColomnNamaMaKul parameters
				Log.d("Skripsi", "mengambil data colom NamaMakul");
				ColomnNamaMaKul = new TextView(getActivity());
				ColomnNamaMaKul.setText(c.getString(NamaMaKul));
				ColomnNamaMaKul.setTextColor(Color.BLACK);
				ColomnNamaMaKul.setTextSize(14);
				ColomnNamaMaKul.setPadding(5, 5, 2, 5);
				ColomnNamaMaKul.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnNamaMaKul); // adding column to row
				Log.d("Skripsi", c.getString(NamaMaKul));

				// Setting up ColomnNilaiHuruf parameters
				Log.d("Skripsi", "mengambil data colom NilaiHuruf");
				ColomnNilaiHuruf = new TextView(getActivity());
				ColomnNilaiHuruf.setText(c.getString(NilaiHuruf));
				ColomnNilaiHuruf.setTextColor(Color.BLACK);
				ColomnNilaiHuruf.setTextSize(14);
				ColomnNilaiHuruf.setPadding(20, 5, 20, 5);
				ColomnNilaiHuruf.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnNilaiHuruf);
				Log.d("Skripsi", c.getString(NilaiHuruf));

				// Setting up ColomnSemester parameters
				Log.d("Skripsi", "mengambil data colom Semester");
				ColomnSemester = new TextView(getActivity());
				ColomnSemester.setText(c.getString(Semester));
				ColomnSemester.setTextColor(Color.BLACK);
				ColomnSemester.setTextSize(14);
				ColomnSemester.setPadding(20, 5, 20, 5);
				ColomnSemester.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnSemester);
				Log.d("Skripsi", c.getString(Semester));

				// Setting up ColomnSKS parameters
				Log.d("Skripsi", "mengambil data colom SKS");
				ColomnSKS = new TextView(getActivity());
				ColomnSKS.setText(c.getString(SKS));
				ColomnSKS.setTextColor(Color.BLACK);
				ColomnSKS.setTextSize(14);
				ColomnSKS.setPadding(20, 5, 20, 5);
				ColomnSKS.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnSKS);
				Log.d("Skripsi", c.getString(SKS));
				tableLayout.addView(row, new TableLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			} while (c.moveToNext());
			db.close();
		} else {
			// Toast.makeText(getActivity().getApplicationContext(),
			// "Event occurred.", Toast.LENGTH_LONG).show();
		}

	}

}