package com.redoct.iclub.ui.activity;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.oct.ga.comm.EcryptUtil;
import com.oct.ga.comm.GlobalArgs;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.ChatMessageAdapter;
import com.redoct.iclub.item.ActivityDetailsItem;
import com.redoct.iclub.item.MemberItem;
import com.redoct.iclub.item.MessageItem;
import com.redoct.iclub.task.ChatMessageSendTask;

public class ChatActivity extends Activity implements OnClickListener {

	private ActivityDetailsItem mActivityDetailsItem;
	private ArrayList<MemberItem> mMemberItems;

	private Handler mHandler;

	private final int cycleTime = 2 * 1000;

	private Runnable getMessageRunnable = new Runnable() {

		@Override
		public void run() {

			mHandler.postDelayed(getMessageRunnable, cycleTime);

			Log.e("zyf", "get message from server......");
		}
	};


	private EditText mContentEt;

	private ArrayList<MessageItem> mMessageItems = new ArrayList<MessageItem>();

	private PullToRefreshListView mContentListView;
	private ChatMessageAdapter mChatMessageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_chat);

		initTitleViews();

		initViews();

		mHandler = new Handler();

		mActivityDetailsItem = (ActivityDetailsItem) getIntent()
				.getSerializableExtra("ActivityDetails");
		mMemberItems = (ArrayList<MemberItem>) getIntent()
				.getSerializableExtra("Members");

		Log.e("zyf", "memberlist size: " + mMemberItems.size());

		if (mActivityDetailsItem.getMemberRank() == GlobalArgs.MEMBER_RANK_NONE) { // 单聊

			Log.e("zyf", "single chat......");
		} else {

			Log.e("zyf", "muiti chat......");
		}

		// mHandler.postDelayed(getMessageRunnable, cycleTime);
	}

	private void initTitleViews() {

		TextView mTitleView = (TextView) findViewById(R.id.mTitleView);
		mTitleView.setText(getResources().getString(R.string.consult));

		Button leftBtn = (Button) findViewById(R.id.leftBtn);
		leftBtn.setBackgroundResource(R.drawable.title_back);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setOnClickListener(this);

		Button rightBtn = (Button) findViewById(R.id.rightBtn);
		rightBtn.setText(getResources().getString(R.string.members));
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setOnClickListener(this);
	}

	private void initViews() {

		

		mContentEt = (EditText) findViewById(R.id.mContentEt);

		mContentListView = (PullToRefreshListView) findViewById(R.id.mContentListView);
		mChatMessageAdapter = new ChatMessageAdapter(mMessageItems, this);
		mContentListView.setAdapter(mChatMessageAdapter);
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.leftBtn:

			mHandler.removeCallbacks(getMessageRunnable);

			finish();

			break;

		case R.id.rightBtn:

			Intent intent = new Intent(ChatActivity.this,
					ChatMembersActivity.class);
			intent.putExtra("Members", mMemberItems);
			startActivity(intent);

			break;

		default:
			break;
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (inputMethodManager.isActive()) {
					inputMethodManager.hideSoftInputFromWindow(
							ChatActivity.this.getCurrentFocus()
									.getWindowToken(), 0);
				}
				Log.i("cc", "send succuful");

				Short channelType;
				String toId;
				if (mActivityDetailsItem.getMemberRank() == GlobalArgs.MEMBER_RANK_NONE) { // 单聊
					channelType = GlobalArgs.CHANNEL_TYPE_QUESTION;
					toId = EcryptUtil.md5ChatId(
							mMemberItems.get(0).getUserId(), mMemberItems
									.get(1).getUserId());
				} else {
					channelType = GlobalArgs.CHANNEL_TYPE_TASK;
					toId = mActivityDetailsItem.getClubId();
				}
				ChatMessageSendTask mChatMessageSendTask = new ChatMessageSendTask(
						UUID.randomUUID().toString(), toId, mContentEt
								.getText().toString(),
						mActivityDetailsItem.getId(), channelType) {

				};
				mChatMessageSendTask.setTimeOutEnabled(true, 10 * 1000);
				mChatMessageSendTask.safeExecute();

				return true;
			}
		}
		return super.dispatchKeyEvent(event);

	}

}
