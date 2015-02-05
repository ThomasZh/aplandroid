package com.redoct.iclub.widget;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.redoct.iclub.R;

public class DateTimeSelectorDialog extends Dialog implements android.view.View.OnClickListener{
	
	private Context mContext;
	
	private DatePicker mDatePicker;
	private TimePicker mTimePicker;
	
	private Button mSummitBtn;
	
	private Button mCancelBtn;
	
	private int mYear,mMonth,mDay,mHour,mMinute;
	
	public interface onDateTimeSubmmitListener{
		void summit(int year,int month,int day,int hour,int minute);
	}
	
	private onDateTimeSubmmitListener mDateTimeSubmmitListener;
	
	public void setOnDateTimeSubmmitListener(onDateTimeSubmmitListener mDateTimeSubmmitListener){
		
		this.mDateTimeSubmmitListener=mDateTimeSubmmitListener;
	}

	public DateTimeSelectorDialog(Context context, int theme) {
		super(context, theme);
		
		mContext=context;
		
	}
	
	 public void init(int year,int month,int day,int hour,int minute){
		 
		 mYear=year;
		 mMonth=month;
		 mDay=day;
		 mHour=hour;
		 mMinute=minute;
		 
		 mDatePicker.init(year, month, day, new OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker arg0, int year, int month, int day) {
				
				Log.e("zyf", "after change date: "+year+"-"+month+"-"+day);
				
				mYear=year;
				mMonth=month;
				mDay=day;
			}
		});
		 
		 mTimePicker.setCurrentHour(hour);
		 mTimePicker.setCurrentMinute(minute);
		 mTimePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(TimePicker arg0, int hour, int minute) {
				
				Log.e("zyf", "after time: "+hour+":"+minute);
				
				mHour=hour;
				mMinute=minute;
			}
		});
	 }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.custom_dialog_date_time_selector);
		
		mDatePicker=(DatePicker)findViewById(R.id.mDatePicker);
		mTimePicker=(TimePicker)findViewById(R.id.mTimePicker);
		
		mTimePicker.setIs24HourView(true);
		mTimePicker.setCurrentHour(new GregorianCalendar().get(Calendar.HOUR_OF_DAY));
		
		mSummitBtn=(Button)findViewById(R.id.mSummitBtn);
		mCancelBtn=(Button)findViewById(R.id.mCancelBtn);
		
		mSummitBtn.setOnClickListener(this);
		mCancelBtn.setOnClickListener(this);
		
		/*WindowManager.LayoutParams  lp=getWindow().getAttributes();
		WindowManager wm=(WindowManager)mContext.getSystemService(mContext.WINDOW_SERVICE);
        int width=wm.getDefaultDisplay().getWidth();
        lp.width=width*2/3;
        getWindow().setAttributes(lp);*/
	}

	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		
		case R.id.mSummitBtn:
			
			mDatePicker.clearFocus();
			mTimePicker.clearFocus();
			
			if(mDateTimeSubmmitListener!=null){
				mDateTimeSubmmitListener.summit(mYear, mMonth, mDay, mHour, mMinute);
			}
			
			dismiss();
			break;
        case R.id.mCancelBtn:
			
			dismiss();
			break;
		default:
			break;
		}
	}

}
