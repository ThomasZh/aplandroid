package com.redoct.iclub.task;

/**
 * Created by chengcai on 15-02-02.
 */

import android.util.Log;

import com.google.gson.Gson;
import com.oct.ga.comm.cmd.club.ClubCreateReq;
import com.oct.ga.comm.cmd.club.ClubCreateResp;
import com.oct.ga.comm.cmd.club.ClubUpdateReq;
import com.oct.ga.comm.cmd.club.ClubUpdateResp;
import com.oct.ga.comm.domain.club.ClubMasterInfo;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.config.NetworkConfig;
import com.redoct.iclub.config.ServiceConfig;
import com.redoct.iclub.item.ActivityItem;
import com.redoct.iclub.util.UploadUtil;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import org.json.JSONObject;

public class CreateClubTask extends TemplateTask {

	private ClubMasterInfo clubInfo;
	private String avatarImgPath;

	public CreateClubTask(ClubMasterInfo clubInfo, String avatarImgPath) {
		// TODO Auto-generated constructor stub
		this.clubInfo = clubInfo;
		this.avatarImgPath= avatarImgPath;
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

		String fileTransferId = null;
		boolean newUpload = false;

		if (avatarImgPath != null) {// captured new image
			fileTransferId = clubInfo.getId() + "_"
					+ (int) (Math.random() * 1000);
			newUpload = true;
		}
		if (newUpload) {
			Log.d("sima", "uploading avatar...");
			String path = Calendar.YEAR + "/" + (Calendar.MONTH + 1) + "/"
					+ Calendar.DAY_OF_MONTH + "/";
			String remoteFilePath = "/" + path + fileTransferId + ".jpg";
			String imageURL = "http://tripc2c-person-face.b0.upaiyun.com"
					+ remoteFilePath;
			boolean result = UploadUtil.upyUploadFaceImage(this.avatarImgPath,
					remoteFilePath);
			if (!result) {
				Log.e("sima", "upload to ali failue!");
				return result;
			}
			clubInfo.setTitleBkImage(imageURL);// use imageUrl to hold file
												// transfer
			Log.d("sima", "upload to ali success!");
		}

		// 3. modify user account
		// id

		
		ClubCreateReq req = new ClubCreateReq(clubInfo);

		try {

			ClubCreateResp resp = (ClubCreateResp) iClubApplication.send(req);
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
