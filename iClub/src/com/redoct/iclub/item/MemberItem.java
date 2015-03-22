package com.redoct.iclub.item;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by liwenzhi on 14-10-14.
 */
public class MemberItem implements Serializable{

    private String groupId;
    private String userId;
    private String userName;
    private String imageUrl;
    private int rank;
    private int state;

    public static MemberItem parseJsonToObj(JSONObject json) throws JSONException {
        MemberItem mo = new MemberItem();

        mo.setUserId(json.getString("id"));
        mo.setUserName(json.getString("name"));
        mo.setImageUrl(json.getString("imageUrl"));

        mo.setGroupId(json.optString("groupId"));
        mo.setRank(json.optInt("rank"));
        mo.setState(json.optInt("state"));

        return mo;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
