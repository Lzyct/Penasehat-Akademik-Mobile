package com.ukietux.pamobile.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.ukietux.pamobile.R;
import com.ukietux.pamobile.database.DBController;
import com.ukietux.pamobile.utils.CustomTextView;

import android.annotation.SuppressLint;
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

public class KHS extends Fragment {

	SQLiteDatabase db;
	CustomTextView ColomnNamaMaKul;
	CustomTextView ColomnNilaiHuruf;
	CustomTextView ColomnSKS;
	CustomTextView IPSx, ket;
	TableRow row, rowIPK, rowHeader;
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

		IPSx = (CustomTextView) v.findViewById(R.id.IPS);

		ket = (CustomTextView) v.findViewById(R.id.Keterangan);
		ket.setBackgroundResource(R.drawable.tv_bg);
		ket.setTextColor(Color.WHITE);
		ket.setTextSize(14);

		ket.setText("Keterangan :\n" + "\tMK \t= Matakuliah\n"
				+ "\tNH \t= Nilai Huruf\n" + "\tSMT \t= Semester");
		ArrayList<String> my_array = new ArrayList<String>();
		my_array = getTableValues();

		Spinner SP_Semester = (Spinner) v.findViewById(R.id.Spinner_Semester);
		SP_Semester.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				DBController controler = new DBController(getActivity());
				String testX = parent.getItemAtPosition(pos).toString();
				String tX = testX.substring(0, 6);

				if (tX.equals("Gasal ")) {
					Semester = testX.substring(6) + "1";
				} else if (tX.equals("Genap ")) {
					Semester = testX.substring(6) + "2";
				}

				// Semester = parent.getItemAtPosition(pos).toString();
				Log.d("semester", Semester);
				// clean all before add view
				tableLayout.removeAllViews();
				TotNilai();
				displayTRANSKIP();
				Log.d("semester", "Masuk ke displayTRANSKIP");
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
		Log.d("Skripsix", "query ke TRANSKIP");
		String query = "SELECT Nama,Nim,NilaiHuruf,SKS,SUM(SKS) as JumSKS,"
				+ "SUM(CASE WHEN NilaiHuruf= 'A' THEN 4*SKS "
				+ "WHEN NilaiHuruf= 'B' THEN 3*SKS "
				+ "WHEN NilaiHuruf= 'C' THEN 2*SKS "
				+ "WHEN NilaiHuruf= 'D' THEN 1*SKS "
				+ "ELSE 0*SKS END)*1.0/SUM(SKS)*1.0 " + "AS IPS FROM TRANSKIP "
				+ "WHERE NilaiHuruf!='' and Semester=" + Semester;
		Cursor a = db.rawQuery(query, null);
		Integer IPS = a.getColumnIndex("IPS");

		if (a.getCount() > 0) {
			// Scanning value field by raw Cursor
			a.moveToFirst();
			do {

				if (a.getString(IPS) == null) {

				} else {
					Log.d("Skripsi", "mengambil data colom Nama");

					Double IPSter = Double.valueOf(a.getString(IPS));
					IPSx.setText("IPS : "
							+ new DecimalFormat("#.##").format(IPSter));
					IPSx.setTextColor(Color.WHITE);
					IPSx.setBackgroundResource(R.drawable.tv_bg);
					IPSx.setGravity(Gravity.CENTER_HORIZONTAL);
					Log.d("Skripsix", a.getString(IPS));
				}
			} while (a.moveToNext());
			// db.close();
		} else {
			// Toast.makeText(getActivity().getApplicationContext(),
			// "Event occurred.", Toast.LENGTH_LONG).show();
		}
	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	private void displayTRANSKIP() {
		Log.d("skripsi", "query ke TRANSKIP");
		Cursor c = db.rawQuery(
				"SELECT * FROM TRANSKIP Where NilaiHuruf!='' and Semester="
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
			rowHeader = new TableRow(getActivity());
			rowHeader.setId(100);

			// Clean All View before display TRANSKIP

			// Setting up the ColomnNamaMaKul parameters
			Log.d("Skripsi", "mengambil data colom NamaMakul");
			ColomnNamaMaKul = new CustomTextView(getActivity());
			ColomnNamaMaKul.setText("MK");
			ColomnNamaMaKul.setTextColor(Color.WHITE);
			ColomnNamaMaKul.setTextSize(14);
			ColomnNamaMaKul.setGravity(Gravity.CENTER);
			ColomnNamaMaKul.setBackgroundResource(R.drawable.tv_bg);
			rowHeader.addView(ColomnNamaMaKul, cellLp);

			// Setting up ColomnNilaiHuruf parameters
			Log.d("Skripsi", "mengambil data colom NilaiHuruf");
			ColomnNilaiHuruf = new CustomTextView(getActivity());
			ColomnNilaiHuruf.setText("NH");
			ColomnNilaiHuruf.setTextColor(Color.WHITE);
			ColomnNilaiHuruf.setTextSize(14);
			ColomnNilaiHuruf.setGravity(Gravity.CENTER);
			ColomnNilaiHuruf.setBackgroundResource(R.drawable.tv_bg);
			rowHeader.addView(ColomnNilaiHuruf, cellLp);

			// Setting up ColomnSKS parameters
			Log.d("Skripsi", "mengambil data colom SKS");
			ColomnSKS = new CustomTextView(getActivity());
			ColomnSKS.setText("SKS");
			ColomnSKS.setTextColor(Color.WHITE);
			ColomnSKS.setTextSize(14);
			ColomnSKS.setGravity(Gravity.CENTER);
			ColomnSKS.setBackgroundResource(R.drawable.tv_bg);
			rowHeader.addView(ColomnSKS, cellLp);
			tableLayout.addView(rowHeader, rowLp);

			c.moveToFirst();
			do {
				row = new TableRow(getActivity());
				row.setId(100);

				// Clean All View before display TRANSKIP

				// Setting up the ColomnNamaMaKul parameters
				Log.d("Skripsi", "mengambil data colom NamaMakul");
				ColomnNamaMaKul = new CustomTextView(getActivity());
				ColomnNamaMaKul.setText(c.getString(NamaMaKul));
				ColomnNamaMaKul.setTextColor(Color.BLACK);
				ColomnNamaMaKul.setTextSize(14);
				ColomnNamaMaKul.setBackgroundResource(R.drawable.edt_bg);
				row.addView(ColomnNamaMaKul, cellLp); // adding column to row
				Log.d("skripsi", c.getString(NamaMaKul));

				// Setting up ColomnNilaiHuruf parameters
				Log.d("Skripsi", "mengambil data colom NilaiHuruf");
				ColomnNilaiHuruf = new CustomTextView(getActivity());
				ColomnNilaiHuruf.setText(c.getString(NilaiHuruf));
				ColomnNilaiHuruf.setTextColor(Color.BLACK);
				ColomnNilaiHuruf.setTextSize(14);
				ColomnNilaiHuruf.setGravity(Gravity.CENTER);
				ColomnNilaiHuruf.setBackgroundResource(R.drawable.edt_bg);
				row.addView(ColomnNilaiHuruf, cellLp);
				Log.d("skripsi", c.getString(NilaiHuruf));

				// Setting up ColomnSKS parameters
				Log.d("Skripsi", "mengambil data colom SKS");
				ColomnSKS = new CustomTextView(getActivity());
				ColomnSKS.setText(c.getString(SKS));
				ColomnSKS.setTextColor(Color.BLACK);
				ColomnSKS.setTextSize(14);
				ColomnSKS.setGravity(Gravity.CENTER);
				ColomnSKS.setBackgroundResource(R.drawable.edt_bg);
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

	public ArrayList<String> getTableValues() {

		ArrayList<String> my_array = new ArrayList<String>();
		try {

			Cursor allrows = db
					.rawQuery(
							"SELECT DISTINCT Semester from TRANSKIP where NilaiHuruf!='' ORDER BY Semester ",
							null);
			System.out.println("COUNT : " + allrows.getCount());

			if (allrows.moveToFirst()) {
				do {
					String Semes = allrows.getString(0);
					String test = Semes.substring(Semes.length() - 1);
					String SemesterX = null;
					if (test.equals("1")) {
						SemesterX = "Gasal "
								+ Semes.substring(0, Semes.length() - 1);
					} else if (test.equals("2")) {
						SemesterX = "Genap "
								+ Semes.substring(0, Semes.length() - 1);
					}
					my_array.add(SemesterX);
					Log.d("Semester", SemesterX);

				} while (allrows.moveToNext());
			}
			allrows.close();
			// db.close();
		} catch (Exception e) {
		}
		return my_array;
	}

}