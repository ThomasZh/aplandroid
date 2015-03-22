package com.redoct.iclub.item;

import java.io.Serializable;

public class MessageItem implements Serializable{
	
	private boolean isFeedback;  //用于标识  别人发给自己的邀请  or 自己发出的邀请后别人的反馈
	private String channelId;
	private String channelName;
	private short channelType;
	private int expiry;
	private String userAvatarUrl;
	private String userId;
	private String userName;
	private String inviteId;
	private short inviteType;
	private int timestamp;
	private String toUserSemiId;
	private boolean isAccept;   //是否接受请求
	private String content;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isFeedback() {
		return isFeedback;
	}
	public void setFeedback(boolean isFeedback) {
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
	public short getChannelType() {
		return channelType;
	}
	public void setChannelType(short channelType) {
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
	public short getInviteType() {
		return inviteType;
	}
	public void setInviteType(short inviteType) {
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
	public boolean isAccept() {
		return isAccept;
	}
	public void setAccept(boolean isAccept) {
		this.isAccept = isAccept;
	}
}
