package com.ukietux.pamobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ukietux.pamobile.Login.Masuk;
import com.ukietux.pamobile.database.DBController;
import com.ukietux.pamobile.database.JSONParser;
import com.ukietux.pamobile.fragment.TranskipNilai;
import com.ukietux.pamobile.fragment.KRS;
import com.ukietux.pamobile.fragment.Profil;
import com.ukietux.pamobile.fragment.KHS;
import com.ukietux.pamobile.utils.ConnectionStatus;
import com.ukietux.pamobile.utils.CustomAdapter;
import com.ukietux.pamobile.utils.RowItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	// deklarasi title, icon dan url menggunakan string array
	String[] menutitles;
	TypedArray menuIcons;

	String FirstPage;

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView sliding_listview;

	private Toolbar toolbar;
	private CharSequence mTitle;

	private List<RowItem> rowItems;
	private CustomAdapter adapter;

	// get session
	SessionManager session;
	DBController controler;
	JSONArray contacts = null;
	String nim;
	String url, url1, success;

	SQLiteDatabase db;
	HashMap<String, String> queryValues;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// get session
		session = new SessionManager(getApplicationContext());
//		Toast.makeText(getApplicationContext(),
//				"User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG)
//				.show();

		session.checkLogin();

		HashMap<String, String> user = session.getUserDetails();

		nim = user.get(SessionManager.KEY_NIM);
		// id = user.get(SessionManager.KEY_ID);

		url = "http://skripsi.ngrok.com/PAMobile/masuk.php?" + "Nim=" + nim;
		url1 = "http://skripsi.ngrok.com/PAMobile/matakuliah.php";

		mTitle = getTitle();

		// menampung string array ke variable
		menutitles = getResources().getStringArray(R.array.titles);
		menuIcons = getResources().obtainTypedArray(R.array.icons);

		// tampilan drawer
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		sliding_listview = (ListView) findViewById(R.id.drawer_list);

		// toolbar
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			toolbar.setTitle(null);
			setSupportActionBar(toolbar);
		}

		rowItems = new ArrayList<RowItem>();
		// add menu menggunakan list view custom adapter
		for (int i = 0; i < menutitles.length; i++) {
			RowItem items = new RowItem(menutitles[i], menuIcons.getResourceId(
					i, -1));
			rowItems.add(items);
		}

		menuIcons.recycle();

		adapter = new CustomAdapter(getApplicationContext(), rowItems);

		sliding_listview.setAdapter(adapter);

		sliding_listview.setOnItemClickListener(new SlideitemListener());
		sliding_listview.setSelector(R.drawable.list_view_selector);

		// Enabling Home button
		getSupportActionBar().setHomeButtonEnabled(true);

		// Enabling Up navigation
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#8E44AD")));
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.string.exit, R.string.exit) {

			@SuppressLint("ResourceAsColor")
			public void onDrawerClosed(View view) {
				(new Handler()).postDelayed(new Runnable() {
					@Override
					public void run() {

						final Typeface myFont = Typeface.createFromAsset(
								getAssets(), "fonts/Roboto.ttf");
						// mengirim value string mtitle ke update display
						String mTitleFix = (String) mTitle;
						String str = String.valueOf(mTitleFix);

						str = str.toUpperCase(Locale.getDefault());
						SpannableString s = new SpannableString(str);
						s.setSpan(new TypefaceSpan("fonts/Roboto.ttf"), 0, s.length(),
						        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

						toolbar.setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + s + "</font>"));
						
						// calling onPrepareOptionsMenu() to show action bar
						// icons
						supportInvalidateOptionsMenu();
					}
				}, 200);
			}

			@SuppressLint("ResourceAsColor")
			public void onDrawerOpened(View drawerView) {
				(new Handler()).postDelayed(new Runnable() {
					@Override
					public void run() {
						final Typeface myFont = Typeface.createFromAsset(
								getAssets(), "fonts/Roboto.ttf");
						// mengirim value string mtitle ke update display

						String str = String.valueOf("PENASEHAT AKADEMIK");
						str = str.toUpperCase(Locale.getDefault());
						SpannableString s = new SpannableString(str);
						s.setSpan(new TypefaceSpan("fonts/Roboto.ttf"), 0, s.length(),
						        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

						toolbar.setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + s + "</font>"));
						// calling onPrepareOptionsMenu() to hide action bar
						// icons
						supportInvalidateOptionsMenu();
						// updateViews();
					}
				}, 200);

			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			updateDisplay(0);

		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// cek jika drawer layout aktif dan tombol back di tekan maka drawer di
		// tutup
		if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
			mDrawerLayout.closeDrawer(Gravity.LEFT);
		} else {
			super.onBackPressed();
		}
	}

	class SlideitemListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			updateDisplay(position);
			// grep selected menu from custom adapter
			((CustomAdapter) sliding_listview.getAdapter())
					.selectItem(position);
			((ListView) parent).invalidateViews();
		}

	}

	private void updateDisplay(int position) {
		final Fragment f;

		switch (position) {
		case 0:
			f = new Profil();
			break;
		case 1:
			f = new KHS();
			break;
		case 2:
			f = new TranskipNilai();
			break;
		case 3:
			f = new KRS();
			break;
		default:
			return;
		}
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, f).commit();

		// set title action bar dari method set title

		setTitle(menutitles[position]);

		// close sliding menu setelah link list view di klik
		mDrawerLayout.closeDrawer(sliding_listview);

	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void setTitle(CharSequence title) {

		mTitle = title;
		final Typeface myFont = Typeface.createFromAsset(getAssets(),
				"fonts/Roboto.ttf");
		// mengirim value string mtitle ke update display
		String str = String.valueOf(mTitle);
		str = str.toUpperCase(Locale.getDefault());
		SpannableString s = new SpannableString(str);
		s.setSpan(new TypefaceSpan("fonts/Roboto.ttf"), 0, s.length(),
		        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		toolbar.setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + s + "</font>"));
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.keluar:
			Keluar();
			return true;
		case R.id.update:
			Update();
			return true;
		case R.id.about:
			about();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// showing about on pop up notification

	public void about() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false)
				.setTitle("About")
				.setMessage(
						"Penasehat Akademik Mobile 1.0\n"
								+ "Development by : Mudassir\n\n\n\n\n\n\n"
								+ "+--------------------------------+\n"
								+ "   Contact Developer:\n"
								+ "   ukie.tux@gmail.com\n"
								+ "+--------------------------------+")
				.setIcon(R.drawable.ic_about)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void Keluar() {

		// connect to dbcontroller

		controler = new DBController(getApplicationContext());
		Log.d("Skripsi", "mengakses database");
		db = controler.getWritableDatabase();

		Context context = MainActivity.this;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false)
				.setTitle("Peringatan!")
				.setMessage("Yakin ingin keluar")
				.setIcon(R.drawable.ic_warning)
				.setCancelable(false)
				.setPositiveButton("Keluar",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity
								Log.d("Skripsi", "delete table");
								controler.deleteAll();
								Log.d("Skripsi", "hapus session");
								session.logoutUser();
								Log.d("Skripsi",
										"berhasil di hapus dan memulai kembali ke login");
								finish();
							}
						})
				.setNegativeButton("Batal",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();

							}
						});
		AlertDialog alert = builder.create();
		alert.show();

	}

	public void Update() {
		controler = new DBController(getApplicationContext());
		Log.d("Skripsi", "mengakses database");
		db = controler.getWritableDatabase();
		ConnectionStatus cs = new ConnectionStatus(getApplicationContext());

		Boolean isInternetPresent = cs.isConnectingToInternet();

		if (isInternetPresent == true) {
				controler.deleteAll();
				new Masuk().execute();
		
		} else {
			Toast.makeText(
					getApplicationContext(),
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

			pDialog = new ProgressDialog(MainActivity.this);
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
					controler.insertDataMHS(queryValues);
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
					queryValues.put("SKSPraktikum", jsonobj.get("SKSPraktikum")
							.toString());
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
					controler.insertMataKuliah(queryValues);
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
					Toast.makeText(getApplicationContext(),
							"Server sedang down", Toast.LENGTH_LONG).show();
					Log.e("erro", "tidak bisa ambil data 0");
				}

			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(getApplicationContext(), "Server sedang down",
						Toast.LENGTH_LONG).show();
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
				Toast.makeText(getApplicationContext(),
						"Berhasil Memperbarui Data ", Toast.LENGTH_LONG).show();
			} else {

				Toast.makeText(getApplicationContext(), "Masukkan Nim Anda",
						Toast.LENGTH_LONG).show();
			}

		}

	}

	/** Called whenever we call supportInvalidateOptionsMenu(); */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the drawer is open, hide action items related to the content view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(sliding_listview);

		menu.findItem(R.id.about).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
