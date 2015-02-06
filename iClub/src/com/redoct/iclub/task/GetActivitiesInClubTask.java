package com.redoct.iclub.task;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.cmd.club.ActivityQueryHistoryPaginationReq;
import com.oct.ga.comm.cmd.club.ActivityQueryHistoryPaginationResp;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.item.ActivityItem;
import com.redoct.iclub.util.DateUtils;

/**
 * Created by liwenzhi on 14-10-30.
 */
public class GetActivitiesInClubTask extends TemplateTask {
	
	private short pageNum;
	private short pageSize;

    private String groupId;
    private ArrayList<ActivityItem> activityItems;
    
    private DateUtils mDateUtils;
    private Context mContext;

    public GetActivitiesInClubTask(Context mContext,String groupId,short pageNum,short pageSize) {
    	this.mContext=mContext;
        this.groupId = groupId;
        this.pageNum=pageNum;
        this.pageSize=pageSize;
        
        activityItems = new ArrayList<ActivityItem>();
        
        mDateUtils=new DateUtils(mContext);
    }

    public ArrayList<ActivityItem> getActivities() {
        return activityItems;
    }

    @Override
    protected boolean justTodo() {

        ActivityQueryHistoryPaginationReq req = new ActivityQueryHistoryPaginationReq();
        req.setClubId(this.groupId);
        req.setPageNum(pageNum);
        req.setPageSize(pageSize);
        req.setSequence(DatetimeUtil.currentTimestamp());
        try {
            ActivityQueryHistoryPaginationResp resp = (ActivityQueryHistoryPaginationResp) iClubApplication.send(req);
            if (resp == null){
                Log.e("zyf", "get activities in group is null!");
                return false;
            }

            Log.e("zyf", "get activities in group "+resp.getRespState()+" returned!");

            if (resp.getJson().length() == 0){
                Log.e("zyf", "get activities in group json content is null!");
                return false;
            } else {
                Log.e("zyf", "get activities in group: "+resp.getJson());
            }

            JSONArray jsonArray = new JSONArray(resp.getJson());
            JSONObject jsonObject;
            ActivityItem item;
            String totalTime;
            
            for (int i=0; i<jsonArray.length(); i++){
            	
            	item=new ActivityItem();
            	jsonObject=jsonArray.getJSONObject(i);
            	
            	item.setClubName(jsonObject.getString("clubName"));
            	item.setId(jsonObject.getString("id"));
            	item.setMemberNum(jsonObject.getString("memberNum"));
            	item.setMemberRank(jsonObject.getString("memberRank"));
            	item.setName(jsonObject.getString("name"));
            	item.setRecommendNum(jsonObject.getString("recommendNum"));
            	item.setState(jsonObject.getString("state"));
            	
            	totalTime=mDateUtils.getFormatDate(jsonObject.getInt("startTime"));
            	
            	item.setStartDate(totalTime.split(" ")[0]+"  "+totalTime.split(" ")[1]);
            	item.setStartTime(totalTime.split(" ")[2]);
            	
            	activityItems.add(item);
            }


        }catch (Exception e) {
            Log.e("zyf", e.toString());
            return false;
        }

        return true;
    }


}
