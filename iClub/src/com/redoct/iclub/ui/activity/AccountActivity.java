package com.redoct.iclub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oct.ga.comm.domain.account.AccountDetailInfo;
import com.redoct.iclub.R;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.config.AppConfig;
import com.redoct.iclub.task.GetAccountTask;
import com.redoct.iclub.task.UpdateAccountTask;
import com.redoct.iclub.ui.activity.SelectPictureDialogAcitvity.ReturnBitmap;
import com.redoct.iclub.util.PersistentUtil;
import com.redoct.iclub.util.ToastUtil;
import com.redoct.iclub.util.UserInformationLocalManagerUtil;
import com.redoct.iclub.util.activity.BaseActivityUtil;
import com.redoct.iclub.widget.CircleBitmapDisplayer;

public class AccountActivity extends Activity implements ReturnBitmap {

	private GetAccountTask getTask;
	private UpdateAccountTask postTask;
	private DisplayImageOptions optionslocal, optionsnet; // 配置图片加载及显示选项
	private AccountDetailInfo act;
	private String avatarImgPath;

	private EditText et_desc, et_name, et_mail, et_phone;
	private Button btn_left, btn_right;
	private ImageView img_head;
	private static final int OPTION_PICIURE_DIALOG = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myinfromation);
		act = new AccountDetailInfo();
		initView();

	}

	private void initView() {
		findViewById(R.id.leftBtn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});
		optionslocal = new DisplayImageOptions.Builder()
		// .cacheInMemory(true)
		// .cacheOnDisk(true)
				.displayer(new CircleBitmapDisplayer()).build();

		optionsnet = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).showImageOnLoading(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.displayer(new CircleBitmapDisplayer()).build();

		img_head = (ImageView) findViewById(R.id.iheadimg);
		img_head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AccountActivity.this,
						SelectPictureDialogAcitvity.class);
				iClubApplication.setActivity(AccountActivity.this);
				startActivityForResult(intent, OPTION_PICIURE_DIALOG);
				BaseActivityUtil.setUpTransition(AccountActivity.this);
			}
		});
		if(!TextUtils.isEmpty(PersistentUtil.getInstance().readString(this, "imageurl", ""))){
			
			ImageLoader.getInstance().displayImage(PersistentUtil.getInstance().readString(this, "imageurl", ""),img_head, optionsnet);
		}
		
		btn_left = (Button) findViewById(R.id.leftBtn);
		btn_left.setVisibility(View.VISIBLE);
		btn_right = (Button) findViewById(R.id.rightBtn);
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setText(R.string.complete);
		btn_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				modifyAccountInfo();
			}
		});
		et_desc = (EditText) findViewById(R.id.idesc);
		et_mail = (EditText) findViewById(R.id.imail);
		et_name = (EditText) findViewById(R.id.iusername);
		et_phone = (EditText) findViewById(R.id.iphone);
		et_name.setText(PersistentUtil.getInstance().readString(this,
				"username", ""));
		et_mail.setText(PersistentUtil.getInstance().readString(this, "mail",
				""));
		et_phone.setText(PersistentUtil.getInstance().readString(this,
				"telephone", ""));
		et_desc.setText(PersistentUtil.getInstance().readString(this, "desc",
				""));

	}

	private void modifyAccountInfo() {
		// String mockAvatar =
		// "https://avatars1.githubusercontent.com/u/920778?v=2&s=460";

		final AccountDetailInfo act = new AccountDetailInfo();
		//act.setId(AppConfig.account.getAccountId());
		act.setName(et_name.getText().toString());
		act.setTelephone(et_phone.getText().toString());
		act.setDesc(et_desc.getText().toString());
		act.setImageUrl(PersistentUtil.getInstance().readString(this,
				"imageurl", ""));

		postTask = new UpdateAccountTask(act, avatarImgPath) {
			@Override
			public void callback() {
				//
				ToastUtil.toastshort(AccountActivity.this, "更新成功！");
				new UserInformationLocalManagerUtil(AccountActivity.this)
						.WriteUserInformation(iClubApplication.act);

			}

			@Override
			public void failure() {
				ToastUtil.toastshort(AccountActivity.this, "更新失败！");
			}

			@Override
			public void pullback() {

			}

			@Override
			public void complete() {
				postTask = null;
				// showHeaderProgress(false);
			}

			@Override
			public void before() {
				// showHeaderProgress(true);
			}
		};
		postTask.safeExecute();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void init() {
	}

	@Override
	public void returnMapandUrl(Bitmap bitmap, String path) {
		// TODO Auto-generated method stub
		avatarImgPath = path;
		// img_head.setImageBitmap(UploadUtil.toRoundBitmap(bitmap));
		ImageLoader.getInstance().displayImage("file:///" + path, img_head,
				optionslocal);
	}

}
