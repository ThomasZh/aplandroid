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
import com.redoct.iclub.task.MeetupDetailsTask;
import com.redoct.iclub.task.MembersListTask;
import com.redoct.iclub.util.DateUtils;

public class ActivityDetaisActivity extends Activity implements OnClickListener{
	
	private MeetupDetailsTask mMeetupDetailsTask;
	
	private ActivityDetailsItem mActivityDetailsItem=new ActivityDetailsItem();
	
	private String id;
	
	private String leaderName;
	
	private String leaderAvatarUrl;
	
	//private Gallery mGallery;
	
	private ActivityDetailsGalleryAdapter mGalleryAdapter;
	
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	
	private ImageView mLeaderAvatarView;
	
	private ScrollView mTotalContainer;
	
	private TextView mLeaderNameTv,mNameTv,mDescTv,mLocDescTv,mStartDateTv,mStartTimeTv,mEndDateTv,mEndTimeTv,mRecommandNumTv;
	
	private RelativeLayout mRecommandContainer,mLocationContainer,mOptionContainer;
	
	private LinearLayout mMemberListContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_activity_details);
		
		id=getIntent().getStringExtra("id");
		leaderName=getIntent().getStringExtra("leaderName");
		leaderAvatarUrl=getIntent().getStringExtra("leaderAvatarUrl");
		
		mImageLoader=ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_launcher)
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new RoundedBitmapDisplayer(100)) 
		.build();
		
		initTitle();
		
		initViews();
		
		Log.e("zyf", "activity id: "+id);
		
		mMeetupDetailsTask=new MeetupDetailsTask(id){

			@Override
			public void callback() {
				// TODO Auto-generated method stub
				super.callback();
				
				mActivityDetailsItem=mMeetupDetailsTask.getDetails();
				
				Log.e("zyf", "call back members num: "+mActivityDetailsItem.getMembers().length());
				
				updateUI();
				
			}

			@Override
			public void before() {
				// TODO Auto-generated method stub
				super.before();
			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();
			}

			@Override
			public void timeout() {
				// TODO Auto-generated method stub
				super.timeout();
				
				mMeetupDetailsTask.cancel(true);
				
				Log.e("zyf", "time out......");
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
						ImageView imageView=new ImageView(ActivityDetaisActivity.this);
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
		TextView mTitleView=(TextView) findViewById(R.id.mTitleView);
		mTitleView.setText(leaderName+getResources().getString(R.string.activity_of_somebody));
		
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
	}
	
	private void updateUI(){
		
		mLeaderNameTv.setText(leaderName);
		
		mImageLoader.displayImage(leaderAvatarUrl, mLeaderAvatarView, options);
		
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
		
		if(mActivityDetailsItem.getRecommends().length()==0){
			mRecommandContainer.setVisibility(View.GONE);
		}else{
			mRecommandContainer.setVisibility(View.VISIBLE);
			
			mRecommandNumTv.setText(mActivityDetailsItem.getRecommends().length()+getString(R.string.recomand_show));
		}
		
		if(mActivityDetailsItem.getLocDesc()!=null&&mActivityDetailsItem.getLocDesc().length()>0){
			mLocationContainer.setVisibility(View.VISIBLE);
			
			mLocDescTv.setText(mActivityDetailsItem.getLocDesc());
			
			mLocationContainer.setOnClickListener(this);
		}else{
			mLocationContainer.setVisibility(View.GONE);
		}
		
		if(mActivityDetailsItem.getState()==GlobalArgs.CLUB_ACTIVITY_STATE_CANCELED
				||mActivityDetailsItem.getState()==GlobalArgs.CLUB_ACTIVITY_STATE_COMPLETED){
			mOptionContainer.setVisibility(View.GONE);
		}else{
			if(mActivityDetailsItem.getMemberRank()==GlobalArgs.MEMBER_RANK_NONE){
				mOptionContainer.setVisibility(View.VISIBLE);
			}else{
				mOptionContainer.setVisibility(View.GONE);
			}
		}
		
		mTotalContainer.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		case R.id.leftBtn:
			
			finish();
			break;
		case R.id.mLocationContainer:
			
			Intent locationIntent=new Intent(ActivityDetaisActivity.this,LocationShowActivity.class);
			locationIntent.putExtra("locX", mActivityDetailsItem.getLocX());
			locationIntent.putExtra("locY", mActivityDetailsItem.getLocY());
			locationIntent.putExtra("locDesc", mActivityDetailsItem.getLocDesc());
			startActivity(locationIntent);
			
			break;

		default:
			break;
		}
	}
}
