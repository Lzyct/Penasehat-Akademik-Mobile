package com.ukietux.pamobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ukietux.pamobile.R;

public class Profil extends Fragment {
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    final View v = inflater.inflate(R.layout.profil_fragment, container, false);
	    return v;
	    
	  }
}
