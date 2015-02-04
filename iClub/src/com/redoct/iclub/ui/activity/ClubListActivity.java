package com.redoct.iclub.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.ActivitiesBaseAdapter;
import com.redoct.iclub.adapter.ClubListAdapter;
import com.redoct.iclub.item.ActivityItem;
import com.redoct.iclub.item.ClubItem;
import com.redoct.iclub.task.GetClubListTask;
import com.redoct.iclub.util.ToastUtil;
import com.redoct.iclub.util.activity.BaseActivityUtil;

public class ClubListActivity extends Activity {

	private ListView lvClubList;

	private List<ClubItem> clubList = new ArrayList<ClubItem>();

	private GetClubListTask task;
	private TextView tvName;
	private TextView tvNum;
	private ImageView ivClubImg;
	private ClubListAdapter adapter;
	private ClubItem item;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_club_list);
		initView();
		lvClubList = (ListView) findViewById(R.id.list_club);
		adapter = new ClubListAdapter(this);
		lvClubList.setAdapter(adapter);
		lvClubList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int i,
					long arg3) {
				// TODO Auto-generated method stub
				
			   Intent in = new Intent();
			   in.setClass(ClubListActivity.this,ClubDetailActivity.class);
			   in.putExtra("id",clubList.get(i).getId());
               BaseActivityUtil.startActivity(ClubListActivity.this, in,false,false);
               
			}
		});

		loadData();

	}

	private void loadData() {

		task = new GetClubListTask() {

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
				clubList = task.getClubList();
				if (clubList.size() == 0) {
					ToastUtil.toastshort(ClubListActivity.this, "返回空置");
				}
				adapter.setList(clubList);
				adapter.notifyDataSetChanged();

			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();
				// ToastUtil.toastshort(ClubListActivity.this,"返回空置");
				loadData();
				Log.e("zyf", "get data failure....");

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

	private void initView() {

		TextView mTitleView = (TextView) findViewById(R.id.mTitleView);
		mTitleView.setText(getResources().getString(R.string.friends));

	}

}
