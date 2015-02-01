package com.redoct.iclub.task;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import android.util.Log;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.cmd.RespCommand;
import com.oct.ga.comm.cmd.account.UploadAccountReq;
import com.oct.ga.comm.cmd.account.UploadAccountResp;
import com.oct.ga.comm.domain.account.AccountDetailInfo;
import com.redoct.iclub.iClubApplication;
//import com.oct.ga.comm.cmd.ftp.ApplyUploadFileReq;
//import com.oct.ga.comm.cmd.ftp.ApplyUploadFileResp;
import com.redoct.iclub.util.UploadUtil;
import com.redoct.iclub.util.UserInformationLocalManagerUtil;

/**
 * Created by liwenzhi on 14-10-27.
 */
public class UpdateAccountTask extends TemplateTask {
    
    private AccountDetailInfo account;
    private short state;

    private String avatarImgPath;

    public UpdateAccountTask(AccountDetailInfo act, String avatarImgPath){
        this.account = act;
        this.avatarImgPath = avatarImgPath;
    }

    public short getState() {
        return state;
    }

    @Override
    protected boolean justTodo() {
        try {
            //0. check file transfer id that upload needed
            String fileTransferId;
            boolean newUpload = false;
            
            if (avatarImgPath != null){//captured new image
                fileTransferId = account.getId()+"_"+(int)(Math.random()*1000);
                newUpload = true;
            } else if (account.getImageUrl().indexOf("http")>-1){//use already file id
                String avatarUrl = account.getImageUrl();
                String[] urlsplitted = avatarUrl.split("[/]");
                String fileName = urlsplitted[urlsplitted.length-1];
                fileTransferId = fileName.split("[.]")[0];//the file name not include suffix
            } else {
                fileTransferId = "";//no avatar assigned;
            }
            String path = Calendar.YEAR + "/" + (Calendar.MONTH +1) + "/" + Calendar.DAY_OF_MONTH + "/";
            String remoteFilePath = "/" + path + fileTransferId + ".jpg";
            String imageURL = "http://tripc2c-person-face.b0.upaiyun.com" + remoteFilePath;
            //1. upload file request
            if (newUpload) {
                Log.d("sima", "uploading avatar...");                
                boolean result = UploadUtil.upyUploadFaceImage(this.avatarImgPath,remoteFilePath);
                if (!result){
                    Log.e("sima", "upload to ali failue!");
                    return result;
                }
                Log.d("sima", "upload to ali success!");
            }

            //3. modify user account
            this.account.setImageUrl(imageURL);//use imageUrl to hold file transfer id
            UploadAccountReq reqAccount = new UploadAccountReq(this.account);
            reqAccount.setSequence(DatetimeUtil.currentTimestamp());
            RespCommand respModify = iClubApplication.send(reqAccount);
            if (respModify == null){
                Log.e("sima", "respModify is null!");
                return false;
            }
            if(respModify instanceof UploadAccountResp){
                state = respModify.getRespState();
                Log.e("sima", "UploadAccountResp state: "+String.valueOf(state));
                iClubApplication.act = account;  //传递act给accoutAcitivity，更新用户基本信息数据
                return true;
            }else {
                Log.e("task", "response is not UploadAccountResp: "+respModify.getClass());
                return false;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }

    }//end of function



}
