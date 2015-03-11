package com.redoct.iclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.redoct.iclub.fragment.ActivitiesFragment;
import com.redoct.iclub.fragment.MessageFragment;
import com.redoct.iclub.fragment.MyActivitiesFragment;
import com.redoct.iclub.fragment.MySelfFragment;
import com.redoct.iclub.task.MessageCommitTask;
import com.redoct.iclub.task.MessageListTask;
import com.redoct.iclub.util.FileUtils;
import com.redoct.iclub.util.MyProgressDialogUtils;
import com.redoct.iclub.widget.MyTabView;
import com.redoct.iclub.widget.MyTabView.MyOnTabClickLister;

public class MainActivity extends FragmentActivity implements MyOnTabClickLister{
	
	private MyTabView mTabView;
	
	private MyActivitiesFragment mPlansFragment;
	private ActivitiesFragment mActivitiesFragment;
	private MessageFragment mMessageFragment;
	private MySelfFragment mMySelfFragment;
	
	private int mTabviewNormalIcons[]={R.drawable.myplan_normal,R.drawable.activity_normal,R.drawable.message_normal,R.drawable.me_normal};
    private int mTabviewSelectedIcons[]={R.drawable.myplan_selected,R.drawable.activity_selected,R.drawable.message_selected,R.drawable.me_selected};
    
    private int preSelectedIndex=-1;
    
    private MessageListTask mMessageListTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mTabView=(MyTabView)findViewById(R.id.mTabView);
        mTabView.setDatas(mTabviewNormalIcons, mTabviewSelectedIcons);
        mTabView.setOnTabClickListener(this);
        Intent intent = new Intent();
        mTabView.setCurItem(0);
        
        mMessageListTask=new MessageListTask(){

			@Override
			public void before() {
				// TODO Auto-generated method stub
				super.before();
			}

			@Override
			public void callback() {
				// TODO Auto-generated method stub
				super.callback();
				
				Log.e("zyf", "message list size: "+mMessageListTask.getMessageList().size());
				
				if(mMessageListTask.getMessageList().size()>0){
					
					mTabView.setUnreadMessageNum(mMessageListTask.getMessageList().size());
				}
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
			}

			@Override
			public void timeout() {
				// TODO Auto-generated method stub
				super.timeout();
			}
			
		};
		mMessageListTask.setTimeOutEnabled(true, 10*1000);
		mMessageListTask.safeExecute();
    }

	@Override
	public void OnTabClick(int choice) {
		
		if(preSelectedIndex==choice){
			Log.e("zyf","click repeatly...");
			return;
		}
		
		preSelectedIndex=choice;
		
		FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
		
		hideFragments(transaction);
		
		switch (choice) {
		case 0:
			if(mPlansFragment==null){
				mPlansFragment=new MyActivitiesFragment();
				
				transaction.add(R.id.contentLayout, mPlansFragment);  
			}else{
				transaction.show(mPlansFragment);
			}
			break;
		case 1:
			if(mActivitiesFragment==null){
				mActivitiesFragment=new ActivitiesFragment();
				
				transaction.add(R.id.contentLayout, mActivitiesFragment);  
			}else{
				transaction.show(mActivitiesFragment);
			}		
			break;
		case 2:
			if(mMessageFragment==null){
				mMessageFragment=new MessageFragment(){

					@Override
					public void updateUnreadMessageNum() {
						// TODO Auto-generated method stub
						super.updateUnreadMessageNum();
						
						Log.e("zyf", "make unread message num gone......");
						
						mTabView.setUnreadMessageNumGone();
					}
				};
				
				transaction.add(R.id.contentLayout, mMessageFragment);  
			}else{
				transaction.show(mMessageFragment);
			}
			break;
		case 3:
			if(mMySelfFragment==null){
				mMySelfFragment=new MySelfFragment();
				
				transaction.add(R.id.contentLayout, mMySelfFragment);  
			}else{
				transaction.show(mMySelfFragment);
			}
			break;
		default:
			break;
		}
		
		transaction.commit();
	}
	
	private void hideFragments(FragmentTransaction transaction){
		
		if(mPlansFragment!=null){
			transaction.hide(mPlansFragment);
		}
		
		if(mActivitiesFragment!=null){
			transaction.hide(mActivitiesFragment);
		}
		
		if(mMessageFragment!=null){
			transaction.hide(mMessageFragment);
		}
		
		if(mMySelfFragment!=null){
			transaction.hide(mMySelfFragment);
		}
	}
    
}
