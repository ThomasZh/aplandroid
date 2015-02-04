package com.redoct.iclub.task;

/**
 * Created by chengcai on 15-02-02.
 */

import android.util.Log;

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

public class GetClubListTask extends TemplateTask {
	private String jsonClub;
	private List<ClubItem> clubList = new ArrayList<ClubItem>();
	
	@Override
	protected void connectCheck() {
		if (ServiceConfig.gateway != null && iClubApplication.apiOk())
			return;

	}

	public List<ClubItem> getClubList() {
		return clubList;
	}

	public void setClubList(List<ClubItem> clubList) {
		this.clubList = clubList;
	}

	@Override
	protected boolean justTodo() {
		if (ServiceConfig.gateway == null)
			return false;

		ClubQueryMyListReq req = new ClubQueryMyListReq();

		try {

			ClubQueryMyListResp resp = (ClubQueryMyListResp) iClubApplication.send(req);
			if (resp == null)
				return false;// log in failure!

			int state = resp.getRespState();
			// invalid account
			if (state != NetworkConfig.RESPONSE_OK)
				return false;

			jsonClub = resp.getJson();
			try {
				JSONArray array = new JSONArray(jsonClub);
				for (int i = 0; i < array.length(); i++) {
					JSONObject club = array.optJSONObject(i);
					ClubItem clubItem = new ClubItem();
					clubItem.setId(club.optString("id"));
					clubItem.setName(club.optString("name"));
					clubItem.setSubscriberNum(club.optString("subscriberNum"));
					clubItem.setTitleBkImage(club.optString("titleBkImage"));
					clubList.add(clubItem);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("cc", "club json  huo qu shi bai!");
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
