package com.redoct.iclub.ui.activity;

import java.util.ArrayList;
import java.util.List;

import com.redoct.iclub.R;
import com.redoct.iclub.adapter.MembersListAdapter;
import com.redoct.iclub.item.MemberItem;
import com.redoct.iclub.util.PersistentUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class AddMenberActivity extends Activity{
	private ListView lvMembersList;
	private MembersListAdapter adapter;
	private List<MemberItem> list ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addmembers);
		list = new ArrayList<MemberItem>();
		MemberItem item = new MemberItem();
		item.setImageUrl(PersistentUtil.getInstance().readString(this,"imageurl", ""));
		item.setUserName(PersistentUtil.getInstance().readString(this,"username", ""));
		list.add(item);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		lvMembersList = (ListView) findViewById(R.id.lv_addmember_memberlist);
		adapter = new MembersListAdapter(list, this);
		lvMembersList.setAdapter(adapter);
		findViewById(R.id.btn_choosemember_add).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(AddMenberActivity.this,ChooseMemberActivity.class);
				startActivityForResult(intent,1);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		
	}

}
