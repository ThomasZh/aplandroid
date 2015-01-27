package com.redoct.iclub;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.redoct.iclub.fragment.ActivitiesFragment;
import com.redoct.iclub.fragment.InvitationFragment;
import com.redoct.iclub.fragment.MyPlansFragment;
import com.redoct.iclub.fragment.MySelfFragment;
import com.redoct.iclub.widget.MyTabView;
import com.redoct.iclub.widget.MyTabView.MyOnTabClickLister;

public class MainActivity extends FragmentActivity implements MyOnTabClickLister{
	
	private MyTabView mTabView;
	
	private MyPlansFragment mPlansFragment;
	private ActivitiesFragment mActivitiesFragment;
	private InvitationFragment mInvitationFragment;
	private MySelfFragment mMySelfFragment;
	
	private int mTabviewNormalIcons[]={R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher};
    private int mTabviewSelectedIcons[]={R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher};
    
    private int preSelectedIndex=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mTabView=(MyTabView)findViewById(R.id.mTabView);
        mTabView.setDatas(mTabviewNormalIcons, mTabviewSelectedIcons);
        mTabView.setOnTabClickListener(this);
        
        mTabView.setCurItem(0);
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
				mPlansFragment=new MyPlansFragment();
				
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
			if(mInvitationFragment==null){
				mInvitationFragment=new InvitationFragment();
				
				transaction.add(R.id.contentLayout, mInvitationFragment);  
			}else{
				transaction.show(mInvitationFragment);
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
		
		if(mInvitationFragment!=null){
			transaction.hide(mInvitationFragment);
		}
		
		if(mMySelfFragment!=null){
			transaction.hide(mMySelfFragment);
		}
	}
    
}
