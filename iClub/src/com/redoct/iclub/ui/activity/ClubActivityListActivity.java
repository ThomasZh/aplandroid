package com.redoct.iclub.ui.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.redoct.iclub.BaseActivity;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.ClubActivitiesBaseAdapter;
import com.redoct.iclub.item.ActivityItem;
import com.redoct.iclub.task.GetActivitiesInClubTask;
import com.redoct.iclub.util.Constant;
import com.redoct.iclub.util.MyProgressDialogUtils;
import com.redoct.iclub.widget.MyToast;

public class ClubActivityListActivity extends BaseActivity implements OnClickListener{
	
	private PullToRefreshListView mPullToRefreshListView;
	
	private ClubActivitiesBaseAdapter mClubActivitiesBaseAdapter;
	
	private ArrayList<ActivityItem> activityItems=new ArrayList<ActivityItem>();
	
	private GetActivitiesInClubTask mGetActivitiesInClubTask;
	
	private String id;
	short pageNum=1;
	short pageSize=10;
	
	private int mode=0;
	
	private MyProgressDialogUtils mProgressDialogUtils;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_club_activity_list);
		
		id=getIntent().getStringExtra("id");
		
		initTitle();
		
		initView();
		
		loadData();
	}
	
	private void initTitle(){
		
		TextView mTitleView = (TextView) findViewById(R.id.mTitleView);
		mTitleView.setText(getResources().getString(R.string.friends));
		
		Button btnRigthButton = (Button) findViewById(R.id.rightBtn);
		btnRigthButton.setVisibility(View.VISIBLE);
		btnRigthButton.setText(getResources().getString(R.string.add));
		btnRigthButton.setOnClickListener(this);
		
		Button leftBtn=(Button) findViewById(R.id.leftBtn);
		leftBtn.setBackgroundResource(R.drawable.title_back);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setOnClickListener(this);
		
	}
	
    private void loadData(){
		
    	mGetActivitiesInClubTask=new GetActivitiesInClubTask(ClubActivityListActivity.this,id,pageNum,pageSize){
			
			@Override
			public void timeout() {
				// TODO Auto-generated method stub
				super.timeout();
				
				mGetActivitiesInClubTask.cancel(true);
				Log.e("zyf", "get data time out....");
				
				mProgressDialogUtils.dismissDialog();
				
				MyToast.makeText(ClubActivityListActivity.this, true, R.string.load_failed, MyToast.LENGTH_SHORT).show();
			}

			@Override
			public void before() {
				// TODO Auto-generated method stub
				super.before();
				
				Log.e("zyf", "start get data....");
				
				mProgressDialogUtils=new MyProgressDialogUtils(R.string.progress_dialog_loading, ClubActivityListActivity.this);
				mProgressDialogUtils.showDialog();
			}

			@Override
			public void callback() {
				// TODO Auto-generated method stub
				super.callback();
				
				Log.e("zyf", "arraylist activity length: "+mGetActivitiesInClubTask.getActivities().size());
				
				if(mode==0){
					activityItems.clear();
				}
				
				ArrayList<ActivityItem> resultActivityItems=mGetActivitiesInClubTask.getActivities();
				for(int i=0;i<resultActivityItems.size();i++){
					activityItems.add(resultActivityItems.get(i));
				}
				
				sortData();
				
				mPullToRefreshListView.onRefreshComplete();
				mClubActivitiesBaseAdapter.notifyDataSetChanged();
				
				if(resultActivityItems.size()<pageSize){
					mPullToRefreshListView.setMode(Mode.PULL_FROM_START);;
				}else{
					mPullToRefreshListView.setMode(Mode.BOTH);
				}
				
				MyToast.makeText(ClubActivityListActivity.this, true, R.string.load_success, MyToast.LENGTH_SHORT).show();
			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();
				
				Log.e("zyf", "get data failure....");
				
				mPullToRefreshListView.onRefreshComplete();
				
				MyToast.makeText(ClubActivityListActivity.this, true, R.string.load_failed, MyToast.LENGTH_SHORT).show();
				
			}

			@Override
			public void complete() {
				// TODO Auto-generated method stub
				super.complete();
				
				Log.e("zyf", "get data complete....");
				
				mPullToRefreshListView.onRefreshComplete();
				
				mProgressDialogUtils.dismissDialog();
			}
		};
		mGetActivitiesInClubTask.setTimeOutEnabled(true, 10*1000);
		mGetActivitiesInClubTask.safeExecute();
	}
	
	private void sortData(){
		String curDate="-1";
		ActivityItem item;
		for(int i=0;i<activityItems.size();i++){
			item=activityItems.get(i);
			
			if(item.getStartDate().equals(curDate)){
				item.setTitle(false);
			}else{
				item.setTitle(true);
				
				curDate=item.getStartDate();
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initView() {

		mPullToRefreshListView=(PullToRefreshListView)findViewById(R.id.mPullToRefreshListView);
		mClubActivitiesBaseAdapter=new ClubActivitiesBaseAdapter(activityItems,ClubActivityListActivity.this);
		mPullToRefreshListView.setAdapter(mClubActivitiesBaseAdapter);
		
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {   //下拉刷新
				
				Log.e("zyf", "on pull down......");
				
				pageNum=1;
				
				mode=0;
				
				loadData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {    //上拉加载更多
				
				Log.e("zyf", "on pull up......");
				
				pageNum++;
				
				mode=1;
				
				loadData();
			}
		});

	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.leftBtn:
			
			finish();
			
			break;
		case R.id.rightBtn:
			
			Intent intent=new Intent(ClubActivityListActivity.this,ThemeSelectActivity.class);
			intent.putExtra("id", id);
			startActivityForResult(intent, Constant.RESULT_CODE_ACTIVITY_CREATE);
			
			break;
		default:
			break;
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==Constant.RESULT_CODE_ACTIVITY_CREATE){
			
			if(data!=null&&data.getBooleanExtra("ActivityCreateSuccess", false)){
				
				Log.e("zyf", "result activity create success...... ");
				
				loadData();
			}
		}
	}

}
