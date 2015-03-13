package com.redoct.iclub.task;

/**
 * Created by chengcai on 15-02-02.
 */

import android.util.Log;

import com.oct.ga.comm.cmd.club.ClubQueryDetailReq;
import com.oct.ga.comm.cmd.club.ClubQueryDetailResp;
import com.oct.ga.comm.cmd.club.ClubQueryMyListReq;
import com.oct.ga.comm.cmd.club.ClubQueryMyListResp;
import com.oct.ga.comm.cmd.invite.InviteReq;
import com.oct.ga.comm.cmd.invite.InviteResp;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.config.NetworkConfig;
import com.redoct.iclub.config.ServiceConfig;
import com.redoct.iclub.item.ClubItem;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Invite2Task extends TemplateTask {
	private String id;
	public String key;
	public short inviteType;
	public short ChannelType;

	public Invite2Task(String id,short inviteType,short ChannelType) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.inviteType=inviteType;
		this.ChannelType = ChannelType;
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

		InviteReq req = new InviteReq();
		req.setInviteType((short) inviteType);
		req.setToUserSemiId("");
		req.setChannelType((short) ChannelType);
		req.setChannelId(id);

		try {

			InviteResp resp = (InviteResp) iClubApplication.send(req);
			if (resp == null)
				return false;// log in failure!

			int state = resp.getRespState();
			// invalid account
			if (state != NetworkConfig.RESPONSE_OK)
				return false;
			key = resp.getInviteId();

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
