package com.redoct.iclub.task;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.cmd.club.ActivityQuerySubscribersReq;
import com.oct.ga.comm.cmd.club.ActivityQuerySubscribersResp;
import com.oct.ga.comm.cmd.club.ClubQuerySubcribersReq;
import com.oct.ga.comm.cmd.club.ClubQuerySubcribersResp;
import com.oct.ga.comm.domain.account.AccountMasterInfo;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.item.ContactItem;

public class GetActivityInviteMembersTask extends TemplateTask {
	
	private String id;
	
	private ArrayList<ContactItem> contactItems;
	
	public ArrayList<ContactItem> getMembers(){
		
		return contactItems;
	}

	public GetActivityInviteMembersTask(String id) {
		super();
		this.id = id;
		
		contactItems=new ArrayList<ContactItem>();
	}

	@Override
	protected boolean justTodo() {
		
		ActivityQuerySubscribersReq req=new ActivityQuerySubscribersReq(DatetimeUtil.currentTimestamp(), id);
		
		ActivityQuerySubscribersResp resp;
		
		try {
			
			resp = (ActivityQuerySubscribersResp) iClubApplication.send(req);
			
			if (resp == null){
	            Log.e("zyf", "resp is null for ClubQuerySubcribersReq!");
	            return false;
	        }
			
			List<AccountMasterInfo> list=resp.getMemberList();
			if(list==null){
				Log.e("zyf", "GetActivityInviteMembersTask list is null......");
			}
			AccountMasterInfo accountMasterInfo;
			ContactItem item;
			for(int i=0;i<list.size();i++){
				
				accountMasterInfo=list.get(i);
				
				item=new ContactItem();
				item.setName(accountMasterInfo.getName());
				item.setId(accountMasterInfo.getId());
				item.setImageUrl(accountMasterInfo.getImageUrl());
				
				contactItems.add(item);
			}
			
		} catch (Exception e) {
			
			Log.e("zyf", "GetActivityInviteMembersTask exception: "+e.toString());
			
			return false;
		}
		
		return true;
	}

}
