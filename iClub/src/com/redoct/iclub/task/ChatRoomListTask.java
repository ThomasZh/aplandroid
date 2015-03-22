package com.redoct.iclub.task;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.cmd.msg.QueryMessageBadgeNumberReq;
import com.oct.ga.comm.cmd.msg.QueryMessageBadgeNumberResp;
import com.oct.ga.comm.domain.MessageBadgeNumberJsonBean;
import com.oct.ga.comm.domain.msg.MessageInlinecast;
import com.oct.ga.comm.domain.msg.MsgExtend;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.item.MessageItem;

public class ChatRoomListTask extends TemplateTask {
	
	private int lastTryTime;
	
	private ArrayList<MessageItem> messageItems;
	
	public int getLastTryTime(){
		
		return lastTryTime;
	}
	
	public ArrayList<MessageItem> getMessageList(){
		
		return messageItems;
	}

	public ChatRoomListTask(int lastTryTime) {
		super();
		this.lastTryTime = lastTryTime;
		
		messageItems=new ArrayList<MessageItem>();
	}

	@Override
	protected boolean justTodo() {
		
		QueryMessageBadgeNumberReq req=new QueryMessageBadgeNumberReq(DatetimeUtil.currentTimestamp(), lastTryTime);
		
		try {
			QueryMessageBadgeNumberResp resp=(QueryMessageBadgeNumberResp)iClubApplication.send(req);
			
			if(resp==null){
				
				Log.e("zyf", "chat room list resp is null......");
				
				return false;
			}
			
			lastTryTime=resp.getTimestamp();
			
			MessageBadgeNumberJsonBean data=resp.getMessageBadge();
			List<MsgExtend> list=data.getMessageList();
			MessageItem messageItem;
			for(int i=0;i<list.size();i++){
				
				messageItem=new MessageItem();
				messageItem.setUserName(list.get(i).getContent());
				
				messageItems.add(messageItem);
			}
			
			return true;
			
		}catch (Exception e) {
			
			Log.e("zyf", "chat room list exception: "+e.toString());
		}
		
		return false;
	}
	
}
