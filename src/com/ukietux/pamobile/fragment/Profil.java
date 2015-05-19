package com.ukietux.pamobile.fragment;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ukietux.pamobile.utils.ConnectionStatus;
import com.ukietux.pamobile.utils.CustomImageView;
import com.ukietux.pamobile.utils.CustomTextView;
import com.ukietux.pamobile.R;
import com.ukietux.pamobile.SessionManager;
import com.ukietux.pamobile.database.DBController;
import com.ukietux.pamobile.database.JSONParser;

public class Profil extends Fragment {
	SQLiteDatabase db;
	CustomTextView Nimx, Namax, JumSKSx, NilaiIPKx, SMTx;
	CustomImageView ProfileImage;

	private String selectedImagePath;
	private static final int SELECT_PICTURE = 1;
	private Uri mCropImagedUri;
	

	// get session
	SessionManager session;
	DBController controler;
	JSONArray contacts = null;
	String nim;
	String url, url1, url2, success;

	HashMap<String, String> queryValues;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.profil, container, false);
		v.setBackgroundResource(R.drawable.profil_bg);
		Namax = (CustomTextView) v.findViewById(R.id.Nama);
		Nimx = (CustomTextView) v.findViewById(R.id.Nim);
		NilaiIPKx = (CustomTextView) v.findViewById(R.id.IPK);
		JumSKSx = (CustomTextView) v.findViewById(R.id.JumSKS);
		SMTx = (CustomTextView) v.findViewById(R.id.Semester);
		
		// get session
		session = new SessionManager(getActivity());

		session.checkLogin();
		HashMap<String, String> user = session.getUserDetails();
		nim = user.get(SessionManager.KEY_NIM);
		url = "http://ukietux.ngrok.com/PAMobile/masuk.php?" + "Nim=" + nim;
		url1 = "http://ukietux.ngrok.com/PAMobile/matakuliah.php";
		url2 = "http://ukietux.ngrok.com/PAMobile/penyetaraan.php";

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
					// set resolution 200x200
					intent.putExtra("outputX", 200);
					intent.putExtra("outputY", 200);
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

				selectedImagePath = mCropImagedUri.getPath(); // To get image
																// path // path
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

	public void peringatan() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setCancelable(false)
				.setTitle("Peringatan!")
				.setMessage("Sebelumnya aplikasi gagal memperbarui data \n"
						+ "Silakan perbarui ulang data Anda")
				.setIcon(R.drawable.ic_warning)
				.setCancelable(false)
				.setPositiveButton("Perbarui Data",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								Update();
							}
						})
				.setNegativeButton("Tidak Sekarang",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								getActivity().finish();

							}
						});
		AlertDialog alert = builder.create();
		alert.show();
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
				+ "AS IPK " + "FROM TRANSKIP WHERE NilaiHuruf!=''";
		Cursor a = db.rawQuery(query, null);
		Integer Nim = a.getColumnIndex("Nim");
		Integer Nama = a.getColumnIndex("Nama");
		Integer JumSKS = a.getColumnIndex("JumSKS");
		Integer IPK = a.getColumnIndex("IPK");
		Integer Semester = a.getColumnIndex("Semester");
		
		String query1 = "SELECT KodeMKBaru from Penyetaraan";
		Cursor a1 = db.rawQuery(query1, null);
		Integer qr1 = a1.getColumnIndex("KodeMKBaru");
		String query2 = "SELECT KodeMaKul from KRS";
		Cursor a2 = db.rawQuery(query2, null);
		Integer qr2 = a2.getColumnIndex("KodeMaKul");

		if (a.getCount() > 0 ||a1.getCount() > 0 ||a2.getCount() > 0) {
			// Scanning value field by raw Cursor
			a.moveToFirst();
			a1.moveToFirst();
			a2.moveToFirst();
			do {
				if (a.getString(Nama) == null||a1.getString(qr1)==null || a2.getString(qr2)==null) {
					peringatan();
				} else {
					Log.d("Skripsi", "mengambil data colom Nama");
					Namax.setText(a.getString(Nama));
					Namax.setGravity(Gravity.CENTER_HORIZONTAL);

					// Setting up the ColomnNim parameters

					Log.d("Skripsi", "mengambil data colom nim");
					Nimx.setText("> \tNIM = " + a.getString(Nim));
					Nimx.setGravity(Gravity.CENTER_HORIZONTAL);
					// Log.d("Skripsix", a.getString(Nim));

					// Setting up the ColomnNama parameters

					// Log.d("Skripsi", "mengambil data colom nim");

					Double IPKx = Double.valueOf(a.getString(IPK));
					NilaiIPKx.setText("> \tIPK =  "
							+ new DecimalFormat("#.##").format(IPKx));
					NilaiIPKx.setGravity(Gravity.CENTER_HORIZONTAL);
					// Log.d("Skripsix", a.getString(IPK));

					// Setting up the ColomnNama parameters
					Log.d("Skripsi", "mengambil data colom Nama");
					JumSKSx.setText("> \tSKS DILULUSI = " + a.getString(JumSKS));
					JumSKSx.setGravity(Gravity.CENTER_HORIZONTAL);
					// Log.d("Skripsix", a.getString(JumSKS));

					Log.d("Skripsi", "mengambil data colom Nama");
					SMTx.setText("> \tSEMESTER  = " + a.getString(Semester));
					SMTx.setGravity(Gravity.CENTER_HORIZONTAL);
					// Log.d("Skripsix", a.getString(Semester));
				}
			} while (a.moveToNext());
			db.close();
		} else {
			// Toast.makeText(getActivity().getApplicationContext(),
			// "Event occurred.", Toast.LENGTH_LONG).show();
		}

	}

	public void Update() {
		controler = new DBController(getActivity());
		Log.d("Skripsi", "mengakses database");
		db = controler.getWritableDatabase();
		ConnectionStatus cs = new ConnectionStatus(getActivity());

		Boolean isInternetPresent = cs.isConnectingToInternet();

		if (isInternetPresent == true) {

			new Masuk().execute();

		} else {
			Toast.makeText(
					getActivity(),
					"Tidak dapat terhubung ke server pastikan Anda terhubung dengan Internet",
					Toast.LENGTH_LONG).show();
		}
		Log.d("Skripsi", "delete table");

	}

	public class Masuk extends AsyncTask<String, String, String> {
		ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Sedang mengambil data dari server \n"
					+ "Harap tidak menghentikan proses ini");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			Log.d("Skripsi", "insert data ke dataMHS");
			JSONParser jParser = new JSONParser();

			JSONObject json = jParser.getJSONFromUrl(url);
			JSONObject json1 = jParser.getJSONFromUrl(url1);
			JSONObject json2 = jParser.getJSONFromUrl(url2);
			if (json != null && json1 != null && json2 != null) {
				controler.deleteAll();
				try {
					success = json.getString("success");

					Log.e("error", "nilai sukses=" + success);

					JSONArray hasil = json.getJSONArray("login");
					JSONArray hasil1 = json1.getJSONArray("matakuliah");
					JSONArray hasil2 = json2.getJSONArray("setara");

					Log.d("Skripsi", "insert looping data ke dataMHS");
					for (int i = 0; i < hasil.length(); i++) {
						JSONObject jsonobj = hasil.getJSONObject(i);
						queryValues = new HashMap<String, String>();
						// Add nim extracted from Object
						queryValues.put("Nim", jsonobj.get("Nim").toString());
						// Add Nama extracted from Object
						queryValues.put("Nama", jsonobj.get("Nama").toString());
						// Add NamaMaKul extracted from Object
						queryValues.put("NamaMaKul", jsonobj.get("NamaMaKul")
								.toString());
						// Add KodeNamaMaKul extracted from Object
						queryValues.put("KodeMaKul", jsonobj.get("KodeMaKul")
								.toString());
						// Add NilaiHuruf extracted from Object
						queryValues.put("NilaiHuruf", jsonobj.get("NilaiHuruf")
								.toString());
						// Add Semester extracted from Object
						queryValues.put("Semester", jsonobj.get("Semester")
								.toString());
						// Add SKS extracted from Object
						queryValues.put("SKS", jsonobj.get("SKS").toString());
						// Insert User into SQLite DB
						controler.insertTRANSKIP(queryValues);
						Log.d("Skripsi", "insert data ke dataMHS");
					}

					for (int i = 0; i < hasil1.length(); i++) {
						JSONObject jsonobj = hasil1.getJSONObject(i);
						queryValues = new HashMap<String, String>();
						// Add nim extracted from Object
						queryValues.put("KodeMaKul", jsonobj.get("KodeMaKul")
								.toString());
						queryValues.put("NamaMaKul", jsonobj.get("NamaMaKul")
								.toString());
						// Add Nama extracted from Object
						queryValues.put("SKSTeori", jsonobj.get("SKSTeori")
								.toString());
						// Add NamaMaKul extracted from Object
						queryValues.put("SKSPraktikum",
								jsonobj.get("SKSPraktikum").toString());
						// Add NilaiHuruf extracted from Object
						queryValues.put("PaketSemester",
								jsonobj.get("PaketSemester").toString());
						// Add Semester extracted from Object
						queryValues.put("SifatMaKul", jsonobj.get("SifatMaKul")
								.toString());
						// Add SKS extracted from Object
						queryValues.put("JKurikulum", jsonobj.get("JKurikulum")
								.toString());
						// Insert User into SQLite DB
						controler.insertKRS(queryValues);
						Log.d("Skripsi", "insert data ke MataKuliah");
					}

					for (int i = 0; i < hasil2.length(); i++) {
						JSONObject jsonobj = hasil2.getJSONObject(i);
						queryValues = new HashMap<String, String>();
						// Add nim extracted from Object
						queryValues.put("KodeMKBaru", jsonobj.get("KodeMKBaru")
								.toString());
						queryValues.put("KodeMKLama", jsonobj.get("KodeMKLama")
								.toString());
						// Insert User into SQLite DB
						controler.insertPenyetaraan(queryValues);
						Log.d("Skripsi", "insert data ke MataKuliah");
					}

				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(getActivity(),
							"Server sedang down", Toast.LENGTH_LONG).show();
					Log.e("erro", "tidak bisa ambil data 1");
					
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.d("Skripsi", "On PostExecute");

			pDialog.dismiss();
			if (success.equals("1")) {
				Toast.makeText(getActivity(),
						"Berhasil Memperbarui Data ", Toast.LENGTH_LONG).show();
			} else {

				Toast.makeText(getActivity(), "Masukkan Nim Anda",
						Toast.LENGTH_LONG).show();
			}

		}

	}
}