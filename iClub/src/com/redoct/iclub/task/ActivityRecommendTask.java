package com.redoct.iclub.task;

import android.content.Context;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.cmd.club.ActivityRecommendReq;
import com.oct.ga.comm.domain.club.ActivityRecommend;
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
		recommend.setActivityId("");
		recommend.setFromUserId(AppConfig.account.getAccountId());
		recommend.setFromUserName(PersistentUtil.getInstance().readString(context,
				"username", ""));
		//recommend.setSyncState(0);
		recommend.setText(recommendReason);
		recommend.setTimestamp(DatetimeUtil.currentTimestamp());
		recommend.setToUserIds(userIds);
		
		ActivityRecommendReq req=new ActivityRecommendReq(recommend);
		req.setSequence(DatetimeUtil.currentTimestamp());
		
		return true;
	}
}
