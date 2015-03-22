package com.redoct.iclub.ui.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.oct.ga.comm.GlobalArgs;
import com.oct.ga.comm.domain.account.AccountDetailInfo;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.ActivityDetailsGalleryAdapter;
import com.redoct.iclub.adapter.MomentAdapter;
import com.redoct.iclub.config.AppConfig;
import com.redoct.iclub.item.ActivityDetailsItem;
import com.redoct.iclub.item.MemberItem;
import com.redoct.iclub.task.ActivityCancelTask;
import com.redoct.iclub.task.ActivityDetailsTask;
import com.redoct.iclub.task.ActivityJoinTask;
import com.redoct.iclub.task.GetContactTask;
import com.redoct.iclub.task.GetMomentTask;
import com.redoct.iclub.task.Invite2Task;
import com.redoct.iclub.task.MembersListTask;
import com.redoct.iclub.util.Constant;
import com.redoct.iclub.util.DateUtils;
import com.redoct.iclub.util.DeviceUtil;
import com.redoct.iclub.util.MyProgressDialogUtils;
import com.redoct.iclub.util.ToastUtil;
import com.redoct.iclub.util.UserInformationLocalManagerUtil;
import com.redoct.iclub.widget.MyGridView;
import com.redoct.iclub.widget.MyToast;

public class ActivityDetailsActivity extends Activity implements
		OnClickListener {

	private ActivityDetailsTask mMeetupDetailsTask;

	private ActivityJoinTask mActivityJoinTask;

	private ActivityCancelTask mActivityCancelTask;

	private ActivityDetailsItem mActivityDetailsItem = new ActivityDetailsItem();

	private String id;

	private String leaderName;

	private String leaderAvatarUrl;

	private ActivityDetailsGalleryAdapter mGalleryAdapter;

	private ImageLoader mImageLoader;
	private DisplayImageOptions options;

	private ImageView mLeaderAvatarView;

	private LinearLayout mTotalContainer;

	private TextView mTitleView;
	private TextView mLeaderNameTv, mNameTv, mDescTv, mLocDescTv, mStartDateTv,
			mStartTimeTv, mEndDateTv, mEndTimeTv, mRecommandNumTv;

	private RelativeLayout mRecommandContainer, mLocationContainer;
	private RelativeLayout mOptionContainer;

	private Button mCancelBtn, mJoinBtn, mSharedBtn,mConsultBtn;

	private LinearLayout mMemberListContainer, mMomentContainer;

	private boolean isJoined;

	private MyProgressDialogUtils mProgressDialogUtils;

	private MyGridView mMomentGridView;

	short pageNum = 1;
	short pageSize = 10;

	private PullToRefreshScrollView mPullToRefreshScrollView;
	private GetMomentTask mGetMomentTask;
	private ArrayList<String> momentList = new ArrayList<String>();
	private MomentAdapter mMomentAdapter;

	private Invite2Task task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_activity_details);

		id = getIntent().getStringExtra("id");
		leaderName = getIntent().getStringExtra("leaderName");
		leaderAvatarUrl = getIntent().getStringExtra("leaderAvatarUrl");

		mImageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.me_normal)
				.showImageForEmptyUri(R.drawable.me_normal).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(100)).build();

		initTitle();

		initViews();

		Log.e("zyf", "activity id: " + id);

		mMeetupDetailsTask = new ActivityDetailsTask(id) {

			@Override
			public void callback() {
				// TODO Auto-generated method stub
				super.callback();

				mActivityDetailsItem = mMeetupDetailsTask.getDetails();

				updateUI();

				loadMoment();

				// MyToast.makeText(ActivityDetailsActivity.this, true,
				// R.string.load_success, MyToast.LENGTH_SHORT).show();

			}

			@Override
			public void before() {
				// TODO Auto-generated method stub
				super.before();

				mProgressDialogUtils = new MyProgressDialogUtils(
						R.string.progress_dialog_loading,
						ActivityDetailsActivity.this);
				mProgressDialogUtils.showDialog();
			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();

				MyToast.makeText(ActivityDetailsActivity.this, true,
						R.string.load_failed, MyToast.LENGTH_SHORT).show();
			}

			@Override
			public void timeout() {
				// TODO Auto-generated method stub
				super.timeout();

				mMeetupDetailsTask.cancel(true);

				mProgressDialogUtils.dismissDialog();

				Log.e("zyf", "time out......");

				MyToast.makeText(ActivityDetailsActivity.this, true,
						R.string.load_failed, MyToast.LENGTH_SHORT).show();
			}

			@Override
			public void complete() {
				// TODO Auto-generated method stub
				super.complete();

				mProgressDialogUtils.dismissDialog();
			}
		};
		mMeetupDetailsTask.setTimeOutEnabled(true, 20 * 1000);
		mMeetupDetailsTask.safeExecute();

		mMeetupDetailsTask.then(new MemberList());
	}

	ArrayList<MemberItem> mMemberItems = new ArrayList<MemberItem>();

	private class MemberList implements Runnable {
		@Override
		public void run() {

			MembersListTask mMembersListTask = new MembersListTask(id) {
				public void callback() {

					mMemberItems = getMembers();

					Log.e("zyf", "members list length: " + mMemberItems.size());

					/*
					 * mGalleryAdapter=new
					 * ActivityDetailsGalleryAdapter(ActivityDetaisActivity
					 * .this, mMemberItems, mImageLoader, options);
					 * mGallery.setAdapter(mGalleryAdapter);
					 */

					LinearLayout.LayoutParams lpLayoutParams = new LayoutParams(
							DeviceUtil.dip2px(ActivityDetailsActivity.this, 50),
							DeviceUtil.dip2px(ActivityDetailsActivity.this, 50));
					lpLayoutParams.setMargins(0, 0,
							DeviceUtil.dip2px(ActivityDetailsActivity.this, 8),
							0);

					for (int i = 0; i < mMemberItems.size(); i++) {
						ImageView imageView = new ImageView(
								ActivityDetailsActivity.this);
						imageView
								.setOnClickListener(new MemberOnclickListenter(
										mMemberItems.get(i)));
						mImageLoader.displayImage(mMemberItems.get(i)
								.getImageUrl(), imageView, options);

						mMemberListContainer.addView(imageView, lpLayoutParams);
					}
				}

				public void failure() {

				}

				public void complete() {

				}

				public void before() {

				}

				@Override
				public void timeout() {

					super.timeout();

					Log.e("zyf", "time out.......");

					this.cancel(true);
				}

			};

			mMembersListTask.setTimeOutEnabled(true, 10 * 1000);
			mMembersListTask.safeExecute();

		}
	}

	class MemberOnclickListenter implements View.OnClickListener {

		private MemberItem memberItem;

		public MemberOnclickListenter(MemberItem memberItem) {
			super();
			this.memberItem = memberItem;
		}

		@Override
		public void onClick(View view) {

			Log.e("zyf", "onclick name: " + memberItem.getUserName());
		}

	}

	private void initTitle() {
		mTitleView = (TextView) findViewById(R.id.mTitleView);
		mTitleView.setText(getResources().getString(R.string.activities));

		Button leftBtn = (Button) findViewById(R.id.leftBtn);
		leftBtn.setBackgroundResource(R.drawable.title_back);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setOnClickListener(this);
	}

	@SuppressWarnings("unchecked")
	private void initViews() {

		mTotalContainer = (LinearLayout) findViewById(R.id.mTotalContainer);
		// mTotalContainer.setMode(Mode.PULL_FROM_END);

		mLeaderAvatarView = (ImageView) findViewById(R.id.mLeaderAvatarView);

		// mGallery=(Gallery)findViewById(R.id.mGallery);

		mLeaderNameTv = (TextView) findViewById(R.id.mLeaderNameTv);
		mNameTv = (TextView) findViewById(R.id.mNameTv);
		mDescTv = (TextView) findViewById(R.id.mDescTv);
		mLocDescTv = (TextView) findViewById(R.id.mLocDescTv);
		mStartDateTv = (TextView) findViewById(R.id.mStartDateTv);
		mStartTimeTv = (TextView) findViewById(R.id.mStartTimeTv);
		mEndDateTv = (TextView) findViewById(R.id.mEndDateTv);
		mEndTimeTv = (TextView) findViewById(R.id.mEndTimeTv);
		mRecommandNumTv = (TextView) findViewById(R.id.mRecommandNumTv);

		mRecommandContainer = (RelativeLayout) findViewById(R.id.mRecommandContainer);
		mLocationContainer = (RelativeLayout) findViewById(R.id.mLocationContainer);
		mOptionContainer = (RelativeLayout) findViewById(R.id.mOptionContainer);

		mMemberListContainer = (LinearLayout) findViewById(R.id.mMemberListContainer);
		mMomentContainer = (LinearLayout) findViewById(R.id.mMomentContainer);

		mCancelBtn = (Button) findViewById(R.id.mCancelBtn);
		mJoinBtn = (Button) findViewById(R.id.mJoinBtn);
		mSharedBtn = (Button) findViewById(R.id.mSharedBtn);
		mConsultBtn = (Button) findViewById(R.id.mConsultBtn);

		mCancelBtn.setOnClickListener(this);
		mJoinBtn.setOnClickListener(this);
		mSharedBtn.setOnClickListener(this);
		mConsultBtn.setOnClickListener(this);

		mMomentGridView = (MyGridView) findViewById(R.id.mMomentGridView);

		mPullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.mPullToRefreshScrollView);
		mPullToRefreshScrollView.setMode(Mode.DISABLED);
		mPullToRefreshScrollView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {

				Log.e("zyf", "scroller view pull up......");

				pageNum++;

				loadMoment();
			}
		});
	}

	private void loadMoment() {

		mGetMomentTask = new GetMomentTask(pageNum, pageSize, id) {

			@Override
			public void callback() {

				Log.e("zyf", "get moment call back......");

				if (mGetMomentTask.getImageUrls().size() == pageSize) {

					mPullToRefreshScrollView.setMode(Mode.PULL_FROM_END);
				} else {
					mPullToRefreshScrollView.setMode(Mode.DISABLED);
				}

				for (int i = 0; i < mGetMomentTask.getImageUrls().size(); i++) {

					Log.e("zyf", "image url" + i + ": "
							+ mGetMomentTask.getImageUrls().get(i));
					momentList.add(mGetMomentTask.getImageUrls().get(i));
				}

				if (momentList.size() == 0) {
					mMomentContainer.setVisibility(View.GONE);
				} else {
					mMomentContainer.setVisibility(View.VISIBLE);
				}

				if (mMomentAdapter == null) {
					mMomentAdapter = new MomentAdapter(
							ActivityDetailsActivity.this, momentList);
					mMomentGridView.setAdapter(mMomentAdapter);
				} else {
					mMomentAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void failure() {

				Log.e("zyf", "get moment failed......");
			}

			@Override
			public void timeout() {

				Log.e("zyf", "get moment time out......");
			}

			@Override
			public void complete() {
				// TODO Auto-generated method stub
				super.complete();

				mPullToRefreshScrollView.onRefreshComplete();
			}

		};
		mGetMomentTask.setTimeOutEnabled(true, 10 * 1000);
		mGetMomentTask.safeExecute();
	}

	private void updateUI() {

		mTitleView.setText(mActivityDetailsItem.getLeaderName()
				+ getResources().getString(R.string.activity_of_somebody));

		mLeaderNameTv.setText(mActivityDetailsItem.getLeaderName());

		mImageLoader.displayImage(mActivityDetailsItem.getLeaderAvatarUrl(),
				mLeaderAvatarView, options);

		mNameTv.setText(mActivityDetailsItem.getName());

		if (mActivityDetailsItem.getDesc() != null
				&& mActivityDetailsItem.getDesc().length() > 0) {

			mDescTv.setVisibility(View.VISIBLE);
			mDescTv.setText(mActivityDetailsItem.getDesc());
		} else {
			mDescTv.setVisibility(View.GONE);
		}

		DateUtils mDateUtils = new DateUtils(this,
				mActivityDetailsItem.getStartTime());
		mStartDateTv.setText(mDateUtils.getDate());
		mStartTimeTv.setText(mDateUtils.getExactlyTime());

		mDateUtils = new DateUtils(this, mActivityDetailsItem.getEndTime());
		mEndDateTv.setText(mDateUtils.getDate());
		mEndTimeTv.setText(mDateUtils.getExactlyTime());

		if (mActivityDetailsItem.getRecommendNum() == 0) {
			mRecommandContainer.setVisibility(View.GONE);
		} else {
			mRecommandContainer.setVisibility(View.VISIBLE);

			mRecommandNumTv.setText(mActivityDetailsItem.getRecommendNum()
					+ getString(R.string.recomand_show));
		}

		if (mActivityDetailsItem.getLocDesc() != null
				&& mActivityDetailsItem.getLocDesc().length() > 0) {
			mLocationContainer.setVisibility(View.VISIBLE);

			mLocDescTv.setText(mActivityDetailsItem.getLocDesc());

			mLocationContainer.setOnClickListener(this);
		} else {
			mLocationContainer.setVisibility(View.GONE);
		}

		if (mActivityDetailsItem.getState() == GlobalArgs.CLUB_ACTIVITY_STATE_CANCELED
				|| mActivityDetailsItem.getState() == GlobalArgs.CLUB_ACTIVITY_STATE_COMPLETED) { // 活动已经完成或者已经取消

			mOptionContainer.setVisibility(View.GONE);
			mJoinBtn.setVisibility(View.GONE);
			mCancelBtn.setVisibility(View.GONE);
		} else { // 活动正在进行中
			if (mActivityDetailsItem.getMemberRank() == GlobalArgs.MEMBER_RANK_LEADER) { // 本次活动的leader

				isJoined = true;
				mJoinBtn.setText(getResources().getString(R.string.recommend));

				mCancelBtn.setVisibility(View.VISIBLE);
				mOptionContainer.setVisibility(View.VISIBLE);

				int curTime = (int) (System.currentTimeMillis() / 1000);
				if (mActivityDetailsItem.getStartTime() > curTime) { // 活动还没开始，可以进行编辑

					// 初始化"编辑"按钮
					Button rightBtn = (Button) findViewById(R.id.rightBtn);
					rightBtn.setVisibility(View.VISIBLE);
					rightBtn.setText(getResources().getString(R.string.edit));
					rightBtn.setOnClickListener(this);
				}

			} else if (mActivityDetailsItem.getMemberRank() == GlobalArgs.MEMBER_RANK_NONE) { // 尚未参加此次活动

				mOptionContainer.setVisibility(View.VISIBLE);

				isJoined = false;
				mJoinBtn.setVisibility(View.VISIBLE);

				mCancelBtn.setVisibility(View.GONE);
			} else { // 已经参加了该次活动

				isJoined = true;
				mJoinBtn.setText(getResources().getString(R.string.recommend));

				mCancelBtn.setVisibility(View.GONE);
			}
		}

		mTotalContainer.setVisibility(View.VISIBLE);
		// mMomentGridView.setMode(Mode.BOTH);
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.leftBtn:

			Intent intent2 = new Intent();
			setResult(Constant.RESULT_CODE_ACTIVITY_READ, intent2);

			finish();
			break;
		case R.id.mLocationContainer:

			Intent locationIntent = new Intent(ActivityDetailsActivity.this,
					LocationShowActivity.class);
			locationIntent.putExtra("locX", mActivityDetailsItem.getLocX());
			locationIntent.putExtra("locY", mActivityDetailsItem.getLocY());
			locationIntent.putExtra("locDesc",
					mActivityDetailsItem.getLocDesc());
			startActivity(locationIntent);

			break;
		case R.id.mCancelBtn:

			mActivityCancelTask = new ActivityCancelTask(id) {

				@Override
				public void callback() {

					Log.e("zyf", "cancel activity success......");

					mActivityDetailsItem
							.setState(GlobalArgs.CLUB_ACTIVITY_STATE_CANCELED);

					updateUI();

					MyToast.makeText(ActivityDetailsActivity.this, true,
							R.string.activity_cancel_success,
							MyToast.LENGTH_SHORT).show();
				}

				@Override
				public void complete() {

					mProgressDialogUtils.dismissDialog();
				}

				@Override
				public void failure() {

					Log.e("zyf", "cancel activity failure......");

					MyToast.makeText(ActivityDetailsActivity.this, true,
							R.string.activity_cancel_failed,
							MyToast.LENGTH_SHORT).show();
				}

				@Override
				public void before() {

					mProgressDialogUtils = new MyProgressDialogUtils(
							R.string.progress_dialog_activity_canceling,
							ActivityDetailsActivity.this);
					mProgressDialogUtils.showDialog();
				}

				@Override
				public void timeout() {
					// TODO Auto-generated method stub
					super.timeout();

					mActivityCancelTask.cancel(true);

					Log.e("zyf", "cancel activity time out......");

					MyToast.makeText(ActivityDetailsActivity.this, true,
							R.string.activity_cancel_failed,
							MyToast.LENGTH_SHORT).show();
				}
			};
			mActivityCancelTask.setTimeOutEnabled(true, 10 * 1000);
			mActivityCancelTask.safeExecute();

			break;
		case R.id.mJoinBtn:

			if (isJoined) {

				Log.e("zyf", "invite invite invite...");

				Intent inviteIntent = new Intent(ActivityDetailsActivity.this,
						ChooseMemberActivity.class);
				inviteIntent.putExtra("isFromActivityDetails", true);
				inviteIntent.putExtra("activityId", id);
				startActivity(inviteIntent);

				return;
			}

			mActivityJoinTask = new ActivityJoinTask(id) {

				@Override
				public void callback() {

					Log.e("zyf", "join activity success......");

					MyToast.makeText(ActivityDetailsActivity.this, true,
							R.string.activity_add_success, MyToast.LENGTH_SHORT)
							.show();

					mActivityDetailsItem
							.setMemberRank(GlobalArgs.MEMBER_RANK_NORMAL);

					updateUI();
				}

				@Override
				public void complete() {

					mProgressDialogUtils.dismissDialog();
				}

				@Override
				public void failure() {

					Log.e("zyf", "join activity failure......");

					MyToast.makeText(ActivityDetailsActivity.this, true,
							R.string.activity_add_failed, MyToast.LENGTH_SHORT)
							.show();
				}

				@Override
				public void before() {

					mProgressDialogUtils = new MyProgressDialogUtils(
							R.string.progress_dialog_activity_adding,
							ActivityDetailsActivity.this);
					mProgressDialogUtils.showDialog();
				}

				@Override
				public void timeout() {
					// TODO Auto-generated method stub
					super.timeout();

					mActivityJoinTask.cancel(true);

					Log.e("zyf", "cancel activity time out......");

					MyToast.makeText(ActivityDetailsActivity.this, true,
							R.string.activity_add_failed, MyToast.LENGTH_SHORT)
							.show();
				}
			};
			mActivityJoinTask.setTimeOutEnabled(true, 10 * 1000);
			mActivityJoinTask.safeExecute();
			break;
		case R.id.mSharedBtn:

			try {
				getKey();
			} catch (IOException e) {

				Log.e("zyf", "share sdk exception: " + e.toString());
			}

			break;
		case R.id.rightBtn:

			Intent intent = new Intent(ActivityDetailsActivity.this,
					ActivityCreateActivity.class);
			intent.putExtra("ActivityDetailsItem", mActivityDetailsItem);
			startActivityForResult(intent, Constant.RESULT_CODE_ACTIVITY_UPDATE);

			break;
		case R.id.mConsultBtn:
			
			Intent chatIntent=new Intent(ActivityDetailsActivity.this,ChatActivity.class);
			chatIntent.putExtra("ActivityDetails", mActivityDetailsItem);
			
			if(mActivityDetailsItem.getMemberRank()==GlobalArgs.MEMBER_RANK_NONE){   //未参加活动
				ArrayList<MemberItem> tempMemberItems=new ArrayList<MemberItem>();
				tempMemberItems.add(mMemberItems.get(0));
				
				AccountDetailInfo accountDetailInfo=new UserInformationLocalManagerUtil(ActivityDetailsActivity.this).ReadUserInformation();
				MemberItem memberItem=new MemberItem();
				memberItem.setUserId(AppConfig.account.getAccountId());
				memberItem.setImageUrl(accountDetailInfo.getImageUrl());
				memberItem.setUserName(accountDetailInfo.getName());
				tempMemberItems.add(memberItem);
				
				chatIntent.putExtra("Members", tempMemberItems);
			}else{
				chatIntent.putExtra("Members", mMemberItems);
			}
			
			
			startActivity(chatIntent);
			
			Log.e("zyf", "user id: "+AppConfig.account.getAccountId());
			
			break;
		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Constant.RESULT_CODE_ACTIVITY_UPDATE) {

			if (data != null) {

				Log.e("zyf", "result activity update success......");

				mActivityDetailsItem = (ActivityDetailsItem) data
						.getSerializableExtra("ActivityDetailsItem");

				updateUI();
			}
		}
	}

	private void showShare(final String key) throws IOException {

		File pathFile = android.os.Environment.getExternalStorageDirectory();

		File myCaptureFile = new File(pathFile, "te.jpg");
		if(!myCaptureFile.exists()){
			
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(myCaptureFile));
			InputStream is = getResources().openRawResource(R.drawable.a);
			
			Bitmap mBitmap = BitmapFactory.decodeStream(is);
			
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
		}

		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用

		oks.setTitle(mNameTv.getText().toString());
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://www.sina.com");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我是程才！！！");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数

		oks.setImagePath("/sdcard/te.jpg");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://www.tripc2c.com/m/index.php?action=signup&ekey="+key);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {

			@Override
			public void onShare(Platform platform, ShareParams paramsToShare) {
				// TODO Auto-generated method stub
				Log.i("cc", platform.getName().toString());
				if ("ShortMessage".equals(platform.getName())) {
					paramsToShare.setText("我在A计划上建了一个活动["+mNameTv.getText().toString()+"]，欢迎你加入：http://www.tripc2c.com/m/index.php?action=fm&ekey="+key);
					paramsToShare.setTitle(null);
					paramsToShare.setImagePath(null);
				}
				if ("Email".equals(platform.getName())) {
					paramsToShare.setText("嗨,我推荐一个《A计划》上的活动，希望你能加入！\n活动 :众筹旅行\n发起人: kevin\n开始时间: 04-05 09:00\n结束时间:04-06 10:30\n在手机上打开邮件，几步就可以开始使用了！\n1), 还没有安装<href=\"http:/www.baidu.com\">点击安装</href>, 已经在使用了就 点击开启");
					paramsToShare.setTitle("活动邀请");
					paramsToShare.setImagePath(null);
					
					return;
				}
				if ("Wechat".equals(platform.getName())) {
					paramsToShare.setText(mActivityDetailsItem.getDesc());
					paramsToShare.setTitle(mNameTv.getText().toString());
				
					
					return;
				}
			}
		});

		// 启动分享GUI
		oks.show(this);

	}

	public void getKey() throws IOException{

		task = new Invite2Task(id,(short)163,(short)110) {

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
				try {
					showShare(task.key);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ToastUtil.toastshort(ActivityDetailsActivity.this,"请重新分享");
				}

			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();
				// ToastUtil.toastshort(ClubListActivity.this,"返回空置");

				Log.e("cc", "get contact data failure....");

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
}
