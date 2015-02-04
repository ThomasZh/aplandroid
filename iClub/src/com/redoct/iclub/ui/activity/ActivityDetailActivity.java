package com.redoct.iclub.ui.activity;

import com.redoct.iclub.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ActivityDetailActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_detail);
		initView();
	}

	private void initView() {

		TextView mTitleView = (TextView) findViewById(R.id.mTitleView);
		mTitleView.setText(getResources().getString(R.string.friends));
		Button btnRigthButton = (Button) findViewById(R.id.rightBtn);
		btnRigthButton.setVisibility(View.VISIBLE);
		btnRigthButton.setText("添加");
		btnRigthButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				
			}
		});

	}

}
