package com.redoct.iclub.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.redoct.iclub.R;

import android.R.integer;
import android.content.Context;

public class DateUtils {
	
	private Context mContext;

	public DateUtils(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public String getFormatDate(int time){
		
		String fromatString="M"+mContext.getString(R.string.month)+"d"+mContext.getString(R.string.day)+" EEEE H:mm";
		SimpleDateFormat sdf= new SimpleDateFormat(fromatString);
		Date dt = new Date(time * 1000);  
		String dateTime = sdf.format(dt);
		
		return dateTime;
	}
}