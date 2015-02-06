package com.redoct.iclub.task;

import android.util.Log;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.cmd.club.ActivityCancelReq;
import com.oct.ga.comm.cmd.club.ActivityCancelResp;
import com.oct.ga.comm.cmd.club.ActivityCreateReq;
import com.oct.ga.comm.cmd.club.ActivityCreateResp;
import com.redoct.iclub.iClubApplication;

public class ActivityCancelTask extends TemplateTask {
	
	private String id;

    public ActivityCancelTask(String id){
        this.id = id;
    }

    @Override
    protected boolean justTodo() {

    	ActivityCancelReq req=new ActivityCancelReq(DatetimeUtil.currentTimestamp(), id);
        
        try {
            ActivityCancelResp resp = (ActivityCancelResp) iClubApplication.send(req);
            
            if (resp == null){
                Log.e("zyf", "activity cancel resp is null......");
                return false;
            }

            if (resp.getRespState() != ErrorCode.SUCCESS){
                Log.e("zyf", "activity cancel error,state: "+resp.getRespState());
                return false;
            }

        } catch (Exception e) {
            Log.e("zyf","activity cancel exception： "+e.toString());
            return false;
        }
        
        return true;
    }
}
