package com.redoct.iclub.fragment;

import java.util.ArrayList;

import android.R.integer;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.ActivitiesBaseAdapter;
import com.redoct.iclub.item.ActivityItem;
import com.redoct.iclub.task.ActivityListTask;
import com.redoct.iclub.util.DateUtils;
import com.redoct.iclub.util.MyProgressDialogUtils;
import com.redoct.iclub.widget.MyToast;

public class ActivitiesFragment extends Fragment{
	
	private PullToRefreshListView mPullToRefreshListView;
	
	private ArrayList<ActivityItem> activityItems=new ArrayList<ActivityItem>();
	
	private ActivitiesBaseAdapter mActivitiesBaseAdapter;
	
	
	short pageNum=1;
	short pageSize=10;
	
	private ActivityListTask task;
	
	private int mode=-1;
	
	private MyProgressDialogUtils myProgressDialogUtils;
	
	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView=inflater.inflate(R.layout.fragment_activities, container, false);
		
		initTitle(contentView);
		
		mPullToRefreshListView=(PullToRefreshListView)contentView.findViewById(R.id.mPullToRefreshListView);
		mActivitiesBaseAdapter=new ActivitiesBaseAdapter(getActivity(),activityItems);
		mPullToRefreshListView.setAdapter(mActivitiesBaseAdapter);
		mPullToRefreshListView.setVerticalScrollBarEnabled(true);
		
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
		
		loadData();
		
		return contentView;
	}
	
	private void loadData(){
		
		task=new ActivityListTask(getActivity(),pageNum,pageSize){
			
			@Override
			public void timeout() {
				// TODO Auto-generated method stub
				super.timeout();
				
				task.cancel(true);
				Log.e("zyf", "get data time out....");
				
				MyToast.makeText(getActivity(), true, R.string.load_failed, MyToast.LENGTH_SHORT).show();
			}

			@Override
			public void before() {
				// TODO Auto-generated method stub
				super.before();
				
				Log.e("zyf", "start get data....");
				
				myProgressDialogUtils=new MyProgressDialogUtils(R.string.progress_dialog_loading, getActivity());
				myProgressDialogUtils.showDialog();
			}

			@Override
			public void callback() {
				// TODO Auto-generated method stub
				super.callback();
				
				Log.e("zyf", "call back result: "+task.getResult());
				
				Log.e("zyf", "arraylist activity length: "+task.getActivities().size());
				
				if(mode==0){
					activityItems.clear();
				}
				
				ArrayList<ActivityItem> resultActivityItems=task.getActivities();
				for(int i=0;i<resultActivityItems.size();i++){
					activityItems.add(resultActivityItems.get(i));
				}
				
				sortData();
				
				mPullToRefreshListView.onRefreshComplete();
				mActivitiesBaseAdapter.notifyDataSetChanged();
				
				if(resultActivityItems.size()<pageSize){
					mPullToRefreshListView.setMode(Mode.PULL_FROM_START);;
				}else{
					mPullToRefreshListView.setMode(Mode.BOTH);
				}
				
				//MyToast.makeText(getActivity(), true, R.string.load_success, MyToast.LENGTH_SHORT).show();
			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();
				
				Log.e("zyf", "get data failure....");
				
				mPullToRefreshListView.onRefreshComplete();
				myProgressDialogUtils.dismissDialog();
				
				MyToast.makeText(getActivity(), true, R.string.load_failed, MyToast.LENGTH_SHORT).show();
				
			}

			@Override
			public void complete() {
				// TODO Auto-generated method stub
				super.complete();
				
				Log.e("zyf", "get data complete....");
				
				mPullToRefreshListView.onRefreshComplete();
				
				myProgressDialogUtils.dismissDialog();
			}
		};
		task.setTimeOutEnabled(true, 10*1000);
		task.safeExecute();
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
	
	private void initTitle(View contentView){
		
		TextView mTitleView=(TextView) contentView.findViewById(R.id.mTitleView);
		mTitleView.setText(getResources().getString(R.string.title_activities));

	}

}
