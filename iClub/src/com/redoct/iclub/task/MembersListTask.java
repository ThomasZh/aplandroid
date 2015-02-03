package com.redoct.iclub.task;

import android.util.Log;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.cmd.group.QueryMemberListReq;
import com.oct.ga.comm.cmd.group.QueryMemberListResp;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.item.MemberItem;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by liwenzhi on 14-10-16.
 */
public class MembersListTask extends TemplateTask {

    private String meetupId;
    private ArrayList<MemberItem> members;

    public MembersListTask(String meetupId){
        this.meetupId = meetupId;
        members = new ArrayList<MemberItem>();
    }

    public ArrayList<MemberItem> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<MemberItem> members) {
        this.members = members;
    }

    @Override
    protected boolean justTodo() {

        QueryMemberListReq req = new QueryMemberListReq();
        req.setSequence(DatetimeUtil.currentTimestamp());
        req.setChannelType(GlobalArgs.CHANNEL_TYPE_ACTIVITY);
        req.setChannelId(this.meetupId);

        try {
            QueryMemberListResp resp = (QueryMemberListResp) iClubApplication.send(req);

            String json = resp.getJson();
            JSONArray jMembers = new JSONArray(json);
            for(int i=0; i<jMembers.length(); i++){
                members.add(MemberItem.parseJsonToObj(jMembers.getJSONObject(i)));
            }
            Log.d("sima", json);

        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }


        return true;
    }

}
