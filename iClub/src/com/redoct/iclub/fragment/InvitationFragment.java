package com.redoct.iclub.fragment;

import com.redoct.iclub.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InvitationFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView=inflater.inflate(R.layout.fragment_invitation, container, false);
		
		return contentView;
	}
}
