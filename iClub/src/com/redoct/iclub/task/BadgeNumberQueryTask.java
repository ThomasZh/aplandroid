package com.redoct.iclub.task;

import java.io.UnsupportedEncodingException;

import android.util.Log;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.cmd.badgenum.BadgeNumQueryReq;
import com.oct.ga.comm.cmd.badgenum.BadgeNumQueryResp;
import com.redoct.iclub.MainActivity;
import com.redoct.iclub.iClubApplication;

public class BadgeNumberQueryTask extends TemplateTask {

	@Override
	protected boolean justTodo() {
		
		BadgeNumQueryReq req=new BadgeNumQueryReq(DatetimeUtil.currentTimestamp());
		
		try {
			BadgeNumQueryResp resp=(BadgeNumQueryResp) iClubApplication.send(req);
			
			if(resp==null){
				
				Log.e("app", "Badge Number task resp is null......");
				
				return false;
			}
			
			//iClubApplication.badgeNumber=resp.getBadgeNum().getInviteNum()+resp.getBadgeNum().getMessageNum()+resp.getBadgeNum().getTaskLogNum();
			
			iClubApplication.badgeNumber=resp.getBadgeNum().getMessageNum();//
			
			Log.e("app", "badge number: "+iClubApplication.badgeNumber);
			
			//MainActivity.handleUnReadMessage(iClubApplication.badgeNumber);
			
		}catch (Exception e) {
			
			Log.e("app", "Badge Number task exception: "+e.toString());
			
			return false;
		}
		
		return true;
	}
}
