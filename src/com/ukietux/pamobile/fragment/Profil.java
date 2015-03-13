package com.ukietux.pamobile.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ukietux.pamobile.R;
import com.ukietux.pamobile.database.DBController;

public class Profil extends Fragment {
	SQLiteDatabase database, db;
	TextView Nimx, Namax, JumSKSx, NilaiIPKx, SMTx;
	ImageView PP;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.profil, container, false);
		Nimx = (TextView) v.findViewById(R.id.Nim);
		Namax = (TextView) v.findViewById(R.id.Nama);
		NilaiIPKx = (TextView) v.findViewById(R.id.IPK);
		JumSKSx = (TextView) v.findViewById(R.id.JumSKS);
		SMTx = (TextView) v.findViewById(R.id.Semester);

		PP = (ImageView) v.findViewById(R.id.ProfilPicture);

		SharedPreferences pref = getActivity().getPreferences(0);
		String profil = pref.getString("profil_pic", "empty");

		if (profil == "empty") {
			PP.setImageResource(R.drawable.profil);
		} else {
			
			PP.setImageBitmap(BitmapFactory.decodeFile(profil));
		}

		PP.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, 1);
			}

		});

		Log.d("Skripsi", "memanggil DBController");
		DBController controler = new DBController(getActivity());
		Log.d("Skripsi", "mengakses database");
		db = controler.getWritableDatabase();

		displayIPK();
		return v;

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (data != null) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Activity act = getActivity();
			Cursor cursor = act.managedQuery(selectedImage, filePathColumn,
					null, null, null);

			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			PP.setImageBitmap(BitmapFactory.decodeFile(picturePath));

			SharedPreferences pref = getActivity().getPreferences(0);
			SharedPreferences.Editor edt = pref.edit();
			edt.putString("profil_pic", picturePath);
			edt.commit();

			cursor.close();
		} else {
			Toast.makeText(getActivity(), "Try Again!!", Toast.LENGTH_SHORT)
					.show();
		}

	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	private void displayIPK() {
		Log.d("Skripsix", "query ke dataMHS");
		String query = "SELECT Nama,Nim,NilaiHuruf,SKS,SUM(SKS) as JumSKS,count(distinct Semester)+1 as Semester,SUM(CASE WHEN NilaiHuruf= 'A' THEN 4*SKS WHEN NilaiHuruf= 'B' THEN 3*SKS WHEN NilaiHuruf= 'C' THEN 2*SKS WHEN NilaiHuruf= 'D' THEN 1*SKS ELSE 0*SKS END)*1.0/SUM(SKS)*1.0 AS IPK FROM DataMHS WHERE NilaiHuruf!=''";
		Cursor a = db.rawQuery(query, null);
		Integer Nim = a.getColumnIndex("Nim");
		Integer Nama = a.getColumnIndex("Nama");
		Integer JumSKS = a.getColumnIndex("JumSKS");
		Integer IPK = a.getColumnIndex("IPK");
		Integer Semester = a.getColumnIndex("Semester");

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

				Log.d("Skripsi", "mengambil data colom Nama");
				SMTx.setText("Semester : " + a.getString(Semester));
				SMTx.setTextColor(Color.BLACK);
				SMTx.setGravity(Gravity.CENTER_HORIZONTAL);
				Log.d("Skripsix", a.getString(Semester));

			} while (a.moveToNext());
			db.close();
		} else {
			// Toast.makeText(getActivity().getApplicationContext(),
			// "Event occurred.", Toast.LENGTH_LONG).show();
		}

	}
}