package com.redoct.iclub.item;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liwenzhi on 14-10-14.
 */
public class MemberItem implements Parcelable{

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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(userName);
		dest.writeString(imageUrl);
		dest.writeString(userId);
		
	}
	public static final Parcelable.Creator<MemberItem> CREATOR = new Parcelable.Creator<MemberItem>() 
		     {
		         public MemberItem createFromParcel(Parcel in) 
		         {
		             return new MemberItem();
		         }

		         public MemberItem[] newArray(int size) 
		         {
		             return new MemberItem[size];
		         }
		     };
		     
}
