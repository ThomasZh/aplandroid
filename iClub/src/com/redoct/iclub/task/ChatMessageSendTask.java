package com.redoct.iclub.task;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import android.util.Log;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.EcryptUtil;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.cmd.msg.UploadMessageReq;
import com.oct.ga.comm.cmd.msg.UploadMessageResp;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.config.NetworkConfig;

public class ChatMessageSendTask extends TemplateTask {
	
	private String messageId;
	////private String toId;
	private String chatId;
	private String toAcountId;
	private String content;
	private String channelId;
	private Short channelType;

	public ChatMessageSendTask(String messageId, String chatId, String toAcountId,String content,
			String channelId, Short channelType) {
		super();
		this.messageId = messageId;
		this.chatId = chatId;
		this.toAcountId = toAcountId;
		this.content = content;
		this.channelId = channelId;
		this.channelType = channelType;
	}

	@Override
	protected boolean justTodo() {
		
		/*UUID.randomUUID().toString();
		//群组 toId  activityId
		//个人 toId  
		EcryptUtil.md5ChatId(me, other);*/
		/*UploadMessageReq req=new UploadMessageReq(DatetimeUtil.currentTimestamp(),
				                                    messageId, 
													GlobalArgs.CONTENT_TYPE_TXT, 
													channelType, 
													channelId,
													toId, 
													content, 
													null);*/
		UploadMessageReq req=new UploadMessageReq(DatetimeUtil.currentTimestamp(),
				                                            messageId, 
															GlobalArgs.CONTENT_TYPE_TXT, 
															channelType, 
															channelId,
															chatId,
															toAcountId,
															content, 
															null);
		
		Log.e("zyf", "chat msg send activity id: "+channelId);
		
		try {
			UploadMessageResp resp=(UploadMessageResp) iClubApplication.send(req);
			
			if (resp.getRespState() == NetworkConfig.RESPONSE_OK){
				
				Log.e("zyf", "message send success......");
				
				return true;
			}
		}catch (Exception e) {
			Log.e("zyf", "Upload message exception: "+e.toString());
		}
		
		return false;
	}

}
