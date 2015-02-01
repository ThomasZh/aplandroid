package com.redoct.iclub.util;

import com.oct.ga.comm.domain.account.AccountDetailInfo;

import android.content.Context;

public class UserInformationLocalManagerUtil {
	private Context context;
	public UserInformationLocalManagerUtil(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	public  void  WriteUserInformation(AccountDetailInfo act){
		  PersistentUtil.getInstance().write(context,"username", act.getName());
          PersistentUtil.getInstance().write(context,"mail", act.getEmail());
          PersistentUtil.getInstance().write(context,"telephone", act.getTelephone());
          PersistentUtil.getInstance().write(context,"imageurl", act.getImageUrl());
          PersistentUtil.getInstance().write(context,"desc", act.getDesc());
       
         
	}
	public  AccountDetailInfo ReadUserInformation(){
		
		AccountDetailInfo  act = new AccountDetailInfo();
		 act.setName(PersistentUtil.getInstance().readString(context,
				"username", ""));
		act.setEmail(PersistentUtil.getInstance().readString(context, "mail",
				""));
		act.setTelephone(PersistentUtil.getInstance().readString(context,
				"telephone", ""));
		act.setDesc(PersistentUtil.getInstance().readString(context, "desc",
				""));
		act.setImageUrl(PersistentUtil.getInstance().readString(context, "imageurl",
				""));
		
		return act;
		
	} 
	
}
