package com.redoct.iclub.task;

import android.util.Log;

import com.oct.ga.comm.cmd.invite.InviteReq;
import com.oct.ga.comm.cmd.invite.InviteResp;
import com.redoct.iclub.iClubApplication;


public class InviteTask extends TemplateTask {
    private String id;
    private String mail;
    private String flag;

    public InviteTask(String id,String mail,String flag){
        this.id = id;
        this.mail =mail;
        this.flag=flag;
    }

    @Override
    protected boolean justTodo() {

    	InviteReq req = new InviteReq();
    	if("yes".equals(flag)){
    		req.setInviteType((short) 147);
    		req.setToUserSemiId(id);
    	}else{
    		req.setInviteType((short) 163);
    		req.setToUserSemiId(mail);
    	}
        try {

        	InviteResp resp = (InviteResp) iClubApplication.send(req);
            if(resp==null) {
                Log.e("zyf", "join activity resp is null......");
                return false;//fetch in failure!
            }
            /*short result = resp.getRespState();
            if (result != 100){
            	
            	Log.e("zyf", "1234567");
                return false;
            }*/

          //  Log.d("zyf", "response state: "+result);

        }catch (Exception e) {    
            Log.e("zyf", "join activity exception: "+e.toString());
            return false;
        }

        return true;
    }

}
 