package com.redoct.iclub.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import android.os.Environment;
import android.util.Log;

import com.redoct.iclub.item.MessageItem;

public class FileUtils {
	
	//用户本地Message记录文件
	private static String FILE_NAME_QUERY_HISTORY="message.out";   
    
	//读取本地的历史查询记录
    public static ArrayList<MessageItem> readMessageHistory() {
    	
		File sdFile = new File(getUserMessageDir(), FILE_NAME_QUERY_HISTORY);  
		          
	    try {  
	        FileInputStream fis=new FileInputStream(sdFile); 
	        ObjectInputStream ois = new ObjectInputStream(fis);  
	        ArrayList<MessageItem> deliveryQueryHistoryList = (ArrayList<MessageItem>)ois.readObject();
	        
	        ois.close();
	        
	        return deliveryQueryHistoryList;
	    }catch (Exception e) {  
	        Log.e("zyf","object file read ecxeption: "+e.toString()); 
	    }
		        
		return new ArrayList<MessageItem>();
	}
	
    //更新本地的历史查询记录
    public static void updateMessageHistory(ArrayList<MessageItem> deliveryQueryHistoryList){
    	
    	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            
            File sdFile = new File(getUserMessageDir(), FILE_NAME_QUERY_HISTORY);     
       
	        try{  
	             FileOutputStream fos = new FileOutputStream(sdFile);  
	             ObjectOutputStream oos = new ObjectOutputStream(fos);  
	             oos.writeObject(deliveryQueryHistoryList);
	             fos.close(); 
	         }catch (Exception e) {  
	             Log.e("zyf","write file exception: "+e.toString());
	         }
    	}
    }
    
    public static File getUserMessageDir(){
		String cachePath = "iClub"+"/cache/message";
		File cacheDir = new File(Environment.getExternalStorageDirectory(),cachePath);
		if(!cacheDir.exists()){
			cacheDir.mkdirs();
		}
		return cacheDir;
	}
	
}
