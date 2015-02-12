package com.redoct.iclub.task;

import android.content.Context;
import android.util.Log;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.cmd.club.ActivityRecommendReq;
import com.oct.ga.comm.cmd.club.ActivityRecommendResp;
import com.oct.ga.comm.domain.club.ActivityRecommend;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.config.AppConfig;
import com.redoct.iclub.util.PersistentUtil;

public class ActivityRecommendTask extends TemplateTask{
	
	private Context context;
	
	private String activityId;
	
	private String [] userIds;
	
	private String recommendReason;

	public ActivityRecommendTask(Context context, String activityId,
			String[] userIds, String recommendReason) {
		super();
		this.context = context;
		this.activityId = activityId;
		this.userIds = userIds;
		this.recommendReason = recommendReason;
	}

	@Override
	protected boolean justTodo() {
		
		ActivityRecommend recommend=new ActivityRecommend();
		recommend.setActivityId(activityId);
		recommend.setFromUserId(AppConfig.account.getAccountId());
		recommend.setText(recommendReason);
		recommend.setToUserIds(userIds);
		
		ActivityRecommendReq req=new ActivityRecommendReq(recommend);
		req.setSequence(DatetimeUtil.currentTimestamp());
		
		try {
			
			ActivityRecommendResp resp=(ActivityRecommendResp)iClubApplication.send(req);
			
			if(resp==null){
				
				Log.e("zyf", "activity recommend resp is null......");
				
				return false;
			}
			
			if(resp.getRespState()!=ErrorCode.SUCCESS){
				
				Log.e("zyf", "activity recommend resp is error......");
				
				return false;
			}
			
		} catch (Exception e) {
			
			Log.e("zyf", "activity recommend exception: "+e.toString());
			
			return false;
		}
		
		return true;
	}
}
