package com.redoct.iclub.ui.activity;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.ActivityDetailsGalleryAdapter;
import com.redoct.iclub.item.ActivityDetailsItem;
import com.redoct.iclub.task.MeetupDetailsTask;
import com.redoct.iclub.util.DateUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ActivityDetaisActivity extends Activity implements OnClickListener{
	
	private MeetupDetailsTask mMeetupDetailsTask;
	
	private ActivityDetailsItem mActivityDetailsItem=new ActivityDetailsItem();
	
	private String id;
	
	private String leaderName;
	
	private String leaderAvatarUrl;
	
	private Gallery mGallery;
	
	private ActivityDetailsGalleryAdapter mGalleryAdapter;
	
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	
	private ImageView mLeaderAvatarView;
	
	private ScrollView mTotalContainer;
	
	private TextView mLeaderNameTv,mNameTv,mDescTv,mLocDescTv,mStartDateTv,mStartTimeTv,mEndDateTv,mEndTimeTv,mRecommandNumTv;
	
	private RelativeLayout mRecommandContainer,mLocationContainer;

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
		
		mGallery=(Gallery)findViewById(R.id.mGallery);
		
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
	}
	
	private void updateUI(){
		
		mGalleryAdapter=new ActivityDetailsGalleryAdapter(ActivityDetaisActivity.this, mActivityDetailsItem.getMembers(), mImageLoader, options);
		mGallery.setAdapter(mGalleryAdapter);
		
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
