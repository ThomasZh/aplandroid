package com.redoct.iclub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.oct.ga.comm.domain.club.ClubMasterInfo;
import com.redoct.iclub.R;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.task.RegisterTask;
import com.redoct.iclub.task.UpdateClubTask;
import com.redoct.iclub.ui.activity.SelectPictureDialogAcitvity.ReturnBitmap;
import com.redoct.iclub.util.EncryptUtil;
import com.redoct.iclub.util.ToastUtil;
import com.redoct.iclub.util.activity.BaseActivityUtil;

public class RegisterActivity extends Activity implements ReturnBitmap {
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	private String avatarImgPath;
	private ImageView ivRegisterImg;
	private EditText etRegisterName, etRegisterMail, etRegisterPass;
	private RegisterTask task;

	private String osVersion; //
	private String gateToken; //
	private String deviceId;//
	private String firstName;//

	private String email;//
	private String md5pwd;//
	private String facePhoto;//
	private String apnsToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		mImageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)

				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(100)).build();
		initView();

		

	}

	private void Register() {
		firstName = etRegisterName.getText().toString().trim();
		email = etRegisterMail.getText().toString().trim();
		md5pwd = EncryptUtil.md5(etRegisterPass.getText().toString().trim());
		if (TextUtils.isEmpty(firstName)) {
			ToastUtil.toastshort(this, "请输入用户名");
			return;
		}
		if (TextUtils.isEmpty(firstName)) {
			ToastUtil.toastshort(this, "请输入用户名");
			return;
		}
		if (TextUtils.isEmpty(email)) {
			ToastUtil.toastshort(this, "请输入密码");
			return;
		}

		task = new RegisterTask(firstName, email, md5pwd, avatarImgPath) {

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

				ToastUtil.toastshort(RegisterActivity.this, "update succeed!");

			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();
				// ToastUtil.toastshort(ClubListActivity.this,"返回空置");
				// updateClub();
				Log.e("zyf", "get data failure....");
				ToastUtil.toastshort(RegisterActivity.this, "update falilure!");

			}

			@Override
			public void complete() {
				// TODO Auto-generated method stub
				super.complete();

				Log.e("zyf", "get data complete....");

				// mPullToRefreshListView.onRefreshComplete();
			}
		};
		task.setTimeOutEnabled(true, 10 * 1000);
		task.safeExecute();
	}

	private void initView() {
		etRegisterMail = (EditText) findViewById(R.id.et_register_mail);
		etRegisterName = (EditText) findViewById(R.id.et_register_name);
		etRegisterPass = (EditText) findViewById(R.id.et_register_pass);

		findViewById(R.id.leftBtn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});
		findViewById(R.id.btn_regis).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Register() ;
			}
		});
		ivRegisterImg = (ImageView) findViewById(R.id.iv_regisiter_img);

		findViewById(R.id.iv_regisiter_img).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(RegisterActivity.this,
								SelectPictureDialogAcitvity.class);
						iClubApplication.setActivity(RegisterActivity.this);
						startActivityForResult(intent, 1);
						BaseActivityUtil.setUpTransition(RegisterActivity.this);
					}
				});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	@Override
	public void returnMapandUrl(Bitmap bitmapk, String path) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		avatarImgPath = path;
		// img_head.setImageBitmap(UploadUtil.toRoundBitmap(bitmap));
		ImageLoader.getInstance().displayImage("file:///" + path,
				ivRegisterImg, options);
	}

}
