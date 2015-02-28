package com.redoct.iclub.fragment;

import java.util.ArrayList;

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
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.MessageBaseAdapter;
import com.redoct.iclub.item.MessageItem;
import com.redoct.iclub.task.InviteFeedbackTask;
import com.redoct.iclub.task.MessageCommitTask;
import com.redoct.iclub.task.MessageListTask;
import com.redoct.iclub.util.FileUtils;
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View contentView=inflater.inflate(R.layout.fragment_message, container, false);
		
		messageItems=FileUtils.readMessageHistory();
		
		initViews(contentView);
		
		Log.e("zyf:", "account id: "+new UserInformationLocalManagerUtil(getActivity()).ReadUserInformation().getId());
		
		load();
		
		return contentView;
	}
	
	public void updateUnreadMessageNum(){
		
	}
	
	private void initViews(View convertView){
		
		TextView mTitleView=(TextView) convertView.findViewById(R.id.mTitleView);
		mTitleView.setText(getResources().getString(R.string.message));
		
		mPullToRefreshListView=(PullToRefreshListView)convertView.findViewById(R.id.mPullToRefreshListView);
		mPullToRefreshListView.setMode(Mode.PULL_FROM_START);
		mBaseAdapter=new MessageBaseAdapter(messageItems, getActivity()){

			@Override
			public void accept(String id,int position) {
				
				short feedbackState=GlobalArgs.INVITE_STATE_ACCPET;
				
				Log.e("zyf", "accept pos: "+position);
				
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
						item.setAccept(true);
						
						FileUtils.updateMessageHistory(messageItems);
						
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
				
				if(mBaseAdapter!=null){
					mBaseAdapter.notifyDataSetChanged();
				}
				
				break;

			default:
				break;
			}
		}
		
	};
	
	private void load(){
		
		mMessageListTask=new MessageListTask(){

			@Override
			public void before() {
				// TODO Auto-generated method stub
				super.before();
				
				mProgressDialogUtils=new MyProgressDialogUtils(R.string.progress_dialog_loading, getActivity());
				mProgressDialogUtils.showDialog();
			}

			@Override
			public void callback() {
				// TODO Auto-generated method stub
				super.callback();
				
				Log.e("zyf", "message list size: "+mMessageListTask.getMessageList().size());
				
				//messageItems.clear();
				
				for(int i=0;i<mMessageListTask.getMessageList().size();i++){
					messageItems.add(mMessageListTask.getMessageList().get(i));
				}
				
				inviteIds=mMessageListTask.getInviteIds();
				inviteFeedbackIds=mMessageListTask.getInviteFeedIds();
				
				if(inviteIds!=null)
					Log.e("zyf", "call back inviteIds length: "+inviteIds.length);
				
				if(inviteFeedbackIds!=null)
					Log.e("zyf", "call back inviteFeedbackIds length: "+inviteFeedbackIds.length);
				
				FileUtils.updateMessageHistory(messageItems);
				
				mPullToRefreshListView.onRefreshComplete();
				mBaseAdapter.notifyDataSetChanged();
				
				updateUnreadMessageNum();
				
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
		mMessageListTask.setTimeOutEnabled(true, 10*1000);
		mMessageListTask.safeExecute();
		
		mMessageListTask.then(messageCommitRunnable);
	}
	
	Runnable messageCommitRunnable=new Runnable() {
		
		@Override
		public void run() {
			
			MessageCommitTask messageCommitTask=new MessageCommitTask(inviteIds, inviteFeedbackIds);
			messageCommitTask.setTimeOutEnabled(true, 10*1000);
			messageCommitTask.safeExecute();
		}
	};
}
