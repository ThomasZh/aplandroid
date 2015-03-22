package com.redoct.iclub.item;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Created by liwenzhi on 14-10-14.
 */
public class ActivityDetailsItem implements Serializable{

    private String id;
    private String name;
    private String desc;
    private String clubId;
    private String clubName;
    private String clubTitleBkImage;
    private int startTime;
    private int endTime;
    private short state;
    private short memberRank;
    private String locDesc;
    private String locX;
    private String locY;
    private int memberNum;
    private String leaderName;
    private String leaderAvatarUrl;

   /* private JSONArray members;
    private JSONArray recommends;*/
    
    private int recommendNum;

    public static ActivityDetailsItem parseJsonToObj(JSONObject json) throws JSONException {
    	
        ActivityDetailsItem md = new ActivityDetailsItem();
        
        md.setClubId(json.getString("pid"));
        md.setClubName(json.getString("clubName"));
        md.setStartTime(json.getInt("startTime"));
        md.setEndTime(json.getInt("endTime"));
        md.setName(json.getString("name"));
        md.setClubTitleBkImage(json.getString("titleBkImage"));
        md.setDesc(json.getString("desc"));
        md.setLocDesc(json.getString("locDesc"));
        
        Log.e("zyf","activity id: "+json.getString("id"));
        
        md.setId(json.getString("id"));
        md.setLocX(json.getString("locX"));
        md.setLocY(json.getString("locY"));
        md.setMemberNum(json.getInt("memberNum"));
        md.setState((short) json.getInt("state"));

        /*md.setMembers(json.getJSONArray("members"));
        md.setRecommends(json.getJSONArray("recommends"));*/
        
        md.setRecommendNum(json.getJSONArray("recommends").length());
        
        md.setState(Short.parseShort(json.getString("state")));
        md.setMemberRank(Short.parseShort(json.getString("memberRank")));
        
        JSONArray memberJsonArray=json.getJSONArray("members");
        JSONObject memberJsonObject=memberJsonArray.getJSONObject(0);
        md.setLeaderName(memberJsonObject.getString("name"));
        md.setLeaderAvatarUrl(memberJsonObject.getString("imageUrl"));

        return md;
    }

    public int getRecommendNum() {
		return recommendNum;
	}

	public void setRecommendNum(int recommendNum) {
		this.recommendNum = recommendNum;
	}





	public String getLeaderName() {
		return leaderName;
	}



	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}



	public String getLeaderAvatarUrl() {
		return leaderAvatarUrl;
	}



	public void setLeaderAvatarUrl(String leaderAvatarUrl) {
		this.leaderAvatarUrl = leaderAvatarUrl;
	}



	public short getMemberRank() {
		return memberRank;
	}



	public void setMemberRank(short memberRank) {
		this.memberRank = memberRank;
	}



	/*public JSONArray getMembers() {
        return members;
    }

    public void setMembers(JSONArray members) {
        this.members = members;
    }

    public JSONArray getRecommends() {
        return recommends;
    }

    public void setRecommends(JSONArray recommends) {
        this.recommends = recommends;
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubTitleBkImage() {
        return clubTitleBkImage;
    }

    public void setClubTitleBkImage(String clubTitleBkImage) {
        this.clubTitleBkImage = clubTitleBkImage;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public String getLocDesc() {
        return locDesc;
    }

    public void setLocDesc(String locDesc) {
        this.locDesc = locDesc;
    }

    public String getLocX() {
        return locX;
    }

    public void setLocX(String locX) {
        this.locX = locX;
    }

    public String getLocY() {
        return locY;
    }

    public void setLocY(String locY) {
        this.locY = locY;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }
}
