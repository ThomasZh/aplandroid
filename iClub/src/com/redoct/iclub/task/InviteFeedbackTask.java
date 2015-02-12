package com.redoct.iclub.task;

import android.util.Log;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.cmd.invite.InviteFeedbackReq;
import com.oct.ga.comm.cmd.invite.InviteFeedbackResp;
import com.redoct.iclub.iClubApplication;

//接受某人的请求
public class InviteFeedbackTask extends TemplateTask {
	
	private String inviteId;
	
	private Short feedbackState;

	public InviteFeedbackTask(String inviteId, Short feedbackState) {
		super();
		this.inviteId = inviteId;
		this.feedbackState = feedbackState;
	}

	@Override
	protected boolean justTodo() {
		
		InviteFeedbackReq req=new InviteFeedbackReq(inviteId,feedbackState);
		
		try {
			
			InviteFeedbackResp resp=(InviteFeedbackResp) iClubApplication.send(req);
			
			if(resp==null){
				
				Log.e("zyf","Invite feedback resp is null.....");
				
				return false;
			}
			
			if(resp.getRespState()!=ErrorCode.SUCCESS){
				
				Log.e("zyf","Invite feedback error code.....");
				
				return false;
			}
			
		} catch (Exception e) {
			
			Log.e("zyf","Invite feedback exception: "+e.toString());
			
			return false;
		}
		
		return true;
	}
}
