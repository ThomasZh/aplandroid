package com.redoct.iclub.task;

import android.util.Log;

import com.oct.ga.comm.cmd.account.SyncMyAccountReq;
import com.oct.ga.comm.cmd.account.SyncMyAccountResp;
import com.oct.ga.comm.domain.account.AccountDetailInfo;
import com.redoct.iclub.iClubApplication;

import java.io.UnsupportedEncodingException;

/**
 * Created by liwenzhi on 14-10-27.
 */
public class GetAccountTask extends TemplateTask {


    private AccountDetailInfo account;

    public GetAccountTask(){

    }

    public AccountDetailInfo getAccount() {
        return account;
    }

    @Override
    protected boolean justTodo() {

        try {
            SyncMyAccountResp resp = (SyncMyAccountResp)iClubApplication.send(new SyncMyAccountReq(0));

            if(resp==null) {
                Log.e("sima", "account response is null!");
                return false;//fetch in failure!
            }

            this.account = resp.getAccount();

        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("sima", "InterruptedException");
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("sima", "UnsupportedEncodingException");
            return false;
        }


        return true;
    }

}
