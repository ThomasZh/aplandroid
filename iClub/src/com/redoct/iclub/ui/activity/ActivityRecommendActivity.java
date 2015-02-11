package com.redoct.iclub.ui.activity;

import java.util.ArrayList;

import com.redoct.iclub.R;
import com.redoct.iclub.adapter.ActivityRecommendAdapter;
import com.redoct.iclub.item.ContactItem;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_activity_recommend);
		
		initTitle();
		
		
		contactItems=(ArrayList<ContactItem>) getIntent().getSerializableExtra("ContactItems");
		Log.i("zyf",contactItems.size()+"");

		
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

			finish();
			
			break;

		default:
			break;
		}
	}
}
