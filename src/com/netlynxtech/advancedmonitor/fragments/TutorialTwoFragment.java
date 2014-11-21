package com.netlynxtech.advancedmonitor.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netlynxtech.advancedmonitor.R;

public class TutorialTwoFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.tutorial_fragment_twoo_layout, container, false);
		return rootView;
	}
}