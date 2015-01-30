package com.redoct.iclub.fragment;

import com.redoct.iclub.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MyPlansFragment extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView=inflater.inflate(R.layout.fragment_my_plans, container, false);
		
		initTitle(contentView);
		
		return contentView;
	}
	
	private void initTitle(View contentView){
		
		TextView mTitleView=(TextView) contentView.findViewById(R.id.mTitleView);
		mTitleView.setText(getResources().getString(R.string.my_plans));
	}

}
