package com.redoct.iclub.task;

/**
 * Created by chengcai on 15-02-02.
 */

import android.util.Log;

import com.oct.ga.comm.ErrorCode;
import com.oct.ga.comm.cmd.auth.RegisterLoginReq;
import com.oct.ga.comm.cmd.auth.RegisterLoginResp;
import com.oct.ga.comm.domain.club.ClubMasterInfo;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.config.AppConfig;
import com.redoct.iclub.config.NetworkConfig;
import com.redoct.iclub.config.ServiceConfig;
import com.redoct.iclub.util.UploadUtil;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

public class RegisterTask extends TemplateTask {

	private ClubMasterInfo clubInfo;
	private String avatarImgPath;
	private String name, mail, pass;

	public RegisterTask(String name, String mail, String pass,
			String avatarImgPath) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.mail = mail;
		this.pass = pass;
		this.avatarImgPath = avatarImgPath;
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
			fileTransferId = ""+(int) (Math.random() * 1000);
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

			// 3. modify user account
			// id
            
			RegisterLoginReq req = new RegisterLoginReq();
			req.setMd5pwd(pass);
			req.setEmail(mail);
			req.setDeviceId(AppConfig.DEVICE_ID);
			req.setFacePhoto(imageURL);
			req.setGateToken(ServiceConfig.gateway.getAppToken());
			req.setOsVersion(AppConfig.APP_VERSION);
			req.setFirstName(name);
			req.setApnsToken("");

			try {

				RegisterLoginResp resp = (RegisterLoginResp) iClubApplication
						.send(req);
				if (resp == null)
					return false;// log in failure!

				int state = resp.getRespState();
				// invalid account
				if (state != NetworkConfig.RESPONSE_OK)
					return false;
				if (state == ErrorCode.REGISTER_EMAIL_EXIST)
					return false;
				

			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return false;
			}


		}
		return true;
	

	}
}
