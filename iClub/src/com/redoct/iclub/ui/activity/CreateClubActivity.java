package com.redoct.iclub.ui.activity;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oct.ga.comm.domain.club.ClubMasterInfo;
import com.redoct.iclub.R;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.item.ContactItem;
import com.redoct.iclub.task.CreateClubTask;
import com.redoct.iclub.task.UpdateClubTask;
import com.redoct.iclub.ui.activity.SelectPictureDialogAcitvity.ReturnBitmap;
import com.redoct.iclub.util.ToastUtil;
import com.redoct.iclub.util.activity.BaseActivityUtil;

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
import android.widget.TextView;

public class CreateClubActivity extends Activity implements ReturnBitmap{
	private ClubMasterInfo  info;
	private EditText etClubName;
	private EditText etClubDesc;
	private ImageView ivClubImg;
	private TextView etClubNum;
	private String avatarImgPath;
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	private CreateClubTask task;
	private String [] id;
	private ArrayList< String> list = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createclub);
		mImageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.icon_club_temp)
				.showImageForEmptyUri(R.drawable.icon_club_temp)

				.bitmapConfig(Bitmap.Config.RGB_565)
				//.displayer(new RoundedBitmapDisplayer(100))
				.build();
		initView();
		
	
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	public void initView() {
		etClubNum = (TextView) findViewById(R.id.tv_clubdetailactivity_peonum);
		ivClubImg = (ImageView) findViewById(R.id.iv_create_clubimg);
		ivClubImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CreateClubActivity.this,
						SelectPictureDialogAcitvity.class);
				iClubApplication.setActivity(CreateClubActivity.this);
				startActivityForResult(intent, 1);
				BaseActivityUtil.setUpTransition(CreateClubActivity.this);
			}
		});
		
		etClubDesc = (EditText) findViewById(R.id.et_createclub_clubdesc);
		etClubName = (EditText) findViewById(R.id.et_createclub_clubname);
		findViewById(R.id.rl_createclub_num).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(CreateClubActivity.this,AddMenberActivity.class);
				startActivityForResult(intent,1);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
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
				createClub();

			}
		});
		
	}
	private void createClub() {
		if(TextUtils.isEmpty(etClubDesc.getText().toString().trim())){
			ToastUtil.toastshort(this,"请输入圈子名称");
			return;
			
		}
		if(TextUtils.isEmpty(etClubName.getText().toString().trim())){
			ToastUtil.toastshort(this,"请输入圈子描述");
			return;
			
		}
		info = new ClubMasterInfo();
		//info.setTitleBkImage(clubImg);
		//info.setSharingUserIds(sharingUserIds)
		info.setName(etClubName.getText().toString().trim());
		info.setDesc(etClubDesc.getText().toString().trim());
		info.setSharingUserIds(id);
	
		task = new CreateClubTask(info, avatarImgPath) {

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

				ToastUtil.toastshort(CreateClubActivity.this, "create succeed!");

			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();
				// ToastUtil.toastshort(ClubListActivity.this,"返回空置");
				// updateClub();
				Log.e("zyf", "get data failure....");
				ToastUtil.toastshort(CreateClubActivity.this,
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

	@Override
	public void returnMapandUrl(Bitmap bitmapk, String path) {
		// TODO Auto-generated method stub
		avatarImgPath = path;
		// img_head.setImageBitmap(UploadUtil.toRoundBitmap(bitmap));
		ImageLoader.getInstance().displayImage("file:///" + path, ivClubImg,
				options);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
			ArrayList<ContactItem> listId =(ArrayList<ContactItem>) data.getSerializableExtra("idList");
            for(int i = 0;i<listId.size();i++){
            	ContactItem item = listId.get(i);
            	list.add(item.getId());
            }
			id = new String[list.size()];
			id =list.toArray(id);
			etClubNum.setText("("+id.length+")");
			
		}
	}
}
