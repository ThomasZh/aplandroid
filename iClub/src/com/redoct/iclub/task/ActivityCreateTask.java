package com.redoct.iclub.task;

import android.util.Log;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.cmd.club.ActivityCreateReq;
import com.oct.ga.comm.cmd.club.ActivityCreateResp;
import com.oct.ga.comm.domain.club.ActivityCreateInfo;
import com.oct.ga.comm.domain.club.ActivityDetailInfo;
import com.redoct.iclub.iClubApplication;

import java.io.UnsupportedEncodingException;

/**
 * Created by liwenzhi on 14-11-4.
 */
public class ActivityCreateTask extends TemplateTask {


    private String meetupJson;
    
    private ActivityCreateInfo activityCreateInfo;
    private String [] subscriberIds;

    public ActivityCreateTask(String meetup){
        this.meetupJson = meetup;
    }

	public ActivityCreateTask(ActivityCreateInfo activityCreateInfo,
			String[] subscriberIds) {
		super();
		this.activityCreateInfo = activityCreateInfo;
		this.subscriberIds = subscriberIds;
	}

	@Override
    protected boolean justTodo() {

        /*ActivityCreateReq req = new ActivityCreateReq;
        req.setSequence(DatetimeUtil.currentTimestamp());
        try {
            ActivityCreateResp resp = (ActivityCreateResp) iClubApplication.send(req);
            if (resp == null){
                Log.e("zyf", "resp is null for ActivityCreateResp!");
                return false;
            }

            if (resp.getRespState() == 200){
                Log.e("zyf", "server response error!");
                return false;
            }

            String meetupId = resp.getClubActivityId();
            Log.d("zyf", "meetup id: "+meetupId);

        } catch (Exception e) {
            Log.e("zyf","activity create exception： "+e.toString());
            return false;
        }*/
    	
		ActivityCreateReq req = new ActivityCreateReq(DatetimeUtil.currentTimestamp(), activityCreateInfo, subscriberIds);
        req.setSequence(DatetimeUtil.currentTimestamp());
        try {
            ActivityCreateResp resp = (ActivityCreateResp) iClubApplication.send(req);
            
            if (resp == null){
                Log.e("zyf", "resp is null for ActivityCreateResp!");
                return false;
            }

            if (resp.getRespState() == 200){
                Log.e("zyf", "server response error!");
                return false;
            }

            String meetupId = resp.getClubActivityId();
            Log.d("zyf", "meetup id: "+meetupId);

        } catch (Exception e) {
            Log.e("zyf","activity create exception： "+e.toString());
            return false;
        }
        
        return true;
    }


}
