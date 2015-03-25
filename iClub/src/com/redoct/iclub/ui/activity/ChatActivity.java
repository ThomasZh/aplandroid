package com.redoct.iclub.ui.activity;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.app.Notification;
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
import android.widget.ListView;
import android.widget.TextView;
import cn.jpush.android.data.l;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.oct.ga.comm.EcryptUtil;
import com.oct.ga.comm.GlobalArgs;
import com.redoct.iclub.BaseActivity;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.ChatMessageAdapter;
import com.redoct.iclub.adapter.MembersListAdapter;
import com.redoct.iclub.item.ActivityDetailsItem;
import com.redoct.iclub.item.MemberItem;
import com.redoct.iclub.item.MessageItem;
import com.redoct.iclub.task.ChatMessageSendTask;
import com.redoct.iclub.util.ToastUtil;

public class ChatActivity extends BaseActivity implements OnClickListener {
	public NotificationManager mNotificationManager;
	private ActivityDetailsItem mActivityDetailsItem;
	private ArrayList<MemberItem> mMemberItems;
	BroadcastReceiver mReceiver = null;
	private Handler mHandler;
	/** Notification构造器 */
	NotificationCompat.Builder mBuilder;
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
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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

		IntentFilter mFilter = new IntentFilter(); // 代码注册广播
		mFilter.addAction("com.cc.msg");

		mReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {

				MessageItem item = (MessageItem) intent
						.getSerializableExtra("msgItem");
				mMessageItems.add(item);
				mChatMessageAdapter.notifyDataSetChanged();
				initNotify(item.getLastContent(),item.getUserName());

			}
		};
		registerReceiver(mReceiver, mFilter);

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
						item.setLastContent(mContentEt.getText().toString());
						item.setIsSend(true);
						mMessageItems.add(item);
						mChatMessageAdapter.notifyDataSetChanged();
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
	
	private void initNotify(String content,String title){
		mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setContentTitle(title)
				.setContentText(content)
				.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
//				.setNumber(number)//显示数量
				.setTicker("iclub 消息！")//通知首次出现在通知栏，带上升动画效果的
				.setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
				.setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
//				.setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消  
				.setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
				.setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
				//Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
				.setSmallIcon(R.drawable.ic_launcher);
		mNotificationManager.notify(100, mBuilder.build());
	}
	public PendingIntent getDefalutIntent(int flags){
		PendingIntent pendingIntent= PendingIntent.getActivity(this, 1, new Intent(), flags);
		return pendingIntent;
	}

}
