package com.ukietux.pamobile.fragment;

import com.ukietux.pamobile.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class KRS extends Fragment {
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    final View v = inflater.inflate(R.layout.krs, container, false);
	    return v;
	  }
}