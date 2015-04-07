package com.redoct.iclub.task;

import android.util.Log;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.cmd.club.ActivityUpdateReq;
import com.oct.ga.comm.cmd.club.ActivityUpdateResp;
import com.oct.ga.comm.domain.club.ActivityDetailInfo;
import com.oct.ga.comm.domain.club.ActivityUpdateInfo;
import com.redoct.iclub.iClubApplication;

public class ActivityUpdateTask extends TemplateTask {
	
	private ActivityUpdateInfo info;

    public ActivityUpdateTask(ActivityUpdateInfo info){
        this.info = info;
    }

    @Override
    protected boolean justTodo() {
    	
    	ActivityUpdateReq req=new ActivityUpdateReq(DatetimeUtil.currentTimestamp(),info);
        
        try {
            ActivityUpdateResp resp = (ActivityUpdateResp) iClubApplication.send(req);
            
            if (resp == null){
                Log.e("zyf", "activity update resp is null......");
                return false;
            }

            if (resp.getRespState() != ErrorCode.SUCCESS){
                Log.e("zyf", "activity update error,state: "+resp.getRespState());
                return false;
            }

        } catch (Exception e) {
            Log.e("zyf","activity update exception： "+e.toString());
            return false;
        }
        
        return true;
    }
}
