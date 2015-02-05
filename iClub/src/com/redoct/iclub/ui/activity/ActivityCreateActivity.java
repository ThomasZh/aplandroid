package com.redoct.iclub.ui.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.redoct.iclub.R;
import com.redoct.iclub.util.Constant;
import com.redoct.iclub.widget.DateTimeSelectorDialog;

public class ActivityCreateActivity extends Activity implements OnClickListener{
	
	private RelativeLayout mLocationContainer,mStartDateTimeConatiner;
	
	private LinearLayout mEndDateTimeConatiner;
	
	private TextView mThemeTv,mStartDateTv,mEndDateTv,mStartTimeTv,mEndTimeTv;
	
	private EditText mLocationEt;
	
	private String theme;
	
	private String locX,locY;
	
	private int startYear,startMonth,startDay;
	private String startDayOfWeek;
	private int startHour=10;
	private int startMinute=0;
	private int endYear,endMonth,endDay;
	private String endDayOfWeek;
	private int endHour=10;
	private int endMinute=30;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_activity_create);
		
		theme=getIntent().getStringExtra("theme");
		
		initTitle();
		
		initViews();
		
		try {
			
			initDateTime();
		} catch (Exception e) {
			Log.e("zyf", e.toString());
		}
	}
	
	private void initTitle(){
		
		TextView mTitleView=(TextView) findViewById(R.id.mTitleView);
		mTitleView.setText(getResources().getString(R.string.create_activity));
		
		Button leftBtn=(Button) findViewById(R.id.leftBtn);
		leftBtn.setBackgroundResource(R.drawable.title_back);
		leftBtn.setVisibility(View.VISIBLE);

		leftBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				finish();
			}
		});
		
		Button rightBtn=(Button) findViewById(R.id.rightBtn);
		rightBtn.setText("完成");;
		rightBtn.setVisibility(View.VISIBLE);

		rightBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
	}
	
	private void initDateTime() throws ParseException{
		 
		/*Date date=new Date();
		
		Log.e("zyf", "start date: "+date.getMonth()+"  "+date.getDay()+"  ");
		
		long curTime=System.currentTimeMillis();
		long nextTime=curTime+1000*1*60*60*24;
		
		date=new Date(nextTime);
		
		Log.e("zyf", "end date: "+date.getMonth()+"  "+date.getDay()+"  ");*/
		
		/*SimpleDateFormat    formatter=new SimpleDateFormat ("yyyy年MM月dd日    HH:mm:ss");       
		Date    curDate    =   new    Date(System.currentTimeMillis());//获取当前时间       
		String    str    =    formatter.format(curDate);
		
		Log.e("zyf", "date: "+str);*/
		
		Date date=new Date();
		
		Calendar calendar=new GregorianCalendar();
		
		calendar.setTime(date);
		
		startYear=calendar.get(calendar.YEAR);
		startMonth=calendar.get(calendar.MONTH)+1;
		startDay=calendar.get(calendar.DAY_OF_MONTH);
		
		Log.e("zyf start date: ", startYear+"-"+startMonth+"-"+startDay);
		
		calendar.add(calendar.DATE, 1);
		
		endYear=calendar.get(calendar.YEAR);
		endMonth=calendar.get(calendar.MONTH)+1;
		endDay=calendar.get(calendar.DAY_OF_MONTH);
		
		Log.e("zyf end date: ", endYear+"-"+endMonth+"-"+endDay);
		
		//获取日期的long值大小
		/*SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");//小写的mm表示的是分钟  
		String dstr="2008-4-24 6:18";  
		java.util.Date date2=sdf.parse(dstr);
		
		long newTime=date2.getTime();
		
		Date curDate=new Date(newTime);
		
        String    str = sdf.format(curDate);
		
		Log.e("zyf", "format date: "+str);*/
		
		updateDateTime();
	}
	
	private void updateDateTime(){
		
		mStartDateTv.setText(startMonth+getResources().getString(R.string.month)+startDay+getResources().getString(R.string.day));
		mEndDateTv.setText(endMonth+getResources().getString(R.string.month)+endDay+getResources().getString(R.string.day));
		
		mStartTimeTv.setText(startHour+":"+getFormatTime(startMinute));
		mEndTimeTv.setText(endHour+":"+getFormatTime(endMinute));
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
		mThemeTv.setText(theme);
		
		mStartDateTv=(TextView)findViewById(R.id.mStartDateTv);
		mEndDateTv=(TextView)findViewById(R.id.mEndDateTv);
		
		mStartTimeTv=(TextView)findViewById(R.id.mStartTimeTv);
		mEndTimeTv=(TextView)findViewById(R.id.mEndTimeTv);
		
		mLocationEt=(EditText)findViewById(R.id.mLocationEt);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==Constant.RESULT_CODE_LOCATION_SELECT){
			
			if(data!=null){
				Log.e("zyf", "result locDesc: "+data.getStringExtra("locDesc"));
				
				locX=data.getStringExtra("locX");
				locY=data.getStringExtra("locY");
				
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
					
					updateDateTime();
				}
			});
			mDateTimeSelectorDialog2.show();
			mDateTimeSelectorDialog2.init(endYear, endMonth-1, endDay,endHour,endMinute);
			
			break;

		default:
			break;
		}
	}
}
