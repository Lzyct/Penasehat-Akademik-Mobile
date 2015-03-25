package com.ukietux.pamobile.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.ukietux.pamobile.R;
import com.ukietux.pamobile.database.DBController;

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
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

@SuppressLint("InlinedApi")
public class KRS extends Fragment {
	SQLiteDatabase db;
	String Semester, LastIPS;
	int MaxSKS;
	TextView IPSTerakhir, Semesterx, ListSemester, ListSemesterGanjil,
			ListSemesterGenap, MaksimumSKS;
	List<String> listSemester = new ArrayList<String>();
	List<String> listSemesterGanjil = new ArrayList<String>();
	List<String> listSemesterGenap = new ArrayList<String>();
	TableLayout tableLayout, tableLayoutCurrent;
	TableRow row, rowCurrent;
	TextView ColomnNamaMakul,ColomnNamaMakulSek;
	TextView ColomnKodeMAkul,ColomnSifatMAkulSek;
	TextView ColomnSemester,ColomnSemesterSek;
	TextView ColomnSKS,ColomnSKSSek, Test;
	Integer Semes,CurSemester;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.krs, container, false);
		DBController controler = new DBController(getActivity());
		db = controler.getWritableDatabase();

		IPSTerakhir = (TextView) v.findViewById(R.id.ips);
		Semesterx = (TextView) v.findViewById(R.id.semester);
		MaksimumSKS = (TextView) v.findViewById(R.id.MaksimumSKS);
		ListSemester = (TextView) v.findViewById(R.id.listSemester);
		Test = (TextView) v.findViewById(R.id.test);
		// tableSemesterLalu = (TableLayout) v.findViewById(R.id.semesterLalu);
		tableLayout = (TableLayout) v.findViewById(R.id.semesterLalu);
		tableLayoutCurrent =(TableLayout)v.findViewById(R.id.semesterSekarang);
		CekKRS();
		getLastSemester();
		getLastIPS();
		
		return v;
	}

	public void getLastSemester() {
		// get semester
		Cursor allrows = db
				.rawQuery(
						"SELECT DISTINCT Semester from DataMHS where NilaiHuruf!='' ORDER BY Semester ",
						null);
		System.out.println("COUNT : " + allrows.getCount());

		allrows.moveToLast();
		Semester = allrows.getString(0);
		// return Semester;
		// Semester.setText(NAME);
	}

	public void getLastIPS() {
		Log.d("Skripsix", "query ke dataMHS");
		String query = "SELECT Nama,Nim,NilaiHuruf,SKS,SUM(SKS) as JumSKS,"
				+ "SUM(CASE WHEN NilaiHuruf= 'A' THEN 4*SKS "
				+ "WHEN NilaiHuruf= 'B' THEN 3*SKS "
				+ "WHEN NilaiHuruf= 'C' THEN 2*SKS "
				+ "WHEN NilaiHuruf= 'D' THEN 1*SKS "
				+ "ELSE 0*SKS END)*1.0/SUM(SKS)*1.0 " + "AS IPS FROM DataMHS "
				+ "WHERE NilaiHuruf!='' and Semester=" + Semester;
		Cursor a = db.rawQuery(query, null);
		Integer IPS = a.getColumnIndex("IPS");

		if (a.getCount() > 0) {
			// Scanning value field by raw Cursor
			a.moveToFirst();
			do {

				Log.d("Skripsi", "mengambil data colom Nama");
				Double IPSx = Double.valueOf(a.getString(IPS));

				IPSTerakhir.setText("IPS Semester Lalu: "
						+ new DecimalFormat("#.##").format(IPSx));
				IPSTerakhir.setTextColor(Color.BLACK);
				IPSTerakhir.setPadding(20, 5, 20, 5);
				IPSTerakhir.setGravity(Gravity.CENTER_HORIZONTAL);
				// Log.d("Skripsix", a.getString(IPS));

				LastIPS = a.getString(IPS);

				Double IPSTerbaru = Double.valueOf(LastIPS);
				if (IPSTerbaru >= 3.00 && IPSTerbaru <= 4.00) {
					MaxSKS = 24;
				} else if (IPSTerbaru >= 2.50 && IPSTerbaru <= 2.99) {
					MaxSKS = 20;
				} else if (IPSTerbaru >= 2.00 && IPSTerbaru <= 2.49) {
					MaxSKS = 16;
				} else {
					MaxSKS = 10;
				}
				MaksimumSKS.setText("Maksimum SKS : " + MaxSKS);

			} while (a.moveToNext());
			// db.close();
		} else {
			// Toast.makeText(getActivity().getApplicationContext(),
			// "Event occurred.", Toast.LENGTH_LONG).show();
		}
	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	private void CekKRS() {
		Log.d("Skripsix", "query ke dataMHS");
		String query = "SELECT DISTINCT Semester as TotSmt " + "FROM DataMHS "
				+ "WHERE NilaiHuruf!='' " + "order by TotSmt";
		String ganjil = "SELECT DISTINCT Semester as TotSmt " + "FROM DataMHS "
				+ "WHERE NilaiHuruf!='' and Semester like '%1' "
				+ "order by TotSmt";
		String genap = "SELECT DISTINCT Semester as TotSmt "
				+ "FROM DataMHS WHERE NilaiHuruf!='' "
				+ "and Semester like '%2' " + "order by TotSmt";
		String getSmt = "SELECT Count(DISTINCT Semester) as TotSmt FROM DataMHS WHERE NilaiHuruf!='' ";
		Cursor all = db.rawQuery(query, null);
		Cursor CGanjil = db.rawQuery(ganjil, null);
		Cursor CGenap = db.rawQuery(genap, null);
		Cursor getSemester = db.rawQuery(getSmt, null);
		Integer Smtr = all.getColumnIndex("TotSmt");
		Integer SmtrGanjil = CGanjil.getColumnIndex("TotSmt");
		Integer SmtrGenap = CGenap.getColumnIndex("TotSmt");
		Integer SemesterF = getSemester.getColumnIndex("TotSmt"); 
		if (all.getCount() > 0) {
			// Scanning value field by raw Cursor
			all.moveToFirst();
			do {
				StringBuilder allSemester = new StringBuilder();
				listSemester.add(all.getString(Smtr));
			} while (all.moveToNext());
			


			
			
			Semes = listSemester.size();
			if (Semes % 2 == 0) {
				Semesterx.setText(Semes + " Semester Genap");
				UlangKRSGanjil();
				AllKRS();
				
				if (CGenap.getCount() > 0) {
					CGenap.moveToFirst();
					do {
						StringBuilder SemesterGenap = new StringBuilder();
						listSemesterGenap.add(CGenap.getString(SmtrGenap));
						for (String s : listSemesterGenap) {
							if (SemesterGenap.length() > 0) {
								SemesterGenap.append(" ");
							}
							SemesterGenap.append(s);
						}

						ListSemester.setText(SemesterGenap.toString());

					} while (CGenap.moveToNext());
				}

			} else {
				Semesterx.setText(Semes + " Semester Ganjil");
				UlangKRSGenap();
				AllKRS();
				if (CGanjil.getCount() > 0) {

					CGanjil.moveToFirst();
					do {
						StringBuilder SemesterGanjil = new StringBuilder();
						listSemesterGanjil.add(CGanjil.getString(SmtrGanjil));
						for (String s : listSemesterGanjil) {
							if (SemesterGanjil.length() > 0) {
								SemesterGanjil.append(" ");
							}
							SemesterGanjil.append(s);
						}

						ListSemester.setText(SemesterGanjil.toString());

					} while (CGanjil.moveToNext());

				}
			}
		}
	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	public void UlangKRSGanjil() {

		Cursor UIN = db.rawQuery("SELECT DISTINCT Semester," + "NamaMakul,"
				+ "KodeMakul as blao," + "SKS " + "FROM DataMHS "
				+ "WHERE NilaiHuruf!='' " + "and Semester like '%1' "
				+ "and NilaiHuruf='E' " + "order by KodeMakul desc", null);

		// untuk MakulUIN
		Integer NamaMaKul = UIN.getColumnIndex("NamaMaKul");
		Integer KodeMaKul = UIN.getColumnIndex("blao");
		Integer SKS = UIN.getColumnIndex("SKS");
		Integer Semester = UIN.getColumnIndex("Semester");

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

		if (UIN.getCount() > 0) {
			// Scanning value field by raw Cursor
			UIN.moveToFirst();
			do {
				row = new TableRow(getActivity());
				row.setId(100);
				// Setting up the ColomnNamaMaKul parameters
				Log.d("Skripsi", "mengambil data colom NamaMakul");
				ColomnNamaMakul = new TextView(getActivity());
				ColomnNamaMakul.setText(UIN.getString(NamaMaKul));
				ColomnNamaMakul.setTextColor(Color.BLACK);
				ColomnNamaMakul.setTextSize(14);
				ColomnNamaMakul.setPadding(20, 5, 20, 5);
				ColomnNamaMakul.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnNamaMakul, cellLp); // adding column to row

				// Setting up ColomnNilaiHuruf parameters
				Log.d("Skripsi", "mengambil data colom NilaiHuruf");
				ColomnKodeMAkul = new TextView(getActivity());
				ColomnKodeMAkul.setText(UIN.getString(KodeMaKul));
				ColomnKodeMAkul.setTextColor(Color.BLACK);
				ColomnKodeMAkul.setGravity(Gravity.CENTER);
				ColomnKodeMAkul.setTextSize(14);
				// ColomnNilaiHuruf.setPadding(20, 5, 20, 5);
				ColomnKodeMAkul.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnKodeMAkul, cellLp);

				// Setting up ColomnSKS parameters
				Log.d("Skripsi", "mengambil data colom SKS");
				ColomnSKS = new TextView(getActivity());
				ColomnSKS.setText(UIN.getString(SKS));
				ColomnSKS.setTextColor(Color.BLACK);
				ColomnSKS.setGravity(Gravity.CENTER);
				ColomnSKS.setTextSize(14);
				// ColomnSKS.setPadding(20, 5, 20, 5);
				ColomnSKS.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnSKS, cellLp);

				// Setting up ColomnSemester parameters
				Log.d("Skripsi", "mengambil data colom Semester");
				ColomnSemester = new TextView(getActivity());
				ColomnSemester.setText(UIN.getString(Semester));
				ColomnSemester.setTextColor(Color.BLACK);
				ColomnSemester.setTextSize(14);
				ColomnSemester.setGravity(Gravity.CENTER);
				// ColomnSemester.setPadding(20, 5, 20, 5);
				ColomnSemester.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnSemester, cellLp);

				tableLayout.addView(row, rowLp);
			} while (UIN.moveToNext());
			// db.close();
		} else {
			// Toast.makeText(getActivity().getApplicationContext(),
			// "Event occurred.", Toast.LENGTH_LONG).show();
			Test.setText("Tidak ada matakuliah yang diulang pada semester ganjil");
		}

	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	public void UlangKRSGenap() {

		Cursor UIN = db.rawQuery("SELECT DISTINCT Semester," + "NamaMakul,"
				+ "KodeMakul as blao," + "SKS " + "FROM DataMHS "
				+ "WHERE NilaiHuruf!='' " + "and Semester like '%2' "
				+ "and NilaiHuruf='E' " + "order by KodeMakul desc", null);

		// untuk MakulUIN
		Integer NamaMaKul = UIN.getColumnIndex("NamaMaKul");
		Integer KodeMaKul = UIN.getColumnIndex("blao");
		Integer SKS = UIN.getColumnIndex("SKS");
		// Integer Semester = UIN.getColumnIndex("Semester");

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

		if (UIN.getCount() > 0) {
			// Scanning value field by raw Cursor
			UIN.moveToFirst();
			do {
				row = new TableRow(getActivity());
				row.setId(100);

				// Test.setText(UIN.getString(KodeMaKul)+" "+UIN.getString(NamaMaKul)+" "+UIN.getString(SKS)+" "+UIN.getString(Semester));
				// Setting up the ColomnNamaMaKul parameters
				Log.d("Skripsi", "mengambil data colom NamaMakul");
				ColomnNamaMakul = new TextView(getActivity());
				ColomnNamaMakul.setText(UIN.getString(NamaMaKul));
				ColomnNamaMakul.setTextColor(Color.BLACK);
				ColomnNamaMakul.setTextSize(14);
				ColomnNamaMakul.setPadding(20, 5, 20, 5);
				ColomnNamaMakul.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnNamaMakul, cellLp); // adding column to row

				// Setting up ColomnNilaiHuruf parameters
				Log.d("Skripsi", "mengambil data colom NilaiHuruf");
				ColomnKodeMAkul = new TextView(getActivity());
				ColomnKodeMAkul.setText(UIN.getString(KodeMaKul));
				ColomnKodeMAkul.setTextColor(Color.BLACK);
				ColomnKodeMAkul.setGravity(Gravity.CENTER);
				ColomnKodeMAkul.setTextSize(14);
				// ColomnNilaiHuruf.setPadding(20, 5, 20, 5);
				ColomnKodeMAkul.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnKodeMAkul, cellLp);

				// Setting up ColomnSKS parameters
				Log.d("Skripsi", "mengambil data colom SKS");
				ColomnSKS = new TextView(getActivity());
				ColomnSKS.setText(UIN.getString(SKS));
				ColomnSKS.setTextColor(Color.BLACK);
				ColomnSKS.setGravity(Gravity.CENTER);
				ColomnSKS.setTextSize(14);
				// ColomnSKS.setPadding(20, 5, 20, 5);
				ColomnSKS.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnSKS, cellLp);

				// // Setting up ColomnSemester parameters
				// Log.d("Skripsi", "mengambil data colom Semester");
				// ColomnSemester = new TextView(getActivity());
				// ColomnSemester.setText(UIN.getString(Semester));
				// ColomnSemester.setTextColor(Color.BLACK);
				// ColomnSemester.setTextSize(14);
				// ColomnSemester.setGravity(Gravity.CENTER);
				// // ColomnSemester.setPadding(20, 5, 20, 5);
				// ColomnSemester.setBackgroundResource(R.drawable.garis);
				// row.addView(ColomnSemester,cellLp);

				tableLayout.addView(row, rowLp);
			} while (UIN.moveToNext());
			// db.close();
		} else {
			// Toast.makeText(getActivity().getApplicationContext(),
			// "Event occurred.", Toast.LENGTH_LONG).show();
			Test.setText("Tidak ada matakuliah yang diulang pada semester genap");
		}

	}

	public void AllKRS() {
		String Smt = String.valueOf(Semes+1);
		Cursor AllKRS = db.rawQuery("select KodeMaKul,"
				+ "NamaMakul"
				+ ",SKSTeori + SKSPraktikum as SKS "
				+ ",SifatMakul"
				+ ",PaketSemester "
				+ "from MataKuliah "
				+ "where PaketSemester="+Smt+ " and  SifatMakul='W'", null);

		// untuk MakulUIN
		Integer NamaMaKul = AllKRS.getColumnIndex("NamaMaKul");
		Integer SKS = AllKRS.getColumnIndex("SKS");
		Integer SifatMakul = AllKRS.getColumnIndex("SifatMaKul");
		Integer Semester = AllKRS.getColumnIndex("PaketSemester");

		FrameLayout.LayoutParams lpx = new FrameLayout.LayoutParams(
				TableLayout.LayoutParams.FILL_PARENT,
				TableLayout.LayoutParams.FILL_PARENT);

		tableLayoutCurrent.setLayoutParams(lpx);
		tableLayoutCurrent.setStretchAllColumns(true);
		TableLayout.LayoutParams rowLpx = new TableLayout.LayoutParams(
				TableLayout.LayoutParams.FILL_PARENT,
				TableLayout.LayoutParams.FILL_PARENT, 1.0f);
		TableRow.LayoutParams cellLpx = new TableRow.LayoutParams(
				TableRow.LayoutParams.FILL_PARENT,
				TableRow.LayoutParams.FILL_PARENT, 1.0f);

		if (AllKRS.getCount() > 0) {
			// Scanning value field by raw Cursor
			AllKRS.moveToFirst();
			do {
				rowCurrent = new TableRow(getActivity());
				rowCurrent.setId(200);
				// Setting up the ColomnNamaMaKul parameters
				Log.d("Skripsi", "mengambil data colom NamaMakul");
				ColomnNamaMakulSek = new TextView(getActivity());
				ColomnNamaMakulSek.setText(AllKRS.getString(NamaMaKul));
				ColomnNamaMakulSek.setTextColor(Color.BLACK);
				ColomnNamaMakulSek.setTextSize(14);
				ColomnNamaMakulSek.setPadding(20, 5, 20, 5);
				ColomnNamaMakulSek.setBackgroundResource(R.drawable.garis);
				rowCurrent.addView(ColomnNamaMakulSek, cellLpx); // adding column to row

				
				// Setting up ColomnSKS parameters
				Log.d("Skripsi", "mengambil data colom SKS");
				ColomnSKSSek = new TextView(getActivity());
				ColomnSKSSek.setText(AllKRS.getString(SKS));
				ColomnSKSSek.setTextColor(Color.BLACK);
				ColomnSKSSek.setGravity(Gravity.CENTER);
				ColomnSKSSek.setTextSize(14);
				// ColomnSKS.setPadding(20, 5, 20, 5);
				ColomnSKSSek.setBackgroundResource(R.drawable.garis);
				rowCurrent.addView(ColomnSKSSek, cellLpx);
				
				// Setting up ColomnNilaiHuruf parameters
				Log.d("Skripsi", "mengambil data colom NilaiHuruf");
				ColomnSifatMAkulSek = new TextView(getActivity());
				ColomnSifatMAkulSek.setText(AllKRS.getString(SifatMakul));
				ColomnSifatMAkulSek.setTextColor(Color.BLACK);
				ColomnSifatMAkulSek.setGravity(Gravity.CENTER);
				ColomnSifatMAkulSek.setTextSize(14);
				// ColomnNilaiHuruf.setPadding(20, 5, 20, 5);
				ColomnSifatMAkulSek.setBackgroundResource(R.drawable.garis);
				rowCurrent.addView(ColomnSifatMAkulSek, cellLpx);

				

				// Setting up ColomnSemester parameters
				Log.d("Skripsi", "mengambil data colom Semester");
				ColomnSemesterSek = new TextView(getActivity());
				ColomnSemesterSek.setText(AllKRS.getString(Semester));
				ColomnSemesterSek.setTextColor(Color.BLACK);
				ColomnSemesterSek.setTextSize(14);
				ColomnSemesterSek.setGravity(Gravity.CENTER);
				// ColomnSemester.setPadding(20, 5, 20, 5);
				ColomnSemesterSek.setBackgroundResource(R.drawable.garis);
				rowCurrent.addView(ColomnSemesterSek, cellLpx);

				tableLayoutCurrent.addView(rowCurrent, rowLpx);
			} while (AllKRS.moveToNext());
			// db.close();
		} else {
			// Toast.makeText(getActivity().getApplicationContext(),
			// "Event occurred.", Toast.LENGTH_LONG).show();
			Test.setText(Smt);
		}

	}
}