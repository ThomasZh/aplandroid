package com.redoct.iclub.task;

import java.io.UnsupportedEncodingException;

import android.util.Log;

import com.oct.ga.comm.cmd.invite.SyncInviteReq;
import com.oct.ga.comm.cmd.invite.SyncInviteResp;
import com.oct.ga.comm.domain.msg.GaInvite;
import com.redoct.iclub.iClubApplication;

public class MessageListTask extends TemplateTask {

	@Override
	protected boolean justTodo(){
		
		SyncInviteReq req=new SyncInviteReq();
		
		try {
			
			SyncInviteResp resp=(SyncInviteResp) iClubApplication.send(req);
			
		} catch (Exception e) {
			
			Log.e("zyf", "message list exception: "+e.toString());
		}
		
		return true;
	}
}
