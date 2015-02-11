package com.redoct.iclub.task;

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
		
		ConfirmReceivedInviteReq req=new ConfirmReceivedInviteReq(inviteIds, inviteFeedbackIds);
		
		try {
			
			iClubApplication.send(req);
			
		} catch (Exception e) {
			
			Log.e("zyf", "Confirm Received Invite Req exception: "+e.toString());
		}
				
		return true;
	}
}
