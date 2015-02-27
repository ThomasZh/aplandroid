package com.redoct.iclub.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.redoct.iclub.R;
import com.redoct.iclub.adapter.MembersListAdapter;
import com.redoct.iclub.item.ContactItem;
import com.redoct.iclub.item.MemberItem;
import com.redoct.iclub.task.GetClubListTask;
import com.redoct.iclub.task.MembersListTask;
import com.redoct.iclub.util.PersistentUtil;
import com.redoct.iclub.util.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class AddMenberActivity extends Activity {
	private ListView lvMembersList;
	private MembersListAdapter adapter;
	private List<MemberItem> list;
	private MembersListTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addmembers);
		list = new ArrayList<MemberItem>();
		MemberItem item = new MemberItem();
		item.setImageUrl(PersistentUtil.getInstance().readString(this,
				"imageurl", ""));
		item.setUserName(PersistentUtil.getInstance().readString(this,
				"username", ""));
		list.add(item);
		initView();
		//loadData();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	private void initView() {
		// TODO Auto-generated method stub
		lvMembersList = (ListView) findViewById(R.id.lv_addmember_memberlist);
		adapter = new MembersListAdapter(list, this);
		lvMembersList.setAdapter(adapter);
		findViewById(R.id.btn_choosemember_add).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setClass(AddMenberActivity.this,
								ChooseMemberActivity.class);
						startActivityForResult(intent, 1);
						overridePendingTransition(R.anim.push_left_in,
								R.anim.push_left_out);
					}
				});
		findViewById(R.id.leftBtn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
			
			ArrayList<ContactItem> listId =(ArrayList<ContactItem>) data.getSerializableExtra("idList");
			Intent in = new Intent();
			in.putExtra("idList", listId);
			setResult(11, in);
			finish();
			
			
		}
	}
}
