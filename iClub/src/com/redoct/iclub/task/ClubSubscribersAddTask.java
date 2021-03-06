package com.redoct.iclub.task;

/**
 * Created by chengcai on 15-02-02.
 */


import com.oct.ga.comm.cmd.club.ClubCreateResp;
import com.oct.ga.comm.cmd.club.ClubSubscribersAddReq;
import com.oct.ga.comm.cmd.club.ClubSubscribersAddResp;
import com.oct.ga.comm.cmd.club.ClubSubscribersRemoveReq;
import com.oct.ga.comm.cmd.club.ClubSubscribersRemoveResp;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.config.NetworkConfig;
import com.redoct.iclub.config.ServiceConfig;

import java.io.UnsupportedEncodingException;


public class ClubSubscribersAddTask extends TemplateTask {
   String id ;
   String id_string[];

	public ClubSubscribersAddTask(String id ,String id_string[]) {
		// TODO Auto-generated constructor stub
		this.id =id;
		this.id_string = id_string;
		
	}

	@Override
	protected void connectCheck() {
		if (ServiceConfig.gateway != null && iClubApplication.apiOk())
			return;

	}

	@Override
	protected boolean justTodo() {
		if (ServiceConfig.gateway == null)
			return false;

		

		
		ClubSubscribersAddReq req = new ClubSubscribersAddReq();
		req.setClubId(id);
		req.setUserIds(id_string);

		try {

			ClubSubscribersAddResp resp = (ClubSubscribersAddResp) iClubApplication.send(req);
			if (resp == null)
				return false;// log in failure!

			int state = resp.getRespState();
			// invalid account
			if (state != NetworkConfig.RESPONSE_OK)
				return false;

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
