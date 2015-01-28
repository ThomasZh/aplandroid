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

public class MyPlansFragment extends Fragment implements OnClickListener{
	
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
		
		Button leftBtn=(Button) contentView.findViewById(R.id.leftBtn);
		Button rightBtn=(Button) contentView.findViewById(R.id.rightBtn);
		
		leftBtn.setVisibility(View.VISIBLE);
		rightBtn.setVisibility(View.VISIBLE);
		
		leftBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		case R.id.leftBtn:
			
			break;
        case R.id.rightBtn:
			
			break;
		default:
			break;
		}
	}
}
