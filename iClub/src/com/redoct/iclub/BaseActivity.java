package com.redoct.iclub;

/**   
 * 文件名：BaseActivity.java   
 * 版本号：        
 * 日期：2012-6-20 
 * 创建人：
 * Copyright wadata 版权所有
 * 变更：
 */
 
 
import java.util.List;

import com.oct.ga.comm.domain.account.AccountDetailInfo;
import com.redoct.iclub.config.AppConfig;
import com.redoct.iclub.task.BadgeNumberQueryTask;
import com.redoct.iclub.task.GetAccountTask;
import com.redoct.iclub.task.ServerConfigTask;
import com.redoct.iclub.task.UserLoginTask;
import com.redoct.iclub.ui.activity.LoginActivity;
import com.redoct.iclub.ui.activity.SplashActivity;
import com.redoct.iclub.util.MyProgressDialogUtils;
import com.redoct.iclub.util.PersistentUtil;
import com.redoct.iclub.util.UserInformationLocalManagerUtil;
import com.redoct.iclub.widget.MyToast;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
 
/**
 * 名称：BaseActivity 
 * 描述： 
 * 创建人： 
 * 日期：2012-6-20 下午5:53:35 
 * 变更：
 */
 
public class BaseActivity extends FragmentActivity{
	
	private MyProgressDialogUtils mLoginProgressDialogUtils;
	
    @Override
    protected void onStop() {
            // TODO Auto-generated method stub
                super.onStop();
 
                if (!isAppOnForeground()) {
                       
                	iClubApplication.isAlive=false;
                    goBack();
                    
                }
                
              
        }
 
        @Override
        protected void onResume() {
                
                super.onResume();
 
                if(iClubApplication.isAlive==false){
                	goForward();
                }
                iClubApplication.isAlive=true;
             
        }
 
        /**
     * 程序是否在前台运行
     * 
     * @return
     */
        public boolean isAppOnForeground() {
                
                ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                String packageName = getApplicationContext().getPackageName();
 
                List<RunningAppProcessInfo> appProcesses = activityManager
                                .getRunningAppProcesses();
                if (appProcesses == null)
                        return false;
 
                for (RunningAppProcessInfo appProcess : appProcesses) {

                        if (appProcess.processName.equals(packageName)
                                        && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                                return true;
                        }
                }
 
                return false;
        }
        
        public void goBack(){
        	
        	Log.e("app", "程序切换到后台.......");
    	
    	    iClubApplication.api.close();
       }
    
       public void goForward(){
    	
    	Log.e("app", "程序从后台唤醒.......");
    	
    	ServerConfigTask server = new ServerConfigTask() {
			
			@Override
			public void callback() {
				
				Log.e("app", "get gate keeper call back......");
				
				autoLogin();
			}

			@Override
			public void failure() {
				
				Log.e("app", "get gate keeper failed......");

				mLoginProgressDialogUtils.dismissDialog();
				MyToast.makeText(BaseActivity.this, true, R.string.load_failed, MyToast.LENGTH_LONG).show();
			}

			@Override
			public void before() {

				Log.e("app", "start getting gate keeper......");
				
				mLoginProgressDialogUtils=new MyProgressDialogUtils(R.string.progress_dialog_loading, BaseActivity.this);
				mLoginProgressDialogUtils.showDialog();
			}

			@Override
			public void timeout() {
				
				Log.e("app", "get gate keeper time out......");
				
				cancel(true);
				
				mLoginProgressDialogUtils.dismissDialog();
				MyToast.makeText(BaseActivity.this, true, R.string.load_failed, MyToast.LENGTH_LONG).show();
			}
		};
		server.setTimeOutEnabled(true, 10*1000);
		server.safeExecute();
    }
    
    private void autoLogin(){
		/*String pass = PersistentUtil.getInstance().readString(this,"passWord", "");
		String name = PersistentUtil.getInstance().readString(this,"loginName", "");*/
		
		String name = iClubApplication.userName;
		String pass = iClubApplication.psw;
		
		if(name!=null&&name.length()>0&&pass!=null&&pass.length()>0){
		
			UserLoginTask login = new UserLoginTask(name,pass) {
				
				public void callback() {
					
					Log.e("app", "auto login call back......");
					
					getBadgeNumber();
					
					fetchAccountInfo();
					
					mLoginProgressDialogUtils.dismissDialog();
					MyToast.makeText(BaseActivity.this, true, R.string.load_success, MyToast.LENGTH_LONG).show();
				}
	
				public void failure() {
					
					Log.e("app", "auto login failed......");
					
					mLoginProgressDialogUtils.dismissDialog();
					MyToast.makeText(BaseActivity.this, true, R.string.load_failed, MyToast.LENGTH_LONG).show();
				}
	
				public void before() {
					
					Log.e("app", "start auto login......");
				}
	
				@Override
				public void timeout() {
	
					Log.e("app", "auto login time out.......");
	
					this.cancel(true);
					
					mLoginProgressDialogUtils.dismissDialog();
					MyToast.makeText(BaseActivity.this, true, R.string.load_failed, MyToast.LENGTH_LONG).show();
				}
	
			};
	
			login.setTimeOutEnabled(true, 10*1000);
			login.safeExecute();
			
		}else {
			
			Log.e("app", "用户尚未登录......");
			
			mLoginProgressDialogUtils.dismissDialog();
			MyToast.makeText(BaseActivity.this, true, R.string.load_success, MyToast.LENGTH_LONG).show();
		}
	}
	
	private GetAccountTask getTask;
	
	private void fetchAccountInfo() {
		if (!AppConfig.isLoggedIn())
			return;

		getTask= new GetAccountTask() {
			
			@Override
			public void callback() {
				
				Log.e("app", "get account call back......");
				
				AccountDetailInfo act = getTask.getAccount();
				
				if (act != null) {
                    Log.e("app","get accout success......");
					new UserInformationLocalManagerUtil(getApplicationContext()).WriteUserInformation(act);
				}
			}

			@Override
			public void failure() {
				Log.e("app","get accout  failure.....");

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
	
	public void getBadgeNumber(){
		
		BadgeNumberQueryTask mBadgeNumberQueryTask=new BadgeNumberQueryTask(){

			@Override
			public void callback() {
				super.callback();
				
				MainActivity.handleUnReadMessage(iClubApplication.badgeNumber);
			}
			
		};
		mBadgeNumberQueryTask.safeExecute();
	}
}