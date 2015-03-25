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
import com.oct.ga.comm.domain.msg.MsgLastCacheJsonBean;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.config.AppConfig;
import com.redoct.iclub.item.MessageItem;
import com.redoct.iclub.util.Constant;

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
		
		Log.e("zyf", "last try time: "+lastTryTime);
		
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
			List<MsgLastCacheJsonBean> list2=data.getChatNumber();
			MessageItem messageItem;
			for(int i=0;i<list.size();i++){
				
				messageItem=new MessageItem();
				messageItem.setAccoutId(AppConfig.account.getAccountId());
				messageItem.setMessageType(Constant.MESSAGE_TYPE_CHAT);
				messageItem.setChatId(list.get(i).getChatId());
				messageItem.setUserName(list.get(i).getChannelName());
				messageItem.setLastContent(list.get(i).getContent());
				messageItem.setTimestamp(list.get(i).getTimestamp());
				messageItem.setUserAvatarUrl(list.get(i).getFromAccountAvatarUrl());
				messageItem.setUnReadNum(list2.get(i).getBadgeNum());
				
				messageItems.add(messageItem);
			}
			
			return true;
			
		}catch (Exception e) {
			
			Log.e("zyf", "chat room list exception: "+e.toString());
		}
		
		return false;
	}
	
}
