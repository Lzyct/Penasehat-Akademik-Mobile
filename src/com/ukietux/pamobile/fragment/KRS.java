package com.ukietux.pamobile.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.TableLayout;
import android.widget.TableRow;

@SuppressLint({ "InlinedApi", "ResourceAsColor" })
public class KRS extends Fragment {
	SQLiteDatabase db;
	String Semester, LastIPS, GenapGanjil;

	int MaxSKS, totalSKSWajib, totalSKSPilihan, Total1, Total2, Total1WSetara,
			Total2WSetara, Total1PSetara, Total2PSetara, TotalSKSUlang,
			TotalSKSWSetara, TotalSKSPSetara, SMT;

	CustomTextView IPSTerakhir, Semesterx, ListSemester, ListSemesterGanjil,
			ListSemesterGenap, MaksimumSKS;
	List<String> listSemester = new ArrayList<String>();
	List<String> listSemesterGanjil = new ArrayList<String>();
	List<String> listSemesterGenap = new ArrayList<String>();

	TableLayout tableLayout, tableLayoutCurrent, tableLayoutCurrentSetara,
			tableLayoutCurrentPilihan, tableLayoutCurrentPilihanSetara,
			tableLayoutTerkait;
	TableRow row, rowCurrent, rowPilihan, rowTerkait;
	TableRow rowHeader1, rowHeader2, rowHeader3, rowHeaderWSetara,
			rowHeaderMakulTerkait;

	CustomTextView ColomnNamaMakul, ColomnKodeMAkul, ColomnSemester, ColomnSKS,
			ColomnNilaiHuruf, ColomnSifatMakul;
	CustomTextView ColomnNamaMakulSekW, ColomnSifatMAkulSekW,
			ColomnSemesterSekW, ColomnSKSSekW;
	CustomTextView ColomnNamaMakulSekP, ColomnSifatMAkulSekP,
			ColomnSemesterSekP, ColomnSKSSekP;
	CustomTextView ColomnNamaMakulTerkait, ColomnSifatMAkulTerkait,
			ColomnSemesterTerkait, ColomnSKSTerkait;

	CustomTextView Notif1, Notif2, Notif3, Notif4, Notif2Setara, Notif3Setara;
	CustomTextView TotSKSLalu, TotSKSSekar, TotSKSSekarP, TotSKSTerkait;

	Double NilaiIPKx;
	Integer JumSKSx, SMTx;
	Integer Semes, CurSemester;
	String QueryKRS, QueryKRSPilihan, cek;
	String QueryTerkaitCek, QueryTerkait;
	String SmT;

	Cursor CursorCek, CursorTerkait;

	TableLayout.LayoutParams rowLp = new TableLayout.LayoutParams(
			TableLayout.LayoutParams.FILL_PARENT,
			TableLayout.LayoutParams.FILL_PARENT, 1.0f);
	TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
			TableRow.LayoutParams.FILL_PARENT,
			TableRow.LayoutParams.FILL_PARENT, 1.0f);

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.krs, container, false);
		DBController controler = new DBController(getActivity());
		db = controler.getWritableDatabase();

		IPSTerakhir = (CustomTextView) v.findViewById(R.id.ips);
		Semesterx = (CustomTextView) v.findViewById(R.id.semester);
		MaksimumSKS = (CustomTextView) v.findViewById(R.id.MaksimumSKS);

		Notif1 = (CustomTextView) v.findViewById(R.id.Notif1);
		Notif2 = (CustomTextView) v.findViewById(R.id.Notif2);
		Notif3 = (CustomTextView) v.findViewById(R.id.Notif3);
		Notif4 = (CustomTextView) v.findViewById(R.id.Notif4);
		Notif2Setara = (CustomTextView) v.findViewById(R.id.Notif2Setara);
		Notif3Setara = (CustomTextView) v.findViewById(R.id.Notif3Setara);

		Notif1.setBackgroundResource(R.drawable.tv_bg);
		Notif1.setTextColor(Color.WHITE);
		Notif1.setTextSize(14);

		Notif2.setBackgroundResource(R.drawable.tv_bg);
		Notif2.setTextColor(Color.WHITE);
		Notif2.setTextSize(14);

		Notif3.setBackgroundResource(R.drawable.tv_bg);
		Notif3.setTextColor(Color.WHITE);
		Notif3.setTextSize(14);

		Notif4.setBackgroundResource(R.drawable.tv_bg);
		Notif4.setTextColor(Color.WHITE);
		Notif4.setTextSize(14);

		Notif2Setara.setBackgroundResource(R.drawable.tv_bg);
		Notif2Setara.setTextColor(Color.WHITE);
		Notif2Setara.setTextSize(14);

		Notif3Setara.setBackgroundResource(R.drawable.tv_bg);
		Notif3Setara.setTextColor(Color.WHITE);
		Notif3Setara.setTextSize(14);

		TotSKSLalu = (CustomTextView) v.findViewById(R.id.TotSKSLalu);
		TotSKSLalu.setBackgroundResource(R.drawable.edt_bg);
		TotSKSLalu.setTextSize(14);

		TotSKSSekar = (CustomTextView) v.findViewById(R.id.TotSKSSek);
		TotSKSSekar.setBackgroundResource(R.drawable.edt_bg);
		TotSKSSekar.setTextSize(14);

		TotSKSSekarP = (CustomTextView) v.findViewById(R.id.TotSKSSekP);
		TotSKSSekarP.setBackgroundResource(R.drawable.edt_bg);
		TotSKSSekarP.setTextSize(14);

		tableLayoutTerkait = (TableLayout) v
				.findViewById(R.id.semesterSekarangTerkait);
		tableLayout = (TableLayout) v.findViewById(R.id.semesterLalu);
		tableLayoutCurrentSetara = (TableLayout) v
				.findViewById(R.id.semesterSekarangSetara);
		tableLayoutCurrentPilihanSetara = (TableLayout) v
				.findViewById(R.id.semesterSekarangPilihanSetara);
		tableLayoutCurrent = (TableLayout) v
				.findViewById(R.id.semesterSekarang);
		tableLayoutCurrentPilihan = (TableLayout) v
				.findViewById(R.id.semesterSekarangPilihan);

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
				+ "AS IPK " + "FROM TRANSKIP WHERE NilaiHuruf!=''";

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
		}

		getLastSemester();
		getLastIPS();
		CekKRS();
		return v;
	}

	/*
	 * Mendapatkan semester terakhir
	 */
	public void getLastSemester() {
		// get semester
		Cursor allrows = db
				.rawQuery(
						"SELECT DISTINCT Semester from TRANSKIP where NilaiHuruf!='' ORDER BY Semester ",
						null);
		System.out.println("COUNT : " + allrows.getCount());

		allrows.moveToLast();
		Semester = allrows.getString(0);
	}

	/*
	 * Mendapatkan IPS Terakhir
	 */

	public void getLastIPS() {
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

				Log.d("Skripsi", "mengambil data colom Nama");
				Double IPSx = Double.valueOf(a.getString(IPS));

				IPSTerakhir.setText("IPS Semester Lalu : "
						+ new DecimalFormat("#.##").format(IPSx));
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

	/*
	 * Cek KRS
	 */

	@SuppressLint({ "InlinedApi", "NewApi" })
	private void CekKRS() {
		Log.d("Skripsix", "query ke TRANSKIP");
		String query = "SELECT DISTINCT Semester as TotSmt " + "FROM TRANSKIP "
				+ "WHERE NilaiHuruf!='' " + "order by TotSmt";
		String ganjil = "SELECT DISTINCT Semester as TotSmt "
				+ "FROM TRANSKIP "
				+ "WHERE NilaiHuruf!='' and Semester like '%1' "
				+ "order by TotSmt";
		String genap = "SELECT DISTINCT Semester as TotSmt "
				+ "FROM TRANSKIP WHERE NilaiHuruf!='' "
				+ "and Semester like '%2' " + "order by TotSmt";
		String getSmt = "SELECT Count(DISTINCT Semester) as TotSmt FROM TRANSKIP WHERE NilaiHuruf!='' ";
		Cursor all = db.rawQuery(query, null);
		Cursor CGanjil = db.rawQuery(ganjil, null);
		Cursor CGenap = db.rawQuery(genap, null);
		Cursor getSemester = db.rawQuery(getSmt, null);
		Integer Smtr = all.getColumnIndex("TotSmt");
		Integer SmtrGanjil = CGanjil.getColumnIndex("TotSmt");
		Integer SmtrGenap = CGenap.getColumnIndex("TotSmt");
		Integer SemesterF = getSemester.getColumnIndex("TotSmt");

		Notif4.setText("Anda tidak dapat memprogramkan mata kuliah dibawah dikarenakan Anda tidak lulus / \n"
				+ "tidak memprogramkan matakuliah yang berkaitan dengan matakuliah tersebut\n");
		if (all.getCount() > 0) {
			// Scanning value field by raw Cursor
			all.moveToFirst();
			do {
				StringBuilder allSemester = new StringBuilder();
				listSemester.add(all.getString(Smtr));
			} while (all.moveToNext());

			Semes = listSemester.size();
			if (Semes % 2 == 0) {
				Semesterx.setText("Semester " + Semes + " (Genap)");

				Notif1.setText("Rekomendasi Matakuliah yang diprogramkan ulang pada"
						+ "\n" + "Semester Ganjil");

				Notif2.setText("Rekomendasi Matakuliah Wajib yang diprogramkan pada "
						+ "\n" + "Semester Ganjil");

				Notif2Setara
						.setText("Rekomendasi Matakuliah Wajib yang telah diprogramkan dan \n"
								+ "di konversi ke kurikulum baru"
								+ "pada Semester Ganjil");

				Notif3.setText("Rekomendasi Matakuliah Pilihan yang diprogramkan pada "
						+ "\n" + "Semester Ganjil");

				Notif3Setara
						.setText("Rekomendasi Matakuliah Pilihan yang telah diprogramkan dan \n"
								+ "di konversi ke kurikulum baru"
								+ "pada Semester Ganjil");

				tableLayout.setStretchAllColumns(true);

				// Setting Up Header table
				rowHeader1 = new TableRow(getActivity());
				rowHeader1.setId(100);

				// Setting up ColomnNilaiHuruf parameters
				Log.d("Skripsi", "mengambil data colom NilaiHuruf");
				ColomnKodeMAkul = new CustomTextView(getActivity());
				ColomnKodeMAkul.setText("KODE MATAKULIAH");
				ColomnKodeMAkul.setTextColor(Color.WHITE);
				ColomnKodeMAkul.setGravity(Gravity.CENTER);
				ColomnKodeMAkul.setTextSize(14);
				ColomnKodeMAkul.setBackgroundResource(R.drawable.tv_bg);
				rowHeader1.addView(ColomnKodeMAkul, cellLp);

				// Setting up the ColomnNamaMaKul parameters
				Log.d("Skripsi", "mengambil data colom NamaMakul");
				ColomnNamaMakul = new CustomTextView(getActivity());
				ColomnNamaMakul.setText("NAMA MATAKULIAH");
				ColomnNamaMakul.setTextColor(Color.WHITE);
				ColomnNamaMakul.setTextSize(14);
				ColomnNamaMakul.setGravity(Gravity.CENTER);
				ColomnNamaMakul.setBackgroundResource(R.drawable.tv_bg);
				rowHeader1.addView(ColomnNamaMakul, cellLp);

				// Setting up ColomnSKS parameters
				Log.d("Skripsi", "mengambil data colom SKS");
				ColomnSKS = new CustomTextView(getActivity());
				ColomnSKS.setText("SKS");
				ColomnSKS.setTextColor(Color.WHITE);
				ColomnSKS.setGravity(Gravity.CENTER);
				ColomnSKS.setTextSize(14);
				ColomnSKS.setBackgroundResource(R.drawable.tv_bg);
				rowHeader1.addView(ColomnSKS, cellLp);

				// Setting up ColomnSKS parameters

				Log.d("Skripsi", "mengambil data colom Semester");
				ColomnSifatMakul = new CustomTextView(getActivity());
				ColomnSifatMakul.setText("SIFAT MATAKULIAH");
				ColomnSifatMakul.setTextColor(Color.WHITE);
				ColomnSifatMakul.setTextSize(14);
				ColomnSifatMakul.setGravity(Gravity.CENTER);
				ColomnSifatMakul.setBackgroundResource(R.drawable.tv_bg);
				rowHeader1.addView(ColomnSifatMakul, cellLp);

				Log.d("Skripsi", "mengambil data colom Semester");
				ColomnNilaiHuruf = new CustomTextView(getActivity());
				ColomnNilaiHuruf.setText("NILAI HURUF");
				ColomnNilaiHuruf.setTextColor(Color.WHITE);
				ColomnNilaiHuruf.setTextSize(14);
				ColomnNilaiHuruf.setGravity(Gravity.CENTER);
				ColomnNilaiHuruf.setBackgroundResource(R.drawable.tv_bg);
				rowHeader1.addView(ColomnNilaiHuruf, cellLp);

				// Setting up ColomnSemester parameters
				Log.d("Skripsi", "mengambil data colom Semester");
				ColomnSemester = new CustomTextView(getActivity());
				ColomnSemester.setText("SEMESTER");
				ColomnSemester.setTextColor(Color.WHITE);
				ColomnSemester.setTextSize(14);
				ColomnSemester.setGravity(Gravity.CENTER);
				ColomnSemester.setBackgroundResource(R.drawable.tv_bg);
				rowHeader1.addView(ColomnSemester, cellLp);
				tableLayout.addView(rowHeader1, rowLp);

				GenapGanjil = "Ganjil";

				for (SMT = 1; SMT <= Semes; SMT++) {
					PenyetaraanUlang();
					Log.d("Cek6", String.valueOf(SMT));
					SMT += 1;
				}

				// SMT = 2;
				// while (SMT < Semes) {
				// PenyetaraanUlang();
				// Log.d("Cek6", String.valueOf(SMT));
				// SMT = SMT + 2;
				// }

				PenyetaraanKRSWajib();
				AllKRSWajib();
				MakulTerkait();

				TotalSKSWSetara = Total1WSetara + Total2WSetara;
				if (TotalSKSWSetara == 0) {
					// Notif2Setara
					// .setText("Tidak ada Rekomendasi Matakuliah Wajib yang sudah diprogramkan dan \n"
					// + "di konversi ke kurikulum baru pada Semester Genap");
					Notif2Setara.setVisibility(View.GONE);
					tableLayoutCurrentSetara.setVisibility(View.GONE);
				}

				TotalSKSPSetara = Total1PSetara + Total2PSetara;
				if (TotalSKSPSetara == 0) {
					// Notif3Setara
					// .setText("Tidak ada Rekomendasi Matakuliah Pilihan yang sudah diprogramkan dan \n"
					// + "di konversi ke kurikulum baru pada Semester Genap");
					Notif3Setara.setVisibility(View.GONE);
					tableLayoutCurrentPilihanSetara.setVisibility(View.GONE);
				}

				TotalSKSUlang = Total1 + Total2;
				if (TotalSKSUlang == 0) {
					Notif1.setText("Tidak ada rekomendasi matakuliah yang \n"
							+ "diprogramkan ulang pada semester " + GenapGanjil);
					tableLayout.setVisibility(View.GONE);
					TotSKSLalu.setVisibility(View.GONE);
				} else {
					TotSKSLalu.setText("TOTAL SKS : " + TotalSKSUlang);
				}

				Integer totSKS = TotalSKSUlang + totalSKSWajib;
				Log.d("SKSGanjil", "Total SKS Ganjil" + TotalSKSUlang);
				if (MaxSKS > totSKS) {
					PenyetaraanKRSPilihan();
					AllKRSPilihan();
				} else {
					Notif3.setVisibility(View.GONE);
					tableLayoutCurrentPilihan.setVisibility(View.GONE);
					TotSKSSekarP.setVisibility(View.GONE);
				}
			} else {
				Semesterx.setText("Semester " + Semes + " (Ganjil)");

				Notif1.setText("Rekomendasi Matakuliah yang diprogramkan ulang pada "
						+ "\n" + "Semester Genap");

				Notif2.setText("Rekomendasi Matakuliah Wajib yang diprogramkan pada "
						+ "\n" + "Semester Genap");

				Notif2Setara
						.setText("Rekomendasi Matakuliah Wajib yang sudah diprogramkan dan \n"
								+ "di konversi ke kurikulum baru pada Semester Genap");

				Notif3.setText("Rekomendasi Matakuliah Pilihan yang diprogramkan pada "
						+ "\n" + "Semester Genap");

				Notif3Setara
						.setText("Rekomendasi Matakuliah Pilihan yang telah diprogramkan dan \n"
								+ "di konversi ke kurikulum baru"
								+ "pada Semester Ganjil");

				tableLayout.setStretchAllColumns(true);

				// Setting Up Header table
				rowHeader1 = new TableRow(getActivity());
				rowHeader1.setId(100);

				// Setting up ColomnNilaiHuruf parameters
				Log.d("Skripsi", "mengambil data colom NilaiHuruf");
				ColomnKodeMAkul = new CustomTextView(getActivity());
				ColomnKodeMAkul.setText("KODE MATAKULIAH");
				ColomnKodeMAkul.setTextColor(Color.WHITE);
				ColomnKodeMAkul.setGravity(Gravity.CENTER);
				ColomnKodeMAkul.setTextSize(14);
				ColomnKodeMAkul.setBackgroundResource(R.drawable.tv_bg);
				rowHeader1.addView(ColomnKodeMAkul, cellLp);

				// Setting up the ColomnNamaMaKul parameters
				Log.d("Skripsi", "mengambil data colom NamaMakul");
				ColomnNamaMakul = new CustomTextView(getActivity());
				ColomnNamaMakul.setText("NAMA MATAKULIAH");
				ColomnNamaMakul.setTextColor(Color.WHITE);
				ColomnNamaMakul.setTextSize(14);
				ColomnNamaMakul.setGravity(Gravity.CENTER);
				ColomnNamaMakul.setBackgroundResource(R.drawable.tv_bg);
				rowHeader1.addView(ColomnNamaMakul, cellLp);

				// Setting up ColomnSKS parameters
				Log.d("Skripsi", "mengambil data colom SKS");
				ColomnSKS = new CustomTextView(getActivity());
				ColomnSKS.setText("SKS");
				ColomnSKS.setTextColor(Color.WHITE);
				ColomnSKS.setGravity(Gravity.CENTER);
				ColomnSKS.setTextSize(14);
				ColomnSKS.setBackgroundResource(R.drawable.tv_bg);
				rowHeader1.addView(ColomnSKS, cellLp);

				// Setting up ColomnSKS parameters

				Log.d("Skripsi", "mengambil data colom Semester");
				ColomnSifatMakul = new CustomTextView(getActivity());
				ColomnSifatMakul.setText("SIFAT MATAKULIAH");
				ColomnSifatMakul.setTextColor(Color.WHITE);
				ColomnSifatMakul.setTextSize(14);
				ColomnSifatMakul.setGravity(Gravity.CENTER);
				ColomnSifatMakul.setBackgroundResource(R.drawable.tv_bg);
				rowHeader1.addView(ColomnSifatMakul, cellLp);

				Log.d("Skripsi", "mengambil data colom Semester");
				ColomnNilaiHuruf = new CustomTextView(getActivity());
				ColomnNilaiHuruf.setText("NILAI HURUF");
				ColomnNilaiHuruf.setTextColor(Color.WHITE);
				ColomnNilaiHuruf.setTextSize(14);
				ColomnNilaiHuruf.setGravity(Gravity.CENTER);
				ColomnNilaiHuruf.setBackgroundResource(R.drawable.tv_bg);
				rowHeader1.addView(ColomnNilaiHuruf, cellLp);

				// Setting up ColomnSemester parameters
				Log.d("Skripsi", "mengambil data colom Semester");
				ColomnSemester = new CustomTextView(getActivity());
				ColomnSemester.setText("SEMESTER");
				ColomnSemester.setTextColor(Color.WHITE);
				ColomnSemester.setTextSize(14);
				ColomnSemester.setGravity(Gravity.CENTER);
				ColomnSemester.setBackgroundResource(R.drawable.tv_bg);
				rowHeader1.addView(ColomnSemester, cellLp);
				tableLayout.addView(rowHeader1, rowLp);

				GenapGanjil = "Genap";
				SMT = 2;
				while (SMT < Semes) {
					PenyetaraanUlang();
					Log.d("Cek6", String.valueOf(SMT));
					SMT = SMT + 2;
				}

				// for (SMT = 1; SMT <= Semes; SMT++) {
				// PenyetaraanUlang();
				// Log.d("Cek6", String.valueOf(SMT));
				// SMT += 1;
				// }

				PenyetaraanKRSWajib();
				AllKRSWajib();
				MakulTerkait();

				TotalSKSWSetara = Total1WSetara + Total2WSetara;
				if (TotalSKSWSetara == 0) {
					// Notif2Setara
					// .setText("Tidak ada Rekomendasi Matakuliah Wajib yang sudah diprogramkan dan \n"
					// + "di konversi ke kurikulum baru pada Semester Genap");
					Notif2Setara.setVisibility(View.GONE);
					tableLayoutCurrentSetara.setVisibility(View.GONE);
				}

				TotalSKSPSetara = Total1PSetara + Total2PSetara;
				if (TotalSKSPSetara == 0) {
					// Notif3Setara
					// .setText("Tidak ada Rekomendasi Matakuliah Pilihan yang sudah diprogramkan dan \n"
					// + "di konversi ke kurikulum baru pada Semester Genap");
					Notif3Setara.setVisibility(View.GONE);
					tableLayoutCurrentPilihanSetara.setVisibility(View.GONE);
				}

				// Mengambil nilai dari fungsi penyetaraan ulang
				TotalSKSUlang = Total1 + Total2;

				if (TotalSKSUlang == 0) {
					Notif1.setText("Tidak ada rekomendasi matakuliah yang \n"
							+ "diprogramkan ulang pada semester " + GenapGanjil);
					tableLayout.setVisibility(View.GONE);
					TotSKSLalu.setVisibility(View.GONE);
				} else {
					TotSKSLalu.setText("TOTAL SKS : " + TotalSKSUlang);
				}

				Integer totSKS = TotalSKSUlang + totalSKSWajib;
				Log.d("SKSGenap", "Total SKS Genap" + totSKS);
				if (MaxSKS > totSKS) {
					PenyetaraanKRSPilihan();
					AllKRSPilihan();
				} else {
					Notif3.setVisibility(View.GONE);
					tableLayoutCurrentPilihan.setVisibility(View.GONE);
					TotSKSSekarP.setVisibility(View.GONE);
				}
			}
		}
	}

	/*
	 * Menampilkan penyetaraan mata kuliah yang mengulang di Semester
	 * Ganjil/Genap
	 */
	@SuppressLint({ "InlinedApi", "NewApi" })
	public void PenyetaraanUlang() {

		// query untuk mengambil matakuliah yang mengulang baik kur lama atau
		// baru
		Cursor cek1 = db.rawQuery("SELECT DISTINCT " + "KodeMakul as blao "
				+ "FROM TRANSKIP " + "WHERE NilaiHuruf!='' "
				// + "and Semester like '%" + SmT + "' "
				+ "and (NilaiHuruf='E' or NilaiHuruf='D') "
				+ "order by KodeMakul desc", null);

		// untuk MakulUIN
		Integer KodeMaKul1 = cek1.getColumnIndex("blao");

		if (cek1.getCount() > 0) {

			cek1.moveToFirst();
			do {
				row = new TableRow(getActivity());
				row.setId(100);
				Log.d("Cek1", cek1.getString(KodeMaKul1));

				// Jika sudah kurikulum baru
				Cursor cek4 = db.rawQuery(
						"select KodeMKBaru as blao from Penyetaraan where KodeMKBaru = "
								+ "'" + cek1.getString(KodeMaKul1) + "'", null);
				Integer KodeMaKul4 = cek4.getColumnIndex("blao");
				cek4.moveToFirst();

				// Query untuk penyetaraan matakuliah
				Cursor cek2 = db.rawQuery(
						"select KodeMKBaru as blao from Penyetaraan where KodeMKLama = "
								+ "'" + cek1.getString(KodeMaKul1) + "'", null);
				Integer KodeMaKul2 = cek2.getColumnIndex("blao");
				cek2.moveToFirst();

				if (!(cek4.getCount() == 0)) {
					Log.d("Cek2", cek4.getString(KodeMaKul4));
					Cursor cek5 = db
							.rawQuery(
									"select t1.KodeMaKul as a,"
											+ "t1.NamaMakul as b,"
											+ "t1.SKSTeori + t1.SKSPraktikum as SKS ,"
											+ "t1.SifatMaKul as c,"
											+ "t1.PaketSemester d,t2.NilaiHuruf e from KRS t1,Transkip t2  "
											+ "where " + "t1.PaketSemester ='"
											+ SMT + "' and" + " t2.KodeMaKul='"
											+ cek1.getString(KodeMaKul1)
											+ "' and  t1.KodeMaKul = " + "'"
											+ cek4.getString(KodeMaKul4) + "'",
									null);
					cek5.moveToFirst();

					if (cek5.getCount() > 0) {

						Integer KodeMK = cek5.getColumnIndex("a");
						Integer MK = cek5.getColumnIndex("b");
						Integer SKS = cek5.getColumnIndex("SKS");
						Integer SM = cek5.getColumnIndex("c");
						Integer PS = cek5.getColumnIndex("d");
						Integer NH = cek5.getColumnIndex("e");

						// Setting up ColomnNilaiHuruf parameters
						Log.d("Skripsi", "mengambil data colom NilaiHuruf");
						ColomnKodeMAkul = new CustomTextView(getActivity());
						ColomnKodeMAkul.setText(cek5.getString(KodeMK));
						ColomnKodeMAkul.setTextColor(Color.BLACK);
						ColomnKodeMAkul.setGravity(Gravity.CENTER);
						ColomnKodeMAkul.setTextSize(14);
						ColomnKodeMAkul
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnKodeMAkul, cellLp);

						// Setting up the ColomnNamaMaKul parameters
						Log.d("Skripsi", "mengambil data colom NamaMakul");
						ColomnNamaMakul = new CustomTextView(getActivity());
						ColomnNamaMakul.setText(cek5.getString(MK));
						ColomnNamaMakul.setTextColor(Color.BLACK);
						ColomnNamaMakul.setTextSize(14);
						ColomnNamaMakul
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnNamaMakul, cellLp);

						// Setting up ColomnSKS parameters
						Log.d("Skripsi", "mengambil data colom SKS");
						ColomnSKS = new CustomTextView(getActivity());
						ColomnSKS.setText(cek5.getString(SKS));
						ColomnSKS.setTextColor(Color.BLACK);
						ColomnSKS.setGravity(Gravity.CENTER);
						ColomnSKS.setTextSize(14);
						ColomnSKS.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnSKS, cellLp);

						Total1 += Integer.valueOf(cek5.getString(SKS));

						Log.d("Skripsi", "mengambil data colom Semester");
						ColomnSifatMakul = new CustomTextView(getActivity());
						ColomnSifatMakul.setText(cek5.getString(SM));
						ColomnSifatMakul.setTextColor(Color.BLACK);
						ColomnSifatMakul.setTextSize(14);
						ColomnSifatMakul.setGravity(Gravity.CENTER);
						ColomnSifatMakul
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnSifatMakul, cellLp);

						Log.d("Skripsi", "mengambil data colom Semester");
						ColomnNilaiHuruf = new CustomTextView(getActivity());
						ColomnNilaiHuruf.setText(cek5.getString(NH));
						ColomnNilaiHuruf.setTextColor(Color.BLACK);
						ColomnNilaiHuruf.setTextSize(14);
						ColomnNilaiHuruf.setGravity(Gravity.CENTER);
						ColomnNilaiHuruf
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnNilaiHuruf, cellLp);

						// Setting up ColomnSemester parameters
						Log.d("Skripsi", "mengambil data colom Semester");
						ColomnSemester = new CustomTextView(getActivity());
						ColomnSemester.setText(cek5.getString(PS));
						ColomnSemester.setTextColor(Color.BLACK);
						ColomnSemester.setTextSize(14);
						ColomnSemester.setGravity(Gravity.CENTER);
						ColomnSemester.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnSemester, cellLp);

						tableLayout.addView(row, rowLp);
					}
					// Konversi Makul lama ke Makul baru
				} else if (!(cek2.getCount() == 0)) {
					Log.d("Cek2", cek2.getString(KodeMaKul2));
					Cursor cek3 = db
							.rawQuery(
									"select t1.KodeMaKul as a,"
											+ "t1.NamaMakul as b,"
											+ "t1.SKSTeori + t1.SKSPraktikum as SKS ,"
											+ "t1.SifatMaKul as c,"
											+ "t1.PaketSemester d,t2.NilaiHuruf e from KRS t1,Transkip t2  "
											+ "where " + "t1.PaketSemester ='"
											+ SMT + "' and  "
											+ "t2.KodeMaKul='"
											+ cek1.getString(KodeMaKul1)
											+ "' and t1.KodeMaKul = " + "'"
											+ cek2.getString(KodeMaKul2) + "'",
									null);
					cek3.moveToFirst();

					if (cek3.getCount() > 0) {

						Integer KodeMK = cek3.getColumnIndex("a");
						Log.d("Cek3", cek3.getString(KodeMK));
						Integer MK = cek3.getColumnIndex("b");
						Integer SKS = cek3.getColumnIndex("SKS");
						Integer SM = cek3.getColumnIndex("c");
						Integer PS = cek3.getColumnIndex("d");
						Integer NH = cek3.getColumnIndex("e");

						Log.d("Skripsi", "mengambil data colom NilaiHuruf");
						ColomnKodeMAkul = new CustomTextView(getActivity());
						ColomnKodeMAkul.setText(cek3.getString(KodeMK));
						ColomnKodeMAkul.setTextColor(Color.BLACK);
						ColomnKodeMAkul.setGravity(Gravity.CENTER);
						ColomnKodeMAkul.setTextSize(14);
						ColomnKodeMAkul
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnKodeMAkul, cellLp);

						// Setting up the ColomnNamaMaKul parameters
						Log.d("Skripsi", "mengambil data colom NamaMakul");
						ColomnNamaMakul = new CustomTextView(getActivity());
						ColomnNamaMakul.setText(cek3.getString(MK));
						ColomnNamaMakul.setTextColor(Color.BLACK);
						ColomnNamaMakul.setTextSize(14);
						ColomnNamaMakul
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnNamaMakul, cellLp);

						// Setting up ColomnSKS parameters
						Log.d("Skripsi", "mengambil data colom SKS");
						ColomnSKS = new CustomTextView(getActivity());
						ColomnSKS.setText(cek3.getString(SKS));
						ColomnSKS.setTextColor(Color.BLACK);
						ColomnSKS.setGravity(Gravity.CENTER);
						ColomnSKS.setTextSize(14);
						ColomnSKS.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnSKS, cellLp);

						Total2 += Integer.valueOf(cek3.getString(SKS));

						Log.d("Skripsi", "mengambil data colom Semester");
						ColomnSifatMakul = new CustomTextView(getActivity());
						ColomnSifatMakul.setText(cek3.getString(SM));
						ColomnSifatMakul.setTextColor(Color.BLACK);
						ColomnSifatMakul.setTextSize(14);
						ColomnSifatMakul.setGravity(Gravity.CENTER);
						ColomnSifatMakul
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnSifatMakul, cellLp);

						Log.d("Skripsi", "mengambil data colom Semester");
						ColomnNilaiHuruf = new CustomTextView(getActivity());
						ColomnNilaiHuruf.setText(cek3.getString(NH));
						ColomnNilaiHuruf.setTextColor(Color.BLACK);
						ColomnNilaiHuruf.setTextSize(14);
						ColomnNilaiHuruf.setGravity(Gravity.CENTER);
						ColomnNilaiHuruf
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnNilaiHuruf, cellLp);

						// Setting up ColomnSemester parameters
						Log.d("Skripsi", "mengambil data colom Semester");
						ColomnSemester = new CustomTextView(getActivity());
						ColomnSemester.setText(cek3.getString(PS));
						ColomnSemester.setTextColor(Color.BLACK);
						ColomnSemester.setTextSize(14);
						ColomnSemester.setGravity(Gravity.CENTER);
						ColomnSemester.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnSemester, cellLp);

						tableLayout.addView(row, rowLp);
					}
				}
			} while (cek1.moveToNext());

		} else {
			// Notif1.setText("Tidak ada rekomendasi matakuliah yang \n"
			// + "diprogramkan ulang pada semester " + GenapGanjil);
			Notif1.setVisibility(View.GONE);
		}

	}

	/*
	 * Menampilkan penyetaraan mata kuliah wajib telah diprogramkan pada
	 * Semester Ganjil/Genap lalu dikonversi ke kurikulum baru
	 */
	@SuppressLint({ "InlinedApi", "NewApi" })
	public void PenyetaraanKRSWajib() {

		// query untuk mengambil matakuliah yang mengulang baik kur lama atau
		// baru
		Cursor cek1 = db.rawQuery("SELECT DISTINCT " + "KodeMakul as blao "
				+ "FROM TRANSKIP " + "WHERE NilaiHuruf!='' "
				// + "and Semester like '%" + SmT + "' "
				+ "and (NilaiHuruf!='' or NilaiHuruf!='E' or NilaiHuruf!='D') "
				+ "order by KodeMakul desc", null);

		// untuk kode matakuliah
		Integer KodeMaKul1 = cek1.getColumnIndex("blao");

		if (cek1.getCount() > 0) {
			tableLayoutCurrentSetara.setStretchAllColumns(true);

			// Setting Up Header table
			rowHeaderWSetara = new TableRow(getActivity());
			rowHeaderWSetara.setId(100);

			// Setting up ColomnNilaiHuruf parameters
			Log.d("Skripsi", "mengambil data colom NilaiHuruf");
			ColomnKodeMAkul = new CustomTextView(getActivity());
			ColomnKodeMAkul.setText("KODE MATAKULIAH");
			ColomnKodeMAkul.setTextColor(Color.WHITE);
			ColomnKodeMAkul.setGravity(Gravity.CENTER);
			ColomnKodeMAkul.setTextSize(14);
			ColomnKodeMAkul.setBackgroundResource(R.drawable.tv_bg);
			rowHeaderWSetara.addView(ColomnKodeMAkul, cellLp);

			// Setting up the ColomnNamaMaKul parameters
			Log.d("Skripsi", "mengambil data colom NamaMakul");
			ColomnNamaMakul = new CustomTextView(getActivity());
			ColomnNamaMakul.setText("NAMA MATAKULIAH");
			ColomnNamaMakul.setTextColor(Color.WHITE);
			ColomnNamaMakul.setTextSize(14);
			ColomnNamaMakul.setGravity(Gravity.CENTER);
			ColomnNamaMakul.setBackgroundResource(R.drawable.tv_bg);
			rowHeaderWSetara.addView(ColomnNamaMakul, cellLp);

			// Setting up ColomnSKS parameters
			Log.d("Skripsi", "mengambil data colom SKS");
			ColomnSKS = new CustomTextView(getActivity());
			ColomnSKS.setText("SKS");
			ColomnSKS.setTextColor(Color.WHITE);
			ColomnSKS.setGravity(Gravity.CENTER);
			ColomnSKS.setTextSize(14);
			ColomnSKS.setBackgroundResource(R.drawable.tv_bg);
			rowHeaderWSetara.addView(ColomnSKS, cellLp);

			// Setting up ColomnSKS parameters

			Log.d("Skripsi", "mengambil data colom Semester");
			ColomnSifatMakul = new CustomTextView(getActivity());
			ColomnSifatMakul.setText("SIFAT MATAKULIAH");
			ColomnSifatMakul.setTextColor(Color.WHITE);
			ColomnSifatMakul.setTextSize(14);
			ColomnSifatMakul.setGravity(Gravity.CENTER);
			ColomnSifatMakul.setBackgroundResource(R.drawable.tv_bg);
			rowHeaderWSetara.addView(ColomnSifatMakul, cellLp);

			Log.d("Skripsi", "mengambil data colom Semester");
			ColomnNilaiHuruf = new CustomTextView(getActivity());
			ColomnNilaiHuruf.setText("NILAI HURUF");
			ColomnNilaiHuruf.setTextColor(Color.WHITE);
			ColomnNilaiHuruf.setTextSize(14);
			ColomnNilaiHuruf.setGravity(Gravity.CENTER);
			ColomnNilaiHuruf.setBackgroundResource(R.drawable.tv_bg);
			rowHeaderWSetara.addView(ColomnNilaiHuruf, cellLp);

			// Setting up ColomnSemester parameters
			Log.d("Skripsi", "mengambil data colom Semester");
			ColomnSemester = new CustomTextView(getActivity());
			ColomnSemester.setText("SEMESTER");
			ColomnSemester.setTextColor(Color.WHITE);
			ColomnSemester.setTextSize(14);
			ColomnSemester.setGravity(Gravity.CENTER);
			ColomnSemester.setBackgroundResource(R.drawable.tv_bg);
			rowHeaderWSetara.addView(ColomnSemester, cellLp);
			tableLayoutCurrentSetara.addView(rowHeaderWSetara, rowLp);
			cek1.moveToFirst();
			do {
				row = new TableRow(getActivity());
				row.setId(100);
				Log.d("Cek1", cek1.getString(KodeMaKul1));

				// Jika sudah kurikulum baru
				Cursor cek4 = db.rawQuery(
						"select KodeMKBaru as blao from Penyetaraan where KodeMKBaru = "
								+ "'" + cek1.getString(KodeMaKul1) + "'", null);
				Integer KodeMaKul4 = cek4.getColumnIndex("blao");
				cek4.moveToFirst();

				// Konversi kurikulum lama ke kurikulum baru
				Cursor cek2 = db.rawQuery(
						"select KodeMKBaru as blao from Penyetaraan where KodeMKLama = "
								+ "'" + cek1.getString(KodeMaKul1) + "'", null);
				Integer KodeMaKul2 = cek2.getColumnIndex("blao");
				cek2.moveToFirst();

				// jika sudah kurikulum baru
				if (!(cek4.getCount() == 0)) {
					// cek1.moveToNext();
					Log.d("Cek2", cek4.getString(KodeMaKul4));
					Cursor cek5 = db
							.rawQuery(
									"select t1.KodeMaKul as a,"
											+ "t1.NamaMakul as b,"
											+ "t1.SKSTeori + t1.SKSPraktikum as SKS ,"
											+ "t1.SifatMaKul as c,"
											+ "t1.PaketSemester d,t2.NilaiHuruf e from KRS t1,Transkip t2  "
											+ "where " + "t1.PaketSemester ='"
											+ SMTx + "'"
											+ "and t1.SifatMaKul='W' "
											+ "and t2.KodeMaKul='"
											+ cek1.getString(KodeMaKul1)
											+ "' and  t1.KodeMaKul = " + "'"
											+ cek4.getString(KodeMaKul4) + "'",
									null);
					cek5.moveToFirst();

					if (cek5.getCount() > 0) {

						Integer KodeMK = cek5.getColumnIndex("a");
						Integer MK = cek5.getColumnIndex("b");
						Integer SKS = cek5.getColumnIndex("SKS");
						Integer SM = cek5.getColumnIndex("c");
						Integer PS = cek5.getColumnIndex("d");
						Integer NH = cek5.getColumnIndex("e");

						// Setting up ColomnNilaiHuruf parameters
						Log.d("Skripsi", "mengambil data colom NilaiHuruf");
						ColomnKodeMAkul = new CustomTextView(getActivity());
						ColomnKodeMAkul.setText(cek5.getString(KodeMK));
						ColomnKodeMAkul.setTextColor(Color.BLACK);
						ColomnKodeMAkul.setGravity(Gravity.CENTER);
						ColomnKodeMAkul.setTextSize(14);
						ColomnKodeMAkul
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnKodeMAkul, cellLp);

						// Setting up the ColomnNamaMaKul parameters
						Log.d("Skripsi", "mengambil data colom NamaMakul");
						ColomnNamaMakul = new CustomTextView(getActivity());
						ColomnNamaMakul.setText(cek5.getString(MK));
						ColomnNamaMakul.setTextColor(Color.BLACK);
						ColomnNamaMakul.setTextSize(14);
						ColomnNamaMakul
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnNamaMakul, cellLp);

						// Setting up ColomnSKS parameters
						Log.d("Skripsi", "mengambil data colom SKS");
						ColomnSKS = new CustomTextView(getActivity());
						ColomnSKS.setText(cek5.getString(SKS));
						ColomnSKS.setTextColor(Color.BLACK);
						ColomnSKS.setGravity(Gravity.CENTER);
						ColomnSKS.setTextSize(14);
						ColomnSKS.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnSKS, cellLp);

						Total1WSetara += Integer.valueOf(cek5.getString(SKS));

						Log.d("Skripsi", "mengambil data colom Semester");
						ColomnSifatMakul = new CustomTextView(getActivity());
						ColomnSifatMakul.setText(cek5.getString(SM));
						ColomnSifatMakul.setTextColor(Color.BLACK);
						ColomnSifatMakul.setTextSize(14);
						ColomnSifatMakul.setGravity(Gravity.CENTER);
						ColomnSifatMakul
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnSifatMakul, cellLp);

						Log.d("Skripsi", "mengambil data colom Semester");
						ColomnNilaiHuruf = new CustomTextView(getActivity());
						ColomnNilaiHuruf.setText(cek5.getString(NH));
						ColomnNilaiHuruf.setTextColor(Color.BLACK);
						ColomnNilaiHuruf.setTextSize(14);
						ColomnNilaiHuruf.setGravity(Gravity.CENTER);
						ColomnNilaiHuruf
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnNilaiHuruf, cellLp);

						// Setting up ColomnSemester parameters
						Log.d("Skripsi", "mengambil data colom Semester");
						ColomnSemester = new CustomTextView(getActivity());
						ColomnSemester.setText(cek5.getString(PS));
						ColomnSemester.setTextColor(Color.BLACK);
						ColomnSemester.setTextSize(14);
						ColomnSemester.setGravity(Gravity.CENTER);
						ColomnSemester.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnSemester, cellLp);

						tableLayout.addView(row, rowLp);
					}
					// Konversi kurikulum lama ke kurikulum baru
				} else if (!(cek2.getCount() == 0)) {
					Log.d("Cek2", cek2.getString(KodeMaKul2));
					Cursor cek3 = db
							.rawQuery(
									"select t1.KodeMaKul as a,"
											+ "t1.NamaMakul as b,"
											+ "t1.SKSTeori + t1.SKSPraktikum as SKS ,"
											+ "t1.SifatMaKul as c,"
											+ "t1.PaketSemester d,t2.NilaiHuruf e from KRS t1,Transkip t2  "
											+ "where " + "t1.PaketSemester ='"
											+ SMTx + "'"
											+ "and t1.SifatMaKul='W' "
											+ "and t2.KodeMaKul='"
											+ cek1.getString(KodeMaKul1)
											+ "' and t1.KodeMaKul = " + "'"
											+ cek2.getString(KodeMaKul2) + "'",
									null);
					cek3.moveToFirst();

					if (cek3.getCount() > 0) {

						Integer KodeMK = cek3.getColumnIndex("a");
						Log.d("Cek3", cek3.getString(KodeMK));
						Integer MK = cek3.getColumnIndex("b");
						Integer SKS = cek3.getColumnIndex("SKS");
						Integer SM = cek3.getColumnIndex("c");
						Integer PS = cek3.getColumnIndex("d");
						Integer NH = cek3.getColumnIndex("e");

						Log.d("Skripsi", "mengambil data colom NilaiHuruf");
						ColomnKodeMAkul = new CustomTextView(getActivity());
						ColomnKodeMAkul.setText(cek3.getString(KodeMK));
						ColomnKodeMAkul.setTextColor(Color.BLACK);
						ColomnKodeMAkul.setGravity(Gravity.CENTER);
						ColomnKodeMAkul.setTextSize(14);
						ColomnKodeMAkul
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnKodeMAkul, cellLp);

						// Setting up the ColomnNamaMaKul parameters
						Log.d("Skripsi", "mengambil data colom NamaMakul");
						ColomnNamaMakul = new CustomTextView(getActivity());
						ColomnNamaMakul.setText(cek3.getString(MK));
						ColomnNamaMakul.setTextColor(Color.BLACK);
						ColomnNamaMakul.setTextSize(14);
						ColomnNamaMakul
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnNamaMakul, cellLp);

						// Setting up ColomnSKS parameters
						Log.d("Skripsi", "mengambil data colom SKS");
						ColomnSKS = new CustomTextView(getActivity());
						ColomnSKS.setText(cek3.getString(SKS));
						ColomnSKS.setTextColor(Color.BLACK);
						ColomnSKS.setGravity(Gravity.CENTER);
						ColomnSKS.setTextSize(14);
						ColomnSKS.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnSKS, cellLp);

						Total2WSetara += Integer.valueOf(cek3.getString(SKS));

						Log.d("Skripsi", "mengambil data colom Semester");
						ColomnSifatMakul = new CustomTextView(getActivity());
						ColomnSifatMakul.setText(cek3.getString(SM));
						ColomnSifatMakul.setTextColor(Color.BLACK);
						ColomnSifatMakul.setTextSize(14);
						ColomnSifatMakul.setGravity(Gravity.CENTER);
						ColomnSifatMakul
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnSifatMakul, cellLp);

						Log.d("Skripsi", "mengambil data colom Semester");
						ColomnNilaiHuruf = new CustomTextView(getActivity());
						ColomnNilaiHuruf.setText(cek3.getString(NH));
						ColomnNilaiHuruf.setTextColor(Color.BLACK);
						ColomnNilaiHuruf.setTextSize(14);
						ColomnNilaiHuruf.setGravity(Gravity.CENTER);
						ColomnNilaiHuruf
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnNilaiHuruf, cellLp);

						// Setting up ColomnSemester parameters
						Log.d("Skripsi", "mengambil data colom Semester");
						ColomnSemester = new CustomTextView(getActivity());
						ColomnSemester.setText(cek3.getString(PS));
						ColomnSemester.setTextColor(Color.BLACK);
						ColomnSemester.setTextSize(14);
						ColomnSemester.setGravity(Gravity.CENTER);
						ColomnSemester.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnSemester, cellLp);

						tableLayoutCurrentSetara.addView(row, rowLp);
					}
				}
			} while (cek1.moveToNext());

		} else {
			Notif2Setara
					.setText("Tidak ada rekomendasi matakuliah wajib yang sudah \n"
							+ "diprogramkan pada semester lalu dan dikonversi ke kurikulum baru "
							+ GenapGanjil);
		}

	}

	/*
	 * Menampilkan penyetaraan mata kuliah wajib telah diprogramkan pada
	 * Semester Ganjil/Genap lalu dikonversi ke kurikulum baru
	 */
	@SuppressLint({ "InlinedApi", "NewApi" })
	public void PenyetaraanKRSPilihan() {

		// query untuk mengambil matakuliah yang mengulang baik kur lama atau
		// baru
		Cursor cek1 = db.rawQuery("SELECT DISTINCT " + "KodeMakul as blao "
				+ "FROM TRANSKIP " + "WHERE NilaiHuruf!='' "
				// + "and Semester like '%" + SmT + "' "
				+ "and (NilaiHuruf!='' or NilaiHuruf!='E' or NilaiHuruf!='D') "
				+ "order by KodeMakul desc", null);

		// untuk MakulUIN
		Integer KodeMaKul1 = cek1.getColumnIndex("blao");

		if (cek1.getCount() > 0) {
			tableLayoutCurrentPilihanSetara.setStretchAllColumns(true);

			// Setting Up Header table
			rowHeaderWSetara = new TableRow(getActivity());
			rowHeaderWSetara.setId(100);

			// Setting up ColomnNilaiHuruf parameters
			Log.d("Skripsi", "mengambil data colom NilaiHuruf");
			ColomnKodeMAkul = new CustomTextView(getActivity());
			ColomnKodeMAkul.setText("KODE MATAKULIAH");
			ColomnKodeMAkul.setTextColor(Color.WHITE);
			ColomnKodeMAkul.setGravity(Gravity.CENTER);
			ColomnKodeMAkul.setTextSize(14);
			ColomnKodeMAkul.setBackgroundResource(R.drawable.tv_bg);
			rowHeaderWSetara.addView(ColomnKodeMAkul, cellLp);

			// Setting up the ColomnNamaMaKul parameters
			Log.d("Skripsi", "mengambil data colom NamaMakul");
			ColomnNamaMakul = new CustomTextView(getActivity());
			ColomnNamaMakul.setText("NAMA MATAKULIAH");
			ColomnNamaMakul.setTextColor(Color.WHITE);
			ColomnNamaMakul.setTextSize(14);
			ColomnNamaMakul.setGravity(Gravity.CENTER);
			ColomnNamaMakul.setBackgroundResource(R.drawable.tv_bg);
			rowHeaderWSetara.addView(ColomnNamaMakul, cellLp);

			// Setting up ColomnSKS parameters
			Log.d("Skripsi", "mengambil data colom SKS");
			ColomnSKS = new CustomTextView(getActivity());
			ColomnSKS.setText("SKS");
			ColomnSKS.setTextColor(Color.WHITE);
			ColomnSKS.setGravity(Gravity.CENTER);
			ColomnSKS.setTextSize(14);
			ColomnSKS.setBackgroundResource(R.drawable.tv_bg);
			rowHeaderWSetara.addView(ColomnSKS, cellLp);

			// Setting up ColomnSKS parameters

			Log.d("Skripsi", "mengambil data colom Semester");
			ColomnSifatMakul = new CustomTextView(getActivity());
			ColomnSifatMakul.setText("SIFAT MATAKULIAH");
			ColomnSifatMakul.setTextColor(Color.WHITE);
			ColomnSifatMakul.setTextSize(14);
			ColomnSifatMakul.setGravity(Gravity.CENTER);
			ColomnSifatMakul.setBackgroundResource(R.drawable.tv_bg);
			rowHeaderWSetara.addView(ColomnSifatMakul, cellLp);

			Log.d("Skripsi", "mengambil data colom Semester");
			ColomnNilaiHuruf = new CustomTextView(getActivity());
			ColomnNilaiHuruf.setText("NILAI HURUF");
			ColomnNilaiHuruf.setTextColor(Color.WHITE);
			ColomnNilaiHuruf.setTextSize(14);
			ColomnNilaiHuruf.setGravity(Gravity.CENTER);
			ColomnNilaiHuruf.setBackgroundResource(R.drawable.tv_bg);
			rowHeaderWSetara.addView(ColomnNilaiHuruf, cellLp);

			// Setting up ColomnSemester parameters
			Log.d("Skripsi", "mengambil data colom Semester");
			ColomnSemester = new CustomTextView(getActivity());
			ColomnSemester.setText("SEMESTER");
			ColomnSemester.setTextColor(Color.WHITE);
			ColomnSemester.setTextSize(14);
			ColomnSemester.setGravity(Gravity.CENTER);
			ColomnSemester.setBackgroundResource(R.drawable.tv_bg);
			rowHeaderWSetara.addView(ColomnSemester, cellLp);
			tableLayoutCurrentPilihanSetara.addView(rowHeaderWSetara, rowLp);
			cek1.moveToFirst();
			do {
				row = new TableRow(getActivity());
				row.setId(100);
				Log.d("Cek1", cek1.getString(KodeMaKul1));

				Cursor cek4 = db.rawQuery(
						"select KodeMKBaru as blao from Penyetaraan where KodeMKBaru = "
								+ "'" + cek1.getString(KodeMaKul1) + "'", null);
				Integer KodeMaKul4 = cek4.getColumnIndex("blao");
				cek4.moveToFirst();

				Cursor cek2 = db.rawQuery(
						"select KodeMKBaru as blao from Penyetaraan where KodeMKLama = "
								+ "'" + cek1.getString(KodeMaKul1) + "'", null);
				Integer KodeMaKul2 = cek2.getColumnIndex("blao");
				cek2.moveToFirst();

				// Konversi kurikulum lama ke kurikulum baru
				if (!(cek4.getCount() == 0)) {
					// cek1.moveToNext();
					Log.d("Cek2", cek4.getString(KodeMaKul4));
					Cursor cek5 = db
							.rawQuery(
									"select t1.KodeMaKul as a,"
											+ "t1.NamaMakul as b,"
											+ "t1.SKSTeori + t1.SKSPraktikum as SKS ,"
											+ "t1.SifatMaKul as c,"
											+ "t1.PaketSemester d,t2.NilaiHuruf e from KRS t1,Transkip t2  "
											+ "where " + "t1.PaketSemester ='"
											+ SMTx + "'"
											+ "and t1.SifatMaKul='P' "
											+ "and t2.KodeMaKul='"
											+ cek1.getString(KodeMaKul1)
											+ "' and  t1.KodeMaKul = " + "'"
											+ cek4.getString(KodeMaKul4) + "'",
									null);
					cek5.moveToFirst();

					if (cek5.getCount() > 0) {

						Integer KodeMK = cek5.getColumnIndex("a");
						Integer MK = cek5.getColumnIndex("b");
						Integer SKS = cek5.getColumnIndex("SKS");
						Integer SM = cek5.getColumnIndex("c");
						Integer PS = cek5.getColumnIndex("d");
						Integer NH = cek5.getColumnIndex("e");

						// Setting up ColomnNilaiHuruf parameters
						Log.d("Skripsi", "mengambil data colom NilaiHuruf");
						ColomnKodeMAkul = new CustomTextView(getActivity());
						ColomnKodeMAkul.setText(cek5.getString(KodeMK));
						ColomnKodeMAkul.setTextColor(Color.BLACK);
						ColomnKodeMAkul.setGravity(Gravity.CENTER);
						ColomnKodeMAkul.setTextSize(14);
						ColomnKodeMAkul
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnKodeMAkul, cellLp);

						// Setting up the ColomnNamaMaKul parameters
						Log.d("Skripsi", "mengambil data colom NamaMakul");
						ColomnNamaMakul = new CustomTextView(getActivity());
						ColomnNamaMakul.setText(cek5.getString(MK));
						ColomnNamaMakul.setTextColor(Color.BLACK);
						ColomnNamaMakul.setTextSize(14);
						ColomnNamaMakul
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnNamaMakul, cellLp);

						// Setting up ColomnSKS parameters
						Log.d("Skripsi", "mengambil data colom SKS");
						ColomnSKS = new CustomTextView(getActivity());
						ColomnSKS.setText(cek5.getString(SKS));
						ColomnSKS.setTextColor(Color.BLACK);
						ColomnSKS.setGravity(Gravity.CENTER);
						ColomnSKS.setTextSize(14);
						ColomnSKS.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnSKS, cellLp);

						Total1PSetara += Integer.valueOf(cek5.getString(SKS));

						Log.d("Skripsi", "mengambil data colom Semester");
						ColomnSifatMakul = new CustomTextView(getActivity());
						ColomnSifatMakul.setText(cek5.getString(SM));
						ColomnSifatMakul.setTextColor(Color.BLACK);
						ColomnSifatMakul.setTextSize(14);
						ColomnSifatMakul.setGravity(Gravity.CENTER);
						ColomnSifatMakul
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnSifatMakul, cellLp);

						Log.d("Skripsi", "mengambil data colom Semester");
						ColomnNilaiHuruf = new CustomTextView(getActivity());
						ColomnNilaiHuruf.setText(cek5.getString(NH));
						ColomnNilaiHuruf.setTextColor(Color.BLACK);
						ColomnNilaiHuruf.setTextSize(14);
						ColomnNilaiHuruf.setGravity(Gravity.CENTER);
						ColomnNilaiHuruf
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnNilaiHuruf, cellLp);

						// Setting up ColomnSemester parameters
						Log.d("Skripsi", "mengambil data colom Semester");
						ColomnSemester = new CustomTextView(getActivity());
						ColomnSemester.setText(cek5.getString(PS));
						ColomnSemester.setTextColor(Color.BLACK);
						ColomnSemester.setTextSize(14);
						ColomnSemester.setGravity(Gravity.CENTER);
						ColomnSemester.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnSemester, cellLp);

						tableLayout.addView(row, rowLp);
					}
				} else if (!(cek2.getCount() == 0)) {
					Log.d("Cek2", cek2.getString(KodeMaKul2));
					Cursor cek3 = db
							.rawQuery(
									"select t1.KodeMaKul as a,"
											+ "t1.NamaMakul as b,"
											+ "t1.SKSTeori + t1.SKSPraktikum as SKS ,"
											+ "t1.SifatMaKul as c,"
											+ "t1.PaketSemester d,t2.NilaiHuruf e from KRS t1,Transkip t2  "
											+ "where " + "t1.PaketSemester ='"
											+ SMTx + "'"
											+ "and t1.SifatMaKul='P' "
											+ "and t2.KodeMaKul='"
											+ cek1.getString(KodeMaKul1)
											+ "' and t1.KodeMaKul = " + "'"
											+ cek2.getString(KodeMaKul2) + "'",
									null);
					cek3.moveToFirst();

					if (cek3.getCount() > 0) {

						Integer KodeMK = cek3.getColumnIndex("a");
						Log.d("Cek3", cek3.getString(KodeMK));
						Integer MK = cek3.getColumnIndex("b");
						Integer SKS = cek3.getColumnIndex("SKS");
						Integer SM = cek3.getColumnIndex("c");
						Integer PS = cek3.getColumnIndex("d");
						Integer NH = cek3.getColumnIndex("e");

						Log.d("Skripsi", "mengambil data colom NilaiHuruf");
						ColomnKodeMAkul = new CustomTextView(getActivity());
						ColomnKodeMAkul.setText(cek3.getString(KodeMK));
						ColomnKodeMAkul.setTextColor(Color.BLACK);
						ColomnKodeMAkul.setGravity(Gravity.CENTER);
						ColomnKodeMAkul.setTextSize(14);
						ColomnKodeMAkul
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnKodeMAkul, cellLp);

						// Setting up the ColomnNamaMaKul parameters
						Log.d("Skripsi", "mengambil data colom NamaMakul");
						ColomnNamaMakul = new CustomTextView(getActivity());
						ColomnNamaMakul.setText(cek3.getString(MK));
						ColomnNamaMakul.setTextColor(Color.BLACK);
						ColomnNamaMakul.setTextSize(14);
						ColomnNamaMakul
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnNamaMakul, cellLp);

						// Setting up ColomnSKS parameters
						Log.d("Skripsi", "mengambil data colom SKS");
						ColomnSKS = new CustomTextView(getActivity());
						ColomnSKS.setText(cek3.getString(SKS));
						ColomnSKS.setTextColor(Color.BLACK);
						ColomnSKS.setGravity(Gravity.CENTER);
						ColomnSKS.setTextSize(14);
						ColomnSKS.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnSKS, cellLp);

						Total2PSetara += Integer.valueOf(cek3.getString(SKS));

						Log.d("Skripsi", "mengambil data colom Semester");
						ColomnSifatMakul = new CustomTextView(getActivity());
						ColomnSifatMakul.setText(cek3.getString(SM));
						ColomnSifatMakul.setTextColor(Color.BLACK);
						ColomnSifatMakul.setTextSize(14);
						ColomnSifatMakul.setGravity(Gravity.CENTER);
						ColomnSifatMakul
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnSifatMakul, cellLp);

						Log.d("Skripsi", "mengambil data colom Semester");
						ColomnNilaiHuruf = new CustomTextView(getActivity());
						ColomnNilaiHuruf.setText(cek3.getString(NH));
						ColomnNilaiHuruf.setTextColor(Color.BLACK);
						ColomnNilaiHuruf.setTextSize(14);
						ColomnNilaiHuruf.setGravity(Gravity.CENTER);
						ColomnNilaiHuruf
								.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnNilaiHuruf, cellLp);

						// Setting up ColomnSemester parameters
						Log.d("Skripsi", "mengambil data colom Semester");
						ColomnSemester = new CustomTextView(getActivity());
						ColomnSemester.setText(cek3.getString(PS));
						ColomnSemester.setTextColor(Color.BLACK);
						ColomnSemester.setTextSize(14);
						ColomnSemester.setGravity(Gravity.CENTER);
						ColomnSemester.setBackgroundResource(R.drawable.edt_bg);
						row.addView(ColomnSemester, cellLp);

						tableLayoutCurrentPilihanSetara.addView(row, rowLp);
					}
				}
			} while (cek1.moveToNext());

		} else {
			Notif3Setara
					.setText("Tidak ada rekomendasi matakuliah wajib yang sudah \n"
							+ "diprogramkan pada semester lalu dan dikonversi ke kurikulum baru "
							+ GenapGanjil);
		}

	}

	public void MakulTerkait() {
		// Row header Table

		tableLayoutTerkait.setStretchAllColumns(true);

		rowHeaderMakulTerkait = new TableRow(getActivity());
		rowHeaderMakulTerkait.setId(300);

		Log.d("Skripsi", "mengambil data colom NamaMakul");
		ColomnNamaMakulTerkait = new CustomTextView(getActivity());
		ColomnNamaMakulTerkait.setText("NAMA MATAKULIAH");
		ColomnNamaMakulTerkait.setTextColor(Color.WHITE);
		ColomnNamaMakulTerkait.setTextSize(14);
		ColomnNamaMakulTerkait.setGravity(Gravity.CENTER);
		ColomnNamaMakulTerkait.setBackgroundResource(R.drawable.tv_bg);
		rowHeaderMakulTerkait.addView(ColomnNamaMakulTerkait, cellLp);

		// Setting up ColomnSKS parameters
		Log.d("Skripsi", "mengambil data colom SKS");
		ColomnSKSTerkait = new CustomTextView(getActivity());
		ColomnSKSTerkait.setText("SKS");
		ColomnSKSTerkait.setTextColor(Color.WHITE);
		ColomnSKSTerkait.setGravity(Gravity.CENTER);
		ColomnSKSTerkait.setTextSize(14);
		ColomnSKSTerkait.setBackgroundResource(R.drawable.tv_bg);
		rowHeaderMakulTerkait.addView(ColomnSKSTerkait, cellLp);

		// Setting up ColomnNilaiHuruf parameters
		Log.d("Skripsi", "mengambil data colom NilaiHuruf");
		ColomnSifatMAkulTerkait = new CustomTextView(getActivity());
		ColomnSifatMAkulTerkait.setText("SIFAT MATAKULIAH");
		ColomnSifatMAkulTerkait.setTextColor(Color.WHITE);
		ColomnSifatMAkulTerkait.setGravity(Gravity.CENTER);
		ColomnSifatMAkulTerkait.setTextSize(14);
		ColomnSifatMAkulTerkait.setBackgroundResource(R.drawable.tv_bg);
		rowHeaderMakulTerkait.addView(ColomnSifatMAkulTerkait, cellLp);

		// Setting up ColomnSemester parameters
		Log.d("Skripsi", "mengambil data colom Semester");
		ColomnSemesterTerkait = new CustomTextView(getActivity());
		ColomnSemesterTerkait.setText("SEMESTER");
		ColomnSemesterTerkait.setTextColor(Color.WHITE);
		ColomnSemesterTerkait.setTextSize(14);
		ColomnSemesterTerkait.setGravity(Gravity.CENTER);
		ColomnSemesterTerkait.setBackgroundResource(R.drawable.tv_bg);
		rowHeaderMakulTerkait.addView(ColomnSemesterTerkait, cellLp);
		tableLayoutTerkait.addView(rowHeaderMakulTerkait, rowLp);

		if (SMTx == 2) {

			// Cek Permakul
			QueryTerkaitCek = "select * from Transkip "
					+ "where KodeMaKul IN ('TIN1313','TIN1202') "
					+ "and NilaiHuruf IN ('D','E','') ";

			CursorCek = db.rawQuery(QueryTerkaitCek, null);
			if (CursorCek.getCount() > 0) {
				QueryTerkait = "select KodeMaKul," + "NamaMakul"
						+ ",SKSTeori + SKSPraktikum as SKS " + ",SifatMakul"
						+ ",PaketSemester " + "from KRS "
						+ "where KodeMakul='TIN1220' ";

				CursorTerkait = db.rawQuery(QueryTerkaitCek, null);
				if (CursorTerkait.getCount() > 0) {
					CursorTerkait.moveToFirst();
					Integer NamaMaKul = CursorTerkait
							.getColumnIndex("NamaMaKul");
					Integer SKS = CursorTerkait.getColumnIndex("SKS");
					Integer SifatMakul = CursorTerkait
							.getColumnIndex("SifatMaKul");
					Integer SemesterJ = CursorTerkait
							.getColumnIndex("PaketSemester");

					tableTerkait(CursorTerkait.getString(NamaMaKul),
							CursorTerkait.getString(SKS),
							CursorTerkait.getString(SifatMakul),
							CursorTerkait.getString(SemesterJ));
				}
			}

			// Cek Permakul
			QueryTerkaitCek = "select * from Transkip "
					+ "where KodeMaKul IN ('TIN1314','TIN1203') "
					+ "and NilaiHuruf IN ('D','E','') ";

			CursorCek = db.rawQuery(QueryTerkaitCek, null);
			if (CursorCek.getCount() > 0) {
				QueryTerkait = "select KodeMaKul," + "NamaMakul"
						+ ",SKSTeori + SKSPraktikum as SKS " + ",SifatMakul"
						+ ",PaketSemester " + "from KRS "
						+ "where KodeMakul='TIN1319' ";

				CursorTerkait = db.rawQuery(QueryTerkaitCek, null);
				if (CursorTerkait.getCount() > 0) {
					CursorTerkait.moveToFirst();
					Integer NamaMaKul = CursorTerkait
							.getColumnIndex("NamaMaKul");
					Integer SKS = CursorTerkait.getColumnIndex("SKS");
					Integer SifatMakul = CursorTerkait
							.getColumnIndex("SifatMaKul");
					Integer SemesterJ = CursorTerkait
							.getColumnIndex("PaketSemester");

					tableTerkait(CursorTerkait.getString(NamaMaKul),
							CursorTerkait.getString(SKS),
							CursorTerkait.getString(SifatMakul),
							CursorTerkait.getString(SemesterJ));
				}
			}

		} else if (SMTx == 3) {
			// Cek Permakul
			QueryTerkaitCek = "select * from Transkip "
					+ "where KodeMaKul IN ('TIN1314','TIN1203','TIN1319','TIN1208','TIN1107') "
					+ "and NilaiHuruf IN ('D','E','') ";

			CursorCek = db.rawQuery(QueryTerkaitCek, null);
			if (CursorCek.getCount() > 0) {
				QueryTerkait = "select KodeMaKul," + "NamaMakul"
						+ ",SKSTeori + SKSPraktikum as SKS " + ",SifatMakul"
						+ ",PaketSemester " + "from KRS "
						+ "where KodeMakul='TIN2327' ";

				CursorTerkait = db.rawQuery(QueryTerkaitCek, null);
				if (CursorTerkait.getCount() > 0) {
					CursorTerkait.moveToFirst();
					Integer NamaMaKul = CursorTerkait
							.getColumnIndex("NamaMaKul");
					Integer SKS = CursorTerkait.getColumnIndex("SKS");
					Integer SifatMakul = CursorTerkait
							.getColumnIndex("SifatMaKul");
					Integer SemesterJ = CursorTerkait
							.getColumnIndex("PaketSemester");

					tableTerkait(CursorTerkait.getString(NamaMaKul),
							CursorTerkait.getString(SKS),
							CursorTerkait.getString(SifatMakul),
							CursorTerkait.getString(SemesterJ));
				}
			}

			// Cek Permakul
			QueryTerkaitCek = "select * from Transkip "
					+ "where KodeMaKul IN ('TIN1323','TIN1208') "
					+ "and NilaiHuruf IN ('D','E','') ";

			CursorCek = db.rawQuery(QueryTerkaitCek, null);
			if (CursorCek.getCount() > 0) {
				QueryTerkait = "select KodeMaKul," + "NamaMakul"
						+ ",SKSTeori + SKSPraktikum as SKS " + ",SifatMakul"
						+ ",PaketSemester " + "from KRS "
						+ "where KodeMakul='TIN2330' ";

				CursorTerkait = db.rawQuery(QueryTerkaitCek, null);
				if (CursorTerkait.getCount() > 0) {
					CursorTerkait.moveToFirst();
					Integer NamaMaKul = CursorTerkait
							.getColumnIndex("NamaMaKul");
					Integer SKS = CursorTerkait.getColumnIndex("SKS");
					Integer SifatMakul = CursorTerkait
							.getColumnIndex("SifatMaKul");
					Integer SemesterJ = CursorTerkait
							.getColumnIndex("PaketSemester");

					tableTerkait(CursorTerkait.getString(NamaMaKul),
							CursorTerkait.getString(SKS),
							CursorTerkait.getString(SifatMakul),
							CursorTerkait.getString(SemesterJ));
				}
			}

		} else if (SMTx == 4) {
			// Cek Permakul
			QueryTerkaitCek = "select * from Transkip "
					+ "where KodeMaKul IN ('TIN1314','TIN1203','TIN1319','TIN1208','TIN1107','TIN2327','TIN2103','TIN2203') "
					+ "and NilaiHuruf IN ('D','E','') ";

			CursorCek = db.rawQuery(QueryTerkaitCek, null);
			if (CursorCek.getCount() > 0) {
				QueryTerkait = "select KodeMaKul," + "NamaMakul"
						+ ",SKSTeori + SKSPraktikum as SKS " + ",SifatMakul"
						+ ",PaketSemester " + "from KRS "
						+ "where KodeMakul='TIN2333' ";

				CursorTerkait = db.rawQuery(QueryTerkaitCek, null);
				if (CursorTerkait.getCount() > 0) {
					CursorTerkait.moveToFirst();
					Integer NamaMaKul = CursorTerkait
							.getColumnIndex("NamaMaKul");
					Integer SKS = CursorTerkait.getColumnIndex("SKS");
					Integer SifatMakul = CursorTerkait
							.getColumnIndex("SifatMaKul");
					Integer SemesterJ = CursorTerkait
							.getColumnIndex("PaketSemester");

					tableTerkait(CursorTerkait.getString(NamaMaKul),
							CursorTerkait.getString(SKS),
							CursorTerkait.getString(SifatMakul),
							CursorTerkait.getString(SemesterJ));
				}
			}

			// Cek Permakul
			QueryTerkaitCek = "select * from Transkip "
					+ "where KodeMaKul IN ('TIN1323','TIN1208','TIN2330','TIN2106','TIN2206') "
					+ "and NilaiHuruf IN ('D','E','') ";

			CursorCek = db.rawQuery(QueryTerkaitCek, null);
			if (CursorCek.getCount() > 0) {
				QueryTerkait = "select KodeMaKul," + "NamaMakul"
						+ ",SKSTeori + SKSPraktikum as SKS " + ",SifatMakul"
						+ ",PaketSemester " + "from KRS "
						+ "where KodeMakul='TIN2334' ";

				CursorTerkait = db.rawQuery(QueryTerkaitCek, null);
				if (CursorTerkait.getCount() > 0) {
					CursorTerkait.moveToFirst();
					Integer NamaMaKul = CursorTerkait
							.getColumnIndex("NamaMaKul");
					Integer SKS = CursorTerkait.getColumnIndex("SKS");
					Integer SifatMakul = CursorTerkait
							.getColumnIndex("SifatMaKul");
					Integer SemesterJ = CursorTerkait
							.getColumnIndex("PaketSemester");

					tableTerkait(CursorTerkait.getString(NamaMaKul),
							CursorTerkait.getString(SKS),
							CursorTerkait.getString(SifatMakul),
							CursorTerkait.getString(SemesterJ));
				}
			}

		} else if (SMTx == 5) {

		} else if (SMTx == 6) {

		} else if (SMTx == 7) {

		} else if (SMTx == 8 || SMTx == 9) {

		}

	}

	public void tableTerkait(String namaMK, String SKS, String SMK, String SMT) {

		// Row Isi Table

		rowTerkait = new TableRow(getActivity());
		rowTerkait.setId(300);

		// Setting up the ColomnNamaMaKul parameters
		Log.d("Skripsi", "mengambil data colom NamaMakul");
		ColomnNamaMakulTerkait = new CustomTextView(getActivity());
		ColomnNamaMakulTerkait.setText(namaMK);
		ColomnNamaMakulTerkait.setTextColor(Color.BLACK);
		ColomnNamaMakulTerkait.setTextSize(14);
		ColomnNamaMakulTerkait.setBackgroundResource(R.drawable.edt_bg);
		rowTerkait.addView(ColomnNamaMakulTerkait, cellLp);

		// Setting up ColomnSKS parameters
		Log.d("Skripsi", "mengambil data colom SKS");
		ColomnSKSTerkait = new CustomTextView(getActivity());
		ColomnSKSTerkait.setText(SKS);
		ColomnSKSTerkait.setTextColor(Color.BLACK);
		ColomnSKSTerkait.setGravity(Gravity.CENTER);
		ColomnSKSTerkait.setTextSize(14);
		ColomnSKSTerkait.setBackgroundResource(R.drawable.edt_bg);
		rowTerkait.addView(ColomnSKSTerkait, cellLp);

		// Setting up ColomnNilaiHuruf parameters
		Log.d("Skripsi", "mengambil data colom NilaiHuruf");
		ColomnSifatMAkulTerkait = new CustomTextView(getActivity());
		ColomnSifatMAkulTerkait.setText(SMK);
		ColomnSifatMAkulTerkait.setTextColor(Color.BLACK);
		ColomnSifatMAkulTerkait.setGravity(Gravity.CENTER);
		ColomnSifatMAkulTerkait.setTextSize(14);
		ColomnSifatMAkulTerkait.setBackgroundResource(R.drawable.edt_bg);
		rowTerkait.addView(ColomnSifatMAkulTerkait, cellLp);

		// Setting up ColomnSemester parameters
		Log.d("Skripsi", "mengambil data colom Semester");
		ColomnSemesterTerkait = new CustomTextView(getActivity());
		ColomnSemesterTerkait.setText(SMT);
		ColomnSemesterTerkait.setTextColor(Color.BLACK);
		ColomnSemesterTerkait.setTextSize(14);
		ColomnSemesterTerkait.setGravity(Gravity.CENTER);
		ColomnSemesterTerkait.setBackgroundResource(R.drawable.edt_bg);
		rowTerkait.addView(ColomnSemesterTerkait, cellLp);

		tableLayoutTerkait.addView(rowTerkait, rowLp);

	}

	/*
	 * Menampilkan mata kuliah Wajib
	 */

	public void AllKRSWajib() {
		Log.d("Skripsix", "query ke TRANSKIP");

		if (SMTx == 7 || SMTx == 8) {

			// seleksi PPL
			if (JumSKSx >= 100) {
				QueryKRS = "select KodeMaKul," + "NamaMakul"
						+ ",SKSTeori + SKSPraktikum as SKS " + ",SifatMakul"
						+ ",PaketSemester " + "from KRS "
						+ "where (PaketSemester='7' or PaketSemester='8')"
						+ " and  SifatMakul='W' and JKurikulum='B'";
			} else {
				QueryKRS = "select KodeMaKul,"
						+ "NamaMakul"
						+ ",SKSTeori + SKSPraktikum as SKS "
						+ ",SifatMakul"
						+ ",PaketSemester "
						+ "from KRS "
						+ "where (PaketSemester='7' or PaketSemester='8')"
						+ " and  SifatMakul='W' and JKurikulum='B' and KodeMaKul!='TIN4259'";
			}

			// seleksi KKN
			if ((NilaiIPKx >= 3.00 && JumSKSx >= 105)
					|| (NilaiIPKx < 3.00 && JumSKSx >= 110)) {
				QueryKRS = "select KodeMaKul," + "NamaMakul"
						+ ",SKSTeori + SKSPraktikum as SKS " + ",SifatMakul"
						+ ",PaketSemester " + "from KRS "
						+ "where (PaketSemester='7' or PaketSemester='8')"
						+ " and  SifatMakul='W' and JKurikulum='B'";
			} else {
				QueryKRS = "select KodeMaKul,"
						+ "NamaMakul"
						+ ",SKSTeori + SKSPraktikum as SKS "
						+ ",SifatMakul"
						+ ",PaketSemester "
						+ "from KRS "
						+ "where (PaketSemester='7' or PaketSemester='8')"
						+ SMTx
						+ " and  SifatMakul='W' and JKurikulum='B' and KodeMaKul!='UIN4411'";
			}

			// seleksi SKRIPSI
			if (JumSKSx >= 130) {
				QueryKRS = "select KodeMaKul," + "NamaMakul"
						+ ",SKSTeori + SKSPraktikum as SKS " + ",SifatMakul"
						+ ",PaketSemester " + "from KRS "
						+ "where (PaketSemester='7' or PaketSemester='8')"
						+ " and  SifatMakul='W' and JKurikulum='B'";
			} else {
				QueryKRS = "select KodeMaKul,"
						+ "NamaMakul"
						+ ",SKSTeori + SKSPraktikum as SKS "
						+ ",SifatMakul"
						+ ",PaketSemester "
						+ "from KRS "
						+ "where (PaketSemester='7' or PaketSemester='8')"
						+ " and  SifatMakul='W' and JKurikulum='B' and KodeMaKul!='TIN4461' and KodeMaKul!='TIN4461'";
			}

		} else {
			QueryKRS = "select KodeMaKul," + "NamaMakul"
					+ ",SKSTeori + SKSPraktikum as SKS " + ",SifatMakul"
					+ ",PaketSemester " + "from KRS " + "where PaketSemester="
					+ SMTx + " and  SifatMakul='W' and JKurikulum='B'";
		}

		String TotQuery = "select sum(SKSTeori + SKSPraktikum) as TotSKS "
				+ "from KRS " + "where PaketSemester=" + SMTx
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
			if (cek == null) {
				totalSKSWajib = 0;
				TotSKSSekar.setVisibility(View.GONE);
			} else {
				totalSKSWajib = Integer.valueOf(cek)
						- (Total1WSetara + Total2WSetara);

				TotSKSSekar
						.setText("TOTAL SKS : "
								+ totalSKSWajib
								+ " \n(Matakuliah yang belum di programkan - Matakuliah yang sudah diprogramkan)");
			}
		}

		tableLayoutCurrent.setStretchAllColumns(true);

		if (AllKRS.getCount() > 0) {
			// Setting Up Header table
			rowHeader2 = new TableRow(getActivity());
			rowHeader2.setId(200);

			Log.d("Skripsi", "mengambil data colom NamaMakul");
			ColomnNamaMakulSekW = new CustomTextView(getActivity());
			ColomnNamaMakulSekW.setText("NAMA MATAKULIAH");
			ColomnNamaMakulSekW.setTextColor(Color.WHITE);
			ColomnNamaMakulSekW.setTextSize(14);
			ColomnNamaMakulSekW.setGravity(Gravity.CENTER);
			ColomnNamaMakulSekW.setBackgroundResource(R.drawable.tv_bg);
			rowHeader2.addView(ColomnNamaMakulSekW, cellLp);

			// Setting up ColomnSKS parameters
			Log.d("Skripsi", "mengambil data colom SKS");
			ColomnSKSSekW = new CustomTextView(getActivity());
			ColomnSKSSekW.setText("SKS");
			ColomnSKSSekW.setTextColor(Color.WHITE);
			ColomnSKSSekW.setGravity(Gravity.CENTER);
			ColomnSKSSekW.setTextSize(14);
			ColomnSKSSekW.setBackgroundResource(R.drawable.tv_bg);
			rowHeader2.addView(ColomnSKSSekW, cellLp);

			// Setting up ColomnNilaiHuruf parameters
			Log.d("Skripsi", "mengambil data colom NilaiHuruf");
			ColomnSifatMAkulSekW = new CustomTextView(getActivity());
			ColomnSifatMAkulSekW.setText("SIFAT MATAKULIAH");
			ColomnSifatMAkulSekW.setTextColor(Color.WHITE);
			ColomnSifatMAkulSekW.setGravity(Gravity.CENTER);
			ColomnSifatMAkulSekW.setTextSize(14);
			ColomnSifatMAkulSekW.setBackgroundResource(R.drawable.tv_bg);
			rowHeader2.addView(ColomnSifatMAkulSekW, cellLp);

			// Setting up ColomnSemester parameters
			Log.d("Skripsi", "mengambil data colom Semester");
			ColomnSemesterSekW = new CustomTextView(getActivity());
			ColomnSemesterSekW.setText("SEMESTER");
			ColomnSemesterSekW.setTextColor(Color.WHITE);
			ColomnSemesterSekW.setTextSize(14);
			ColomnSemesterSekW.setGravity(Gravity.CENTER);
			ColomnSemesterSekW.setBackgroundResource(R.drawable.tv_bg);
			rowHeader2.addView(ColomnSemesterSekW, cellLp);
			tableLayoutCurrent.addView(rowHeader2, rowLp);

			AllKRS.moveToFirst();
			do {
				rowCurrent = new TableRow(getActivity());
				rowCurrent.setId(200);
				// Setting up the ColomnNamaMaKul parameters
				Log.d("Skripsi", "mengambil data colom NamaMakul");
				ColomnNamaMakulSekW = new CustomTextView(getActivity());
				ColomnNamaMakulSekW.setText(AllKRS.getString(NamaMaKul));
				ColomnNamaMakulSekW.setTextColor(Color.BLACK);
				ColomnNamaMakulSekW.setTextSize(14);
				ColomnNamaMakulSekW.setBackgroundResource(R.drawable.edt_bg);
				rowCurrent.addView(ColomnNamaMakulSekW, cellLp);

				// Setting up ColomnSKS parameters
				Log.d("Skripsi", "mengambil data colom SKS");
				ColomnSKSSekW = new CustomTextView(getActivity());
				ColomnSKSSekW.setText(AllKRS.getString(SKS));
				ColomnSKSSekW.setTextColor(Color.BLACK);
				ColomnSKSSekW.setGravity(Gravity.CENTER);
				ColomnSKSSekW.setTextSize(14);
				ColomnSKSSekW.setBackgroundResource(R.drawable.edt_bg);
				rowCurrent.addView(ColomnSKSSekW, cellLp);

				// Setting up ColomnNilaiHuruf parameters
				Log.d("Skripsi", "mengambil data colom NilaiHuruf");
				ColomnSifatMAkulSekW = new CustomTextView(getActivity());
				ColomnSifatMAkulSekW.setText(AllKRS.getString(SifatMakul));
				ColomnSifatMAkulSekW.setTextColor(Color.BLACK);
				ColomnSifatMAkulSekW.setGravity(Gravity.CENTER);
				ColomnSifatMAkulSekW.setTextSize(14);
				ColomnSifatMAkulSekW.setBackgroundResource(R.drawable.edt_bg);
				rowCurrent.addView(ColomnSifatMAkulSekW, cellLp);

				// Setting up ColomnSemester parameters
				Log.d("Skripsi", "mengambil data colom Semester");
				ColomnSemesterSekW = new CustomTextView(getActivity());
				ColomnSemesterSekW.setText(AllKRS.getString(SemesterJ));
				ColomnSemesterSekW.setTextColor(Color.BLACK);
				ColomnSemesterSekW.setTextSize(14);
				ColomnSemesterSekW.setGravity(Gravity.CENTER);
				ColomnSemesterSekW.setBackgroundResource(R.drawable.edt_bg);
				rowCurrent.addView(ColomnSemesterSekW, cellLp);

				tableLayoutCurrent.addView(rowCurrent, rowLp);
			} while (AllKRS.moveToNext());
			// db.close();
		} else {
			// Toast.makeText(getActivity().getApplicationContext(),
			// "Event occurred.", Toast.LENGTH_LONG).show();
			Notif2.setText("Tidak ada rekomendasi matakuliah wajib \n"
					+ "yang perlu diprogramkan");
		}
	}

	/*
	 * Menampilkan mata kuliah Pilihan
	 */
	public void AllKRSPilihan() {

		String QueryKRSPilihan = "select KodeMaKul," + "NamaMakul"
				+ ",SKSTeori + SKSPraktikum as SKS " + ",SifatMakul"
				+ ",PaketSemester " + "from KRS " + "where PaketSemester="
				+ SMTx + " and  SifatMakul='P' and JKurikulum='B'";

		String TotQuery = "select sum(SKSTeori + SKSPraktikum) as TotSKS "
				+ "from KRS " + "where PaketSemester=" + SMTx
				+ " and  SifatMakul='P' and JKurikulum='B'";
		Cursor AllKRSPilihan = db.rawQuery(QueryKRSPilihan, null);

		Cursor TotSKSSekarangPilihan = db.rawQuery(TotQuery, null);
		// untuk MakulUIN
		Integer NamaMaKul = AllKRSPilihan.getColumnIndex("NamaMaKul");
		Integer SKS = AllKRSPilihan.getColumnIndex("SKS");
		Integer SifatMakul = AllKRSPilihan.getColumnIndex("SifatMaKul");
		Integer SemesterJ = AllKRSPilihan.getColumnIndex("PaketSemester");

		Integer TotSKSSekPil = TotSKSSekarangPilihan.getColumnIndex("TotSKS");

		if (TotSKSSekarangPilihan.getCount() > 0) {
			TotSKSSekarangPilihan.moveToFirst();
			do {
				cek = TotSKSSekarangPilihan.getString(TotSKSSekPil);
			} while (TotSKSSekarangPilihan.moveToNext());
			if (cek == null) {
				TotSKSSekarP.setVisibility(View.GONE);
			} else {
				totalSKSPilihan = Integer.valueOf(cek)
						- (Total1PSetara + Total2PSetara);

				TotSKSSekarP
						.setText("TOTAL SKS : "
								+ totalSKSPilihan
								+ " \n(Matakuliah yang belum di programkan - Matakuliah yang sudah diprogramkan)");
			}
		}

		tableLayoutCurrent.setStretchAllColumns(true);

		if (AllKRSPilihan.getCount() > 0) {
			// Setting Up Header table
			rowHeader3 = new TableRow(getActivity());
			rowHeader3.setId(300);

			Log.d("Skripsi", "mengambil data colom NamaMakul");
			ColomnNamaMakulSekP = new CustomTextView(getActivity());
			ColomnNamaMakulSekP.setText("NAMA MATAKULIAH");
			ColomnNamaMakulSekP.setTextColor(Color.WHITE);
			ColomnNamaMakulSekP.setTextSize(14);
			ColomnNamaMakulSekP.setGravity(Gravity.CENTER);
			ColomnNamaMakulSekP.setBackgroundResource(R.drawable.tv_bg);
			rowHeader3.addView(ColomnNamaMakulSekP, cellLp);

			// Setting up ColomnSKS parameters
			Log.d("Skripsi", "mengambil data colom SKS");
			ColomnSKSSekP = new CustomTextView(getActivity());
			ColomnSKSSekP.setText("SKS");
			ColomnSKSSekP.setTextColor(Color.WHITE);
			ColomnSKSSekP.setGravity(Gravity.CENTER);
			ColomnSKSSekP.setTextSize(14);
			ColomnSKSSekP.setBackgroundResource(R.drawable.tv_bg);
			rowHeader3.addView(ColomnSKSSekP, cellLp);

			// Setting up ColomnNilaiHuruf parameters
			Log.d("Skripsi", "mengambil data colom NilaiHuruf");
			ColomnSifatMAkulSekP = new CustomTextView(getActivity());
			ColomnSifatMAkulSekP.setText("SIFAT MATAKULIAH");
			ColomnSifatMAkulSekP.setTextColor(Color.WHITE);
			ColomnSifatMAkulSekP.setGravity(Gravity.CENTER);
			ColomnSifatMAkulSekP.setTextSize(14);
			ColomnSifatMAkulSekP.setBackgroundResource(R.drawable.tv_bg);
			rowHeader3.addView(ColomnSifatMAkulSekP, cellLp);

			// Setting up ColomnSemester parameters
			Log.d("Skripsi", "mengambil data colom Semester");
			ColomnSemesterSekP = new CustomTextView(getActivity());
			ColomnSemesterSekP.setText("SEMESTER");
			ColomnSemesterSekP.setTextColor(Color.WHITE);
			ColomnSemesterSekP.setTextSize(14);
			ColomnSemesterSekP.setGravity(Gravity.CENTER);
			ColomnSemesterSekP.setBackgroundResource(R.drawable.tv_bg);
			rowHeader3.addView(ColomnSemesterSekP, cellLp);
			tableLayoutCurrentPilihan.addView(rowHeader3, rowLp);

			AllKRSPilihan.moveToFirst();
			do {
				rowPilihan = new TableRow(getActivity());
				rowPilihan.setId(300);
				// Setting up the ColomnNamaMaKul parameters
				Log.d("Skripsi", "mengambil data colom NamaMakul");
				ColomnNamaMakulSekP = new CustomTextView(getActivity());
				ColomnNamaMakulSekP.setText(AllKRSPilihan.getString(NamaMaKul));
				ColomnNamaMakulSekP.setTextColor(Color.BLACK);
				ColomnNamaMakulSekP.setTextSize(14);
				ColomnNamaMakulSekP.setBackgroundResource(R.drawable.edt_bg);
				rowPilihan.addView(ColomnNamaMakulSekP, cellLp);

				// Setting up ColomnSKS parameters
				Log.d("Skripsi", "mengambil data colom SKS");
				ColomnSKSSekP = new CustomTextView(getActivity());
				ColomnSKSSekP.setText(AllKRSPilihan.getString(SKS));
				ColomnSKSSekP.setTextColor(Color.BLACK);
				ColomnSKSSekP.setGravity(Gravity.CENTER);
				ColomnSKSSekP.setTextSize(14);
				ColomnSKSSekP.setBackgroundResource(R.drawable.edt_bg);
				rowPilihan.addView(ColomnSKSSekP, cellLp);

				// Setting up ColomnNilaiHuruf parameters
				Log.d("Skripsi", "mengambil data colom NilaiHuruf");
				ColomnSifatMAkulSekP = new CustomTextView(getActivity());
				ColomnSifatMAkulSekP.setText(AllKRSPilihan
						.getString(SifatMakul));
				ColomnSifatMAkulSekP.setTextColor(Color.BLACK);
				ColomnSifatMAkulSekP.setGravity(Gravity.CENTER);
				ColomnSifatMAkulSekP.setTextSize(14);
				ColomnSifatMAkulSekP.setBackgroundResource(R.drawable.edt_bg);
				rowPilihan.addView(ColomnSifatMAkulSekP, cellLp);

				// Setting up ColomnSemester parameters
				Log.d("Skripsi", "mengambil data colom Semester");
				ColomnSemesterSekP = new CustomTextView(getActivity());
				ColomnSemesterSekP.setText(AllKRSPilihan.getString(SemesterJ));
				ColomnSemesterSekP.setTextColor(Color.BLACK);
				ColomnSemesterSekP.setTextSize(14);
				ColomnSemesterSekP.setGravity(Gravity.CENTER);
				ColomnSemesterSekP.setBackgroundResource(R.drawable.edt_bg);
				rowPilihan.addView(ColomnSemesterSekP, cellLp);

				tableLayoutCurrentPilihan.addView(rowPilihan, rowLp);
			} while (AllKRSPilihan.moveToNext());
			// db.close();
		} else {
			// Toast.makeText(getActivity().getApplicationContext(),
			// "Event occurred.", Toast.LENGTH_LONG).show();
			Notif3.setText("Tidak ada rekomendasi matakuliah pilihan \n"
					+ "yang perlu diprogramkan");
		}
	}
}