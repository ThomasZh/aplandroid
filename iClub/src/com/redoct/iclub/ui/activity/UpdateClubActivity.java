package com.redoct.iclub.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oct.ga.comm.domain.club.ClubMasterInfo;
import com.redoct.iclub.BaseActivity;
import com.redoct.iclub.R;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.task.UpdateClubTask;
import com.redoct.iclub.ui.activity.SelectPictureDialogAcitvity.ReturnBitmap;
import com.redoct.iclub.util.ToastUtil;
import com.redoct.iclub.util.activity.BaseActivityUtil;

public class UpdateClubActivity extends BaseActivity implements ReturnBitmap {
	private EditText et_clubname;
	private EditText et_clubdesc;
	private ImageView iv_clubimg;
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	private String clubName, clubDesc, clubImg, clubId;
	private UpdateClubTask task;
	private ClubMasterInfo info;
	private String avatarImgPath;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updateclub);

		mImageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)

				.bitmapConfig(Bitmap.Config.RGB_565)
				//.displayer(new RoundedBitmapDisplayer(100))
				.build();
		initView();
	}

	private void updateClub() {
		info = new ClubMasterInfo();
		info.setTitleBkImage(clubImg);
		info.setName(et_clubname.getText().toString());
		info.setDesc(et_clubdesc.getText().toString());
		info.setId(clubId);
		task = new UpdateClubTask(info, avatarImgPath) {

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

				ToastUtil
						.toastshort(UpdateClubActivity.this, "update succeed!");

			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();
				// ToastUtil.toastshort(ClubListActivity.this,"返回空置");
				// updateClub();
				Log.e("zyf", "get data failure....");
				ToastUtil.toastshort(UpdateClubActivity.this,
						"update falilure!");

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

	public void initView() {
		findViewById(R.id.leftBtn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});

		TextView btnRigthButton = (TextView) findViewById(R.id.rightBtn);

		btnRigthButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stubsss
				updateClub();

			}
		});

		et_clubdesc = (EditText) findViewById(R.id.et_updateclub_clubdesc);
		et_clubdesc.setText(getIntent().getStringExtra("desc"));
		et_clubname = (EditText) findViewById(R.id.et_updateclub_clubname);
		et_clubname.setText(getIntent().getStringExtra("name"));
		iv_clubimg = (ImageView) findViewById(R.id.iv_update_clubimg);
		iv_clubimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(UpdateClubActivity.this,
						SelectPictureDialogAcitvity.class);
				iClubApplication.setActivity(UpdateClubActivity.this);
				startActivityForResult(intent, 1);
				BaseActivityUtil.setUpTransition(UpdateClubActivity.this);
			}
		});
		clubImg = getIntent().getStringExtra("img");
		clubId = getIntent().getStringExtra("id");
		mImageLoader.displayImage(getIntent().getStringExtra("img"),
				iv_clubimg, options);
	}

	@Override
	public void returnMapandUrl(Bitmap bitmapk, String path) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		avatarImgPath = path;
		// img_head.setImageBitmap(UploadUtil.toRoundBitmap(bitmap));
		ImageLoader.getInstance().displayImage("file:///" + path, iv_clubimg,
				options);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

}
