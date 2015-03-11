package com.redoct.iclub.task;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.cmd.club.ActivityQueryMyFuturePaginationReq;
import com.oct.ga.comm.cmd.club.ActivityQueryMyFuturePaginationResp;
import com.oct.ga.comm.domain.club.ActivitySubscribeInfo;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.item.ActivityItem;
import com.redoct.iclub.util.DateUtils;

public class MyActivityTask extends TemplateTask {
	
	private short pageNum;
	private short pageSize;
	
	private DateUtils mDateUtils;
	
	private ArrayList<ActivityItem> activityItems;
    
    public ArrayList<ActivityItem> getActivities() {
        return activityItems;
    }

	public MyActivityTask(Context mContext, short pageNum, short pageSize) {
		super();
		
		activityItems = new ArrayList<ActivityItem>();
        
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		
		mDateUtils=new DateUtils(mContext);
	}

	@Override
	protected boolean justTodo() {
		// TODO Auto-generated method stub
		
		ActivityQueryMyFuturePaginationReq req=new ActivityQueryMyFuturePaginationReq(DatetimeUtil.currentTimestamp(), pageNum, pageSize);
		
		try {
			ActivityQueryMyFuturePaginationResp resp=(ActivityQueryMyFuturePaginationResp) iClubApplication.send(req);
			
			List<ActivitySubscribeInfo> list=resp.getActivities();
			ActivitySubscribeInfo activitySubscribeInfo;
			ActivityItem activityItem;
			String totalTime;
			for(int i=0;i<list.size();i++){
				activitySubscribeInfo=list.get(i);
				
				activityItem=new ActivityItem();
            	
				activityItem.setClubName(activitySubscribeInfo.getClubName());
				activityItem.setId(activitySubscribeInfo.getId());
				activityItem.setLeaderAvatarUrl(activitySubscribeInfo.getLeaderAvatarUrl());
				activityItem.setLeaderName(activitySubscribeInfo.getLeaderName());
            	activityItem.setLocDesc(activitySubscribeInfo.getLocDesc());
            	activityItem.setMemberNum(activitySubscribeInfo.getMemberNum()+"");
            	activityItem.setMemberRank(Short.parseShort(activitySubscribeInfo.getMemberRank()+""));
            	activityItem.setName(activitySubscribeInfo.getName());
            	activityItem.setPid(activitySubscribeInfo.getPid());
            	activityItem.setRecommendNum(activitySubscribeInfo.getRecommendNum()+"");
            	activityItem.setState(activitySubscribeInfo.getState()+"");
            	
            	totalTime=mDateUtils.getFormatDate(activitySubscribeInfo.getStartTime());
            	
            	activityItem.setStartDate(totalTime.split(" ")[0]+"  "+totalTime.split(" ")[1]);
            	activityItem.setStartTime(totalTime.split(" ")[2]);
            	
            	activityItems.add(activityItem);
			}
			
			return true;
		} catch (Exception e) {
			Log.e("zyf","MyActivityTask exception: "+e.toString());
		}
		
		return false;
	}
	
	
}
