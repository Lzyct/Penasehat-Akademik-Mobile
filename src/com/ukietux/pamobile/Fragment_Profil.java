package com.ukietux.pamobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Fragment_Profil extends Fragment {
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    final View v = inflater.inflate(R.layout.profil, container, false);
	    return v;
	  }
}
