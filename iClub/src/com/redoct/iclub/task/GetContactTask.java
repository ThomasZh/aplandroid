package com.redoct.iclub.task;

import android.util.Log;

import com.oct.ga.comm.cmd.following.SyncFollowingReq;
import com.oct.ga.comm.cmd.following.SyncFollowingResp;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.item.ContactItem;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liwenzhi on 14-10-27.
 */
public class GetContactTask extends TemplateTask {


    private String  json;
   public  List<ContactItem> listContact = new ArrayList<ContactItem>() ;
    

    public GetContactTask(){

    }

   
    @Override
    protected boolean justTodo() {

        try {
        	SyncFollowingReq req = new SyncFollowingReq();
        	req.setLastTryTime(0);
        	SyncFollowingResp resp = (SyncFollowingResp)iClubApplication.send(req);

            if(resp==null) {
                Log.e("sima", "account response is null!");
                return false;//fetch in failure!
            }

            json =resp.getJson();
            try {
				JSONArray array = new JSONArray(json);
				for(int i = 0;i<array.length();i++){
					JSONObject obj = array.getJSONObject(i);
					ContactItem contact = new ContactItem();
					contact.setDesc(obj.optString("desc"));
					contact.setEmail(obj.optString("email"));
					contact.setId(obj.optString("id"));
					contact.setImageUrl(obj.optString("imageUrl"));
					contact.setLoginName(obj.optString("loginName"));
					contact.setName(obj.optString("name"));
					contact.setState(obj.optInt("state"));
					contact.setTelephone(obj.optString("desc"));
					listContact.add(contact);
				}
			} catch (JSONException e) {
               Log.e("json","contact json  jiexi  fail!!!!");
				e.printStackTrace();
			}
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
