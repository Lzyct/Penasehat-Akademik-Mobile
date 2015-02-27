package com.ukietux.pamobile.fragment;

import com.ukietux.pamobile.R;
import com.ukietux.pamobile.database.DBController;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;

public class CekNilai extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.cek_nilai, container, false);
		 DBController controller = new DBController(getActivity());
	    ArrayList<HashMap<String, String>> dataMHSList = controller.getdataMHS();
	        /* jikta tidak kosong, tampilkan data kampus ke ListView
	         * 
	         */
//	        if (dataMHSList.size() != 0) {
//	     
//	          ListAdapter adapter = new SimpleAdapter(CekNilai.this,
//	        		  dataMHSList, R.layout.cek_nilai_listview, new String[] {
//	                  "Nim", "Nama", "NamaMaKul","NilaiHuruf", "Semester", "SKS" }, new int[] {
//	                  R.id.Nim, R.id.Nama, R.id.NamaMaKul});
//	          setListAdapter(adapter);
//	        }
		return v;
	}
}