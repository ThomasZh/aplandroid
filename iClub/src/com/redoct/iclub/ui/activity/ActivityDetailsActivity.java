package com.redoct.iclub.ui.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.oct.ga.comm.GlobalArgs;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.ActivityDetailsGalleryAdapter;
import com.redoct.iclub.item.ActivityDetailsItem;
import com.redoct.iclub.item.MemberItem;
import com.redoct.iclub.task.ActivityCancelTask;
import com.redoct.iclub.task.ActivityDetailsTask;
import com.redoct.iclub.task.ActivityJoinTask;
import com.redoct.iclub.task.MembersListTask;
import com.redoct.iclub.util.Constant;
import com.redoct.iclub.util.DateUtils;
import com.redoct.iclub.util.MyProgressDialogUtils;
import com.redoct.iclub.widget.MyToast;

public class ActivityDetailsActivity extends Activity implements OnClickListener{
	
	private ActivityDetailsTask mMeetupDetailsTask;
	
	private ActivityJoinTask mActivityJoinTask;
	
	private ActivityCancelTask mActivityCancelTask;
	
	private ActivityDetailsItem mActivityDetailsItem=new ActivityDetailsItem();
	
	private String id;
	
	private String leaderName;
	
	private String leaderAvatarUrl;
	
	private ActivityDetailsGalleryAdapter mGalleryAdapter;
	
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	
	private ImageView mLeaderAvatarView;
	
	private ScrollView mTotalContainer;
	
	private TextView mLeaderNameTv,mNameTv,mDescTv,mLocDescTv,mStartDateTv,mStartTimeTv,mEndDateTv,mEndTimeTv,mRecommandNumTv;
	
	private RelativeLayout mRecommandContainer,mLocationContainer;
	private RelativeLayout mOptionContainer;
	
	private Button mCancelBtn,mJoinBtn;
	
	private LinearLayout mMemberListContainer;
	
	private boolean isJoined;
	
	private MyProgressDialogUtils mProgressDialogUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_activity_details);
		
		id=getIntent().getStringExtra("id");
		leaderName=getIntent().getStringExtra("leaderName");
		leaderAvatarUrl=getIntent().getStringExtra("leaderAvatarUrl");
		
		mImageLoader=ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.me_normal)
		.showImageForEmptyUri(R.drawable.me_normal)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new RoundedBitmapDisplayer(100)) 
		.build();
		
		initTitle();
		
		initViews();
		
		Log.e("zyf", "activity id: "+id);
		
		mMeetupDetailsTask=new ActivityDetailsTask(id){

			@Override
			public void callback() {
				// TODO Auto-generated method stub
				super.callback();
				
				mActivityDetailsItem=mMeetupDetailsTask.getDetails();
				
				updateUI();
				
				//MyToast.makeText(ActivityDetailsActivity.this, true, R.string.load_success, MyToast.LENGTH_SHORT).show();
				
			}

			@Override
			public void before() {
				// TODO Auto-generated method stub
				super.before();
				
				mProgressDialogUtils=new MyProgressDialogUtils(R.string.progress_dialog_loading, ActivityDetailsActivity.this);
				mProgressDialogUtils.showDialog();
			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();
				
				MyToast.makeText(ActivityDetailsActivity.this, true, R.string.load_failed, MyToast.LENGTH_SHORT).show();
			}

			@Override
			public void timeout() {
				// TODO Auto-generated method stub
				super.timeout();
				
				mMeetupDetailsTask.cancel(true);
				
				mProgressDialogUtils.dismissDialog();
				
				Log.e("zyf", "time out......");
				
				MyToast.makeText(ActivityDetailsActivity.this, true, R.string.load_failed, MyToast.LENGTH_SHORT).show();
			}

			@Override
			public void complete() {
				// TODO Auto-generated method stub
				super.complete();
				
				mProgressDialogUtils.dismissDialog();
			}
		};
		mMeetupDetailsTask.setTimeOutEnabled(true, 10*1000);
		mMeetupDetailsTask.safeExecute();
		
		mMeetupDetailsTask.then(new MemberList());
	}
	
	ArrayList<MemberItem> mMemberItems=new ArrayList<MemberItem>();
	
	private class MemberList implements Runnable {
		@Override
		public void run() {

			MembersListTask mMembersListTask = new MembersListTask(id) {
				public void callback() {
					
					mMemberItems=getMembers();
					
					Log.e("zyf", "members list length: "+mMemberItems.size());
					
					/*mGalleryAdapter=new ActivityDetailsGalleryAdapter(ActivityDetaisActivity.this, mMemberItems, mImageLoader, options);
					mGallery.setAdapter(mGalleryAdapter);*/
					
					LinearLayout.LayoutParams lpLayoutParams=new LayoutParams(120, 120);
					lpLayoutParams.setMargins(0, 0, 10, 0);
					
					for(int i=0;i<mMemberItems.size();i++){
						ImageView imageView=new ImageView(ActivityDetailsActivity.this);
						mImageLoader.displayImage(mMemberItems.get(i).getImageUrl(), imageView, options);
						
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

			mMembersListTask.setTimeOutEnabled(true, 10*1000);
			mMembersListTask.safeExecute();

		}
	}
	
	private void initTitle(){
		//TextView mTitleView=(TextView) findViewById(R.id.mTitleView);
		//mTitleView.setText(leaderName+getResources().getString(R.string.activity_of_somebody));
		
		Button leftBtn=(Button) findViewById(R.id.leftBtn);
		leftBtn.setBackgroundResource(R.drawable.title_back);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setOnClickListener(this);
	}
	
	private void initViews(){
		
		mTotalContainer=(ScrollView)findViewById(R.id.mTotalContainer);
		
		mLeaderAvatarView=(ImageView)findViewById(R.id.mLeaderAvatarView);
		
		//mGallery=(Gallery)findViewById(R.id.mGallery);
		
		mLeaderNameTv=(TextView) findViewById(R.id.mLeaderNameTv);
		mNameTv=(TextView) findViewById(R.id.mNameTv);
		mDescTv=(TextView) findViewById(R.id.mDescTv);
		mLocDescTv=(TextView) findViewById(R.id.mLocDescTv);
		mStartDateTv=(TextView) findViewById(R.id.mStartDateTv);
		mStartTimeTv=(TextView) findViewById(R.id.mStartTimeTv);
		mEndDateTv=(TextView) findViewById(R.id.mEndDateTv);
		mEndTimeTv=(TextView) findViewById(R.id.mEndTimeTv);
		mRecommandNumTv=(TextView) findViewById(R.id.mRecommandNumTv);
		
		mRecommandContainer=(RelativeLayout)findViewById(R.id.mRecommandContainer);
		mLocationContainer=(RelativeLayout)findViewById(R.id.mLocationContainer);
		mOptionContainer=(RelativeLayout)findViewById(R.id.mOptionContainer);
		
		mMemberListContainer=(LinearLayout)findViewById(R.id.mMemberListContainer);
		
		mCancelBtn=(Button)findViewById(R.id.mCancelBtn);
		mJoinBtn=(Button)findViewById(R.id.mJoinBtn);
		
		mCancelBtn.setOnClickListener(this);
		mJoinBtn.setOnClickListener(this);
	}
	
	private void updateUI(){
		
		TextView mTitleView=(TextView) findViewById(R.id.mTitleView);
		mTitleView.setText(mActivityDetailsItem.getLeaderName()+getResources().getString(R.string.activity_of_somebody));
		
		mLeaderNameTv.setText(mActivityDetailsItem.getLeaderName());
		
		mImageLoader.displayImage(mActivityDetailsItem.getLeaderAvatarUrl(), mLeaderAvatarView, options);
		
		mNameTv.setText(mActivityDetailsItem.getName());
		
		if(mActivityDetailsItem.getDesc()!=null&&mActivityDetailsItem.getDesc().length()>0){
			
			mDescTv.setVisibility(View.VISIBLE);
			mDescTv.setText(mActivityDetailsItem.getDesc());
		}else{
			mDescTv.setVisibility(View.GONE);
		}
		
		DateUtils mDateUtils=new DateUtils(this,mActivityDetailsItem.getStartTime());
		mStartDateTv.setText(mDateUtils.getDate());
		mStartTimeTv.setText(mDateUtils.getExactlyTime());
		
		mDateUtils=new DateUtils(this, mActivityDetailsItem.getEndTime());
		mEndDateTv.setText(mDateUtils.getDate());
		mEndTimeTv.setText(mDateUtils.getExactlyTime());
		
		if(mActivityDetailsItem.getRecommendNum()==0){
			mRecommandContainer.setVisibility(View.GONE);
		}else{
			mRecommandContainer.setVisibility(View.VISIBLE);
			
			mRecommandNumTv.setText(mActivityDetailsItem.getRecommendNum()+getString(R.string.recomand_show));
		}
		
		if(mActivityDetailsItem.getLocDesc()!=null&&mActivityDetailsItem.getLocDesc().length()>0){
			mLocationContainer.setVisibility(View.VISIBLE);
			
			mLocDescTv.setText(mActivityDetailsItem.getLocDesc());
			
			mLocationContainer.setOnClickListener(this);
		}else{
			mLocationContainer.setVisibility(View.GONE);
		}
		
		if(mActivityDetailsItem.getState()==GlobalArgs.CLUB_ACTIVITY_STATE_CANCELED
				||mActivityDetailsItem.getState()==GlobalArgs.CLUB_ACTIVITY_STATE_COMPLETED){  //活动已经完成或者已经取消
			
			mOptionContainer.setVisibility(View.GONE);
			mJoinBtn.setVisibility(View.GONE);
			mCancelBtn.setVisibility(View.GONE);
		}else{   //活动正在进行中
			if(mActivityDetailsItem.getMemberRank()==GlobalArgs.MEMBER_RANK_LEADER){   //本次活动的leader
				
				isJoined=true;
				mJoinBtn.setText(getResources().getString(R.string.recommend));
				
				mCancelBtn.setVisibility(View.VISIBLE);
				mOptionContainer.setVisibility(View.VISIBLE);
				
				int curTime=(int)(System.currentTimeMillis()/1000);
				if(mActivityDetailsItem.getStartTime()>curTime){   //活动还没开始，可以进行编辑
					
					//初始化"编辑"按钮
					Button rightBtn=(Button)findViewById(R.id.rightBtn);
					rightBtn.setVisibility(View.VISIBLE);
					rightBtn.setText(getResources().getString(R.string.edit));
					rightBtn.setOnClickListener(this);
				}
				
			}else if(mActivityDetailsItem.getMemberRank()==GlobalArgs.MEMBER_RANK_NONE){  //尚未参加此次活动
				
				mOptionContainer.setVisibility(View.VISIBLE);
				
				isJoined=false;
				mJoinBtn.setVisibility(View.VISIBLE);
				
				mCancelBtn.setVisibility(View.GONE);
			}else{  //已经参加了该次活动
				
				isJoined=true;
				mJoinBtn.setText(getResources().getString(R.string.recommend));
				
				mCancelBtn.setVisibility(View.GONE);
			}
		}
		
		mTotalContainer.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		case R.id.leftBtn:
			
			Intent intent2=new Intent();
			setResult(Constant.RESULT_CODE_ACTIVITY_READ,intent2);
			
			finish();
			break;
		case R.id.mLocationContainer:
			
			Intent locationIntent=new Intent(ActivityDetailsActivity.this,LocationShowActivity.class);
			locationIntent.putExtra("locX", mActivityDetailsItem.getLocX());
			locationIntent.putExtra("locY", mActivityDetailsItem.getLocY());
			locationIntent.putExtra("locDesc", mActivityDetailsItem.getLocDesc());
			startActivity(locationIntent);
			
			break;
		case R.id.mCancelBtn:
			
			mActivityCancelTask=new ActivityCancelTask(id){
				
				@Override
	            public void callback(){
	               
	            	Log.e("zyf", "cancel activity success......");
	            	
	            	MyToast.makeText(ActivityDetailsActivity.this, true, R.string.activity_cancel_success, MyToast.LENGTH_SHORT).show();
	            }
	            
	            @Override
	            public void complete(){
	            	
	            	mProgressDialogUtils.dismissDialog();
	            }
	            
	            @Override
	            public void failure(){
	                
	                Log.e("zyf", "cancel activity failure......");
	                
	                MyToast.makeText(ActivityDetailsActivity.this, true, R.string.activity_cancel_failed, MyToast.LENGTH_SHORT).show();
	            }
	            
	            @Override
	            public void before(){
	            	
	            	mProgressDialogUtils=new MyProgressDialogUtils(R.string.progress_dialog_activity_canceling, ActivityDetailsActivity.this);
					mProgressDialogUtils.showDialog();
	            }

				@Override
				public void timeout() {
					// TODO Auto-generated method stub
					super.timeout();
					
					mActivityCancelTask.cancel(true);
					
					Log.e("zyf", "cancel activity time out......");
					
					MyToast.makeText(ActivityDetailsActivity.this, true, R.string.activity_cancel_failed, MyToast.LENGTH_SHORT).show();
				} 
			};
			mActivityCancelTask.setTimeOutEnabled(true, 10*1000);
			mActivityCancelTask.safeExecute();
			
			break;
		case R.id.mJoinBtn:
			
			if(isJoined){
				
				Log.e("zyf", "invite invite invite...");
				
				Intent inviteIntent=new Intent(ActivityDetailsActivity.this,ActivityRecommendActivity.class);
				startActivity(inviteIntent);
				
				return;
			}
			
			mActivityJoinTask=new ActivityJoinTask(id){
				
				@Override
	            public void callback(){
	               
	            	Log.e("zyf", "join activity success......");
	            	
	            	MyToast.makeText(ActivityDetailsActivity.this, true, R.string.activity_add_success, MyToast.LENGTH_SHORT).show();
	            }
	            
	            @Override
	            public void complete(){
	            	
	            	mProgressDialogUtils.dismissDialog();
	            }
	            
	            @Override
	            public void failure(){
	                
	                Log.e("zyf", "join activity failure......");
	                
	                MyToast.makeText(ActivityDetailsActivity.this, true, R.string.activity_add_failed, MyToast.LENGTH_SHORT).show();
	            }
	            
	            @Override
	            public void before(){
	            	
	            	mProgressDialogUtils=new MyProgressDialogUtils(R.string.progress_dialog_activity_adding, ActivityDetailsActivity.this);
					mProgressDialogUtils.showDialog();
	            }

				@Override
				public void timeout() {
					// TODO Auto-generated method stub
					super.timeout();
					
					mActivityJoinTask.cancel(true);
					
					Log.e("zyf", "create activity time out......");
					
					MyToast.makeText(ActivityDetailsActivity.this, true, R.string.activity_add_failed, MyToast.LENGTH_SHORT).show();
				} 
			};
			mActivityJoinTask.setTimeOutEnabled(true, 10*1000);
			mActivityJoinTask.safeExecute();
			break;
		case R.id.rightBtn:
			
			Intent intent=new Intent(ActivityDetailsActivity.this,ActivityCreateActivity.class);
			intent.putExtra("ActivityDetailsItem", mActivityDetailsItem);
			startActivityForResult(intent, Constant.RESULT_CODE_ACTIVITY_UPDATE);
			
			break;
		default:
			break;
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==Constant.RESULT_CODE_ACTIVITY_UPDATE){
			
			if(data!=null){
				
				Log.e("zyf", "result activity update success......");
				
				mActivityDetailsItem=(ActivityDetailsItem)data.getSerializableExtra("ActivityDetailsItem");
				
				updateUI();
			}
		}
	}
}
