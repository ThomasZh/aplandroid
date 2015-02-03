package com.redoct.iclub.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.redoct.iclub.R;

import android.R.integer;
import android.content.Context;

public class DateUtils {
	
	private Context mContext;
	
	private String date;
	
	private String exactlyTime;

	public DateUtils(Context mContext) {
		super();
		this.mContext = mContext;
	}
	
	public DateUtils(Context mContext,int time){
		super();
		this.mContext = mContext;
		
		String fromatString="yy"+mContext.getString(R.string.year)+"M"+mContext.getString(R.string.month)+"d"+mContext.getString(R.string.day)+"_EEEE HH:mm";
		SimpleDateFormat sdf= new SimpleDateFormat(fromatString);
		long realTime=(long)time*1000;
		String dateTime = sdf.format(realTime);
		
		date=dateTime.split("_")[0];
		exactlyTime=dateTime.split("_")[1];
	}
	
	public String getDate(){
		
		return date;
	}
	
	public String getExactlyTime(){
		
		return exactlyTime;
	}

	public String getFormatDate(int time){
		
		String fromatString="M"+mContext.getString(R.string.month)+"d"+mContext.getString(R.string.day)+" EEEE H:mm";

		SimpleDateFormat sdf= new SimpleDateFormat(fromatString);
		long realTime=(long)time*1000;
		String dateTime = sdf.format(realTime);
		
		return dateTime;
	}
}
