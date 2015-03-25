package com.redoct.iclub.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.redoct.iclub.BaseActivity;
import com.redoct.iclub.R;
import com.redoct.iclub.task.ChangePasswordTask;
import com.redoct.iclub.util.EncryptUtil;
import com.redoct.iclub.util.PersistentUtil;
import com.redoct.iclub.util.ToastUtil;
import com.redoct.iclub.widget.DeleteEditText;

public class UpdatePasswordActivity extends BaseActivity{
	private TextView tvCenterTitle;
	private DeleteEditText etOldPassword,etNewPassword,etSecondNewPassword;
	private String oldPassword,secondNewPassword,newPassword,mail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updatepassword);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		mail = "15656586483@163.com";
		tvCenterTitle = (TextView) findViewById(R.id.mTitleView);
		tvCenterTitle.setText("修改密码");
		etNewPassword = (DeleteEditText) findViewById(R.id.et_updatepassword_newpassword);
		etSecondNewPassword = (DeleteEditText) findViewById(R.id.et_updatepassword_second_newpassword);
		etOldPassword = (DeleteEditText) findViewById(R.id.et_updatepassword_oldpassword);
		findViewById(R.id.btn_updatapass_complete).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				oldPassword = etOldPassword.getText().toString().trim();
				newPassword = etNewPassword.getText().toString().trim();
				secondNewPassword = etSecondNewPassword.getText().toString().trim();
				if(TextUtils.isEmpty(oldPassword)){
					ToastUtil.toastshort(UpdatePasswordActivity.this,"请输入原密码");
					return;
				}
				if(TextUtils.isEmpty(newPassword)){
					ToastUtil.toastshort(UpdatePasswordActivity.this,"请输入新密码");
					return;
				}
				if(TextUtils.isEmpty(secondNewPassword)){
					ToastUtil.toastshort(UpdatePasswordActivity.this,"请输入新密码");
					return;
				}
				if(!newPassword.equals(secondNewPassword)){
					ToastUtil.toastshort(UpdatePasswordActivity.this,"新密码两次不一致");
					return;
				}
				final String  newpwd = EncryptUtil.md5(newPassword);
				String  oldpwd = EncryptUtil.md5(oldPassword);
				ChangePasswordTask update = new ChangePasswordTask(mail,
						oldpwd,newpwd) {
						public void callback() {
						    
							PersistentUtil.getInstance().write(UpdatePasswordActivity.this,"passWord",newpwd);
                            ToastUtil.toastshort(UpdatePasswordActivity.this, "密码修改成功！");
                            finish();
						}

						public void failure() {
							// showToast(R.string.login_failure);
							// popupLogin();//re login
			                ToastUtil.toastshort(UpdatePasswordActivity.this,"密码修改失败！");
			               
						}

						public void complete() {
							/*
							 * login = null; BusProvider.getInstance().post(new
							 * ProgressEvent(false));
							 */
						}

						public void before() {
							// BusProvider.getInstance().post(new ProgressEvent(true));
						}

						@Override
						public void timeout() {
							// TODO Auto-generated method stub
							super.timeout();

							Log.e("zyf", "time out.......");

							this.cancel(true);
						}

					};

					// login.setTimeOutEnabled(true, 100);
					update.safeExecute();
				
			}
		});
		
		Button leftBtn=(Button)findViewById(R.id.leftBtn);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setBackgroundResource(R.drawable.title_back);
		leftBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				finish();
			}
		});
	}

}
