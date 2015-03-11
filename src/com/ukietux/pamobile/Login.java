package com.ukietux.pamobile;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ukietux.pamobile.R;
import com.ukietux.pamobile.database.DBController;
import com.ukietux.pamobile.database.JSONParser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends ActionBarActivity {

	Button daftar, login;
	Intent a;
	EditText nim;
	String url, url1, success;
	SessionManager session;
	DBController controller = new DBController(this);
	HashMap<String, String> queryValues;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		session = new SessionManager(getApplicationContext());
		Toast.makeText(getApplicationContext(),
				"User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG)
				.show();

		login = (Button) findViewById(R.id.login);
		nim = (EditText) findViewById(R.id.nim);

		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				url = "http://1467fb1b.ngrok.com/PAMobile/masuk.php?" + "Nim="
						+ nim.getText().toString();
				url1 = "http://1467fb1b.ngrok.com/PAMobile/matakuliah.php";

				if (nim.getText().toString().trim().length() > 0) {
					new Masuk().execute();
				} else {
					Toast.makeText(getApplicationContext(),
							"Masukkan Nim yang benar", Toast.LENGTH_LONG)
							.show();
				}

			}
		});

		if (session.isLoggedIn() == true) {
			a = new Intent(Login.this, MainActivity.class);
			startActivity(a);
			finish();
		}
	}

	public class Masuk extends AsyncTask<String, String, String> {
		ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pDialog = new ProgressDialog(Login.this);
			pDialog.setMessage("Mengambil data dari server");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			Log.d("Skripsi", "insert data ke dataMHS");
			JSONParser jParser = new JSONParser();

			JSONObject json = jParser.getJSONFromUrl(url);
			JSONObject json1 = jParser.getJSONFromUrl(url1);

			try {
				success = json.getString("success");

				Log.e("error", "nilai sukses=" + success);

				JSONArray hasil = json.getJSONArray("login");
				JSONArray hasil1 = json1.getJSONArray("matakuliah");

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
					// Add NilaiHuruf extracted from Object
					queryValues.put("NilaiHuruf", jsonobj.get("NilaiHuruf")
							.toString());
					// Add Semester extracted from Object
					queryValues.put("Semester", jsonobj.get("Semester")
							.toString());
					// Add SKS extracted from Object
					queryValues.put("SKS", jsonobj.get("SKS").toString());
					// Insert User into SQLite DB
					controller.insertDataMHS(queryValues);
					Log.d("Skripsi", "insert data ke dataMHS");
				}
				
				for (int i = 0; i < hasil1.length(); i++) {
					JSONObject jsonobj = hasil1.getJSONObject(i);
					queryValues = new HashMap<String, String>();
					// Add nim extracted from Object
					queryValues.put("NamaMaKul", jsonobj.get("NamaMaKul").toString());
					// Add Nama extracted from Object
					queryValues.put("SKSTeori", jsonobj.get("SKSTeori").toString());
					// Add NamaMaKul extracted from Object
					queryValues.put("SKSPraktikum", jsonobj.get("SKSPraktikum")
							.toString());
					// Add NilaiHuruf extracted from Object
					queryValues.put("PaketSemester", jsonobj.get("PaketSemester")
							.toString());
					// Add Semester extracted from Object
					queryValues.put("SifatMaKul", jsonobj.get("SifatMaKul")
							.toString());
					// Add SKS extracted from Object
					queryValues.put("JKurikulum", jsonobj.get("JKurikulum").toString());
					// Insert User into SQLite DB
					controller.insertMataKuliah(queryValues);
					Log.d("Skripsi", "insert data ke MataKuliah");
				}

				if (success.equals("1")) {

					for (int i = 0; i < hasil.length(); i++) {

						JSONObject c = hasil.getJSONObject(i);

						String nim = c.getString("Nim").trim();
						session.createLoginSession(nim);
						Log.e("ok", " ambil data");

					}
				} else {
					Log.e("erro", "tidak bisa ambil data 0");
				}

			} catch (Exception e) {
				// TODO: handle exception
				Log.e("erro", "tidak bisa ambil data 1");
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
				a = new Intent(Login.this, MainActivity.class);
				startActivity(a);
				finish();
			} else {

				Toast.makeText(getApplicationContext(), "Masukkan Nim Anda",
						Toast.LENGTH_LONG).show();
			}

		}

	}

}
