package com.redoct.iclub.task;

import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.util.NetworkChecker;

import android.R.integer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.text.ICUCompat;
import android.util.Log;

/**
 * Created by liwenzhi on 14-9-23.
 */
public class TemplateTask extends AsyncTask<Void, Void, Boolean> {

    private Runnable next;
    private boolean networkAvailable;
    
    //<add by Kevin.zhang 2015.1.27
    private boolean timeOutEnabled;
    private int time;
    public void setTimeOutEnabled(boolean timeOutEnabled,int time){
    	this.timeOutEnabled=timeOutEnabled;
    	this.time=time;
    }
    private Handler mHandler=new Handler();
    Runnable timeoutRunnable=new Runnable() {
		
		@Override
		public void run() {
			
			timeout();
		}
	};
    //>end by Kevin.zhang

    /**
     * each time before send request to check this!
     *
     * @return
     */
    public boolean isRunning(){
        return iClubApplication.apiRunning();
    	
    	//return false;
    }

    /**
     * network check before sending request
     * @2014/12/29
     *
     * @return execute result
     */
    public boolean safeExecute(){
        if(isRunning()) return false;
    	
    	if(iClubApplication.apiOk()){
    		Log.e("zyf", "api is ok.......");
    	}else{
    		Log.e("zyf", "api is not not not ok.......");
    	}

        this.execute();

        return true;
    }


    @Override
    protected void onPreExecute(){
        before();
        
        if(timeOutEnabled){
        	mHandler.postDelayed(timeoutRunnable, time);
        }
    }


    @Override
    protected Boolean doInBackground(Void... params) {
    	
        if(!NetworkChecker.available()){
            networkAvailable = false;
            return false;
        }else {
            networkAvailable = true;
        }

        connectCheck();

        return justTodo();
    }


    /**
     * for subclass to implement
     *
     * @return
     */
    protected boolean justTodo(){
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
    	
    	if(timeOutEnabled){
    		Log.e("zyf", "call back result,remove time out.......");
    		mHandler.removeCallbacks(timeoutRunnable);
    	}
    	
    	if(isCancelled()){
    		
    		Log.e("zyf", "task is cancelled,so did not do any actions......");
    		
    		return;
    	}
    	
        if(success) {
            callback();
            if (next != null) next.run();
        } else {
            if(!networkAvailable){
                unavailable();
            }else {
                failure();
            }
        }
        complete();//clear job
    }

    @Override
    protected void onCancelled() {
        pullback();
        
        complete();
    }




//    ******** call back area *****************

    /**
     * before sending request, to show progress...
     * @2014/12/29
     */
    public void before() {

    }

    /**
     * on success
     *
     */
    public void callback() {

    }

    /**
     * on failure
     */
    public void failure() {

    }

    /**
     * on cancel
     */
    public void pullback() {

    }

    /**
     * network unavailable
     */
    private void unavailable(){
        //BusProvider.getInstance().post(new NetworkUnavailable());
    }

    /**
     * when complete to do
     */
    public void then(Runnable next) {
        this.next = next;
    }

    /**
     * callback/failure and next execute finished!
     * lwz7512@2014/10/30
     */
    public void complete() {

    }

    /**
     * server connected callback only used in subclass
     */
    protected void connectCheck(){

    }

    /**
     * network timeout callback
     * @2014/12/16
     */
    public void timeout(){
        
    }

}
