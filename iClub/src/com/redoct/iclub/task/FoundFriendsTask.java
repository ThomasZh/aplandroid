package com.redoct.iclub.task;

/**
 * Created by chengcai on 15-02-02.
 */


import com.oct.ga.comm.cmd.following.QueryAccountReq;
import com.oct.ga.comm.cmd.following.QueryAccountResp;
import com.oct.ga.comm.domain.account.AccountDetailInfo;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.config.NetworkConfig;
import com.redoct.iclub.config.ServiceConfig;

import java.io.UnsupportedEncodingException;

public class FoundFriendsTask extends TemplateTask {
	private String mail;
	public AccountDetailInfo account ;

	@Override
	protected void connectCheck() {
		if (ServiceConfig.gateway != null && iClubApplication.apiOk())
			return;

	}

	public FoundFriendsTask(String mail) {
		super();
		this.mail = mail;
	}

	@Override
	protected boolean justTodo() {
		if (ServiceConfig.gateway == null)
			return false;

		QueryAccountReq req = new QueryAccountReq(0,mail);

		try {

			QueryAccountResp resp = (QueryAccountResp) iClubApplication
					.send(req);
			if (resp == null)
				return false;// log in failure!

			int state = resp.getRespState();
			// invalid account
			if (state == NetworkConfig.RESPONSE_OK){
				 account = resp.getAccount();
			}
				

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
