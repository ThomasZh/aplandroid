package com.redoct.iclub.ui.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.redoct.iclub.BaseActivity;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.ChatMemberAdapter;
import com.redoct.iclub.item.MemberItem;
import com.redoct.iclub.task.MembersListTask;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;

public class ChatMembersActivity extends BaseActivity {
	
	private ArrayList<MemberItem> mMemberItems;
	
	private String channelId;
	private Short channelType;
	
	private PullToRefreshListView mContentListView;
	private ChatMemberAdapter mChatMemberAdapter;
	
	private MembersListTask mMembersListTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_chat_members);
		
		initTitleViews();
		
		mMemberItems=(ArrayList<MemberItem>)getIntent().getSerializableExtra("Members");
		channelId=getIntent().getStringExtra("channelId");
		channelType=getIntent().getShortExtra("channelType",(short)0);
		
		mContentListView=(PullToRefreshListView)findViewById(R.id.mContentListView);
		mContentListView.setMode(Mode.DISABLED);
		
		
		if(mMemberItems!=null){
			
			Log.e("zyf", "get members directly.....");
			
			mChatMemberAdapter=new ChatMemberAdapter(mMemberItems, this);
			mContentListView.setAdapter(mChatMemberAdapter);
			
		}else{
			
			Log.e("zyf", "get members from server.....");
			
			mMembersListTask=new MembersListTask(channelId,channelType){

				@Override
				public void callback() {
					super.callback();
					
					mMemberItems=mMembersListTask.getMembers();
					mChatMemberAdapter=new ChatMemberAdapter(mMemberItems, ChatMembersActivity.this);
					mContentListView.setAdapter(mChatMemberAdapter);
				}
				
			};
			mMembersListTask.safeExecute();
		}
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
