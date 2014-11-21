package com.netlynxtech.advancedmonitor.fragments;

import info.hoang8f.widget.FButton;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.netlynxtech.advancedmonitor.ChooseDeviceActivity;
import com.netlynxtech.advancedmonitor.R;

public class TutorialThreeFragment extends Fragment {
	FButton bSetup;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.tutorial_fragment_threee_layout, container, false);
		bSetup = (FButton) rootView.findViewById(R.id.bSetup);
		bSetup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), ChooseDeviceActivity.class));
				getActivity().finish();
			}
		});
		return rootView;
	}
}