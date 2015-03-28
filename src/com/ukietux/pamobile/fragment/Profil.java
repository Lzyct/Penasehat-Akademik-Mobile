package com.ukietux.pamobile.fragment;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
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
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.ukietux.pamobile.utils.CustomImageView;
import com.ukietux.pamobile.utils.GalleryUtil;
import com.ukietux.pamobile.R;
import com.ukietux.pamobile.database.DBController;

public class Profil extends Fragment {
	SQLiteDatabase db;
	TextView Nimx, Namax, JumSKSx, NilaiIPKx, SMTx;
	CustomImageView ProfileImage;

	private String selectedImagePath;
	private static final int SELECT_PICTURE = 1;
	private Uri mCropImagedUri;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.profil, container, false);
		v.setBackgroundResource(R.drawable.profil_bg);
		Namax = (TextView) v.findViewById(R.id.Nama);
		Nimx = (TextView) v.findViewById(R.id.Nim);
		NilaiIPKx = (TextView) v.findViewById(R.id.IPK);
		JumSKSx = (TextView) v.findViewById(R.id.JumSKS);
		SMTx = (TextView) v.findViewById(R.id.Semester);

		ProfileImage = (CustomImageView) v.findViewById(R.id.profilImage);

		SharedPreferences pref = getActivity()
				.getSharedPreferences("Profil", 0);
		String profilPath = pref.getString("profil_pic", "empty");

		Log.d("Cekidot", profilPath);
		if (profilPath == "empty") {
			ProfileImage.setImageResource(R.drawable.profil);
		} else {

			ProfileImage.setImageBitmap(BitmapFactory.decodeFile(profilPath));
		}

		ProfileImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					// cropping image
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					intent.putExtra("crop", "true");
					intent.putExtra("aspectX", 0);
					intent.putExtra("aspectY", 0);
					// set resolution 400x400
					intent.putExtra("outputX", 400);
					intent.putExtra("outputY", 400);
					intent.putExtra("return-data", false);

					File f = createNewFile("PROFIL_");
					try {
						f.createNewFile();
					} catch (IOException ex) {
						Log.e("io", ex.getMessage());
					}

					mCropImagedUri = Uri.fromFile(f);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImagedUri);
					// start the activity - we handle returning in
					// onActivityResult
					startActivityForResult(intent, SELECT_PICTURE);
				} catch (ActivityNotFoundException anfe) {
					// display an error message

				}
			}

		});

		Log.d("Skripsi", "memanggil DBController");
		DBController controler = new DBController(getActivity());
		Log.d("Skripsi", "mengakses database");
		db = controler.getWritableDatabase();

		displayProfil();
		return v;

	}

	private File createNewFile(String prefix) {
		if (prefix == null || "".equalsIgnoreCase(prefix)) {
			prefix = "IMG_";
		}
		// Saved Crop Image Directory
		File newDirectory = new File(Environment.getExternalStorageDirectory()
				+ "/DCIM/PAMOBILE/");
		if (!newDirectory.exists()) {
			if (newDirectory.mkdir()) {
				Log.d(this.getClass().getName(), newDirectory.getAbsolutePath()
						+ " directory created");
			}
		}
		File file = new File(newDirectory,
				(prefix + System.currentTimeMillis() + ".png"));
		if (file.exists()) {
			// this wont be executed
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return file;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {

				selectedImagePath = mCropImagedUri.getPath(); // To get image path															// path
				SharedPreferences pref = getActivity().getSharedPreferences(
						"Profil", 0);
				SharedPreferences.Editor edt = pref.edit();
				edt.putString("profil_pic", selectedImagePath);
				edt.commit();

				ProfileImage.setImageBitmap(BitmapFactory
						.decodeFile(selectedImagePath));
				Log.d("cekgambar", "Image Path : " + selectedImagePath);

			}
		}

	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	private void displayProfil() {
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
		Integer Nim = a.getColumnIndex("Nim");
		Integer Nama = a.getColumnIndex("Nama");
		Integer JumSKS = a.getColumnIndex("JumSKS");
		Integer IPK = a.getColumnIndex("IPK");
		Integer Semester = a.getColumnIndex("Semester");

		if (a.getCount() > 0) {
			// Scanning value field by raw Cursor
			a.moveToFirst();
			do {

				Log.d("Skripsi", "mengambil data colom Nama");
				Namax.setText("Nama : " + a.getString(Nama));
				Namax.setTextColor(Color.BLACK);
				Namax.setGravity(Gravity.CENTER_HORIZONTAL);
				// Setting up the ColomnNim parameters
				Log.d("Skripsi", "mengambil data colom nim");
				Nimx.setText("Nim : " + a.getString(Nim));
				Nimx.setTextColor(Color.BLACK);
				Nimx.setGravity(Gravity.CENTER_HORIZONTAL);
				Log.d("Skripsix", a.getString(Nim));

				// Setting up the ColomnNama parameters

				Log.d("Skripsi", "mengambil data colom nim");

				Double IPKx = Double.valueOf(a.getString(IPK));
				NilaiIPKx.setText("IPK = "
						+ new DecimalFormat("#.##").format(IPKx));
				NilaiIPKx.setTextColor(Color.BLACK);
				NilaiIPKx.setGravity(Gravity.CENTER_HORIZONTAL);
				Log.d("Skripsix", a.getString(IPK));

				// Setting up the ColomnNama parameters
				Log.d("Skripsi", "mengambil data colom Nama");
				JumSKSx.setText("SKS Dilulusi : " + a.getString(JumSKS));
				JumSKSx.setTextColor(Color.BLACK);
				JumSKSx.setGravity(Gravity.CENTER_HORIZONTAL);
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