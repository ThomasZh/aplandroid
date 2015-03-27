package com.redoct.iclub.fragment;

import java.util.ArrayList;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.oct.ga.comm.GlobalArgs;
import com.redoct.iclub.MainActivity;
import com.redoct.iclub.R;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.adapter.MessageBaseAdapter;
import com.redoct.iclub.config.AppConfig;
import com.redoct.iclub.item.MessageItem;
import com.redoct.iclub.task.ChatRoomListTask;
import com.redoct.iclub.task.InviteFeedbackTask;
import com.redoct.iclub.task.MessageCommitTask;
import com.redoct.iclub.task.MessageListTask;
import com.redoct.iclub.ui.activity.ChatActivity;
import com.redoct.iclub.util.Constant;
import com.redoct.iclub.util.FileUtils;
import com.redoct.iclub.util.MessageDatabaseHelperUtil;
import com.redoct.iclub.util.MyProgressDialogUtils;
import com.redoct.iclub.util.UserInformationLocalManagerUtil;
import com.redoct.iclub.widget.MyToast;

public class MessageFragment extends Fragment{
	
	private PullToRefreshListView mPullToRefreshListView;
	private MessageBaseAdapter mBaseAdapter;
	private ArrayList<MessageItem> messageItems=new ArrayList<MessageItem>();
	
	private MessageListTask mMessageListTask;
	
	private InviteFeedbackTask mInviteFeedbackTask;
	
	//用于确认已收到消息
	private String [] inviteIds;
	private String [] inviteFeedbackIds;
	
	private MyProgressDialogUtils mProgressDialogUtils;
	
	private int lastTryTime=0;
	
	MessageDatabaseHelperUtil mDatabaseHelperUtil;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View contentView=inflater.inflate(R.layout.fragment_message, container, false);
		
		mDatabaseHelperUtil=new MessageDatabaseHelperUtil(getActivity());
		
		messageItems=mDatabaseHelperUtil.getMessages(AppConfig.account.getAccountId());
		
		updateUnreadMessageNum(messageItems);
		
		Log.e("zyf", "db saved message size: "+messageItems.size());
		
		initViews(contentView);
		
		//Log.e("zyf:", "account id: "+new UserInformationLocalManagerUtil(getActivity()).ReadUserInformation().getId());
		
		lastTryTime=mDatabaseHelperUtil.getLastTryTime(AppConfig.account.getAccountId());
		if(lastTryTime==-1){
			lastTryTime=0;
			
			mDatabaseHelperUtil.addChatLastTryTime(AppConfig.account.getAccountId(), 0);
		}
		
		//load();
		
		IntentFilter mFilter = new IntentFilter(); // 代码注册广播
		mFilter.addAction("com.cc.msg");
		getActivity().registerReceiver(mReceiver, mFilter);
		
		return contentView;
	}
	
	public void updateUnreadMessageNum(ArrayList<MessageItem> messageItems){
		
		int count=0;
		for(int i=0;i<messageItems.size();i++){
			count+=messageItems.get(i).getUnReadNum();
		}
		
		iClubApplication.badgeNumber=count;
		
		MainActivity.handleUnReadMessage(iClubApplication.badgeNumber);
	}
	
	private void initViews(View convertView){
		
		TextView mTitleView=(TextView) convertView.findViewById(R.id.mTitleView);
		mTitleView.setText(getResources().getString(R.string.message));
		
		mPullToRefreshListView=(PullToRefreshListView)convertView.findViewById(R.id.mPullToRefreshListView);
		mPullToRefreshListView.setMode(Mode.PULL_FROM_START);
		mBaseAdapter=new MessageBaseAdapter(messageItems, getActivity()){

			@Override
			public void accept(String id,int position) {
				
				Log.e("zyf", "accept pos: "+position);
				
				short feedbackState=GlobalArgs.INVITE_STATE_ACCPET;
				
				final int pos=position;
				
				mInviteFeedbackTask=new InviteFeedbackTask(id, feedbackState){

					@Override
					public void before() {
						// TODO Auto-generated method stub
						super.before();
						
						mProgressDialogUtils=new MyProgressDialogUtils(R.string.progress_dialog_committing, getActivity());
						mProgressDialogUtils.showDialog();
					}

					@Override
					public void callback() {
						// TODO Auto-generated method stub
						super.callback();
						
						//接受好友成功，更新界面
						MessageItem item=messageItems.get(pos);
						item.setIsAccept(Constant.INVITE_ACCEPTTED);   //0:已接受
						
						mDatabaseHelperUtil.updateChatMessage(item);
						
						notifyDataSetChanged();
						
						MyToast.makeText(getActivity(), true, R.string.invite_commit_success, MyToast.LENGTH_SHORT).show();
				
					}

					@Override
					public void failure() {
						// TODO Auto-generated method stub
						super.failure();
						
						MyToast.makeText(getActivity(), true, R.string.invite_commit_failed, MyToast.LENGTH_SHORT).show();
					}

					@Override
					public void complete() {
						// TODO Auto-generated method stub
						super.complete();
						
						mProgressDialogUtils.dismissDialog();
					}

					@Override
					public void timeout() {
						// TODO Auto-generated method stub
						super.timeout();
						
						mProgressDialogUtils.dismissDialog();
						
						MyToast.makeText(getActivity(), true, R.string.invite_commit_failed, MyToast.LENGTH_SHORT).show();
					}
					
				};
				mInviteFeedbackTask.setTimeOutEnabled(true, 10*1000);
				mInviteFeedbackTask.safeExecute();
			}

			@Override
			public void gotoChat(int position) {
				
				Log.e("zyf", "go to chat pos: "+position);
				
				/*Log.e("zyf", "channelType: "+ messageItems.get(position).getChannelType());
				Log.e("zyf", "channelId: "+messageItems.get(position).getChannelId());
				Log.e("zyf", "toId: "+messageItems.get(position).getChatId());*/
				
				Intent intent=new Intent(getActivity(),ChatActivity.class);
				intent.putExtra("channelType", GlobalArgs.CHANNEL_TYPE_QUESTION);
				intent.putExtra("channelId", messageItems.get(position).getChannelId());
			    intent.putExtra("toId", messageItems.get(position).getChatId());
				
				getActivity().startActivity(intent);
			}
			
		};
		mPullToRefreshListView.setAdapter(mBaseAdapter);
		mPullToRefreshListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				
				Log.e("zyf", "refresh refresh refresh......");
				
				load();
			}
		});
	}
	
	private Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case 1:
				
				mPullToRefreshListView.onRefreshComplete();
				
				messageItems=mDatabaseHelperUtil.getMessages(AppConfig.account.getAccountId());
				
				Log.e("zyf", "message UI is coming,update it...messageItems size: "+messageItems.size());
				
				//mBaseAdapter.notifyDataSetChanged();
				
				if(mBaseAdapter!=null){
					mBaseAdapter.notifyDataSetChanged();
				}
				
				break;

			default:
				break;
			}
		}
		
	};
	
	private ChatRoomListTask mChatRoomListTask;
	
	private void load(){
		
		//获取邀请信息
		mMessageListTask=new MessageListTask(){

			@Override
			public void before() {
				// TODO Auto-generated method stub
				super.before();
				
				/*mProgressDialogUtils=new MyProgressDialogUtils(R.string.progress_dialog_loading, getActivity());
				mProgressDialogUtils.showDialog();*/
			}

			@Override
			public void callback() {
				// TODO Auto-generated method stub
				super.callback();
				
				Log.e("zyf", "message list size: "+mMessageListTask.getMessageList().size());
				
				//messageItems.clear();
				
				for(int i=0;i<mMessageListTask.getMessageList().size();i++){
					messageItems.add(mMessageListTask.getMessageList().get(i));
					
					mDatabaseHelperUtil.addNewMessage(mMessageListTask.getMessageList().get(i));
				}
				
				updateUnreadMessageNum(messageItems);
				
				inviteIds=mMessageListTask.getInviteIds();
				inviteFeedbackIds=mMessageListTask.getInviteFeedIds();
				
				if(inviteIds!=null)
					Log.e("zyf", "call back inviteIds length: "+inviteIds.length);
				
				if(inviteFeedbackIds!=null)
					Log.e("zyf", "call back inviteFeedbackIds length: "+inviteFeedbackIds.length);
				
				//FileUtils.updateMessageHistory(messageItems);
				
				//mPullToRefreshListView.onRefreshComplete();
				mBaseAdapter.notifyDataSetChanged();
				
				//updateUnreadMessageNum();
				
			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();
			}

			@Override
			public void complete() {
				// TODO Auto-generated method stub
				super.complete();
				
				mPullToRefreshListView.onRefreshComplete();
				//mProgressDialogUtils.dismissDialog();
			}

			@Override
			public void timeout() {
				// TODO Auto-generated method stub
				super.timeout();
				
				mPullToRefreshListView.onRefreshComplete();
				//mProgressDialogUtils.dismissDialog();
			}
			
		};
		mMessageListTask.setTimeOutEnabled(true, 10*1000);
		mMessageListTask.safeExecute();
		mMessageListTask.then(messageCommitRunnable);
		
		
		//获取聊天信息
		mChatRoomListTask=new ChatRoomListTask(lastTryTime){

			@Override
			public void before() {
				super.before();
				
				mProgressDialogUtils=new MyProgressDialogUtils(R.string.progress_dialog_loading, getActivity());
				mProgressDialogUtils.showDialog();
			}

			@Override
			public void callback() {
				// TODO Auto-generated method stub
				super.callback();
				
				Log.e("zyf", "message list size: "+mChatRoomListTask.getMessageList().size());
				
				ArrayList<MessageItem> tempMessageItems=mChatRoomListTask.getMessageList();
				
				for(int i=0;i<tempMessageItems.size();i++){
					
					boolean isFound=false;
					
					for(int j=0;j<messageItems.size();j++){
						
						if(tempMessageItems.get(i).getChatId().equals(messageItems.get(j).getChatId())){
							
							Log.e("zyf", "found found found....update...update...update...");
							
							//messageItems.add(j, tempMessageItems.get(i));
							messageItems.set(j, tempMessageItems.get(i));
							isFound=true;
							
							mDatabaseHelperUtil.updateChatMessage(messageItems.get(j));
							
							break;
						}
					}
					
					if(!isFound){   //插入操作
						
						Log.e("zyf", "no no no found....insert...insert...insert...");
						
						messageItems.add(tempMessageItems.get(i));
						
						mDatabaseHelperUtil.addNewMessage(tempMessageItems.get(i));
					}
				}
				
				updateUnreadMessageNum(messageItems);
				
				mPullToRefreshListView.onRefreshComplete();
				mBaseAdapter.notifyDataSetChanged();
				
				lastTryTime=mChatRoomListTask.getLastTryTime();
				
				mDatabaseHelperUtil.updateChatLastTryTime(AppConfig.account.getAccountId(),lastTryTime);
			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();
			}

			@Override
			public void complete() {
				// TODO Auto-generated method stub
				super.complete();
				
				mPullToRefreshListView.onRefreshComplete();
				mProgressDialogUtils.dismissDialog();
			}

			@Override
			public void timeout() {
				// TODO Auto-generated method stub
				super.timeout();
				
				mPullToRefreshListView.onRefreshComplete();
				mProgressDialogUtils.dismissDialog();
			}
		};
		mChatRoomListTask.setTimeOutEnabled(true, 10*1000);
		mChatRoomListTask.safeExecute();
	}
	
	Runnable messageCommitRunnable=new Runnable() {
		
		@Override
		public void run() {
			
			MessageCommitTask messageCommitTask=new MessageCommitTask(inviteIds, inviteFeedbackIds){

				@Override
				public void callback() {
					Log.e("zyf", "invite message commit success......");
					
					if(iClubApplication.badgeNumber>0){
						
						if(inviteIds!=null){
							iClubApplication.badgeNumber-=inviteIds.length;
						}
						
						if(inviteFeedbackIds!=null){
							iClubApplication.badgeNumber-=inviteFeedbackIds.length;
						}
						
						if(iClubApplication.badgeNumber<0){
							iClubApplication.badgeNumber=0;
						}
					}
				}
			};
			messageCommitTask.setTimeOutEnabled(true, 10*1000);
			messageCommitTask.safeExecute();
		}
	};
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			Log.e("zyf", "message fragment receive a msg from jpush......");
			
			messageItems.clear();
			
			ArrayList<MessageItem> tempMessageItems=new MessageDatabaseHelperUtil(getActivity()).getMessages(AppConfig.account.getAccountId());
			for(int i=0;i<tempMessageItems.size();i++){
				
				Log.e("zyf", "message last content: "+tempMessageItems.get(i).getLastContent());
				
				messageItems.add(tempMessageItems.get(i));
			}
			
			mBaseAdapter.notifyDataSetChanged();

		}
	};

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		
		if(!hidden){
			
			
			
			Log.e("zyf", "message UI is coming,update it...messageItems size: "+messageItems.size());
			
			messageItems.clear();
			
			ArrayList<MessageItem> tempMessageItems=new MessageDatabaseHelperUtil(getActivity()).getMessages(AppConfig.account.getAccountId());
			for(int i=0;i<tempMessageItems.size();i++){
				
				Log.e("zyf", "message last content: "+tempMessageItems.get(i).getLastContent());
				
				messageItems.add(tempMessageItems.get(i));
			}
			
			mBaseAdapter.notifyDataSetChanged();
			
		}else{
			//getActivity().unregisterReceiver(mReceiver);
		}
	}
}
