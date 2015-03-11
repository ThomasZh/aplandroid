package com.redoct.iclub.task;

import android.util.Log;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.cmd.club.ActivityQueryDetailReq;
import com.oct.ga.comm.cmd.club.ActivityQueryDetailResp;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.item.ActivityDetailsItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by liwenzhi on 14-10-14.
 */
public class ActivityDetailsTask extends TemplateTask {

    private String meetupId;
    private ActivityDetailsItem activityDetailsItem;


    public ActivityDetailsTask(String meetupId) {
        this.meetupId = meetupId;
    }

    public ActivityDetailsItem getDetails() {
        return activityDetailsItem;
    }

    /*public void setDetails(MeetupDetailsVO details) {
        this.details = details;
    }*/

    @Override
    protected boolean justTodo() {

        ActivityQueryDetailReq req = new ActivityQueryDetailReq();
        req.setActivityId(this.meetupId);
        req.setSequence(DatetimeUtil.currentTimestamp());

        try {

            ActivityQueryDetailResp resp = (ActivityQueryDetailResp) iClubApplication.send(req);
            if(resp==null) {
                //FileLogger.writeLogFileToSDCard("ERROR: ActivityQueryDetailResp is null!");
                return false;//fetch in failure!
            }

            String json = resp.getJson();
            Log.e("zyf", "activity details: "+json);

            JSONObject jo = new JSONObject(json);

            activityDetailsItem = ActivityDetailsItem.parseJsonToObj(jo);

        } catch (Exception e) {
            Log.e("zyf", "Activity Details exception: "+e.toString());
            return false;
        }

        return true;
    }

}
