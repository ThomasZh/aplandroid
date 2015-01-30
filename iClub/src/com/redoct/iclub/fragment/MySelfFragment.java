package com.redoct.iclub.fragment;

import com.redoct.iclub.R;
import com.redoct.iclub.ui.activity.MyInformationActivity;
import com.redoct.iclub.util.activity.BaseActivityUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

public class MySelfFragment extends Fragment implements OnClickListener{
	private View contentView;
	private TextView tv_title,tv_alterpass,tv_quit,tv_friends,tv_connectors;
	private RelativeLayout rl_myinformation;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_myself, container,
				false);
		initView();
		
		return contentView;
	}

	private void initView() {
		// TODO Auto-generated method stub
		tv_title = (TextView) contentView.findViewById(R.id.mTitleView);
		tv_title.setText(R.string.me);
		tv_alterpass = (TextView) contentView.findViewById(R.id.alterpass);
		tv_alterpass.setOnClickListener(this);
		tv_quit = (TextView) contentView.findViewById(R.id.quit);
		tv_quit.setOnClickListener(this);
		tv_friends = (TextView) contentView.findViewById(R.id.friends);
		tv_friends.setOnClickListener(this);
		tv_connectors = (TextView) contentView.findViewById(R.id.connctor);
		tv_connectors.setOnClickListener(this);
		rl_myinformation = (RelativeLayout) contentView.findViewById(R.id.myself_information);
		rl_myinformation.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.alterpass:
			
			break;
		case R.id.quit:
			
			break;
		case R.id.connctor:
			
			break;
		case R.id.friends:
			
			break;
		case R.id.myself_information:
			BaseActivityUtil.startActivity(getActivity(),MyInformationActivity.class, false);
			break;

	
		}
	}
}
