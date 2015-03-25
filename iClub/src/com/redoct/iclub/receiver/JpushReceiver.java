package com.redoct.iclub.receiver;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.redoct.iclub.config.AppConfig;
import com.redoct.iclub.item.MemberItem;
import com.redoct.iclub.item.MessageItem;
import com.redoct.iclub.util.Constant;
import com.redoct.iclub.util.MessageDatabaseHelperUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这�? Receiver，则�? 1) 默认用户会打�?主界�? 2) 接收不到自定义消�?
 */
public class JpushReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	
	private MessageDatabaseHelperUtil mMessageDatabaseHelperUtil;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		mMessageDatabaseHelperUtil=new MessageDatabaseHelperUtil(context);
		
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));
		// processCustomMessage( context, bundle);
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 接收到推送下来的自定义消�?: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了�?�知");

			// 打开自定义的Activity
			/*
			 * Intent i = new Intent(context, TestActivity.class);
			 * i.putExtras(bundle); //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 * i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
			 * Intent.FLAG_ACTIVITY_CLEAR_TOP ); context.startActivity(i);
			 */

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根�? JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity�?
			// 打开�?个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction()
					+ " connected state change to " + connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印�?有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	// send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {

		bundle.getString(JPushInterface.EXTRA_TITLE);
		String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
		try {
			
			Log.e("zyf", "message: "+message);
			
			JSONObject json = new JSONObject(message);
			MessageItem item = new MessageItem();
			item.setAccoutId(AppConfig.account.getAccountId());
			item.setMessageType(Constant.MESSAGE_TYPE_CHAT);
			item.setChatId(json.optString("chatId"));
			item.setTimestamp(json.optInt("timestamp"));
			item.setUserName(json.optString("channelName"));
			item.setLastContent(json.optString("content"));
			item.setUserAvatarUrl(json.optString("fromAccountAvatarUrl"));
			item.setChannelId(json.optString("channelId"));
			item.setChannelType(json.optInt("channelType"));
			item.setIsSend(false);
			
			int originalUnReadNum=mMessageDatabaseHelperUtil.getUnReadNumWithChatId(AppConfig.account.getAccountId(), item.getChatId());
			if(originalUnReadNum==-1){   //尚无该条记录
				Log.e("zyf", "收到推送消息,数据库中之前无该会话记录.......");
				item.setUnReadNum(1);
				mMessageDatabaseHelperUtil.addNewMessage(item);
			}else{
				Log.e("zyf", "收到推送消息,数据库中之前保存有该会话记录.......originalUnReadNum: "+originalUnReadNum);
				item.setUnReadNum(originalUnReadNum+1);
				mMessageDatabaseHelperUtil.updateChatMessage(item);
			}
			
			Intent in = new Intent("com.cc.msg");
			in.putExtra("msgItem", item);
			context.sendBroadcast(in);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
