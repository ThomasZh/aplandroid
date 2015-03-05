package com.redoct.iclub.task;

import android.util.Log;

import com.oct.ga.comm.cmd.account.SyncMyAccountReq;
import com.oct.ga.comm.cmd.account.SyncMyAccountResp;
import com.oct.ga.comm.cmd.club.ClubQueryDetailReq;
import com.oct.ga.comm.cmd.club.ClubQueryDetailResp;
import com.oct.ga.comm.cmd.club.ClubQuerySubcribersReq;
import com.oct.ga.comm.cmd.club.ClubQuerySubcribersResp;
import com.oct.ga.comm.domain.account.AccountDetailInfo;
import com.oct.ga.comm.domain.account.AccountMasterInfo;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.config.NetworkConfig;
import com.redoct.iclub.config.ServiceConfig;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by liwenzhi on 14-10-27.
 */
public class ClubQuerySubcribersTask extends TemplateTask {


    private String id;
    public  List<AccountMasterInfo> list ;

    public ClubQuerySubcribersTask(String id ){
    	this.id  = id;

    }
   
   
    public List<AccountMasterInfo> getList() {
		return list;
	}


	public void setList(List<AccountMasterInfo> list) {
		this.list = list;
	}


	@Override
    protected boolean justTodo() {

    	if (ServiceConfig.gateway == null)
			return false;

    	ClubQuerySubcribersReq req = new ClubQuerySubcribersReq();
		req.setClubId(id);

		try {

			ClubQuerySubcribersResp resp = (ClubQuerySubcribersResp) iClubApplication.send(req);
			if (resp == null)
				return false;// log in failure!

			int state = resp.getRespState();
			// invalid account
			if (state != NetworkConfig.RESPONSE_OK)
				return false;
            list = resp.getMemberList();
		    
			

		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}

		return true;
    }

}
