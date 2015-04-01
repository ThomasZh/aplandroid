package com.redoct.iclub.util;

import java.util.ArrayList;

import com.redoct.iclub.item.MessageItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Message;
import android.util.Log;

public class MessageDatabaseHelperUtil {

	private static MessageDatabaseHelper mDatabaseHelper;
	private Context mContext;

	private static String TABLE_NAME_MESSAGE = "message_table";
	private static String TABLE_NAME_MESSAGE_CONFIG = "message_config_table";
	private static String TABLE_CHAT_MESSAGE = "message_chat_table";

	public MessageDatabaseHelperUtil(Context context) {
		mContext = context;
		mDatabaseHelper = new MessageDatabaseHelper(mContext);
	}

	public void addNewMessage(MessageItem item) {

		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put("accoutId", item.getAccoutId());
		values.put("messageType", item.getMessageType());
		values.put("isFeedback", item.getIsFeedback());
		values.put("channelId", item.getChannelId());
		values.put("channelName", item.getChannelName());
		values.put("channelType", item.getChannelType());
		values.put("expiry", item.getExpiry());
		values.put("userAvatarUrl", item.getUserAvatarUrl());
		values.put("userId", item.getUserId());
		values.put("userName", item.getUserName());
		values.put("inviteId", item.getInviteId());
		values.put("inviteType", item.getInviteType());
		values.put("timestamp", item.getTimestamp());
		values.put("toUserSemiId", item.getToUserSemiId());
		values.put("isAccept", item.getIsAccept());
		values.put("chatId", item.getChatId());
		values.put("lastContent", item.getLastContent());
		values.put("unReadNum", item.getUnReadNum());
		values.put("userName", item.getUserName());

		db.insert(TABLE_NAME_MESSAGE, null, values);

		Log.e("zyf", "insert new message success.....");

		if (db.isOpen()) {
			db.close();
		}
	}

	public void addChatMessage(MessageItem item) {

		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put("accoutId", item.getAccoutId());
        values.put("userAvatarUrl", item.getUserAvatarUrl());
        values.put("lastContent", item.getLastContent());
        values.put("fomeName", item.getFromName());
		db.insert(TABLE_CHAT_MESSAGE, null, values);

		Log.e("CC", "insert new message success.....");

		if (db.isOpen()) {
			db.close();
		}
	}

	public ArrayList<MessageItem> getChatMessages(String accoutId) {

		ArrayList<MessageItem> messageItems = new ArrayList<MessageItem>();

		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

		String[] args = { accoutId };

		Cursor cursor = db.query(TABLE_CHAT_MESSAGE, null, "accoutId=?", args,
				null, null, null);

		if (cursor == null) {

			db.close();
			return messageItems;
		}

		if (cursor.getCount() > 0) {
			MessageItem messageItem;

			while (cursor.moveToNext()) {
				messageItem = new MessageItem();

				messageItem.setAccoutId(cursor.getString(cursor
						.getColumnIndex("accoutId")));
				
				messageItem.setUserAvatarUrl(cursor.getString(cursor
						.getColumnIndex("userAvatarUrl")));
				
				messageItem.setLastContent(cursor.getString(cursor
						.getColumnIndex("lastContent")));
				
				messageItem.setFromName(cursor.getString(cursor
						.getColumnIndex("fomeName")));

				messageItems.add(messageItem);
			}

			Log.e("zyf", "have message info size: " + cursor.getCount());

		} else {
			Log.e("zyf", "no message info ...");
		}

		cursor.close();
		db.close();

		return messageItems;
	}

	public void updateChatMessage(MessageItem item) {

		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("lastContent", item.getLastContent());
		values.put("userAvatarUrl", item.getUserAvatarUrl());
		values.put("timestamp", item.getTimestamp());
		values.put("unReadNum", item.getUnReadNum());
		values.put("userName", item.getUserName());
		//values.put("inviteId", item.getInviteId());
		values.put("isAccept", item.getIsAccept());
		values.put("isFeedback", item.getIsFeedback());

		if (item.getMessageType() == Constant.MESSAGE_TYPE_CHAT) {

			String[] args = { item.getAccoutId(), item.getChatId() };
			db.update(TABLE_NAME_MESSAGE, values, "accoutId=? and chatId=?",
					args);

		} else {

			String[] args = { item.getAccoutId(), item.getInviteId() };
			db.update(TABLE_NAME_MESSAGE, values, "accoutId=? and inviteId=?",
					args);
		}

		Log.e("zyf", "update message success...");

		if (db.isOpen()) {
			db.close();
		}
	}

	public int getUnReadNumWithChatId(String accoutId, String chatId) {

		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		Cursor cursor = db.query(TABLE_NAME_MESSAGE, null,
				"accoutId=? and chatId=?", new String[] { accoutId, chatId },
				null, null, null);

		if (cursor == null) {

			db.close();
			return 0;
		}

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Log.e("zyf",
						"get unread num: "
								+ cursor.getInt(cursor
										.getColumnIndex("unReadNum")));

				return cursor.getInt(cursor.getColumnIndex("unReadNum"));
			}

		} else {
			Log.e("zyf", "no un read num info ...");

			return -1; // 没有该项
		}

		cursor.close();
		db.close();

		return 0;
	}
	
	public int getUnReadNumWithChatId(MessageItem item) {

		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		Cursor cursor;
		if(item.getMessageType()==Constant.MESSAGE_TYPE_CHAT){
			
			Log.e("zyf", "查询会话信息是否存在......");
			
			cursor=db.query(TABLE_NAME_MESSAGE, null,
					"accoutId=? and chatId=?", new String[] { item.getAccoutId(), item.getChatId() },
					null, null, null);
		}else{
			
			Log.e("zyf", "查询邀请信息是否存在......");
			
			cursor=db.query(TABLE_NAME_MESSAGE, null,
					"accoutId=? and inviteId=?", new String[] { item.getAccoutId(), item.getInviteId() },
					null, null, null);
		}

		if (cursor == null) {

			db.close();
			return 0;
		}

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Log.e("zyf",
						"get unread num: "
								+ cursor.getInt(cursor
										.getColumnIndex("unReadNum")));

				return cursor.getInt(cursor.getColumnIndex("unReadNum"));
			}

		} else {
			Log.e("zyf", "no un read num info ...");

			return -1; // 没有该项
		}

		cursor.close();
		db.close();

		return 0;
	}

	public ArrayList<MessageItem> getMessages(String accoutId) {

		ArrayList<MessageItem> messageItems = new ArrayList<MessageItem>();

		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

		String[] args = { accoutId };

		Cursor cursor = db.query(TABLE_NAME_MESSAGE, null, "accoutId=?", args,
				null, null, null);

		if (cursor == null) {

			db.close();
			return messageItems;
		}

		if (cursor.getCount() > 0) {
			MessageItem messageItem;

			while (cursor.moveToNext()) {
				messageItem = new MessageItem();

				messageItem.setId(cursor.getInt(cursor.getColumnIndex("id")));

				messageItem.setAccoutId(cursor.getString(cursor
						.getColumnIndex("accoutId")));
				messageItem.setMessageType(cursor.getInt(cursor
						.getColumnIndex("messageType")));
				messageItem.setIsFeedback(cursor.getInt(cursor
						.getColumnIndex("isFeedback")));
				messageItem.setChannelId(cursor.getString(cursor
						.getColumnIndex("channelId")));
				messageItem.setChannelName(cursor.getString(cursor
						.getColumnIndex("channelName")));
				messageItem.setChannelType(cursor.getInt(cursor
						.getColumnIndex("channelType")));
				messageItem.setExpiry(cursor.getInt(cursor
						.getColumnIndex("expiry")));
				messageItem.setUserAvatarUrl(cursor.getString(cursor
						.getColumnIndex("userAvatarUrl")));
				messageItem.setUserId(cursor.getString(cursor
						.getColumnIndex("userId")));
				messageItem.setUserName(cursor.getString(cursor
						.getColumnIndex("userName")));
				messageItem.setInviteId(cursor.getString(cursor
						.getColumnIndex("inviteId")));
				messageItem.setInviteType(cursor.getInt(cursor
						.getColumnIndex("inviteType")));
				messageItem.setTimestamp(cursor.getInt(cursor
						.getColumnIndex("timestamp")));
				messageItem.setToUserSemiId(cursor.getString(cursor
						.getColumnIndex("toUserSemiId")));
				messageItem.setIsAccept(cursor.getInt(cursor
						.getColumnIndex("isAccept")));
				messageItem.setChatId(cursor.getString(cursor
						.getColumnIndex("chatId")));
				messageItem.setLastContent(cursor.getString(cursor
						.getColumnIndex("lastContent")));
				messageItem.setUnReadNum(cursor.getInt(cursor
						.getColumnIndex("unReadNum")));

				messageItems.add(messageItem);
			}

			Log.e("zyf", "have message info size: " + cursor.getCount());

		} else {
			Log.e("zyf", "no message info ...");
		}

		cursor.close();
		db.close();

		return messageItems;
	}

	public void addChatLastTryTime(String accountId, int lastTryTime) {

		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put("accoutId", accountId);
		values.put("lastTryTime", lastTryTime);

		db.insert(TABLE_NAME_MESSAGE_CONFIG, null, values);

		Log.e("zyf", "insert new last try time  success.....");

		if (db.isOpen()) {
			db.close();
		}
	}

	public void updateChatLastTryTime(String accoutId, int lastTryTime) {

		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		// values.put("accoutId",accoutId);
		values.put("lastTryTime", lastTryTime);

		db.update(TABLE_NAME_MESSAGE_CONFIG, values, "accoutId=?",
				new String[] { accoutId });

		Log.e("zyf", "update last try time success...");

		if (db.isOpen()) {
			db.close();
		}
	}

	public int getLastTryTime(String accoutId) {

		SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
		Cursor cursor = db.query(TABLE_NAME_MESSAGE_CONFIG, null, "accoutId=?",
				new String[] { accoutId }, null, null, null);

		if (cursor == null) {

			db.close();
			return -1;
		}

		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Log.e("zyf",
						"last try time: "
								+ cursor.getInt(cursor
										.getColumnIndex("lastTryTime")));

				return cursor.getInt(cursor.getColumnIndex("lastTryTime"));
			}

		} else {
			Log.e("zyf", "no last try time info ...");
		}

		cursor.close();
		db.close();

		return -1;
	}

}
