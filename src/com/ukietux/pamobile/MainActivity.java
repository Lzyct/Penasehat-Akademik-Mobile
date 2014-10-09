package com.ukietux.pamobile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.ukietux.pamobile.fragment.CekNilai;
import com.ukietux.pamobile.fragment.KRS;
import com.ukietux.pamobile.fragment.Profil;
import com.ukietux.pamobile.fragment.Settings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {

	// deklarasi title, icon dan url menggunakan string array
	String[] menutitles;
	TypedArray menuIcons;

	String FirstPage;

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView sliding_listview;

	private CharSequence mTitle;

	private List<RowItem> rowItems;
	private CustomAdapter adapter;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = getTitle();

		// menampung string array ke variable
		menutitles = getResources().getStringArray(R.array.titles);
		menuIcons = getResources().obtainTypedArray(R.array.icons);

		// tampilan drawer
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		sliding_listview = (ListView) findViewById(R.id.drawer_list);

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
				new ColorDrawable(Color.parseColor("#00b16a")));

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {

			@SuppressLint("ResourceAsColor")
			public void onDrawerClosed(View view) {
				(new Handler()).postDelayed(new Runnable() {
					@Override
					public void run() {

						final Typeface myFont = Typeface.createFromAsset(
								getAssets(), "fonts/manteka.ttf");
						// mengirim value string mtitle ke update display
						String mTitleFix = (String) mTitle;
						String str = String.valueOf(mTitleFix);

						str = str.toUpperCase(Locale.getDefault());
						SpannableString s = new SpannableString(str);
						MetricAffectingSpan span = new MetricAffectingSpan() {
							@Override
							public void updateMeasureState(TextPaint p) {
								p.setTypeface(myFont);
								p.setColor(R.color.white);
							}

							@Override
							public void updateDrawState(TextPaint tp) {
								tp.setTypeface(myFont);
								tp.setColor(R.color.white);
							}
						};

						s.setSpan(span, 0, s.length(),
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						getSupportActionBar().setTitle(s);
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
								getAssets(), "fonts/manteka.ttf");
						// mengirim value string mtitle ke update display

						String str = String.valueOf("(PA) Mobile");
						str = str.toUpperCase(Locale.getDefault());
						SpannableString s = new SpannableString(str);
						MetricAffectingSpan span = new MetricAffectingSpan() {
							@Override
							public void updateMeasureState(TextPaint p) {
								p.setTypeface(myFont);
								p.setColor(R.color.white);
							}

							@Override
							public void updateDrawState(TextPaint tp) {
								tp.setTypeface(myFont);
								tp.setColor(R.color.white);
							}
						};

						s.setSpan(span, 0, s.length(),
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						getSupportActionBar().setTitle(s);

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
			f = new CekNilai();
			break;
		case 2:
			f = new KRS();
			break;
		case 3:
			f = new Settings();
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
				"fonts/manteka.ttf");
		// mengirim value string mtitle ke update display
		String str = String.valueOf(mTitle);
		str = str.toUpperCase(Locale.getDefault());
		SpannableString s = new SpannableString(str);
		MetricAffectingSpan span = new MetricAffectingSpan() {
			@Override
			public void updateMeasureState(TextPaint p) {
				p.setTypeface(myFont);
				p.setColor(R.color.white);
			}

			@Override
			public void updateDrawState(TextPaint tp) {
				tp.setTypeface(myFont);
				tp.setColor(R.color.white);
			}
		};

		s.setSpan(span, 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		getSupportActionBar().setTitle(s);

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
		case R.id.about:
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
