package com.redoct.iclub.task;

import android.R.integer;
import android.util.Log;

import com.oct.ga.comm.cmd.invite.ConfirmReceivedInviteReq;
import com.redoct.iclub.iClubApplication;

public class MessageCommitTask extends TemplateTask {

	private String [] inviteIds;
	private String [] inviteFeedbackIds;
	
	
	public MessageCommitTask(String[] inviteIds, String[] inviteFeedbackIds) {
		super();
		this.inviteIds = inviteIds;
		this.inviteFeedbackIds = inviteFeedbackIds;
	}


	@Override
	protected boolean justTodo() {
		
		if(inviteIds==null&&inviteFeedbackIds==null){
			
			Log.e("zyf", "no need to commit........");
			return true;
		}
		
		if(inviteIds!=null){
			Log.e("zyf", "task call back inviteIds length: "+inviteIds.length);
			
			for(int i=0;i<inviteIds.length;i++){
				Log.e("zyf", "commit invite id: "+inviteIds[i]);
			}
		}
		
		if(inviteFeedbackIds!=null)
			Log.e("zyf", "task call back inviteFeedbackIds length: "+inviteFeedbackIds.length);
		
		ConfirmReceivedInviteReq req=new ConfirmReceivedInviteReq(inviteIds, inviteFeedbackIds);
		
		try {
			
			iClubApplication.send(req,false);
			
			Log.e("zyf", "confirm Received Invite Req completely......");
			
		} catch (Exception e) {
			
			Log.e("zyf", "Confirm Received Invite Req exception: "+e.toString());
		}
				
		return true;
	}
}
