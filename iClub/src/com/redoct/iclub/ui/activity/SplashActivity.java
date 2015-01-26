package com.redoct.iclub.ui.activity;

import com.redoct.iclub.MainActivity;
import com.redoct.iclub.R;
import com.redoct.iclub.util.activity.BaseActivityUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity{
	
	@SuppressLint("HandlerLeak")
	private Handler handle = new Handler() {
		public void handleMessage(android.os.Message msg) {

			BaseActivityUtil.startActivity(SplashActivity.this,MainActivity.class, true);

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		handle.sendEmptyMessageDelayed(1, 3000);
		
	}
}
