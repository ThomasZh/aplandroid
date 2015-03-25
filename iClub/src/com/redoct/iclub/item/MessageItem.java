package com.redoct.iclub.item;

import java.io.Serializable;

import android.R.integer;

public class MessageItem implements Serializable{
	
	private int id;  //数据库中的id
	private String accoutId;   //登陆的用户id，一个软件可能登陆过多个用户
	
	private int messageType;   //0:邀请信息   1：会话信息
	
	private int isFeedback;  //用于标识  0:别人发给自己的邀请   1:自己发出的邀请后别人的反馈
	private String channelId;
	private String channelName;
	private int channelType;
	private int expiry;
	private String userAvatarUrl;
	private String userId;
	private String userName;
	private String inviteId;
	private int inviteType;
	private int timestamp;
	private String toUserSemiId;
	private int isAccept;   //是否接受请求  0:已接受  1：未接受
	
	//记录所有的聊天会话
	private String chatId;
	private String lastContent;
	private int unReadNum;
	
	
	
	public String getAccoutId() {
		return accoutId;
	}
	public void setAccoutId(String accoutId) {
		this.accoutId = accoutId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLastContent() {
		return lastContent;
	}
	public void setLastContent(String lastContent) {
		this.lastContent = lastContent;
	}
	public int getIsFeedback() {
		return isFeedback;
	}
	public void setIsFeedback(int isFeedback) {
		this.isFeedback = isFeedback;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public int getChannelType() {
		return channelType;
	}
	public void setChannelType(int channelType) {
		this.channelType = channelType;
	}
	public int getExpiry() {
		return expiry;
	}
	public void setExpiry(int expiry) {
		this.expiry = expiry;
	}
	public String getUserAvatarUrl() {
		return userAvatarUrl;
	}
	public void setUserAvatarUrl(String userAvatarUrl) {
		this.userAvatarUrl = userAvatarUrl;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getInviteId() {
		return inviteId;
	}
	public void setInviteId(String inviteId) {
		this.inviteId = inviteId;
	}
	public int getInviteType() {
		return inviteType;
	}
	public void setInviteType(int inviteType) {
		this.inviteType = inviteType;
	}
	public int getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	public String getToUserSemiId() {
		return toUserSemiId;
	}
	public void setToUserSemiId(String toUserSemiId) {
		this.toUserSemiId = toUserSemiId;
	}
	public int getIsAccept() {
		return isAccept;
	}
	public void setIsAccept(int isAccept) {
		this.isAccept = isAccept;
	}
	public int getMessageType() {
		return messageType;
	}
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	public String getChatId() {
		return chatId;
	}
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	public int getUnReadNum() {
		return unReadNum;
	}
	public void setUnReadNum(int unReadNum) {
		this.unReadNum = unReadNum;
	}
}
