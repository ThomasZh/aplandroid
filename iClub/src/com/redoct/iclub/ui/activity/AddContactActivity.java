
package com.redoct.iclub.ui.activity;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.oct.ga.comm.domain.account.AccountDetailInfo;
import com.redoct.iclub.R;
import com.redoct.iclub.item.ContactItem;
import com.redoct.iclub.task.FoundFriendsTask;
import com.redoct.iclub.util.ToastUtil;
import com.redoct.iclub.util.activity.BaseActivityUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

public class AddContactActivity extends Activity{
	ImageView ivLeftBtn ;
	private FoundFriendsTask task;
	private EditText etMail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcontact);
		initView();
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.push_right_in,
				R.anim.push_right_out);
	}
	private void  initView(){
		etMail = (EditText) findViewById(R.id.et_addcontact_mail);
		ivLeftBtn = (ImageView) findViewById(R.id.leftBtn);
		ivLeftBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});
		findViewById(R.id.btn_addcontact_find).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(etMail.getText().toString())){
				    ToastUtil.toastshort(AddContactActivity.this,"请输入邮箱！");
				    return;
				}
				Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
				Matcher matcher = pattern.matcher(etMail.getText().toString());
				if(!matcher.matches()){
					 ToastUtil.toastshort(AddContactActivity.this,"请输入正确邮箱格式！");
					 return;
				}
				FindFriends(etMail.getText().toString().trim());
			}
		});
		
		
	}
private void FindFriends(final String mail){
		
    	task=new FoundFriendsTask(mail){
			
			@Override
			public void timeout() {
				// TODO Auto-generated method stub
				super.timeout();
				
				task.cancel(true);
				Log.e("zyf", "get data time out....");
			}

			@Override
			public void before() {
				// TODO Auto-generated method stub
				super.before();
				
				Log.e("zyf", "start get data....");
			}

			@Override
			public void callback() {
				// TODO Auto-generated method stub
				super.callback();
				Log.e("zyf", "hahahhahahahhahah");
				AccountDetailInfo account = task.account;
				if(account==null){
					Intent intent = new Intent();
					intent.setClass(AddContactActivity.this,ContactQueryResultActivity.class);
					intent.putExtra("flag","no");
					intent.putExtra("email", mail);
					BaseActivityUtil.startActivity(AddContactActivity.this, intent, false, false);
				}else{
					Intent intent = new Intent();
					intent.setClass(AddContactActivity.this,ContactQueryResultActivity.class);
					intent.putExtra("name",account.getName());
					intent.putExtra("flag","yes");
					intent.putExtra("email",account.getEmail());
				    intent.putExtra("img", account.getImageUrl());
				    intent.putExtra("id", account.getId());
					BaseActivityUtil.startActivity(AddContactActivity.this, intent, false, false);
				}
				
				
			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();
				
				Log.e("zyf", "get data failure....");
				
			
				
			}

			@Override
			public void complete() {
				// TODO Auto-generated method stub
				super.complete();
				
			}
		};
		task.setTimeOutEnabled(true, 10*1000);
		task.safeExecute();
	}

	

}
