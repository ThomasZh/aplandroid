package com.redoct.iclub;

import java.io.UnsupportedEncodingException;

import org.apache.mina.core.session.IoSession;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.StpCommand;
import com.redoct.iclub.config.AppConfig;
import com.redoct.iclub.task.ServerConfigTask;
import com.redoct.iclub.task.StpClient;
import com.redoct.iclub.task.StpHandler;
import com.redoct.iclub.task.UserLoginTask;
import com.redoct.iclub.util.DeviceUtil;
import com.redoct.iclub.util.NetworkChecker;
import com.redoct.iclub.util.ToastUtil;

public class iClubApplication extends Application implements
		Thread.UncaughtExceptionHandler {

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {

		super.onCreate();
		//Thread.setDefaultUncaughtExceptionHandler(this);

		initImageLoader(getApplicationContext());

		NetworkChecker.init(this);

		AppConfig.DEVICE_ID = DeviceUtil.getRawDeviceId(this);
		Log.e("zyf", "device_id: " + AppConfig.DEVICE_ID);

		AppConfig.OS_VERSION = DeviceUtil.getDeviceOS();
		Log.e("zyf", "os: " + AppConfig.OS_VERSION);

		api = new StpClient(new StpHandler(api) {

			/*
			 * @Override public void sessionClosed(IoSession session) throws
			 * Exception { // TODO Auto-generated method stub
			 * super.sessionClosed(session);
			 * 
			 * Log.e("zyf", "stp handler session closed......"); }
			 * 
			 * @Override public void sessionOpened(IoSession session) throws
			 * Exception { // TODO Auto-generated method stub
			 * super.sessionOpened(session);
			 * 
			 * Log.e("zyf", "stp handler session opened......"); }
			 */
		});
		IntentFilter mFilter = new IntentFilter(); 
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
		

	}

	public static void initImageLoader(Context context) {

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app

				.build();
		ImageLoader.getInstance().init(config);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		System.exit(0);
	}

	private static StpClient api;

	public static void connect(String ip, int port) {

		api.start(ip, port);

	}

	public static RespCommand send(StpCommand req) throws InterruptedException,
			UnsupportedEncodingException {
		if (api == null)
			return null;

		RespCommand respCommand = api.send(req);

		return respCommand;
	}

	public static void close() {
		api.close();
	}

	public static boolean apiOk() {
		return api.isConnected();
	}

	public static boolean apiRunning() {
		return api.isRunning();
	}

	private void reconnect() {
		/**
		 * status check is required!
		 * 
		 * @2014/12/15
		 */
		if (api.isConnected()) {
			Log.w("sima", ">>> app server connected!");
			return;
		}

		Log.d("sima", ">>> connecting server...");

		ServerConfigTask server = new ServerConfigTask() {
			@Override
			public void callback() {
				Log.d("sima", "stp server connected!");
			}

			@Override
			public void complete() {

				Log.d("sima", ">>>>> connected successfully!");
			}

			@Override
			public void failure() {

			}

			@Override
			public void pullback() {

			}

			@Override
			public void before() {

			}
		};
		server.safeExecute();
		server.then(new AutoLogin());

	}
	
	private class AutoLogin implements Runnable{
        @Override
        public void run() {
            
        	UserLoginTask login = new UserLoginTask("thomas.zh@qq.com", "t"){
                public void callback(){
                    //popupHomepage();
                }
                public void failure(){
                   // showToast(R.string.login_failure);
//                    popupLogin();//re login
                }
                public void complete(){
                    /*login = null;
                    BusProvider.getInstance().post(new ProgressEvent(false));*/
                }
                public void before(){
                    //BusProvider.getInstance().post(new ProgressEvent(true));
                }
                
				@Override
				public void timeout() {
					// TODO Auto-generated method stub
					super.timeout();
					
					Log.e("zyf", "time out.......");
					
					this.cancel(true);
				}
         
            };
            
            //login.setTimeOutEnabled(true, 100);
            login.safeExecute();

        }
    }

	private BroadcastReceiver mReceiver =

	new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

				ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = connectivityManager.getActiveNetworkInfo();
				if (info != null && info.isAvailable()) {
					reconnect();
				} else {
					ToastUtil.toastshort(getApplicationContext(), "net is notwork");

				}
			}
		}
	};

}