package com.redoct.iclub.task;

import java.io.UnsupportedEncodingException;

import android.util.Log;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.cmd.club.ActivityUpdateSubscribersReq;
import com.oct.ga.comm.cmd.club.ActivityUpdateSubscribersResp;
import com.redoct.iclub.iClubApplication;

public class ModifyAcitivityMembersTask extends TemplateTask {
	
	private String activityId;
	private String [] subscriberIds;
	
	public ModifyAcitivityMembersTask(String activityId, String[] subscriberIds) {
		super();
		this.activityId = activityId;
		this.subscriberIds = subscriberIds;
		
		Log.e("zyf", "ModifyAcitivityMembersTask activityId: "+activityId);
		for(int i=0;i<subscriberIds.length;i++){
			Log.e("zyf", "ModifyAcitivityMembersTask subscriberId: "+subscriberIds[i]);
		}
	}

	@Override
	protected boolean justTodo() {
		
		ActivityUpdateSubscribersReq req=new ActivityUpdateSubscribersReq(DatetimeUtil.currentTimestamp(), activityId, subscriberIds);
		
		try {
			ActivityUpdateSubscribersResp resp=(ActivityUpdateSubscribersResp) iClubApplication.send(req);
			
			if (resp == null){
                Log.e("zyf", "activity member modify resp is null......");
                return false;
            }

            if (resp.getRespState() != ErrorCode.SUCCESS){
                Log.e("zyf", "activity member modify failed,state: "+resp.getRespState());
                return false;
            }
            
		} catch (Exception e) {
			
			Log.e("zyf", "ModifyAcitivityMembersTask exception: "+e.toString());
			
			return false;
		}

		return true;
	}

}
