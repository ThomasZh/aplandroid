package com.redoct.iclub.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.redoct.iclub.BaseActivity;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.ClubListAdapter;
import com.redoct.iclub.item.ClubItem;
import com.redoct.iclub.task.GetClubListTask;
import com.redoct.iclub.util.ToastUtil;
import com.redoct.iclub.util.activity.BaseActivityUtil;

public class ClubListActivity extends BaseActivity {

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
		initTitleViews();
		lvClubList = (ListView) findViewById(R.id.list_club);
		adapter = new ClubListAdapter(this);
		lvClubList.setAdapter(adapter);
		lvClubList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int i,
					long arg3) {

				/*Intent in = new Intent();
				in.setClass(ClubListActivity.this, ClubDetailActivity.class);
				in.putExtra("id", clubList.get(i).getId());
				in.putExtra("memberNum", clubList.get(i).getSubscriberNum());
				BaseActivityUtil.startActivity(ClubListActivity.this, in,
						false, false);*/
				
				Intent intent = new Intent(ClubListActivity.this,AddMenberActivity.class);
				intent.putExtra("id", clubList.get(i).getId());
				startActivity(intent);
				BaseActivityUtil.setStartTransition(ClubListActivity.this);

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

	private void initTitleViews() {

		TextView mTitleView = (TextView) findViewById(R.id.mTitleView);
		mTitleView.setText(getResources().getString(R.string.friends));
		
		Button leftBtn=(Button)findViewById(R.id.leftBtn);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setBackgroundResource(R.drawable.title_back);
		leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});
		
		Button rightBtn=(Button)findViewById(R.id.rightBtn);
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setBackgroundResource(R.drawable.title_add);
		rightBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent();
				in.setClass(ClubListActivity.this,CreateClubActivity.class);
				startActivityForResult(in, 1);
				BaseActivityUtil.setStartTransition(ClubListActivity.this);
			}
		});

	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.push_right_in,
				R.anim.push_right_out);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		loadData();
	}
	

}
