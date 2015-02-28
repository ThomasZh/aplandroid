package com.redoct.iclub.ui.activity;

import org.apache.mina.core.session.IoSession;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oct.ga.comm.domain.account.AccountDetailInfo;
import com.redoct.iclub.MainActivity;
import com.redoct.iclub.R;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.config.AppConfig;
import com.redoct.iclub.task.GetAccountTask;
import com.redoct.iclub.task.ServerConfigTask;
import com.redoct.iclub.task.StpClient;
import com.redoct.iclub.task.StpHandler;
import com.redoct.iclub.task.UserLoginTask;
import com.redoct.iclub.util.UserInformationLocalManagerUtil;

public class SplashActivity extends Activity{
	
	private TextView mShowInfoTv;
	
	private ProgressBar mProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		mShowInfoTv=(TextView)findViewById(R.id.mShowInfoTv);
		mProgressBar=(ProgressBar)findViewById(R.id.mProgressBar);
		
		iClubApplication.api = new StpClient(new StpHandler(iClubApplication.api) {

			@Override
			public void sessionClosed(IoSession session) throws Exception {
				super.sessionClosed(session);

				Log.e("zyf", "stp handler session closed......");
			}

			@Override
			public void sessionOpened(IoSession session) throws Exception {
				super.sessionOpened(session);

				Log.e("zyf", "stp handler session opened......");
			}
		});
		
		ServerConfigTask server = new ServerConfigTask() {
			
			@Override
			public void callback() {
				
				Log.e("zyf", "get gate keeper call back......");
				
				//autoLogin();
				
				Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
				SplashActivity.this.startActivity(intent);
				
				finish();
			}

			@Override
			public void failure() {
				
				Log.e("zyf", "get gate keeper failed......");

				updateShowInfo();
			}

			@Override
			public void before() {

				Log.e("zyf", "start getting gate keeper......");
			}

			@Override
			public void timeout() {
				
				Log.e("zyf", "get gate keeper time out......");
				
				cancel(true);
				
				updateShowInfo();
			}
		};
		server.setTimeOutEnabled(true, 10*1000);
		server.safeExecute();
		
	}
	
	private void updateShowInfo(){
		mProgressBar.setVisibility(View.GONE);
		mShowInfoTv.setText("服务不可用，请稍后重试......");
	}
	
	private void autoLogin(){
		
		UserLoginTask login = new UserLoginTask("thomas@c2c.com","t") {
			
			public void callback() {
				
				Log.e("zyf", "auto login call back......");
				
				fetchAccountInfo();
				
				Intent intent=new Intent(SplashActivity.this,MainActivity.class);
				SplashActivity.this.startActivity(intent);
				
				SplashActivity.this.finish();
			}

			public void failure() {
				
				Log.e("zyf", "auto login failed......");
				
				updateShowInfo();
			}

			public void before() {
				
				Log.e("zyf", "start auto login......");
			}

			@Override
			public void timeout() {

				Log.e("zyf", "auto login time out.......");

				this.cancel(true);
				
				updateShowInfo();
			}

		};

		login.setTimeOutEnabled(true, 10*1000);
		login.safeExecute();
	}
	
	private GetAccountTask getTask;
	
	private void fetchAccountInfo() {
		if (!AppConfig.isLoggedIn())
			return;

		getTask= new GetAccountTask() {
			
			@Override
			public void callback() {
				
				Log.e("zyf", "get account call back......");
				
				AccountDetailInfo act = getTask.getAccount();
				
				if (act != null) {
                    Log.e("zyf","get accout success......");
					new UserInformationLocalManagerUtil(getApplicationContext()).WriteUserInformation(act);
				}
			}

			@Override
			public void failure() {
				Log.e("zyf","get accout  failure.....");

			}

			@Override
			public void complete() {
				getTask = null;
			}

			@Override
			public void pullback() {
				getTask = null;
			}

			@Override
			public void before() {
			}
		};
		getTask.safeExecute();
	}
}
