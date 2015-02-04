package com.redoct.iclub.task;

/**
 * Created by chengcai on 15-02-02.
 */

import com.oct.ga.comm.cmd.account.ChangePasswordReq;
import com.oct.ga.comm.cmd.account.ChangePasswordResp;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.config.NetworkConfig;
import com.redoct.iclub.config.ServiceConfig;

import java.io.UnsupportedEncodingException;

/**
 * Represents an asynchronous login/registration task used to authenticate the
 * user.
 */
public class ChangePasswordTask extends TemplateTask {

	private String mEmail;
	private String newPassword, oldPassword;

	public ChangePasswordTask(String email, String oldPassword,
			String newPassword) {
		 mEmail = email;
		this.newPassword = newPassword;
		this.oldPassword = oldPassword;
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

		ChangePasswordReq req = new ChangePasswordReq("912627598@qq.com",oldPassword,newPassword);
		

		try {

			ChangePasswordResp resp = (ChangePasswordResp) iClubApplication.send(req);
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
