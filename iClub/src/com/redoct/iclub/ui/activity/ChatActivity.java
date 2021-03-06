package com.redoct.iclub.ui.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.oct.ga.comm.DatetimeUtil;
import com.oct.ga.comm.EcryptUtil;
import com.oct.ga.comm.GlobalArgs;
import com.redoct.iclub.BaseActivity;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.ChatMessageAdapter;
import com.redoct.iclub.config.AppConfig;
import com.redoct.iclub.item.ActivityDetailsItem;
import com.redoct.iclub.item.MemberItem;
import com.redoct.iclub.item.MessageItem;
import com.redoct.iclub.task.ChatMessageSendTask;
import com.redoct.iclub.task.ConfirmMessageReadTask;
import com.redoct.iclub.task.QueryMessagePaginationTask;
import com.redoct.iclub.util.Constant;
import com.redoct.iclub.util.MessageDatabaseHelperUtil;
import com.redoct.iclub.util.ToastUtil;
import com.redoct.iclub.util.UserInformationLocalManagerUtil;

public class ChatActivity extends BaseActivity implements OnClickListener,
		OnRefreshListener {
	public NotificationManager mNotificationManager;
	private ActivityDetailsItem mActivityDetailsItem;
	private ArrayList<MemberItem> mMemberItems;
	QueryMessagePaginationTask queryMessageTask = null;
	BroadcastReceiver mReceiver = null;
	private Handler mHandler;
	/** Notification构造器 */
	NotificationCompat.Builder mBuilder;
	private final int cycleTime = 2 * 1000;
	private short num = 1;
	private Runnable getMessageRunnable = new Runnable() {

		@Override
		public void run() {

			mHandler.postDelayed(getMessageRunnable, cycleTime);

			Log.e("zyf", "get message from server......");
		}
	};

	private EditText mContentEt;

	private ArrayList<MessageItem> mMessageItems = new ArrayList<MessageItem>();
	private ArrayList<MessageItem> mTempMessageItems = new ArrayList<MessageItem>();

	private PullToRefreshListView mContentListView;
	private ChatMessageAdapter mChatMessageAdapter;

	private String channelId;
	private Short channelType;
	private String toId;
	private String chatId;
	private String toAccountId;
	private boolean haveUnReadMessage;
	
	private MessageDatabaseHelperUtil mMessageDatabaseHelperUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_chat);

		mHandler = new Handler();
		
		mMessageDatabaseHelperUtil=new MessageDatabaseHelperUtil(this);

		mActivityDetailsItem = (ActivityDetailsItem) getIntent()
				.getSerializableExtra("ActivityDetails");
		mMemberItems = (ArrayList<MemberItem>) getIntent()
				.getSerializableExtra("Members");

		channelId = getIntent().getStringExtra("channelId");
		channelType = getIntent().getShortExtra("channelType",
				Short.parseShort("0"));
		toId = getIntent().getStringExtra("toId");
		haveUnReadMessage = getIntent().getBooleanExtra("haveUnReadMessage",
				false);
		chatId = getIntent().getStringExtra("chatId");

		if (mActivityDetailsItem != null) {

			Log.e("zyf", "memberlist size: " + mMemberItems.size());

			if (mActivityDetailsItem.getMemberRank() == GlobalArgs.MEMBER_RANK_NONE) { // 单聊

				Log.e("zyf", "single chat......");

				channelType = GlobalArgs.CHANNEL_TYPE_CREATE_QUESTION; // 第一次进来时需要创建
				//toId = mMemberItems.get(0).getUserId();
				
				toAccountId=mMemberItems.get(0).getUserId();
				chatId=EcryptUtil.md5ChatId(AppConfig.account.getAccountId(), toAccountId);
			} else {

				Log.e("zyf", "muiti chat......");

				channelType = GlobalArgs.CHANNEL_TYPE_TASK;
				//toId = mActivityDetailsItem.getClubId();
				
				chatId=mActivityDetailsItem.getId();
			}

			channelId = mActivityDetailsItem.getId();

			Log.e("zyf", "md5 chatId: "+chatId);
		}

		Log.e("zyf", "chat id:" + chatId);

		initTitleViews();
		initViews();
	}

	private void initTitleViews() {
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		TextView mTitleView = (TextView) findViewById(R.id.mTitleView);

		if (channelType == GlobalArgs.CHANNEL_TYPE_TASK) {
			mTitleView.setText(getResources().getString(R.string.muti_chat));
		} else {
			mTitleView.setText(getResources().getString(R.string.consult));
		}

		Button leftBtn = (Button) findViewById(R.id.leftBtn);
		leftBtn.setBackgroundResource(R.drawable.title_back);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setOnClickListener(this);

		Button rightBtn = (Button) findViewById(R.id.rightBtn);
		rightBtn.setText(getResources().getString(R.string.members));
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setOnClickListener(this);

		IntentFilter mFilter = new IntentFilter(); // 代码注册广播
		mFilter.addAction("com.cc.msg");

		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {

				MessageItem item = (MessageItem) intent.getSerializableExtra("msgItem");
				if(item.getChatId().equals(chatId)){
					
					mMessageItems.add(item);
					mChatMessageAdapter.notifyDataSetChanged();
					mContentListView.getRefreshableView().setSelection(
							mChatMessageAdapter.getCount() - 1);
					new ConfirmMessageReadTask(chatId).safeExecute();
					
					//add by Kevin
					item.setUnReadNum(0);
					new MessageDatabaseHelperUtil(ChatActivity.this).updateChatMessage(item);
				}
			}
		};
		registerReceiver(mReceiver, mFilter);

	}

	private void initViews() {
		mContentEt = (EditText) findViewById(R.id.mContentEt);

		mContentListView = (PullToRefreshListView) findViewById(R.id.mContentListView);
		mContentListView.setOnRefreshListener(this);
		/*if (!haveUnReadMessage) {

			List<MessageItem> list = new MessageDatabaseHelperUtil(this)
					.getChatMessages(AppConfig.account.getAccountId(), chatId);
			Collections.reverse(list);
			for (int i = 0; i < list.size(); i++) {
				if (i >= 20) {
					break;
				}
				mMessageItems.add(list.get(i));
			}
			Collections.reverse(mMessageItems);
			mChatMessageAdapter = new ChatMessageAdapter(mMessageItems, this);
			mContentListView.setAdapter(mChatMessageAdapter);

			mContentListView.getRefreshableView().setSelection(
					mChatMessageAdapter.getCount() - 1);
		} else {*/
			firstQuerry();
		//}

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
			if (mMemberItems != null) {
				intent.putExtra("Members", mMemberItems);
			} else {
				intent.putExtra("channelId", chatId);
				intent.putExtra("channelType", channelType);
			}
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

				/*
				 * if (mActivityDetailsItem.getMemberRank() ==
				 * GlobalArgs.MEMBER_RANK_NONE) { // 单聊 channelType =
				 * GlobalArgs.CHANNEL_TYPE_QUESTION; toId =
				 * EcryptUtil.md5ChatId( mMemberItems.get(0).getUserId(),
				 * mMemberItems .get(1).getUserId()); } else { channelType =
				 * GlobalArgs.CHANNEL_TYPE_TASK; toId =
				 * mActivityDetailsItem.getClubId(); }
				 */
				ChatMessageSendTask mChatMessageSendTask = new ChatMessageSendTask(
						UUID.randomUUID().toString(), chatId, toAccountId,mContentEt
								.getText().toString(), channelId, channelType) {
					@Override
					public void timeout() {
						// TODO Auto-generated method stub
						super.timeout();

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
						MessageItem item = new MessageItem();
						item.setAccoutId(AppConfig.account.getAccountId());
						item.setLastContent(mContentEt.getText().toString());
						item.setIsSend("2");

						item.setChatId(chatId);
						item.setMessageType(Constant.MESSAGE_TYPE_CHAT);
						item.setUserAvatarUrl(new UserInformationLocalManagerUtil(
								ChatActivity.this).ReadUserInformation()
								.getImageUrl());
						item.setTimestamp(DatetimeUtil.currentTimestamp());
						item.setUnReadNum(0);
						
						if(mMessageDatabaseHelperUtil.getUnReadNumWithChatId(AppConfig.account.getAccountId(),
										item.getChatId())==-1){
							Log.e("zyf", "create a new chat room......");
							item.setChannelName(mActivityDetailsItem.getName());
							item.setChannelType(channelType);
							item.setChannelId(channelId);
							mMessageDatabaseHelperUtil.addNewMessage(item);
						}else {
							mMessageDatabaseHelperUtil.updateChatMessage(item);
						}
						

						new MessageDatabaseHelperUtil(ChatActivity.this)
								.addChatMessage(item);
						mMessageItems.add(item);
						mChatMessageAdapter = new ChatMessageAdapter(mMessageItems, ChatActivity.this);
						mContentListView.setAdapter(mChatMessageAdapter);

						mContentListView.getRefreshableView().setSelection(
								mChatMessageAdapter.getCount() - 1);
						
						mContentEt.setText("");

					}

					@Override
					public void failure() {
						// TODO Auto-generated method stub
						super.failure();

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
				mChatMessageSendTask.setTimeOutEnabled(true, 10 * 1000);
				mChatMessageSendTask.safeExecute();

				return true;
			}
		}
		return super.dispatchKeyEvent(event);

	}

	public PendingIntent getDefalutIntent(int flags) {
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,
				new Intent(), flags);
		return pendingIntent;
	}

	@Override
	public void onRefresh(PullToRefreshBase refreshView) {
		num++;
		// TODO Auto-generated method stub
		queryMessageTask = new QueryMessagePaginationTask(chatId, num,
				(short) 20, this) {
			@Override
			public void timeout() {
				// TODO Auto-generated method stub
				super.timeout();

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
				Log.i("cc", "message  refresh  succesful........");
				mContentListView.onRefreshComplete();
				
				 mTempMessageItems = mMessageItems;
				 mMessageItems = queryMessageTask.getmMessageItems();
				 
				mMessageItems.addAll(mTempMessageItems);

				mChatMessageAdapter = new ChatMessageAdapter(mMessageItems,
						ChatActivity.this);
				mContentListView.setAdapter(mChatMessageAdapter);

				mContentListView.getRefreshableView().setSelection(
						mChatMessageAdapter.getCount() - 1);

			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();

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
		queryMessageTask.setTimeOutEnabled(true, 10 * 1000);
		queryMessageTask.safeExecute();

	}

	public void firstQuerry() {
		queryMessageTask = new QueryMessagePaginationTask(chatId, (short) 1,
				(short) 20, this) {
			@Override
			public void timeout() {
				// TODO Auto-generated method stub
				super.timeout();

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
				Log.i("cc", "message  refresh  succesful........");

				mMessageItems = queryMessageTask.getmMessageItems();
				if (mMessageItems==null||mMessageItems.size()<=0){
					return;
				}
				mChatMessageAdapter = new ChatMessageAdapter(mMessageItems,
						ChatActivity.this);
				mContentListView.setAdapter(mChatMessageAdapter);

				mContentListView.getRefreshableView().setSelection(
						mChatMessageAdapter.getCount() - 1);
				// int i = mMessageItems.size();
				// add by kevin
				

					MessageItem item = new MessageItem();
					item.setAccoutId(AppConfig.account.getAccountId());
					item.setLastContent(mMessageItems.get(
							mMessageItems.size() - 1).getLastContent());
					item.setChatId(chatId);
					item.setMessageType(Constant.MESSAGE_TYPE_CHAT);
					item.setUserAvatarUrl(mMessageItems.get(
							mMessageItems.size() - 1).getUserAvatarUrl());
					item.setTimestamp(DatetimeUtil.currentTimestamp());
					item.setUnReadNum(0);
					new MessageDatabaseHelperUtil(ChatActivity.this)
							.updateChatMessage(item);
				

			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();

				// loadData();
				Log.e("zyf", "get data failure....");
				ToastUtil.toastshort(ChatActivity.this, "消息加载失败！！");

			}

			@Override
			public void complete() {
				// TODO Auto-generated method stub
				super.complete();

				Log.e("zyf", "get data complete....");

				// mPullToRefreshListView.onRefreshComplete();
			}

		};
		queryMessageTask.setTimeOutEnabled(true, 10 * 1000);
		queryMessageTask.safeExecute();
	}

}
