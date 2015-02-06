package com.redoct.iclub.task;

import android.util.Log;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.cmd.club.ActivityCreateReq;
import com.oct.ga.comm.cmd.club.ActivityCreateResp;
import com.oct.ga.comm.domain.club.ActivityDetailInfo;
import com.redoct.iclub.iClubApplication;

import java.io.UnsupportedEncodingException;

/**
 * Created by liwenzhi on 14-11-4.
 */
public class ActivityCreateTask extends TemplateTask {


    private String meetupJson;

    public ActivityCreateTask(String meetup){
        this.meetupJson = meetup;
    }

    @Override
    protected boolean justTodo() {

        ActivityCreateReq req = new ActivityCreateReq(meetupJson);
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
