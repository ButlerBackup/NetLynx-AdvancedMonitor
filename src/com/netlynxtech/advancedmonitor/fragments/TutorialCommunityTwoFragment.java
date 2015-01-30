package com.netlynxtech.advancedmonitor.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.netlynxtech.advancedmonitor.R;
import com.netlynxtech.advancedmonitor.RegisterPhoneActivity;

public class TutorialCommunityTwoFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.tutorial_community_fragment_two_layout, container, false);
		rootView.findViewById(R.id.bSetup).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), RegisterPhoneActivity.class).putExtra("tutorialOnly", true));
				getActivity().finish();
			}
		});
		return rootView;
	}
}