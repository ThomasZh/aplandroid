package com.redoct.iclub.ui.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.crypto.Mac;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oct.ga.comm.LogErrorMessage;
import com.oct.ga.comm.domain.club.ActivityDetailInfo;
import com.redoct.iclub.R;
import com.redoct.iclub.item.ActivityDetailsItem;
import com.redoct.iclub.task.ActivityCreateTask;
import com.redoct.iclub.task.ActivityUpdateTask;
import com.redoct.iclub.util.Constant;
import com.redoct.iclub.util.MyProgressDialogUtils;
import com.redoct.iclub.widget.DateTimeSelectorDialog;
import com.redoct.iclub.widget.MyToast;

public class ActivityCreateActivity extends Activity implements OnClickListener{
	
	private RelativeLayout mLocationContainer,mStartDateTimeConatiner;
	
	private LinearLayout mEndDateTimeConatiner;
	
	private TextView mThemeTv,mStartDateTv,mEndDateTv,mStartTimeTv,mEndTimeTv;
	
	private EditText mLocationEt,mDescEt;
	
	private String theme;
	
	private String id;
	
	private String locX="";
	private String locY="";
	
	private int startYear,startMonth,startDay;
	private String startDayOfWeek;
	private int startHour=10;
	private int startMinute=0;
	private int endYear,endMonth,endDay;
	private String endDayOfWeek;
	private int endHour=10;
	private int endMinute=30;
	
	private ActivityCreateTask mCreateActivityTask;
	private ActivityUpdateTask mUpdateActivityTask;
	
	private ActivityDetailsItem mActivityDetailsItem;
	
	private MyProgressDialogUtils mProgressDialogUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_activity_create);
		
		initViews();
		
		mActivityDetailsItem=(ActivityDetailsItem) getIntent().getSerializableExtra("ActivityDetailsItem");
		if(mActivityDetailsItem!=null){
			Log.e("zyf", "activity edit mode......");
			
			initDatas();
			
			initTitle();
			
			return;
		}else{
			
			theme=getIntent().getStringExtra("theme");
			id=getIntent().getStringExtra("id");
			
			mThemeTv.setText(theme);
			
			try {
				
				initDateTime();
			} catch (Exception e) {
				Log.e("zyf", e.toString());
			}
			
			initTitle();
		}
	}
	
	private void initDatas(){
		
		theme=mActivityDetailsItem.getName();
		id=mActivityDetailsItem.getId();
		
		mThemeTv.setText(theme);
		
		mDescEt.setText(mActivityDetailsItem.getDesc());
		
		mLocationEt.setText(mActivityDetailsItem.getLocDesc());
		locX=mActivityDetailsItem.getLocX();
		locY=mActivityDetailsItem.getLocY();
		
		//开始时间
		Date date=new Date(((long)mActivityDetailsItem.getStartTime())*1000);
		
		Calendar calendar=new GregorianCalendar();
		calendar.setTime(date);
		
		//获取星期几
		/*SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
	    Log.e("zyf", "init date: "+dateFm.format(date))*/;
		
		startYear=calendar.get(calendar.YEAR);
		startMonth=calendar.get(calendar.MONTH)+1;
		startDay=calendar.get(calendar.DAY_OF_MONTH);
		startHour=calendar.get(calendar.HOUR_OF_DAY);
		startMinute=calendar.get(calendar.MINUTE);
		
		startDayOfWeek=getDayOfWeek(startYear, startMonth, startDay);
		
		Log.e("zyf start date: ", startYear+"-"+startMonth+"-"+startDay+"  "+startHour+":"+startMinute);
		
		date.setTime(((long)mActivityDetailsItem.getEndTime())*1000);
		calendar.setTime(date);
		
		endYear=calendar.get(calendar.YEAR);
		endMonth=calendar.get(calendar.MONTH)+1;
		endDay=calendar.get(calendar.DAY_OF_MONTH);
		endHour=calendar.get(calendar.HOUR_OF_DAY);
		endMinute=calendar.get(calendar.MINUTE);
		
		endDayOfWeek=getDayOfWeek(endYear, endMonth, endDay);
		
		Log.e("zyf end date: ", endYear+"-"+endMonth+"-"+endDay+" "+endHour+":"+endMinute);
		
		updateDateTime();
	}
	
	private void initTitle(){
		
		TextView mTitleView=(TextView) findViewById(R.id.mTitleView);
		
		if(mActivityDetailsItem==null){
			mTitleView.setText(getResources().getString(R.string.create_activity));
		}else{
			mTitleView.setText(getResources().getString(R.string.edit_activity));
		}
		
		Button leftBtn=(Button) findViewById(R.id.leftBtn);
		leftBtn.setBackgroundResource(R.drawable.title_back);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setOnClickListener(this);
		
		Button rightBtn=(Button) findViewById(R.id.rightBtn);
		rightBtn.setText("完成");
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setOnClickListener(this);
	}
	
	private void initDateTime() throws ParseException{
		
		Date date=new Date();
		
		Calendar calendar=new GregorianCalendar();
		
		calendar.setTime(date);
		
		startYear=calendar.get(calendar.YEAR);
		startMonth=calendar.get(calendar.MONTH)+1;
		startDay=calendar.get(calendar.DAY_OF_MONTH);
		
		startDayOfWeek=getDayOfWeek(startYear, startMonth, startDay);
		
		Log.e("zyf start date: ", startYear+"-"+startMonth+"-"+startDay);
		
		calendar.add(calendar.DATE, 1);
		
		endYear=calendar.get(calendar.YEAR);
		endMonth=calendar.get(calendar.MONTH)+1;
		endDay=calendar.get(calendar.DAY_OF_MONTH);
		
		endDayOfWeek=getDayOfWeek(endYear, endMonth, endDay);
		
		Log.e("zyf end date: ", endYear+"-"+endMonth+"-"+endDay);
		
		updateDateTime();
	}
	
	private void updateDateTime(){
		
		mStartDateTv.setText(startMonth+getResources().getString(R.string.month)+startDay+getResources().getString(R.string.day));
		mEndDateTv.setText(endMonth+getResources().getString(R.string.month)+endDay+getResources().getString(R.string.day));
		
		mStartTimeTv.setText(startDayOfWeek+"  "+startHour+":"+getFormatTime(startMinute));
		mEndTimeTv.setText(endDayOfWeek+"  "+endHour+":"+getFormatTime(endMinute));
	}
	
	private String getFormatTime(int minute){
		
		if(minute<10){
			return "0"+minute;
		}
		
		return ""+minute;
	}
	
	private void initViews(){
		
		mLocationContainer=(RelativeLayout)findViewById(R.id.mLocationContainer);
		mStartDateTimeConatiner=(RelativeLayout)findViewById(R.id.mStartDateTimeConatiner);
		mEndDateTimeConatiner=(LinearLayout)findViewById(R.id.mEndDateTimeConatiner);
		
		mLocationContainer.setOnClickListener(this);
		mStartDateTimeConatiner.setOnClickListener(this);
		mEndDateTimeConatiner.setOnClickListener(this);
		
		mThemeTv=(TextView)findViewById(R.id.mThemeTv);
		
		mStartDateTv=(TextView)findViewById(R.id.mStartDateTv);
		mEndDateTv=(TextView)findViewById(R.id.mEndDateTv);
		
		mStartTimeTv=(TextView)findViewById(R.id.mStartTimeTv);
		mEndTimeTv=(TextView)findViewById(R.id.mEndTimeTv);
		
		mLocationEt=(EditText)findViewById(R.id.mLocationEt);
		mDescEt=(EditText)findViewById(R.id.mDescEt);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==Constant.RESULT_CODE_LOCATION_SELECT){
			
			if(data!=null){
				Log.e("zyf", "result locDesc: "+data.getStringExtra("locDesc"));
				
				locX=data.getStringExtra("locX");
				locY=data.getStringExtra("locY");
				
				Log.e("zyf", "locX: "+locX);
				
				if(data.getStringExtra("locDesc")!=null&&data.getStringExtra("locDesc").length()>0){
					mLocationEt.setText(data.getStringExtra("locDesc"));;
				}

			}
		}
	}

	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		case R.id.mLocationContainer:
			
			Intent intent=new Intent(this,LocationSelectActivity.class);
			startActivityForResult(intent,Constant.RESULT_CODE_LOCATION_SELECT);
			
			break;
		case R.id.mStartDateTimeConatiner:
			
			DateTimeSelectorDialog mDateTimeSelectorDialog=new DateTimeSelectorDialog(ActivityCreateActivity.this, R.style.MyDialog);
			mDateTimeSelectorDialog.setOnDateTimeSubmmitListener(new DateTimeSelectorDialog.onDateTimeSubmmitListener() {
				
				@Override
				public void summit(int year, int month, int day, int hour,
						int minute) {
					
					startYear=year;
					startMonth=month+1;
					startDay=day;
					
					startHour=hour;
					startMinute=minute;
					
					startDayOfWeek=getDayOfWeek(startYear, startMonth, startDay);
					
					updateDateTime();
				}
			});
			mDateTimeSelectorDialog.show();
			mDateTimeSelectorDialog.init(startYear, startMonth-1, startDay,startHour,startMinute);
			
			break;
		case R.id.mEndDateTimeConatiner:
			
			DateTimeSelectorDialog mDateTimeSelectorDialog2=new DateTimeSelectorDialog(ActivityCreateActivity.this, R.style.MyDialog);
			mDateTimeSelectorDialog2.setOnDateTimeSubmmitListener(new DateTimeSelectorDialog.onDateTimeSubmmitListener() {
				
				@Override
				public void summit(int year, int month, int day, int hour,
						int minute) {
					
					endYear=year;
					endMonth=month+1;
					endDay=day;
					
					endHour=hour;
					endMinute=minute;
					
					endDayOfWeek=getDayOfWeek(endYear, endMonth, endDay);
					
					updateDateTime();
				}
			});
			mDateTimeSelectorDialog2.show();
			mDateTimeSelectorDialog2.init(endYear, endMonth-1, endDay,endHour,endMinute);
			
			break;
		case R.id.leftBtn:
			finish();
			
			break;
			
		case R.id.rightBtn:
			
			if(mActivityDetailsItem!=null){   //活动的编辑
				
				ActivityDetailInfo info=new ActivityDetailInfo();
		    	info.setId(id);
		    	info.setName(theme);
		    	info.setStartTime((int)getFormatTime(startYear, startMonth, startDay, startHour, startMinute));
		    	info.setEndTime((int)getFormatTime(endYear, endMonth, endDay, endHour, endMinute));
		    	info.setDesc(mDescEt.getText().toString());
		    	info.setLocX(locX);
		    	info.setLocY(locY);
		    	info.setLocDesc(mLocationEt.getText().toString());
		    	
				mUpdateActivityTask=new ActivityUpdateTask(info){
					
					@Override
		            public void callback(){
		               
		            	Log.e("zyf", "update activity success......");
		            	
		            	mActivityDetailsItem.setName(theme);
		            	mActivityDetailsItem.setStartTime((int)getFormatTime(startYear, startMonth, startDay, startHour, startMinute));
		            	mActivityDetailsItem.setEndTime((int)getFormatTime(endYear, endMonth, endDay, endHour, endMinute));
		            	mActivityDetailsItem.setDesc(mDescEt.getText().toString());
		            	mActivityDetailsItem.setLocX(locX);
		            	mActivityDetailsItem.setLocY(locY);
		            	mActivityDetailsItem.setLocDesc(mLocationEt.getText().toString());
		            	
		            	Intent intent=new Intent();
		            	intent.putExtra("ActivityDetailsItem", mActivityDetailsItem);
		            	ActivityCreateActivity.this.setResult(Constant.RESULT_CODE_ACTIVITY_UPDATE, intent);
		            	
		            	finish();
		            	
		            	MyToast.makeText(ActivityCreateActivity.this, true, R.string.activity_update_success, MyToast.LENGTH_SHORT).show();
		            }
		            
		            @Override
		            public void complete(){
		            	
		            	mProgressDialogUtils.dismissDialog();
		            }
		            
		            @Override
		            public void failure(){
		                
		                Log.e("zyf", "update activity failure......");
		                
		                MyToast.makeText(ActivityCreateActivity.this, true, R.string.activity_update_failed, MyToast.LENGTH_SHORT).show();
		            }
		            
		            @Override
		            public void before(){
		            	
		            	mProgressDialogUtils=new MyProgressDialogUtils(R.string.progress_dialog_activity_updating, ActivityCreateActivity.this);
						mProgressDialogUtils.showDialog();
		            }

					@Override
					public void timeout() {
						// TODO Auto-generated method stub
						super.timeout();
						
						mUpdateActivityTask.cancel(true);
						
						mProgressDialogUtils.dismissDialog();
						
						Log.e("zyf", "update activity time out......");
						
						MyToast.makeText(ActivityCreateActivity.this, true, R.string.activity_update_failed, MyToast.LENGTH_SHORT).show();
					} 
				};
				mUpdateActivityTask.setTimeOutEnabled(true, 10*1000);
				mUpdateActivityTask.safeExecute();
				return;
			}
			
			JSONObject contentJsonObject = new JSONObject();

	        try {
	        	contentJsonObject.put("name", theme);
	        	contentJsonObject.put("desc", mDescEt.getText().toString());
	        	contentJsonObject.put("locDesc", mLocationEt.getText().toString());
	        	contentJsonObject.put("startTime", getFormatTime(startYear,startMonth,startDay,startHour,startMinute));
	        	contentJsonObject.put("endTime", getFormatTime(endYear,endMonth,endDay,endHour,endMinute));
	        	contentJsonObject.put("locX", locX);
	        	contentJsonObject.put("locY", locY);
	        	contentJsonObject.put("pid", id);
	            
	            Log.e("zyf", "post content: "+contentJsonObject.toString());
	        } catch (JSONException e) {
	            Log.e("zyf", "format json exception: "+e.toString());
	            return;
	        }
	        
	        mCreateActivityTask=new ActivityCreateTask(contentJsonObject.toString()){

	            @Override
	            public void callback(){
	               
	            	Log.e("zyf", "create activity success......");
	            	
	            	Intent intent=new Intent();
	            	ActivityCreateActivity.this.setResult(Constant.RESULT_CODE_ACTIVITY_CREATE, intent);
	            	ActivityCreateActivity.this.finish();
	            	
	            	MyToast.makeText(ActivityCreateActivity.this, true, R.string.activity_create_success,MyToast.LENGTH_SHORT).show();
	            }
	            
	            @Override
	            public void complete(){
	            	
	            	mProgressDialogUtils.dismissDialog();
	            }
	            
	            @Override
	            public void failure(){
	                
	                Log.e("zyf", "create activity failure......");
	                
	                MyToast.makeText(ActivityCreateActivity.this, true, R.string.activity_create_failed,MyToast.LENGTH_SHORT).show();
	            }
	            
	            @Override
	            public void before(){
	            	
	            	mProgressDialogUtils=new MyProgressDialogUtils(R.string.progress_dialog_activity_creating, ActivityCreateActivity.this);
					mProgressDialogUtils.showDialog();
	            }

				@Override
				public void timeout() {
					// TODO Auto-generated method stub
					super.timeout();
					
					mCreateActivityTask.cancel(true);
					mProgressDialogUtils.dismissDialog();
					
					Log.e("zyf", "create activity time out......");
					
					MyToast.makeText(ActivityCreateActivity.this, true, R.string.activity_create_failed,MyToast.LENGTH_SHORT).show();
				} 

	        };
	        mCreateActivityTask.setTimeOutEnabled(true, 10*1000);
	        mCreateActivityTask.safeExecute();
	        
			break;

		default:
			break;
		}
	}
	
	private String getDayOfWeek(int year,int month,int day){
		
		String sDt=year+"/"+month+"/"+day;
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd");
		Date date;
		try {
			date = sdf.parse(sDt);
			SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		    return dateFm.format(date);
		} catch (ParseException e) {
			Log.e("zyf","get day of week exception: "+e.toString());
		}
		
		return "";
	}
	
	private long getFormatTime(int year,int month,int day,int hour,int minute){
		
		//have a try
		try {
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");//小写的mm表示的是分钟  
			String totolDate=year+"-"+month+"-"+day+" "+hour+":"+minute;  
			
			//Log.e("zyf", "before format date: "+totolDate);
			
			Date date=sdf.parse(totolDate);
			
			long longTime=date.getTime()/1000;
			
			return longTime;
			
			/*Log.e("zyf", "fromat long time: "+longTime);
			
			Date curDate=new Date(longTime);
			
	        String  formatDate=sdf.format(curDate);
			
			Log.e("zyf", "after format date: "+formatDate);*/
		} catch (Exception e) {
			Log.e("zyf", "format time exception: "+e.toString());
		}
		
		return 0;
	}
}
