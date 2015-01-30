package com.redoct.iclub.ui.activity;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.ActivityDetailsGalleryAdapter;
import com.redoct.iclub.item.ActivityDetailsItem;
import com.redoct.iclub.task.MeetupDetailsTask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;

public class ActivityDetaisActivity extends Activity implements OnClickListener{
	
	private MeetupDetailsTask mMeetupDetailsTask;
	
	private ActivityDetailsItem mActivityDetailsItem=new ActivityDetailsItem();
	
	private String id;
	
	private String leaderName;
	
	private Gallery mGallery;
	
	private ActivityDetailsGalleryAdapter mGalleryAdapter;
	
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_activity_details);
		
		id=getIntent().getStringExtra("id");
		leaderName=getIntent().getStringExtra("leaderName");
		
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
				
				mGalleryAdapter=new ActivityDetailsGalleryAdapter(ActivityDetaisActivity.this, mActivityDetailsItem.getMembers(), mImageLoader, options);
				mGallery.setAdapter(mGalleryAdapter);
			}
		};
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
		mGallery=(Gallery)findViewById(R.id.mGallery);
	}

	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		case R.id.leftBtn:
			
			finish();
			break;

		default:
			break;
		}
	}
}
