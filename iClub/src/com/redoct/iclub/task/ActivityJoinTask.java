package com.redoct.iclub.task;

import android.util.Log;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.cmd.club.ActivityJoinReq;
import com.oct.ga.comm.cmd.club.ActivityJoinResp;
import com.redoct.iclub.iClubApplication;

import java.io.UnsupportedEncodingException;

public class ActivityJoinTask extends TemplateTask {

    private String id;

    public ActivityJoinTask(String id){
        this.id = id;
    }

    @Override
    protected boolean justTodo() {

        ActivityJoinReq req = new ActivityJoinReq();
        req.setActivityId(this.id);
        req.setSequence(DatetimeUtil.currentTimestamp());

        try {

            ActivityJoinResp resp = (ActivityJoinResp) iClubApplication.send(req);
            if(resp==null) {
                Log.e("zyf", "join activity resp is null......");
                return false;//fetch in failure!
            }
            short result = resp.getRespState();
            if (result != 100){
            	
            	Log.e("zyf", "1234567");
                return false;
            }

            Log.d("zyf", "response state: "+result);

        }catch (Exception e) {    
            Log.e("zyf", "join activity exception: "+e.toString());
            return false;
        }

        return true;
    }

}
