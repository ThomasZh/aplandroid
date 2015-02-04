package com.redoct.iclub.task;

/**
 * Created by chengcai on 15-02-02.
 */

import android.util.Log;

import com.oct.ga.comm.cmd.club.ClubQueryDetailReq;
import com.oct.ga.comm.cmd.club.ClubQueryDetailResp;
import com.oct.ga.comm.cmd.club.ClubQueryMyListReq;
import com.oct.ga.comm.cmd.club.ClubQueryMyListResp;
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

public class GetClubDetailTask extends TemplateTask {
	
	private String jsonClubDetail;
	public String getJsonClubDetail() {
		return jsonClubDetail;
	}
	public void setJsonClubDetail(String jsonClubDetail) {
		this.jsonClubDetail = jsonClubDetail;
	}




	private String clubId;
	
	public GetClubDetailTask(String clubId) {
		// TODO Auto-generated constructor stub
		this.clubId =clubId;
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

		ClubQueryDetailReq req = new ClubQueryDetailReq();
		req.setClubId(clubId);

		try {

			ClubQueryDetailResp resp = (ClubQueryDetailResp) iClubApplication.send(req);
			if (resp == null)
				return false;// log in failure!

			int state = resp.getRespState();
			// invalid account
			if (state != NetworkConfig.RESPONSE_OK)
				return false;

			jsonClubDetail = resp.getJson();
			

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
