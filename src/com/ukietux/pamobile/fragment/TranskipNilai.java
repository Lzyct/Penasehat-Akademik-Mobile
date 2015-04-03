package com.ukietux.pamobile.fragment;

import com.ukietux.pamobile.R;
import com.ukietux.pamobile.database.DBController;
import com.ukietux.pamobile.utils.CustomTextView;

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
	TableRow row, rowIPK, rowHeader;
	CustomTextView ColomnNim;
	CustomTextView ViewColomnNama;
	CustomTextView ColomnNamaMaKul;
	CustomTextView ColomnNilaiHuruf;
	CustomTextView ColomnSemester;
	CustomTextView ColomnSKS;

	CustomTextView NHuruf, SKSx, totIPK, totSKS, NilaiA, NilaiB, NilaiC,
			NilaiD, NilaiE, Judul;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.transkip, container, false);

		tableLayout = (TableLayout) view.findViewById(R.id.TBLdataMHS);

		Judul = (CustomTextView) view.findViewById(R.id.judul);
		Judul.setTextColor(Color.WHITE);
		Judul.setTextSize(14);
		Judul.setGravity(Gravity.CENTER);
		Judul.setText("JUMLAH NILAI HURUF");
		Judul.setBackgroundResource(R.drawable.tv_bg);
		
		
		NilaiA = (CustomTextView) view.findViewById(R.id.a);
		NilaiB = (CustomTextView) view.findViewById(R.id.b);
		NilaiC = (CustomTextView) view.findViewById(R.id.c);
		NilaiD = (CustomTextView) view.findViewById(R.id.d);
		NilaiE = (CustomTextView) view.findViewById(R.id.e);

		Log.d("Skripsi", "memanggil DBController");
		DBController controler = new DBController(getActivity());
		Log.d("Skripsi", "mengakses database");
		db = controler.getWritableDatabase();
		displayDB();
		return view;
	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	private void displayDB() {
		Log.d("Skripsi", "query ke dataMHS");
		Cursor c = db.rawQuery(
				"SELECT * FROM TRANSKIP Where NilaiHuruf!='' ORDER BY Semester",
				null);

		Cursor A = db
				.rawQuery(
						"select count(NilaiHuruf) as Nilai from TRANSKIP where NilaiHuruf='A' ",
						null);
		Cursor B = db
				.rawQuery(
						"select count(NilaiHuruf) as Nilai from TRANSKIP where NilaiHuruf='B' ",
						null);
		Cursor C = db
				.rawQuery(
						"select count(NilaiHuruf) as Nilai from TRANSKIP where NilaiHuruf='C' ",
						null);
		Cursor D = db
				.rawQuery(
						"select count(NilaiHuruf) as Nilai from TRANSKIP where NilaiHuruf='D' ",
						null);
		Cursor E = db
				.rawQuery(
						"select count(NilaiHuruf) as Nilai from TRANSKIP where NilaiHuruf='E' ",
						null);

		Integer NilA = A.getColumnIndex("Nilai");
		Integer NilB = B.getColumnIndex("Nilai");
		Integer NilC = C.getColumnIndex("Nilai");
		Integer NilD = D.getColumnIndex("Nilai");
		Integer NilE = E.getColumnIndex("Nilai");

		A.moveToFirst();
		B.moveToFirst();
		C.moveToFirst();
		D.moveToFirst();
		E.moveToFirst();

		NilaiA.setText("A = " + A.getString(NilA));
		NilaiA.setTextColor(Color.WHITE);
		NilaiA.setTextSize(14);
		NilaiA.setGravity(Gravity.CENTER);
		NilaiA.setBackgroundResource(R.drawable.tv_bg);

		NilaiA.setText("A = " + A.getString(NilA));
		NilaiA.setTextColor(Color.WHITE);
		NilaiA.setTextSize(14);
		NilaiA.setGravity(Gravity.CENTER);
		NilaiA.setBackgroundResource(R.drawable.tv_bg);

		NilaiB.setText("B = " + B.getString(NilB));
		NilaiB.setTextColor(Color.WHITE);
		NilaiB.setTextSize(14);
		NilaiB.setGravity(Gravity.CENTER);
		NilaiB.setBackgroundResource(R.drawable.tv_bg);
		
		NilaiC.setText("C = " + B.getString(NilC));
		NilaiC.setTextColor(Color.WHITE);
		NilaiC.setTextSize(14);
		NilaiC.setGravity(Gravity.CENTER);
		NilaiC.setBackgroundResource(R.drawable.tv_bg);

		NilaiD.setText("D = " + D.getString(NilD));
		NilaiD.setTextColor(Color.WHITE);
		NilaiD.setTextSize(14);
		NilaiD.setGravity(Gravity.CENTER);
		NilaiD.setBackgroundResource(R.drawable.tv_bg);

		NilaiE.setText("E = " + E.getString(NilE));
		NilaiE.setTextColor(Color.WHITE);
		NilaiE.setTextSize(14);
		NilaiE.setGravity(Gravity.CENTER);
		NilaiE.setBackgroundResource(R.drawable.tv_bg);

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
			rowHeader = new TableRow(getActivity());
			rowHeader.setId(100);

			// Setting up the ColomnNamaMaKul parameters
			Log.d("Skripsi", "mengambil data colom NamaMakul");
			ColomnNamaMaKul = new CustomTextView(getActivity());
			ColomnNamaMaKul.setText("NAMA MATAKULIAH");
			ColomnNamaMaKul.setTextColor(Color.WHITE);
			ColomnNamaMaKul.setTextSize(14);
			ColomnNamaMaKul.setGravity(Gravity.CENTER);
			ColomnNamaMaKul.setBackgroundResource(R.drawable.tv_bg);
			rowHeader.addView(ColomnNamaMaKul, cellLp); // adding column to row

			// Setting up ColomnNilaiHuruf parameters
			Log.d("Skripsi", "mengambil data colom NilaiHuruf");
			ColomnNilaiHuruf = new CustomTextView(getActivity());
			ColomnNilaiHuruf.setText("NILAI HURUF");
			ColomnNilaiHuruf.setTextColor(Color.WHITE);
			ColomnNilaiHuruf.setGravity(Gravity.CENTER);
			ColomnNilaiHuruf.setTextSize(14);
			ColomnNilaiHuruf.setBackgroundResource(R.drawable.tv_bg);
			rowHeader.addView(ColomnNilaiHuruf, cellLp);

			// Setting up ColomnSemester parameters
			Log.d("Skripsi", "mengambil data colom Semester");
			ColomnSemester = new CustomTextView(getActivity());
			ColomnSemester.setText("SEMESTER");
			ColomnSemester.setTextColor(Color.WHITE);
			ColomnSemester.setTextSize(14);
			ColomnSemester.setGravity(Gravity.CENTER);
			ColomnSemester.setBackgroundResource(R.drawable.tv_bg);
			rowHeader.addView(ColomnSemester, cellLp);

			// Setting up ColomnSKS parameters
			Log.d("Skripsi", "mengambil data colom SKS");
			ColomnSKS = new CustomTextView(getActivity());
			ColomnSKS.setText("SKS");
			ColomnSKS.setTextColor(Color.WHITE);
			ColomnSKS.setGravity(Gravity.CENTER);
			ColomnSKS.setTextSize(14);
			ColomnSKS.setBackgroundResource(R.drawable.tv_bg);
			rowHeader.addView(ColomnSKS, cellLp);
			tableLayout.addView(rowHeader, rowLp);

			c.moveToFirst();
			do {
				row = new TableRow(getActivity());
				row.setId(100);

				// Setting up the ColomnNamaMaKul parameters
				Log.d("Skripsi", "mengambil data colom NamaMakul");
				ColomnNamaMaKul = new CustomTextView(getActivity());
				ColomnNamaMaKul.setText(c.getString(NamaMaKul));
				ColomnNamaMaKul.setTextColor(Color.BLACK);
				ColomnNamaMaKul.setTextSize(14);
				ColomnNamaMaKul.setBackgroundResource(R.drawable.edt_bg);
				row.addView(ColomnNamaMaKul, cellLp); // adding column to row
				Log.d("Skripsi", c.getString(NamaMaKul));
				Log.d("KodeMaKul", c.getString(KodeMakul));

				// Setting up ColomnNilaiHuruf parameters
				Log.d("Skripsi", "mengambil data colom NilaiHuruf");
				ColomnNilaiHuruf = new CustomTextView(getActivity());
				ColomnNilaiHuruf.setText(c.getString(NilaiHuruf));
				ColomnNilaiHuruf.setTextColor(Color.BLACK);
				ColomnNilaiHuruf.setGravity(Gravity.CENTER);
				ColomnNilaiHuruf.setTextSize(14);
				ColomnNilaiHuruf.setBackgroundResource(R.drawable.edt_bg);
				row.addView(ColomnNilaiHuruf, cellLp);
				Log.d("Skripsi", c.getString(NilaiHuruf));

				// Setting up ColomnSemester parameters
				Log.d("Skripsi", "mengambil data colom Semester");
				ColomnSemester = new CustomTextView(getActivity());
				ColomnSemester.setText(c.getString(Semester));
				ColomnSemester.setTextColor(Color.BLACK);
				ColomnSemester.setTextSize(14);
				ColomnSemester.setGravity(Gravity.CENTER);
				ColomnSemester.setBackgroundResource(R.drawable.edt_bg);
				row.addView(ColomnSemester, cellLp);
				Log.d("Skripsi", c.getString(Semester));

				// Setting up ColomnSKS parameters
				Log.d("Skripsi", "mengambil data colom SKS");
				ColomnSKS = new CustomTextView(getActivity());
				ColomnSKS.setText(c.getString(SKS));
				ColomnSKS.setTextColor(Color.BLACK);
				ColomnSKS.setGravity(Gravity.CENTER);
				ColomnSKS.setTextSize(14);
				ColomnSKS.setBackgroundResource(R.drawable.edt_bg);
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