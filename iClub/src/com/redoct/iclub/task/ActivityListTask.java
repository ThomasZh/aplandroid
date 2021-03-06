package com.redoct.iclub.task;

import android.R.integer;
import android.content.Context;
import android.util.Log;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.club.ActivityQuerySubscribePaginationReq;
import com.oct.ga.comm.cmd.club.ActivityQuerySubscribePaginationResp;
import com.oct.ga.comm.domain.club.ActivitySubscribeInfo;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.R.string;
import com.redoct.iclub.item.ActivityItem;
import com.redoct.iclub.util.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by liwenzhi on 14-10-8.
 */
public class ActivityListTask extends TemplateTask {
	
	private short pageNum;
	private short pageSize;
	
	private Context mContext;
	
	private DateUtils mDateUtils;
	
	private String result;
    
    public String getResult(){
    	return result;
    }
    
    private ArrayList<ActivityItem> activityItems;
    
    public ArrayList<ActivityItem> getActivities() {
        return activityItems;
    }
    
    public ActivityListTask(Context mContext,short pageNum,short pageSize){
    	activityItems = new ArrayList<ActivityItem>();
        
    	this.mContext=mContext;
        this.pageNum=pageNum;
        this.pageSize=pageSize;
        
        mDateUtils=new DateUtils(mContext);
    }

    @Override
    protected boolean justTodo() {

       /* //ActivityQuerySubscribeReq req = new ActivityQuerySubscribeReq();
    	
        ActivityQuerySubscribePaginationReq req=new ActivityQuerySubscribePaginationReq(pageNum, pageSize);
        
        req.setSequence(DatetimeUtil.currentTimestamp());
        
        try {

            //ActivityQuerySubscribeResp resp = (ActivityQuerySubscribeResp)iClubApplication.send(req);
        	
        	ActivityQuerySubscribePaginationResp resp=(ActivityQuerySubscribePaginationResp)iClubApplication.send(req);
        	
        	//RespCommand resp=iClubApplication.send(req);
        	
            if(resp==null) {
            	
                return false;//fetch in failure!
            }

           String meetups = resp.getJson();
        
            Log.e("zyf", "json: "+meetups);
            
            result=meetups;
            
            JSONArray activityJsonArray=new JSONArray(meetups);
            JSONObject jsonObject;
            ActivityItem item;
            String totalTime;
            for(int i=0;i<activityJsonArray.length();i++){
            	item=new ActivityItem();
            	jsonObject=activityJsonArray.getJSONObject(i);
            	
            	item.setClubName(jsonObject.optString("clubName"));
            	item.setId(jsonObject.optString("id"));
            	item.setLeaderAvatarUrl(jsonObject.optString("leaderAvatarUrl"));
            	item.setLeaderName(jsonObject.optString("leaderName"));
            	item.setLocDesc(jsonObject.optString("locDesc"));
            	item.setMemberNum(jsonObject.optString("memberNum"));
            	item.setMemberRank(Short.parseShort(jsonObject.optString("memberRank")));
            	item.setName(jsonObject.optString("name"));
            	item.setPid(jsonObject.optString("pid"));
            	item.setRecommendNum(jsonObject.optString("recommendNum"));
            	item.setState(jsonObject.optString("state"));
            	
            	totalTime=mDateUtils.getFormatDate(jsonObject.getInt("startTime"));
            	
            	item.setStartDate(totalTime.split(" ")[0]+"  "+totalTime.split(" ")[1]);
            	item.setStartTime(totalTime.split(" ")[2]);
            	
            	activityItems.add(item);
            }


        } catch (InterruptedException e) {
            Log.e("zyf", "InterruptedException");
            return false;
        } catch (UnsupportedEncodingException e) {
            Log.e("zyf", "UnsupportedEncodingException");
            return false;
        } catch (JSONException e) {
            Log.e("zyf", "JSONException: "+e.toString());
            return false;
        }

        //FileLogger.writeLogFileToSDCard("INFO: Fetching success!");
*/
    	
    	ActivityQuerySubscribePaginationReq req=new ActivityQuerySubscribePaginationReq(pageNum, pageSize);
        
        req.setSequence(DatetimeUtil.currentTimestamp());
        
        try {

        	ActivityQuerySubscribePaginationResp resp=(ActivityQuerySubscribePaginationResp)iClubApplication.send(req);
        	
            if(resp==null) {
            	
                return false;//fetch in failure!
            }

           List<ActivitySubscribeInfo> activityList = resp.getActivities();
        
            //Log.e("zyf", "json: "+meetups);
            
            //result=meetups;
            
            //JSONArray activityJsonArray=new JSONArray(meetups);
            ActivitySubscribeInfo activitySubscribeInfo;
            ActivityItem item;
            String totalTime;
            for(int i=0;i<activityList.size();i++){
            	item=new ActivityItem();
            	activitySubscribeInfo=activityList.get(i);
            	
            	item.setClubName(activitySubscribeInfo.getClubName());
            	item.setId(activitySubscribeInfo.getId());
            	item.setLeaderAvatarUrl(activitySubscribeInfo.getLeaderAvatarUrl());
            	item.setLeaderName(activitySubscribeInfo.getLeaderName());
            	item.setLocDesc(activitySubscribeInfo.getLocDesc());
            	item.setMemberNum(activitySubscribeInfo.getMemberNum()+"");
            	item.setMemberRank((short)activitySubscribeInfo.getMemberRank());
            	item.setName(activitySubscribeInfo.getName());
            	item.setPid(activitySubscribeInfo.getPid());
            	item.setRecommendNum(activitySubscribeInfo.getRecommendNum()+"");
            	item.setState(activitySubscribeInfo.getState()+"");
            	
            	totalTime=mDateUtils.getFormatDate(activitySubscribeInfo.getStartTime());
            	
            	item.setStartDate(totalTime.split(" ")[0]+"  "+totalTime.split(" ")[1]);
            	item.setStartTime(totalTime.split(" ")[2]);
            	
            	activityItems.add(item);
            }


        } catch (Exception e) {
            Log.e("zyf", "activity list exception: "+e.toString());
            return false;
        }
        
        return true;
    }



}
