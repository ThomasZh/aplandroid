package com.redoct.iclub.task;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.cmd.invite.SyncInviteReq;
import com.oct.ga.comm.cmd.invite.SyncInviteResp;
import com.oct.ga.comm.domain.msg.GaInvite;
import com.oct.ga.comm.domain.msg.GaInviteFeedback;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.item.MessageItem;

public class MessageListTask extends TemplateTask {
	
	private ArrayList<MessageItem> messageItems=new ArrayList<MessageItem>();
	
	private int inviteSize;
	
	private int inviteFeedbackSize;
	
	private String [] inviteIds;
	private String [] inviteFeedbackIds;
	
	public ArrayList<MessageItem> getMessageList(){
		
		return messageItems;
	}
	
	public String [] getInviteIds(){
		
		return inviteIds;
	}
	
	public String []  getInviteFeedIds(){
		
		return inviteFeedbackIds;
	}

	@Override
	protected boolean justTodo(){
		
		SyncInviteReq req=new SyncInviteReq();
		
		try {
			
			SyncInviteResp resp=(SyncInviteResp) iClubApplication.send(req);
			
			if(resp==null){
				
				Log.e("zyf", "message list resp is null......");
				
				return false;
			}
			
			if(resp.getRespState()!=ErrorCode.SUCCESS){
				
				Log.e("zyf", "message list resp state is error......");
				
				return false;
			}
			
			messageItems=new ArrayList<MessageItem>();
			
			MessageItem messageItem;
			
			List<GaInvite> gaInvites=resp.getInviteList();
			
			if(gaInvites!=null){
				
				Log.e("zyf","getInviteList().size(): "+gaInvites.size());
				
				inviteSize=gaInvites.size();
				inviteIds=new String[inviteSize];
				
				GaInvite gaInvite;
				for(int i=0;i<inviteSize;i++){
					
					gaInvite=gaInvites.get(i);
					
					inviteIds[i]=gaInvite.getInviteId();
					
					messageItem=new MessageItem();
					
					messageItem.setFeedback(false);
					messageItem.setChannelId(gaInvite.getChannelId());
					messageItem.setChannelName(gaInvite.getChannelName());
					messageItem.setChannelType(gaInvite.getChannelType());
					messageItem.setExpiry(gaInvite.getExpiry());
					messageItem.setInviteId(gaInvite.getInviteId());
					messageItem.setInviteType(gaInvite.getInviteType());
					messageItem.setTimestamp(gaInvite.getTimestamp());
					messageItem.setToUserSemiId(gaInvite.getToUserSemiId());
					messageItem.setUserId(gaInvite.getFromUserId());
					messageItem.setUserName(gaInvite.getFromUserName());
					messageItem.setUserAvatarUrl(gaInvite.getFromUserAvatarUrl());
					
					messageItems.add(messageItem);
				}
			}else {
				Log.e("zyf","getInviteList() is null......");
			}
			
			List<GaInviteFeedback> gaInviteFeedbacks=resp.getInviteFeedbackList();
			
			if(gaInviteFeedbacks!=null){
				
				Log.e("zyf","getInviteFeedbackList().size(): "+gaInviteFeedbacks.size());
				
				inviteFeedbackSize=gaInviteFeedbacks.size();
				inviteFeedbackIds=new String[inviteFeedbackSize];
				
				GaInviteFeedback gaInviteFeedback;
				for(int i=0;i<inviteFeedbackSize;i++){
					
					gaInviteFeedback=gaInviteFeedbacks.get(i);
					
					inviteFeedbackIds[i]=gaInviteFeedback.getInviteId();
					
					messageItem=new MessageItem();
					
					messageItem.setFeedback(true);
					
					messageItem.setChannelId(gaInviteFeedback.getFeedbackChannelId());
					messageItem.setChannelName(gaInviteFeedback.getFeedbackChannelName());
					messageItem.setChannelType(gaInviteFeedback.getFeedbackChannelType());
					messageItem.setInviteId(gaInviteFeedback.getInviteId());
					messageItem.setInviteType(gaInviteFeedback.getInviteType());
					messageItem.setTimestamp(gaInviteFeedback.getTimestamp());
					messageItem.setToUserSemiId(gaInviteFeedback.getToUserSemiId());
					messageItem.setUserId(gaInviteFeedback.getFromUserId());
					messageItem.setUserName(gaInviteFeedback.getFeedbackUserName());
					messageItem.setUserAvatarUrl(gaInviteFeedback.getFeedbackUserAvatarUrl());
					
					messageItems.add(messageItem);
				}
			}else {
				Log.e("zyf","getInviteFeedbackList() is null......");
			}
			
		} catch (Exception e) {
			
			Log.e("zyf", "message list exception: "+e.toString());
		}
		
		return true;
	}
}
