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
	
	private static String TABLE_NAME_MESSAGE="message_table";
	private static String TABLE_NAME_MESSAGE_CONFIG="message_config_table";
	
	public MessageDatabaseHelperUtil(Context context)
	{
		mContext=context;
		mDatabaseHelper=new MessageDatabaseHelper(mContext);
	}
	
	public void addNewMessage(MessageItem item){
		
		SQLiteDatabase db=mDatabaseHelper.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put("accoutId",item.getAccoutId());
		values.put("messageType",item.getMessageType());
		values.put("isFeedback",item.getIsFeedback());
		values.put("channelId",item.getChannelId());
		values.put("channelName",item.getChannelName());
		values.put("channelType",item.getChannelType());
		values.put("expiry",item.getExpiry());
		values.put("userAvatarUrl",item.getUserAvatarUrl());
		values.put("userId",item.getUserId());
		values.put("userName",item.getUserName());
		values.put("inviteId",item.getInviteId());
		values.put("inviteType",item.getInviteType());
		values.put("timestamp",item.getTimestamp());
		values.put("toUserSemiId",item.getToUserSemiId());
		values.put("isAccept",item.getIsAccept());
		values.put("chatId",item.getChatId());
		values.put("lastContent",item.getLastContent());
		values.put("unReadNum",item.getUnReadNum());

		db.insert(TABLE_NAME_MESSAGE, null, values);
		
		Log.e("zyf", "insert new message success.....");
		
		if(db.isOpen()){
			db.close();
		}
	}
	
	public void updateChatMessage(MessageItem item){
		
		SQLiteDatabase db=mDatabaseHelper.getWritableDatabase();
		
		ContentValues values=new ContentValues();
		values.put("lastContent", item.getLastContent());
		values.put("userAvatarUrl", item.getUserAvatarUrl());
		values.put("timestamp", item.getTimestamp());
		values.put("unReadNum", item.getUnReadNum());
		values.put("userName", item.getUserName());
		values.put("inviteId", item.getInviteId());
		values.put("isAccept", item.getIsAccept());
		
		if(item.getMessageType()==Constant.MESSAGE_TYPE_CHAT){
			
			String[] args={item.getAccoutId(),item.getChatId()};
			db.update(TABLE_NAME_MESSAGE, values, "accoutId=? and chatId=?", args);
			
		}else{
			
			String[] args={item.getAccoutId(),item.getInviteId()};
			db.update(TABLE_NAME_MESSAGE, values, "accoutId=? and inviteId=?", args);
		}
		
		Log.e("zyf","update message success...");
		
		if(db.isOpen()){
			db.close();
		}
	}
	
	public ArrayList<MessageItem> getMessages(String accoutId){
		
		ArrayList<MessageItem> messageItems=new ArrayList<MessageItem>();
		
		SQLiteDatabase db=mDatabaseHelper.getWritableDatabase();
		
		String[] args={accoutId};
		
		Cursor cursor = db.query(TABLE_NAME_MESSAGE, null, "accoutId=?", args, null, null, null);
		
		if(cursor==null){
			
			db.close();
			return messageItems;
		}
		
		if(cursor.getCount()>0)
    	{
			MessageItem messageItem;
			
    		while(cursor.moveToNext())
        	{
    			messageItem=new MessageItem();
    			
    			messageItem.setId(cursor.getInt(cursor.getColumnIndex("id")));
    			
    			messageItem.setAccoutId(cursor.getString(cursor.getColumnIndex("accoutId")));
    			messageItem.setMessageType(cursor.getInt(cursor.getColumnIndex("messageType")));
    			messageItem.setIsFeedback(cursor.getInt(cursor.getColumnIndex("isFeedback")));
    			messageItem.setChannelId(cursor.getString(cursor.getColumnIndex("channelId")));
    			messageItem.setChannelName(cursor.getString(cursor.getColumnIndex("channelName")));
    			messageItem.setChannelType(cursor.getInt(cursor.getColumnIndex("channelType")));
    			messageItem.setExpiry(cursor.getInt(cursor.getColumnIndex("expiry")));
    			messageItem.setUserAvatarUrl(cursor.getString(cursor.getColumnIndex("userAvatarUrl")));
    			messageItem.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
    			messageItem.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
    			messageItem.setInviteId(cursor.getString(cursor.getColumnIndex("inviteId")));
    			messageItem.setInviteType(cursor.getInt(cursor.getColumnIndex("inviteType")));
    			messageItem.setTimestamp(cursor.getInt(cursor.getColumnIndex("timestamp")));
    			messageItem.setToUserSemiId(cursor.getString(cursor.getColumnIndex("toUserSemiId")));
    			messageItem.setIsAccept(cursor.getInt(cursor.getColumnIndex("isAccept")));
    			messageItem.setChatId(cursor.getString(cursor.getColumnIndex("chatId")));
    			messageItem.setLastContent(cursor.getString(cursor.getColumnIndex("lastContent")));
    			messageItem.setUnReadNum(cursor.getInt(cursor.getColumnIndex("unReadNum")));    			
    			
    			messageItems.add(messageItem);
        	}
			
			Log.e("zyf","have message info size: "+cursor.getCount());
    		
    	}else{
    		Log.e("zyf","no message info ...");
    	}
     
		cursor.close();
		db.close();
		
		return messageItems;
	}
	
	public void addChatLastTryTime(String accountId,int lastTryTime){
		
		SQLiteDatabase db=mDatabaseHelper.getWritableDatabase();
		ContentValues values=new ContentValues();

		values.put("accoutId",accountId);
		values.put("lastTryTime",lastTryTime);

		db.insert(TABLE_NAME_MESSAGE_CONFIG, null, values);
		
		Log.e("zyf", "insert new last try time  success.....");
		
		if(db.isOpen()){
			db.close();
		}
	}
	
	public void updateChatLastTryTime(String accoutId,int lastTryTime){
		
		SQLiteDatabase db=mDatabaseHelper.getWritableDatabase();
		
		ContentValues values=new ContentValues();
		//values.put("accoutId",accoutId);
		values.put("lastTryTime", lastTryTime);
		
		db.update(TABLE_NAME_MESSAGE_CONFIG, values, "accoutId=?", new String[]{accoutId});
		
		Log.e("zyf","update last try time success...");
		
		if(db.isOpen()){
			db.close();
		}
	}
	
	public int getLastTryTime(String accoutId){
		
		SQLiteDatabase db=mDatabaseHelper.getWritableDatabase();
		Cursor cursor = db.query(TABLE_NAME_MESSAGE_CONFIG, null, "accoutId=?", new String[]{accoutId}, null, null, null);
		
		if(cursor==null){
			
			db.close();
			return -1;
		}
		
		if(cursor.getCount()>0)
    	{
			while(cursor.moveToNext())
        	{
				Log.e("zyf","last try time: "+cursor.getInt(cursor.getColumnIndex("lastTryTime")));
				
				return cursor.getInt(cursor.getColumnIndex("lastTryTime"));
        	}
    		
    	}else{
    		Log.e("zyf","no last try time info ...");
    	}
     
		cursor.close();
		db.close();
		
		return -1;
	}
	
	/*public void addNewChessmanInfos(ArrayList<Chessman> chessmans){
		
		SQLiteDatabase db=mDatabaseHelper.getWritableDatabase();
		Chessman chessman;
		ContentValues values;
		for(int i=0;i<chessmans.size();i++){
			
			chessman=chessmans.get(i);
			
			values=new ContentValues();
			values.put("gameId",chessman.getGameId());
			if(chessman.isRed()){
				values.put("isRed",0);
			}else {
				values.put("isRed",1);
			}
			values.put("type",chessman.getType());
			values.put("locX",chessman.getLocX());
			values.put("locY",chessman.getLocY());
			db.insert(TABLE_NAME_CHESSMAN, null, values);
		}
		
		if(db.isOpen()){
			db.close();
		}
	}
	
	public void addNewGameInfo(GameInfo gameInfo){
		
		SQLiteDatabase db=mDatabaseHelper.getWritableDatabase();
		

		ContentValues values=new ContentValues();
		values.put("gameId",gameInfo.getGameId());
		values.put("isWhoTurn", gameInfo.getIsWhoTurn());
		values.put("status", gameInfo.getStatus());
		values.put("selectedLocX", gameInfo.getSelectedLocX());
		values.put("selectedLocY", gameInfo.getSelectedLocY());
		values.put("pointerLocX", gameInfo.getPointerLocX());
		values.put("pointerLocY", gameInfo.getPointerLocY());
			
		db.insert(TABLE_NAME_GAME, null, values);
		
		if(db.isOpen()){
			db.close();
		}
	}
	
	public static void deleteGameWithId(String gameId){
		
		SQLiteDatabase db=mDatabaseHelper.getWritableDatabase();
		String[] args={String.valueOf(gameId)};
        db.delete(TABLE_NAME_CHESSMAN, "gameId=?", args);
        db.delete(TABLE_NAME_GAME, "gameId=?", args);
		
		Log.e("zyf","delete game success...");
		
		if(db.isOpen()){
			db.close();
		}
	}
	
	public void deleteChessmanWithId(int id){
		
		SQLiteDatabase db=mDatabaseHelper.getWritableDatabase();
		
		String[] args={String.valueOf(id)};
        db.delete(TABLE_NAME_CHESSMAN, "id=?", args);
		
		Log.e("zyf","delete chessman success...");
		
		if(db.isOpen()){
			db.close();
		}
	}
	
	public void updateGameWithId(GameInfo gameInfo){
		
		SQLiteDatabase db=mDatabaseHelper.getWritableDatabase();
		
		String[] args={String.valueOf(gameInfo.getGameId())};
		
		ContentValues values=new ContentValues();
		//values.put("gameId", gameInfo.getGameId());
		values.put("isWhoTurn", gameInfo.getIsWhoTurn());
		values.put("status", gameInfo.getStatus());
		values.put("selectedLocX", gameInfo.getSelectedLocX());
		values.put("selectedLocY", gameInfo.getSelectedLocY());
		values.put("pointerLocX", gameInfo.getPointerLocX());
		values.put("pointerLocY", gameInfo.getPointerLocY());
		
		db.update(TABLE_NAME_GAME, values, "gameId=?", args);
		
		Log.e("zyf","update game success...");
		
		if(db.isOpen()){
			db.close();
		}
	}
	
	public void updateChessmanWithId(Chessman chessman){
		
		SQLiteDatabase db=mDatabaseHelper.getWritableDatabase();
		
		String[] args={String.valueOf(chessman.getId())};
		
		ContentValues values=new ContentValues();
		values.put("id", chessman.getId());
		values.put("gameId", chessman.getGameId());
		if(chessman.isRed()){
			values.put("isRed", 0);
		}else{
			values.put("isRed", 1);
		}
		values.put("type", chessman.getType());
		values.put("locX", chessman.getLocX());
		values.put("locY", chessman.getLocY());
		
		db.update(TABLE_NAME_CHESSMAN, values, "id=?", args);
		
		Log.e("zyf","update chessman success...");
		
		if(db.isOpen()){
			db.close();
		}
	}
	
	public ArrayList<Chessman> getChessmans(String gameId){
		
		ArrayList<Chessman> chessmans=new ArrayList<Chessman>();
		
		SQLiteDatabase db=mDatabaseHelper.getWritableDatabase();
		Cursor cursor = db.query(TABLE_NAME_CHESSMAN, null, "gameId=?", new String[]{gameId}, null, null, null);
		
		if(cursor==null){
			
			db.close();
			return chessmans;
		}
		
		if(cursor.getCount()>0)
    	{
			Chessman chessman;
			int isRed;
    		while(cursor.moveToNext())
        	{
    			chessman=new Chessman(mContext);
    			
    			chessman.setId(cursor.getInt(cursor.getColumnIndex("id")));
    			chessman.setGameId(cursor.getString(cursor.getColumnIndex("gameId")));
    			chessman.setType(cursor.getInt(cursor.getColumnIndex("type")));
    			chessman.setLocX(cursor.getInt(cursor.getColumnIndex("locX")));
    			chessman.setLocY(cursor.getInt(cursor.getColumnIndex("locY")));
    			
    			isRed=cursor.getInt(cursor.getColumnIndex("isRed"));
    			if(isRed==0){
    				chessman.setRed(true);
    			}else{
    				chessman.setRed(false);
    			}
    			
    			chessmans.add(chessman);
        	}
			
			Log.e("zyf","have chessman info size: "+cursor.getCount());
    		
    	}else{
    		Log.e("zyf","no chessman info ...");
    	}
     
		cursor.close();
		db.close();
		
		return chessmans;
	}
	
	public GameInfo getGameInfo(String gameId){
		
		SQLiteDatabase db=mDatabaseHelper.getWritableDatabase();
		Cursor cursor = db.query(TABLE_NAME_GAME, null, "gameId=?", new String[]{gameId}, null, null, null);
		
		GameInfo gameInfo=null;
		
		if(cursor!=null&&cursor.getCount()>0)
    	{
    		while(cursor.moveToNext())
        	{
    			gameInfo=new GameInfo();
    			
    			gameInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
    			gameInfo.setGameId(cursor.getString(cursor.getColumnIndex("gameId")));
    			gameInfo.setIsWhoTurn(cursor.getInt(cursor.getColumnIndex("isWhoTurn")));
    			gameInfo.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
    			gameInfo.setPointerLocX(cursor.getInt(cursor.getColumnIndex("pointerLocX")));
    			gameInfo.setPointerLocY(cursor.getInt(cursor.getColumnIndex("pointerLocY")));
    			gameInfo.setSelectedLocX(cursor.getInt(cursor.getColumnIndex("selectedLocX")));
    			gameInfo.setSelectedLocY(cursor.getInt(cursor.getColumnIndex("selectedLocY")));
    			
    			break;
        	}
    	}else{
    		Log.e("zyf","no game info ...");
    	}
        
		cursor.close();
		db.close();
		
		return gameInfo;
	}
	
	public boolean isGameInfoExist(String gameId)
	{
		SQLiteDatabase db=mDatabaseHelper.getWritableDatabase();
		Cursor cursor = db.query(TABLE_NAME_GAME, null, "gameId=?", new String[]{gameId}, null, null, null);
		if(cursor==null){
			db.close();
			return false;
		}
		while(cursor.moveToNext()){
			cursor.close();
			db.close();
			return true;
		}
		
		cursor.close();
		db.close();
		return false;
	}*/
}
