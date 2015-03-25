package com.redoct.iclub.ui.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.redoct.iclub.BaseActivity;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.ChatMemberAdapter;
import com.redoct.iclub.item.MemberItem;

public class ChatMembersActivity extends BaseActivity {
	
	private ArrayList<MemberItem> mMemberItems;
	
	private PullToRefreshListView mContentListView;
	private ChatMemberAdapter mChatMemberAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_chat_members);
		
		initTitleViews();
		
		mMemberItems=(ArrayList<MemberItem>)getIntent().getSerializableExtra("Members");		
		
		mContentListView=(PullToRefreshListView)findViewById(R.id.mContentListView);
		mContentListView.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.DISABLED);
		mChatMemberAdapter=new ChatMemberAdapter(mMemberItems, this);
		mContentListView.setAdapter(mChatMemberAdapter);
	}
	
	private void initTitleViews(){
		
		TextView mTitleView=(TextView) findViewById(R.id.mTitleView);
		mTitleView.setText(getResources().getString(R.string.members));
		
		
		Button leftBtn=(Button) findViewById(R.id.leftBtn);
		leftBtn.setBackgroundResource(R.drawable.title_back);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				finish();
			}
		});
		
	}
}
