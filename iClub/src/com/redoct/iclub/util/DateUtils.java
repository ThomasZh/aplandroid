package com.redoct.iclub.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	public static String getFormatDate(int time){
		
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd HH:mm");
		//前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
		java.util.Date dt = new Date(time * 1000);  
		String dateTime = sdf.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00
		
		return dateTime;
	}
}
