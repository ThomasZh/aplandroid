package com.redoct.iclub.ui.activity;

import java.util.ArrayList;

import com.redoct.iclub.R;
import com.redoct.iclub.adapter.ActivityRecommendAdapter;
import com.redoct.iclub.item.ContactItem;
import com.redoct.iclub.task.ActivityRecommendTask;
import com.redoct.iclub.util.MyProgressDialogUtils;
import com.redoct.iclub.widget.MyToast;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityRecommendActivity extends Activity implements OnClickListener{
	
	private EditText mEditText;
	
	private ListView mListView;
	
	private ArrayList<ContactItem> contactItems;
	
	private ActivityRecommendAdapter mActivityRecommendAdapter;
	
	private String activityId;
	
	private String [] userIds;
	
	private ActivityRecommendTask mActivityRecommendTask;
	
	private MyProgressDialogUtils mProgressDialogUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_activity_recommend);
		
		initTitle();
		
		contactItems=(ArrayList<ContactItem>) getIntent().getSerializableExtra("ContactItems");
		activityId=getIntent().getStringExtra("activityId");
		
		initViews();
	}
	
	private void initTitle(){
		
		TextView mTitleView=(TextView) findViewById(R.id.mTitleView);
		mTitleView.setText(getResources().getString(R.string.invitation));
		
		Button leftBtn=(Button) findViewById(R.id.leftBtn);
		leftBtn.setBackgroundResource(R.drawable.title_back);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setOnClickListener(this);
		
		Button rightBtn=(Button) findViewById(R.id.rightBtn);
		rightBtn.setText(getResources().getString(R.string.complete));
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setOnClickListener(this);
	}
	
	private void initViews(){
		
		mEditText=(EditText)findViewById(R.id.mEditText);
		
		mListView=(ListView)findViewById(R.id.mListView);
		mActivityRecommendAdapter=new ActivityRecommendAdapter(this, contactItems);
		mListView.setAdapter(mActivityRecommendAdapter);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.leftBtn:

			finish();
			
			break;
		case R.id.rightBtn:
			
			if(contactItems==null)
				return;
			
			userIds=new String[contactItems.size()];
			for(int i=0;i<contactItems.size();i++){
				userIds[i]=contactItems.get(i).getId();
			}
			
			mActivityRecommendTask=new ActivityRecommendTask(this, activityId, userIds, mEditText.getText().toString()){

				@Override
				public void before() {
					// TODO Auto-generated method stub
					super.before();
					
					mProgressDialogUtils=new MyProgressDialogUtils(R.string.progress_dialog_activity_recommending, ActivityRecommendActivity.this);
					mProgressDialogUtils.showDialog();
				}

				@Override
				public void callback() {
					// TODO Auto-generated method stub
					super.callback();
					
					MyToast.makeText(ActivityRecommendActivity.this, true, R.string.activity_recommend_success, MyToast.LENGTH_SHORT).show();
					
					ActivityRecommendActivity.this.finish();
				}

				@Override
				public void failure() {
					// TODO Auto-generated method stub
					super.failure();
					
					MyToast.makeText(ActivityRecommendActivity.this, true, R.string.activity_recommend_failed, MyToast.LENGTH_SHORT).show();
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
					
					MyToast.makeText(ActivityRecommendActivity.this, true, R.string.activity_recommend_failed, MyToast.LENGTH_SHORT).show();
				}
				
			};
			mActivityRecommendTask.setTimeOutEnabled(true, 10*1000);
			mActivityRecommendTask.safeExecute();
			
			break;

		default:
			break;
		}
	}
}
