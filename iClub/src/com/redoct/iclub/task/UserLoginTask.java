package com.redoct.iclub.task;

/**
 * Created by liwenzhi on 14-9-23.
 */

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.cmd.auth.LoginReq;
import com.oct.ga.comm.cmd.auth.LoginResp;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.config.AppConfig;
import com.redoct.iclub.config.NetworkConfig;
import com.redoct.iclub.config.ServiceConfig;
import com.redoct.iclub.param.AccountParams;
import com.redoct.iclub.util.DeviceUtil;
import com.redoct.iclub.util.EncryptUtil;

import java.io.UnsupportedEncodingException;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class UserLoginTask extends TemplateTask{

    private  String mEmail;
    private  String mPassword;
    private Activity ctx;

    public void setCtx(Activity ctx) {
        this.ctx = ctx;
    }

    public UserLoginTask(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    @Override
    protected void connectCheck(){
        if(ServiceConfig.gateway != null && iClubApplication.apiOk()) return;

    }

    @Override
    protected boolean justTodo() {
        if(ServiceConfig.gateway == null) return false;

        LoginReq req = new LoginReq();
        req.setApnsToken("android_no_apn_token_currently");
        req.setEmail(mEmail);
        req.setGateToken(ServiceConfig.gateway.getAppToken());
        req.setMyDeviceId(AppConfig.DEVICE_ID);
        req.setOsVersion(DeviceUtil.getDeviceOS());
        //FIXME, MD5 encrypt@2014/09/25
        mPassword = EncryptUtil.md5(mPassword);
        req.setPassword(mPassword);
        req.setSequence(DatetimeUtil.currentTimestamp());

        try {

            LoginResp resp = (LoginResp) iClubApplication.send(req);
            if(resp==null) 
            	return false;//log in failure!

            String accountId = resp.getAccountId();
            Log.e("zyf", "login account_id: " + accountId);

            String sessionId = resp.getSessionToken();
            Log.e("zyf", "login session id: "+sessionId);

            int state = resp.getRespState();
            Log.e("zyf", "login state value: "+state);

            // invalid account
            if (state != NetworkConfig.RESPONSE_OK) 
            	return false;

            AccountParams account = new AccountParams();
            account.setAccountId(accountId);
            account.setSessionId(sessionId);

            //save the newly logged on user
            AppConfig.account = account;
            //HomeApp.setGuestMode(false);


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
