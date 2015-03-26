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
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
	TableRow row, rowCurrent, rowHeader1, rowHeader2;
	TextView ColomnNamaMakul, ColomnNamaMakulSekW,ColomnNamaMakulSekP;
	TextView ColomnKodeMAkul, ColomnSifatMAkulSekW,ColomnSifatMAkulSekP;
	TextView ColomnSemester, ColomnSemesterSekW,ColomnSemesterSekP;
	TextView ColomnSKS, ColomnSKSSekW,ColomnSKSSekP, ColomnNilaiHuruf;
	TextView Notif1, Notif2, TotSKSLalu, TotSKSSekar;
	Double NilaiIPKx;
	Integer JumSKSx, SMTx;
	Integer Semes, CurSemester;
	String QueryKRS, cek;

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

		Notif1 = (TextView) v.findViewById(R.id.Notif1);
		Notif2 = (TextView) v.findViewById(R.id.Notif2);
		TotSKSLalu = (TextView) v.findViewById(R.id.TotSKSLalu);
		TotSKSSekar = (TextView) v.findViewById(R.id.TotSKSSek);

		// tableSemesterLalu = (TableLayout) v.findViewById(R.id.semesterLalu);
		tableLayout = (TableLayout) v.findViewById(R.id.semesterLalu);
		tableLayoutCurrent = (TableLayout) v
				.findViewById(R.id.semesterSekarang);
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
				Notif1.setText("Rekomendasi Matakuliah yang diprogramkan ulang pada Semester Ganjil");
				UlangKRSGanjil();
				Notif2.setText("Rekomendasi Matakuliah yang diprogramkan pada Semester Ganjil");
				AllKRSWajib();

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
				Notif1.setText("Rekomendasi Matakuliah yang diprogramkan ulang pada Semester Genap");
				UlangKRSGenap();
				Notif2.setText("Rekomendasi Matakuliah yang diprogramkan pada Semester Genap");

				AllKRSWajib();
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
				+ "KodeMakul as blao," + "NilaiHuruf,SKS " + "FROM DataMHS "
				+ "WHERE NilaiHuruf!='' " + "and Semester like '%1' "
				+ "and NilaiHuruf='E' " + "order by KodeMakul desc", null);

		Cursor UINTotSKS = db.rawQuery("SELECT SUM(SKS) as TotSKS"
				+ " FROM DataMHS " + "WHERE NilaiHuruf!='' "
				+ "and Semester like '%1' " + "and NilaiHuruf='E' "
				+ "order by KodeMakul desc", null);

		// untuk MakulUIN
		Integer NamaMaKul = UIN.getColumnIndex("NamaMaKul");
		Integer KodeMaKul = UIN.getColumnIndex("blao");
		Integer SKS = UIN.getColumnIndex("SKS");
		Integer Semester = UIN.getColumnIndex("Semester");
		Integer NilaiHuruf = UIN.getColumnIndex("NilaiHuruf");

		Integer TotSKS = UINTotSKS.getColumnIndex("TotSKS");
		if (UINTotSKS.getCount() > 0) {
			UINTotSKS.moveToFirst();
			do {
				cek = UINTotSKS.getString(TotSKS);
			} while (UINTotSKS.moveToNext());
			if (cek==null) {
				TotSKSLalu.setVisibility(View.GONE);
			} else {
				TotSKSLalu.setText("Total SKS : " + cek);
			}
		}

		// FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
		// TableLayout.LayoutParams.FILL_PARENT,
		// TableLayout.LayoutParams.FILL_PARENT);
		//
		// tableLayout.setLayoutParams(lp);
		tableLayout.setStretchAllColumns(true);
		TableLayout.LayoutParams rowLp = new TableLayout.LayoutParams(
				TableLayout.LayoutParams.FILL_PARENT,
				TableLayout.LayoutParams.FILL_PARENT, 1.0f);
		TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
				TableRow.LayoutParams.FILL_PARENT,
				TableRow.LayoutParams.FILL_PARENT, 1.0f);

		if (UIN.getCount() > 0) {
			// Setting Up Header table
			rowHeader1 = new TableRow(getActivity());
			rowHeader1.setId(100);

			// Setting up the ColomnNamaMaKul parameters
			Log.d("Skripsi", "mengambil data colom NamaMakul");
			ColomnNamaMakul = new TextView(getActivity());
			ColomnNamaMakul.setText("Nama Matakuliah");
			ColomnNamaMakul.setTextColor(Color.BLACK);
			ColomnNamaMakul.setTextSize(14);
			ColomnNamaMakul.setGravity(Gravity.CENTER);
			ColomnNamaMakul.setBackgroundResource(R.drawable.garis);
			rowHeader1.addView(ColomnNamaMakul, cellLp); // adding column to row

			// Setting up ColomnNilaiHuruf parameters
			Log.d("Skripsi", "mengambil data colom NilaiHuruf");
			ColomnKodeMAkul = new TextView(getActivity());
			ColomnKodeMAkul.setText("Kode Matakuliah");
			ColomnKodeMAkul.setTextColor(Color.BLACK);
			ColomnKodeMAkul.setGravity(Gravity.CENTER);
			ColomnKodeMAkul.setTextSize(14);
			// ColomnNilaiHuruf.setPadding(20, 5, 20, 5);
			ColomnKodeMAkul.setBackgroundResource(R.drawable.garis);
			rowHeader1.addView(ColomnKodeMAkul, cellLp);

			// Setting up ColomnSKS parameters
			Log.d("Skripsi", "mengambil data colom SKS");
			ColomnSKS = new TextView(getActivity());
			ColomnSKS.setText("SKS");
			ColomnSKS.setTextColor(Color.BLACK);
			ColomnSKS.setGravity(Gravity.CENTER);
			ColomnSKS.setTextSize(14);
			// ColomnSKS.setPadding(20, 5, 20, 5);
			ColomnSKS.setBackgroundResource(R.drawable.garis);
			rowHeader1.addView(ColomnSKS, cellLp);

			// Setting up ColomnSKS parameters

			Log.d("Skripsi", "mengambil data colom Semester");
			ColomnNilaiHuruf = new TextView(getActivity());
			ColomnNilaiHuruf.setText("Nilai Huruf");
			ColomnNilaiHuruf.setTextColor(Color.BLACK);
			ColomnNilaiHuruf.setTextSize(14);
			ColomnNilaiHuruf.setGravity(Gravity.CENTER);
			// ColomnSemester.setPadding(20, 5, 20, 5);
			ColomnNilaiHuruf.setBackgroundResource(R.drawable.garis);
			rowHeader1.addView(ColomnNilaiHuruf, cellLp);

			// Setting up ColomnSemester parameters
			Log.d("Skripsi", "mengambil data colom Semester");
			ColomnSemester = new TextView(getActivity());
			ColomnSemester.setText("Semester");
			ColomnSemester.setTextColor(Color.BLACK);
			ColomnSemester.setTextSize(14);
			ColomnSemester.setGravity(Gravity.CENTER);
			// ColomnSemester.setPadding(20, 5, 20, 5);
			ColomnSemester.setBackgroundResource(R.drawable.garis);
			rowHeader1.addView(ColomnSemester, cellLp);
			tableLayout.addView(rowHeader1, rowLp);

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

				Log.d("Skripsi", "mengambil data colom Semester");
				ColomnNilaiHuruf = new TextView(getActivity());
				ColomnNilaiHuruf.setText(UIN.getString(NilaiHuruf));
				ColomnNilaiHuruf.setTextColor(Color.BLACK);
				ColomnNilaiHuruf.setTextSize(14);
				ColomnNilaiHuruf.setGravity(Gravity.CENTER);
				// ColomnSemester.setPadding(20, 5, 20, 5);
				ColomnNilaiHuruf.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnNilaiHuruf, cellLp);

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
			Notif1.setText("Tidak ada rekomendasi matakuliah yang diprogramkan ulang pada semester ganjil");
		}

	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	public void UlangKRSGenap() {

		Cursor UIN = db.rawQuery("SELECT DISTINCT Semester," + "NamaMakul,"
				+ "KodeMakul as blao," + "NilaiHuruf,SKS " + "FROM DataMHS "
				+ "WHERE NilaiHuruf!='' " + "and Semester like '%2' "
				+ "and NilaiHuruf='E' " + "order by KodeMakul desc", null);

		Cursor UINTotSKS = db.rawQuery("SELECT SUM(SKS) as TotSKS"
				+ " FROM DataMHS " + "WHERE NilaiHuruf!='' "
				+ "and Semester like '%2' " + "and NilaiHuruf='E' "
				+ "order by KodeMakul desc", null);

		// untuk MakulUIN
		Integer NamaMaKul = UIN.getColumnIndex("NamaMaKul");
		Integer KodeMaKul = UIN.getColumnIndex("blao");
		Integer SKS = UIN.getColumnIndex("SKS");
		Integer NilaiHuruf = UIN.getColumnIndex("NilaiHuruf");
		Integer SemesterZ = UIN.getColumnIndex("Semester");

		Integer TotSKS = UINTotSKS.getColumnIndex("TotSKS");

		if (UINTotSKS.getCount() > 0) {
			UINTotSKS.moveToFirst();
			do {
				cek = UINTotSKS.getString(TotSKS);
			} while (UINTotSKS.moveToNext());
			if (cek==null) {
				TotSKSLalu.setVisibility(View.GONE);
			} else {
				TotSKSLalu.setText("Total SKS : " + cek);
			}
		}

		// FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
		// TableLayout.LayoutParams.FILL_PARENT,
		// TableLayout.LayoutParams.FILL_PARENT);
		//
		// tableLayout.setLayoutParams(lp);
		tableLayout.setStretchAllColumns(true);
		TableLayout.LayoutParams rowLp = new TableLayout.LayoutParams(
				TableLayout.LayoutParams.FILL_PARENT,
				TableLayout.LayoutParams.FILL_PARENT, 1.0f);
		TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
				TableRow.LayoutParams.FILL_PARENT,
				TableRow.LayoutParams.FILL_PARENT, 1.0f);

		if (UIN.getCount() > 0) {
			// Setting Up Header table
			rowHeader1 = new TableRow(getActivity());
			rowHeader1.setId(100);

			// Setting up the ColomnNamaMaKul parameters
			Log.d("Skripsi", "mengambil data colom NamaMakul");
			ColomnNamaMakul = new TextView(getActivity());
			ColomnNamaMakul.setText("Nama Matakuliah");
			ColomnNamaMakul.setTextColor(Color.BLACK);
			ColomnNamaMakul.setTextSize(14);
			ColomnNamaMakul.setGravity(Gravity.CENTER);
			ColomnNamaMakul.setBackgroundResource(R.drawable.garis);
			rowHeader1.addView(ColomnNamaMakul, cellLp); // adding column to row

			// Setting up ColomnNilaiHuruf parameters
			Log.d("Skripsi", "mengambil data colom NilaiHuruf");
			ColomnKodeMAkul = new TextView(getActivity());
			ColomnKodeMAkul.setText("Kode Matakuliah");
			ColomnKodeMAkul.setTextColor(Color.BLACK);
			ColomnKodeMAkul.setGravity(Gravity.CENTER);
			ColomnKodeMAkul.setTextSize(14);
			// ColomnNilaiHuruf.setPadding(20, 5, 20, 5);
			ColomnKodeMAkul.setBackgroundResource(R.drawable.garis);
			rowHeader1.addView(ColomnKodeMAkul, cellLp);

			// Setting up ColomnSKS parameters
			Log.d("Skripsi", "mengambil data colom SKS");
			ColomnSKS = new TextView(getActivity());
			ColomnSKS.setText("SKS");
			ColomnSKS.setTextColor(Color.BLACK);
			ColomnSKS.setGravity(Gravity.CENTER);
			ColomnSKS.setTextSize(14);
			// ColomnSKS.setPadding(20, 5, 20, 5);
			ColomnSKS.setBackgroundResource(R.drawable.garis);
			rowHeader1.addView(ColomnSKS, cellLp);

			// Setting up ColomnSKS parameters

			Log.d("Skripsi", "mengambil data colom Semester");
			ColomnNilaiHuruf = new TextView(getActivity());
			ColomnNilaiHuruf.setText("Nilai Huruf");
			ColomnNilaiHuruf.setTextColor(Color.BLACK);
			ColomnNilaiHuruf.setTextSize(14);
			ColomnNilaiHuruf.setGravity(Gravity.CENTER);
			// ColomnSemester.setPadding(20, 5, 20, 5);
			ColomnNilaiHuruf.setBackgroundResource(R.drawable.garis);
			rowHeader1.addView(ColomnNilaiHuruf, cellLp);

			// Setting up ColomnSemester parameters
			Log.d("Skripsi", "mengambil data colom Semester");
			ColomnSemester = new TextView(getActivity());
			ColomnSemester.setText("Semester");
			ColomnSemester.setTextColor(Color.BLACK);
			ColomnSemester.setTextSize(14);
			ColomnSemester.setGravity(Gravity.CENTER);
			// ColomnSemester.setPadding(20, 5, 20, 5);
			ColomnSemester.setBackgroundResource(R.drawable.garis);
			rowHeader1.addView(ColomnSemester, cellLp);
			tableLayout.addView(rowHeader1, rowLp);

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

				// Setting up ColomnSKS parameters

				Log.d("Skripsi", "mengambil data colom Semester");
				ColomnNilaiHuruf = new TextView(getActivity());
				ColomnNilaiHuruf.setText(UIN.getString(NilaiHuruf));
				ColomnNilaiHuruf.setTextColor(Color.BLACK);
				ColomnNilaiHuruf.setTextSize(14);
				ColomnNilaiHuruf.setGravity(Gravity.CENTER);
				// ColomnSemester.setPadding(20, 5, 20, 5);
				ColomnNilaiHuruf.setBackgroundResource(R.drawable.garis);
				row.addView(ColomnNilaiHuruf, cellLp);

				// Setting up ColomnSemester parameters
				Log.d("Skripsi", "mengambil data colom Semester");
				ColomnSemester = new TextView(getActivity());
				ColomnSemester.setText(UIN.getString(SemesterZ));
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
			Notif1.setText("Tidak ada rekomendasi matakuliah yang diprogramkan ulang pada semester genap");
		}

	}

	public void AllKRSWajib() {
		Log.d("Skripsix", "query ke dataMHS");
		String query = "SELECT "
				+ "Nama,"
				+ "Nim,"
				+ "NilaiHuruf,"
				+ "SKS,"
				+ "SUM(SKS) as JumSKS,"
				+ "count(distinct Semester)+1 as Semester,"
				+ "SUM(CASE WHEN NilaiHuruf= 'A' THEN 4*SKS "
				+ "WHEN NilaiHuruf= 'B' THEN 3*SKS "
				+ "WHEN NilaiHuruf= 'C' THEN 2*SKS "
				+ "WHEN NilaiHuruf= 'D' THEN 1*SKS ELSE 0*SKS END)*1.0/SUM(SKS)*1.0 "
				+ "AS IPK " + "FROM DataMHS WHERE NilaiHuruf!=''";

		Cursor a = db.rawQuery(query, null);
		Integer JumSKS = a.getColumnIndex("JumSKS");
		Integer IPK = a.getColumnIndex("IPK");
		Integer Semester = a.getColumnIndex("Semester");

		if (a.getCount() > 0) {
			// Scanning value field by raw Cursor
			a.moveToFirst();
			do {
				NilaiIPKx = Double.valueOf(new DecimalFormat("#.##")
						.format(IPK));

				JumSKSx = Integer.valueOf(a.getString(JumSKS));

				SMTx = Integer.valueOf(a.getString(Semester));

			} while (a.moveToNext());
			if (SMTx == 7 || SMTx == 8) {

				// seleksi PPL
				if (JumSKSx >= 100) {
					QueryKRS = "select KodeMaKul," + "NamaMakul"
							+ ",SKSTeori + SKSPraktikum as SKS "
							+ ",SifatMakul" + ",PaketSemester "
							+ "from MataKuliah "
							+ "where (PaketSemester='7' or PaketSemester='8')"
							+ " and  SifatMakul='W' and JKurikulum='B'";
				} else {
					QueryKRS = "select KodeMaKul,"
							+ "NamaMakul"
							+ ",SKSTeori + SKSPraktikum as SKS "
							+ ",SifatMakul"
							+ ",PaketSemester "
							+ "from MataKuliah "
							+ "where (PaketSemester='7' or PaketSemester='8')"
							+ " and  SifatMakul='W' and JKurikulum='B' and KodeMaKul!='TIN4259'";
				}

				// seleksi KKN
				if ((NilaiIPKx >= 3.00 && JumSKSx >= 105)
						|| (NilaiIPKx < 3.00 && JumSKSx >= 110)) {
					QueryKRS = "select KodeMaKul," + "NamaMakul"
							+ ",SKSTeori + SKSPraktikum as SKS "
							+ ",SifatMakul" + ",PaketSemester "
							+ "from MataKuliah "
							+ "where (PaketSemester='7' or PaketSemester='8')"
							+ " and  SifatMakul='W' and JKurikulum='B'";
				} else {
					QueryKRS = "select KodeMaKul,"
							+ "NamaMakul"
							+ ",SKSTeori + SKSPraktikum as SKS "
							+ ",SifatMakul"
							+ ",PaketSemester "
							+ "from MataKuliah "
							+ "where (PaketSemester='7' or PaketSemester='8')"
							+ SMTx
							+ " and  SifatMakul='W' and JKurikulum='B' and KodeMaKul!='UIN4411'";
				}

				// seleksi SKRIPSI
				if (JumSKSx >= 130) {
					QueryKRS = "select KodeMaKul," + "NamaMakul"
							+ ",SKSTeori + SKSPraktikum as SKS "
							+ ",SifatMakul" + ",PaketSemester "
							+ "from MataKuliah "
							+ "where (PaketSemester='7' or PaketSemester='8')"
							+ " and  SifatMakul='W' and JKurikulum='B'";
				} else {
					QueryKRS = "select KodeMaKul,"
							+ "NamaMakul"
							+ ",SKSTeori + SKSPraktikum as SKS "
							+ ",SifatMakul"
							+ ",PaketSemester "
							+ "from MataKuliah "
							+ "where (PaketSemester='7' or PaketSemester='8')"
							+ " and  SifatMakul='W' and JKurikulum='B' and KodeMaKul!='TIN4461' and KodeMaKul!='TIN4461'";
				}

			} else {
				QueryKRS = "select KodeMaKul," + "NamaMakul"
						+ ",SKSTeori + SKSPraktikum as SKS " + ",SifatMakul"
						+ ",PaketSemester " + "from MataKuliah "
						+ "where PaketSemester=" + SMTx
						+ " and  SifatMakul='W' and JKurikulum='B'";
			}

			String TotQuery = "select sum(SKSTeori + SKSPraktikum) as TotSKS "
					+ "from MataKuliah " + "where PaketSemester=" + SMTx
					+ " and  SifatMakul='W' and JKurikulum='B'";
			Cursor AllKRS = db.rawQuery(QueryKRS, null);

			Cursor TotSKSSekarang = db.rawQuery(TotQuery, null);

			// untuk MakulUIN
			Integer NamaMaKul = AllKRS.getColumnIndex("NamaMaKul");
			Integer SKS = AllKRS.getColumnIndex("SKS");
			Integer SifatMakul = AllKRS.getColumnIndex("SifatMaKul");
			Integer SemesterJ = AllKRS.getColumnIndex("PaketSemester");

			Integer TotSKSSek = TotSKSSekarang.getColumnIndex("TotSKS");
			
			if (TotSKSSekarang.getCount() > 0) {
				TotSKSSekarang.moveToFirst();
				do {
					cek = TotSKSSekarang.getString(TotSKSSek);
				} while (TotSKSSekarang.moveToNext());
				if (cek==null) {
					TotSKSSekar.setVisibility(View.GONE);
				} else {
					TotSKSSekar.setText("Total SKS : " + cek);
				}
			}

			// FrameLayout.LayoutParams lpx = new FrameLayout.LayoutParams(
			// TableLayout.LayoutParams.FILL_PARENT,
			// TableLayout.LayoutParams.FILL_PARENT);
			//
			// tableLayoutCurrent.setLayoutParams(lpx);
			tableLayoutCurrent.setStretchAllColumns(true);
			TableLayout.LayoutParams rowLpx = new TableLayout.LayoutParams(
					TableLayout.LayoutParams.FILL_PARENT,
					TableLayout.LayoutParams.FILL_PARENT, 1.0f);
			TableRow.LayoutParams cellLpx = new TableRow.LayoutParams(
					TableRow.LayoutParams.FILL_PARENT,
					TableRow.LayoutParams.FILL_PARENT, 1.0f);

			if (AllKRS.getCount() > 0) {
				// Setting Up Header table
				rowHeader2 = new TableRow(getActivity());
				rowHeader2.setId(200);

				Log.d("Skripsi", "mengambil data colom NamaMakul");
				ColomnNamaMakulSekW = new TextView(getActivity());
				ColomnNamaMakulSekW.setText("Nama Matakuliah");
				ColomnNamaMakulSekW.setTextColor(Color.BLACK);
				ColomnNamaMakulSekW.setTextSize(14);
				ColomnNamaMakulSekW.setGravity(Gravity.CENTER);
				ColomnNamaMakulSekW.setBackgroundResource(R.drawable.garis);
				rowHeader2.addView(ColomnNamaMakulSekW, cellLpx);

				// Setting up ColomnSKS parameters
				Log.d("Skripsi", "mengambil data colom SKS");
				ColomnSKSSekW = new TextView(getActivity());
				ColomnSKSSekW.setText("SKS");
				ColomnSKSSekW.setTextColor(Color.BLACK);
				ColomnSKSSekW.setGravity(Gravity.CENTER);
				ColomnSKSSekW.setTextSize(14);
				// ColomnSKS.setPadding(20, 5, 20, 5);
				ColomnSKSSekW.setBackgroundResource(R.drawable.garis);
				rowHeader2.addView(ColomnSKSSekW, cellLpx);

				// Setting up ColomnNilaiHuruf parameters
				Log.d("Skripsi", "mengambil data colom NilaiHuruf");
				ColomnSifatMAkulSekW = new TextView(getActivity());
				ColomnSifatMAkulSekW.setText("Sifat Matakuliah");
				ColomnSifatMAkulSekW.setTextColor(Color.BLACK);
				ColomnSifatMAkulSekW.setGravity(Gravity.CENTER);
				ColomnSifatMAkulSekW.setTextSize(14);
				// ColomnNilaiHuruf.setPadding(20, 5, 20, 5);
				ColomnSifatMAkulSekW.setBackgroundResource(R.drawable.garis);
				rowHeader2.addView(ColomnSifatMAkulSekW, cellLpx);

				// Setting up ColomnSemester parameters
				Log.d("Skripsi", "mengambil data colom Semester");
				ColomnSemesterSekW = new TextView(getActivity());
				ColomnSemesterSekW.setText("Semester");
				ColomnSemesterSekW.setTextColor(Color.BLACK);
				ColomnSemesterSekW.setTextSize(14);
				ColomnSemesterSekW.setGravity(Gravity.CENTER);
				// ColomnSemester.setPadding(20, 5, 20, 5);
				ColomnSemesterSekW.setBackgroundResource(R.drawable.garis);
				rowHeader2.addView(ColomnSemesterSekW, cellLpx);
				tableLayoutCurrent.addView(rowHeader2, rowLpx);

				AllKRS.moveToFirst();
				do {
					rowCurrent = new TableRow(getActivity());
					rowCurrent.setId(200);
					// Setting up the ColomnNamaMaKul parameters
					Log.d("Skripsi", "mengambil data colom NamaMakul");
					ColomnNamaMakulSekW = new TextView(getActivity());
					ColomnNamaMakulSekW.setText(AllKRS.getString(NamaMaKul));
					ColomnNamaMakulSekW.setTextColor(Color.BLACK);
					ColomnNamaMakulSekW.setTextSize(14);
					ColomnNamaMakulSekW.setBackgroundResource(R.drawable.garis);
					rowCurrent.addView(ColomnNamaMakulSekW, cellLpx);

					// Setting up ColomnSKS parameters
					Log.d("Skripsi", "mengambil data colom SKS");
					ColomnSKSSekW = new TextView(getActivity());
					ColomnSKSSekW.setText(AllKRS.getString(SKS));
					ColomnSKSSekW.setTextColor(Color.BLACK);
					ColomnSKSSekW.setGravity(Gravity.CENTER);
					ColomnSKSSekW.setTextSize(14);
					// ColomnSKS.setPadding(20, 5, 20, 5);
					ColomnSKSSekW.setBackgroundResource(R.drawable.garis);
					rowCurrent.addView(ColomnSKSSekW, cellLpx);

					// Setting up ColomnNilaiHuruf parameters
					Log.d("Skripsi", "mengambil data colom NilaiHuruf");
					ColomnSifatMAkulSekW = new TextView(getActivity());
					ColomnSifatMAkulSekW.setText(AllKRS.getString(SifatMakul));
					ColomnSifatMAkulSekW.setTextColor(Color.BLACK);
					ColomnSifatMAkulSekW.setGravity(Gravity.CENTER);
					ColomnSifatMAkulSekW.setTextSize(14);
					// ColomnNilaiHuruf.setPadding(20, 5, 20, 5);
					ColomnSifatMAkulSekW.setBackgroundResource(R.drawable.garis);
					rowCurrent.addView(ColomnSifatMAkulSekW, cellLpx);

					// Setting up ColomnSemester parameters
					Log.d("Skripsi", "mengambil data colom Semester");
					ColomnSemesterSekW = new TextView(getActivity());
					ColomnSemesterSekW.setText(AllKRS.getString(SemesterJ));
					ColomnSemesterSekW.setTextColor(Color.BLACK);
					ColomnSemesterSekW.setTextSize(14);
					ColomnSemesterSekW.setGravity(Gravity.CENTER);
					// ColomnSemester.setPadding(20, 5, 20, 5);
					ColomnSemesterSekW.setBackgroundResource(R.drawable.garis);
					rowCurrent.addView(ColomnSemesterSekW, cellLpx);

					tableLayoutCurrent.addView(rowCurrent, rowLpx);
				} while (AllKRS.moveToNext());
				// db.close();
			} else {
				// Toast.makeText(getActivity().getApplicationContext(),
				// "Event occurred.", Toast.LENGTH_LONG).show();
				Notif2.setText("Tidak ada rekomendasi matakuliah yang perlu diprogramkan");
			}
		}
	}
}