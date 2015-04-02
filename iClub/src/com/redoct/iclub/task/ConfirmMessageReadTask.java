package com.redoct.iclub.task;


import android.util.Log;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.cmd.msg.ConfirmMessageReadReq;
import com.redoct.iclub.iClubApplication;

public class ConfirmMessageReadTask extends TemplateTask {
	
	private String chatId;
	
	
   
	
	
	public ConfirmMessageReadTask(String chatId) {
		super();
		this.chatId = chatId;
	
	}

	@Override
	protected boolean justTodo() {
		
		
		ConfirmMessageReadReq req=new ConfirmMessageReadReq();
		req.setSequence(DatetimeUtil.currentTimestamp());
	    req.setChatId(chatId);
		
		
		try {
			
		 iClubApplication.send(req);
			
			
		} catch (Exception e) {
			
			Log.e("zyf", "GetMomentTask exception: "+e.toString());
			
			return false;
		}

		return true;
	}

}
