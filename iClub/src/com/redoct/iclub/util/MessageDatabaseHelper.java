package com.redoct.iclub.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MessageDatabaseHelper extends SQLiteOpenHelper{

	private static final String DB_NAME = "message.db";
	private static final int DB_VERSION = 1;

	private static final String CREATE_MESSAGE_TABLE = "create table message_table ("
			                                                        +"id integer primary key autoincrement,"
			                                                        +"accoutId String,"
			                                                        +"messageType integer,"
			                                                        +"isFeedback integer,"
			                                                        +"channelId String,"
			                                                        +"channelName String,"
			                                                        +"channelType integer,"
			                                                        +"expiry integer,"
			                                                        +"userAvatarUrl String,"
			                                                        +"userId String,"
			                                                        +"userName String,"
			                                                        +"inviteId String,"
			                                                        +"inviteType integer,"
			                                                        +"timestamp integer,"
			                                                        +"toUserSemiId String,"
			                                                        +"isAccept integer,"
																	+"chatId String,"
																+"lastContent String,"
																	+"unReadNum integer);";
	private static final String CREATE_MESSAGE_CONFIG_TABLE = "create table message_config_table ("
												            +"id integer primary key autoincrement,"
												            +"accoutId String,"
															+"lastTryTime integer);";
	private static final String CREATE_CHAT_MESSAGE__TABLE = "create table message_chat_table ("
			+"id integer primary key autoincrement,"
			+"accoutId String,"
			+"chatId String,"
			+"isSend String,"
			+"userAvatarUrl String,"
		
			+"lastContent String,"
			+"fomeName String);";
			
	
	
	public MessageDatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(CREATE_MESSAGE_TABLE);
		
		db.execSQL(CREATE_MESSAGE_CONFIG_TABLE);
		
		db.execSQL(CREATE_CHAT_MESSAGE__TABLE);
		
		/*ContentValues values=new ContentValues();
		values.put("lastTryTime",0);
		db.insert("message_config_table", null, values);*/
		
		Log.e("zyf:", "message.db onCreate...");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		
		db.execSQL("drop table message_table if exists");	
		db.execSQL(CREATE_MESSAGE_TABLE);
		
		db.execSQL("drop table message_config_table if exists");	
		db.execSQL(CREATE_MESSAGE_CONFIG_TABLE);
		
		db.execSQL("drop message_chat_table if exists");	
		db.execSQL(CREATE_CHAT_MESSAGE__TABLE);
		
		/*ContentValues values=new ContentValues();
		values.put("lastTryTime",0);
		db.insert("message_config_table", null, values);*/
		
		Log.e("zyf:", "message.db onUpgrade...");
	}

}
