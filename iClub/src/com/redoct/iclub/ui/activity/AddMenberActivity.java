package com.redoct.iclub.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.oct.ga.comm.domain.account.AccountMasterInfo;
import com.redoct.iclub.BaseActivity;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.MembersListAdapter;
import com.redoct.iclub.task.ClubQuerySubcribersTask;
import com.redoct.iclub.task.ClubSubscribersRemoveTask;
import com.redoct.iclub.util.PersistentUtil;
import com.redoct.iclub.util.ToastUtil;

public class AddMenberActivity extends BaseActivity {
	private ListView lvMembersList;
	private MembersListAdapter adapter;
	private List<AccountMasterInfo> list, temp_list;
	private ClubQuerySubcribersTask task;
	private ClubSubscribersRemoveTask task_remove;
	private String id;
	private String id_string[];
	private List<String> id_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addmembers);
		id = getIntent().getStringExtra("id");
		list = new ArrayList<AccountMasterInfo>();
		temp_list = new ArrayList<AccountMasterInfo>();
		id_list = new ArrayList<String>();
		AccountMasterInfo item = new AccountMasterInfo();
		item.setImageUrl(PersistentUtil.getInstance().readString(this,
				"imageurl", ""));
		item.setName(PersistentUtil.getInstance().readString(this, "username",
				""));
		list.add(item);
		initView();
		loadData();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}
	
	private Button leftBtn,rightBtn;

	private void initView() {
		// TODO Auto-generated method stub
		lvMembersList = (ListView) findViewById(R.id.lv_addmember_memberlist);

		findViewById(R.id.btn_choosemember_add).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						id_string = new String[task.getList().size()];
					    for(int i =0;i<task.getList().size();i++){
					    	id_string[i]= task.getList().get(i).getId();
					    }
						Intent intent = new Intent();
						intent.setClass(AddMenberActivity.this,
								ChooseMemberActivity.class);
						intent.putExtra("membering", id_string);
						intent.putExtra("id", id);
						startActivityForResult(intent, 1);
						overridePendingTransition(R.anim.push_left_in,
								R.anim.push_left_out);
					}
				});
		
		TextView mTitleView=(TextView)findViewById(R.id.mTitleView);
		mTitleView.setText(getString(R.string.members));
		
		leftBtn=(Button)findViewById(R.id.leftBtn);
		leftBtn.setBackgroundResource(R.drawable.title_back);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});
		
		rightBtn=(Button)findViewById(R.id.rightBtn);
		rightBtn.setText(getString(R.string.complete));
		rightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RemovePeopelFromeClub();
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {

			/*ArrayList<ContactItem> listId = (ArrayList<ContactItem>) data
					.getSerializableExtra("idList");
			Intent in = new Intent();
			in.putExtra("idList", listId);
			setResult(11, in);
			finish();*/
			loadData();

		}
	}

	private void loadData() {

		task = new ClubQuerySubcribersTask(id) {

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

				adapter = new MembersListAdapter(task.getList(),
						AddMenberActivity.this);
				lvMembersList.setAdapter(adapter);

			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();
				ToastUtil.toastshort(AddMenberActivity.this, "failure");

				// loadData();
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

	public void setCompleteVisible(int size) {
		if (size == 0) {

			rightBtn.setVisibility(View.INVISIBLE);
		} else {
			rightBtn.setVisibility(View.VISIBLE);
		}

	}

	public void RemovePeopelFromeClub() {
		ArrayList<AccountMasterInfo> list = (ArrayList<AccountMasterInfo>) adapter
				.getTemp_list();
		for (int i = 0; i < list.size(); i++) {
			AccountMasterInfo item = list.get(i);
			id_list.add(item.getId());
		}
		id_string = new String[id_list.size()];
		id_string =id_list.toArray(id_string);
		task_remove = new ClubSubscribersRemoveTask(id, id_string) {

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
				ToastUtil.toastshort(AddMenberActivity.this, "succeful!");
				Intent intent = new Intent();
				intent.putExtra("joinpeople", (task.getList().size()-id_list.size())+"");
				setResult(1, intent);
				finish();

			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();
				ToastUtil.toastshort(AddMenberActivity.this, "failure");

				// loadData();
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
		task_remove.setTimeOutEnabled(true, 10 * 1000);
		task_remove.safeExecute();
	}
}
