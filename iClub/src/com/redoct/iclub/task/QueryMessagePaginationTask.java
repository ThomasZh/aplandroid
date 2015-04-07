package com.redoct.iclub.task;

/**
 * Created by chengcai on 15-02-02.
 */

import android.content.Context;

import com.oct.ga.comm.cmd.msg.QueryMessagePaginationReq;
import com.oct.ga.comm.cmd.msg.QueryMessagePaginationResp;
import com.oct.ga.comm.domain.msg.MsgExtend;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.config.AppConfig;
import com.redoct.iclub.config.NetworkConfig;
import com.redoct.iclub.config.ServiceConfig;
import com.redoct.iclub.item.MessageItem;
import com.redoct.iclub.util.MessageDatabaseHelperUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class QueryMessagePaginationTask extends TemplateTask {
	private ArrayList<MessageItem> mMessageItems = new ArrayList<MessageItem>();
	private String chatId;
	private short pageNum;
	private short pageSize;
	private MessageDatabaseHelperUtil mMessageDatabaseHelperUtil;

	public QueryMessagePaginationTask(String chatId, short pageNum,
			short pageSize,Context context) {

		// TODO Auto-generated constructor stub
		this.chatId = chatId;
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		mMessageDatabaseHelperUtil=new MessageDatabaseHelperUtil(context);

	}

	@Override
	protected void connectCheck() {
		if (ServiceConfig.gateway != null && iClubApplication.apiOk())
			return;

	}

	@Override
	protected boolean justTodo() {
		if (ServiceConfig.gateway == null)
			return false;

		QueryMessagePaginationReq req = new QueryMessagePaginationReq(chatId,pageNum, pageSize);

		try {
			/*
			values.put("accoutId", item.getAccoutId());
			
			values.put("isSend", item.getIsSend());
	        values.put("userAvatarUrl", item.getUserAvatarUrl());
	        values.put("lastContent", item.getLastContent());
	        values.put("chatId", item.getChatId());
	        values.put("fomeName", item.getFromName());
			
*/
			QueryMessagePaginationResp resp = (QueryMessagePaginationResp) iClubApplication
					.send(req);
			if (resp == null)
				return false;// log in failure!

			int state = resp.getRespState();
			// invalid account
			if (state != NetworkConfig.RESPONSE_OK)
				return false;
		     List <MsgExtend>  mslist =  resp.getMessages();
		     for(int i = 0;i<mslist.size();i++){
		    	 MessageItem item = new MessageItem();
		    	 item.setAccoutId(mslist.get(i).getFromAccountId());
		    	 if(mslist.get(i).getFromAccountId().equals(AppConfig.account.getAccountId())){
		    		 item.setIsSend("2");
		    	 }else{
		    		 item.setIsSend("1");
		    	 }
		    	 item.setChatId(mslist.get(i).getChatId());
		    	 item.setFromName(mslist.get(i).getFromAccountName());
		    	 item.setLastContent(mslist.get(i).getContent());
		    	 item.setUserAvatarUrl(mslist.get(i).getAttachUrl());
		    	 mMessageDatabaseHelperUtil.addChatMessage(item);     //保存到消息表
		    	 mMessageItems.add(item);
		     }
		     

		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public ArrayList<MessageItem> getmMessageItems() {
		return mMessageItems;
	}

	public void setmMessageItems(ArrayList<MessageItem> mMessageItems) {
		this.mMessageItems = mMessageItems;
	}

}
