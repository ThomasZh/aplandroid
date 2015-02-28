package com.redoct.iclub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.redoct.iclub.R;
import com.redoct.iclub.adapter.ThemeSelectBaseAdapter;
import com.redoct.iclub.util.Constant;

public class ThemeSelectActivity extends Activity implements OnClickListener{
	
	private RelativeLayout mTitleContainer;
	
	private String [] iconUrls;
	private String [] iconTitles;
	
	private LinearLayout mTheme0Container,mTheme1Container,mTheme2Container,mTheme3Container,mTheme4Container;
	private LinearLayout mTheme5Container,mTheme6Container,mTheme7Container,mTheme8Container,mTheme9Container;
	private LinearLayout mTheme10Container,mTheme11Container,mTheme12Container,mTheme13Container;
	
	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_theme_select);
		
		id=getIntent().getStringExtra("id");
		
		initTitle();
		
		mTitleContainer=(RelativeLayout)findViewById(R.id.mTitleContainer);
		
		mTheme0Container=(LinearLayout)findViewById(R.id.mTheme0Container);
		mTheme1Container=(LinearLayout)findViewById(R.id.mTheme1Container);
		mTheme2Container=(LinearLayout)findViewById(R.id.mTheme2Container);
		mTheme3Container=(LinearLayout)findViewById(R.id.mTheme3Container);
		mTheme4Container=(LinearLayout)findViewById(R.id.mTheme4Container);
		mTheme5Container=(LinearLayout)findViewById(R.id.mTheme5Container);
		mTheme6Container=(LinearLayout)findViewById(R.id.mTheme6Container);
		mTheme7Container=(LinearLayout)findViewById(R.id.mTheme7Container);
		mTheme8Container=(LinearLayout)findViewById(R.id.mTheme8Container);
		mTheme9Container=(LinearLayout)findViewById(R.id.mTheme9Container);
		mTheme10Container=(LinearLayout)findViewById(R.id.mTheme10Container);
		mTheme11Container=(LinearLayout)findViewById(R.id.mTheme11Container);
		mTheme12Container=(LinearLayout)findViewById(R.id.mTheme12Container);
		mTheme13Container=(LinearLayout)findViewById(R.id.mTheme13Container);
		
		mTheme0Container.setOnClickListener(this);
		mTheme1Container.setOnClickListener(this);
		mTheme2Container.setOnClickListener(this);
		mTheme3Container.setOnClickListener(this);
		mTheme4Container.setOnClickListener(this);
		mTheme5Container.setOnClickListener(this);
		mTheme6Container.setOnClickListener(this);
		mTheme7Container.setOnClickListener(this);
		mTheme8Container.setOnClickListener(this);
		mTheme9Container.setOnClickListener(this);
		mTheme10Container.setOnClickListener(this);
		mTheme11Container.setOnClickListener(this);
		mTheme12Container.setOnClickListener(this);
		mTheme13Container.setOnClickListener(this);
		
		mLayoutInflater=LayoutInflater.from(ThemeSelectActivity.this);
		
		mPopContentView=mLayoutInflater.inflate(R.layout.popupwindow_theme_second, null);
		
		mPopGridView=(GridView)mPopContentView.findViewById(R.id.mPopGridView);
		mPopGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mPopGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				Log.e("zyf", "select: "+iconTitles[position]);
				
				mPopupWindow.dismiss();
				
				Intent intent=new Intent(ThemeSelectActivity.this,ActivityCreateActivity.class);
				intent.putExtra("id", id);
				intent.putExtra("theme", iconTitles[position]);
				startActivityForResult(intent, Constant.RESULT_CODE_ACTIVITY_CREATE);
				
				//finish();
			}
		});
		
		mPopOtherContainer=(LinearLayout)mPopContentView.findViewById(R.id.mPopOtherContainer);
		mPopOtherContainer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mPopupWindow.dismiss();
			}
		});

		mPopupWindow=new PopupWindow(mPopContentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	}
	
	private GridView mPopGridView;
	
	private LinearLayout mPopOtherContainer;
	
	private PopupWindow mPopupWindow;
	
	private View mPopContentView;
	
	private ThemeSelectBaseAdapter mThemeSelectBaseAdapter;
	
	private LayoutInflater mLayoutInflater;
	
	private int count=10;
	
	private void initTitle(){
		
		TextView mTitleView=(TextView) findViewById(R.id.mTitleView);
		mTitleView.setText(getResources().getString(R.string.theme));
		
		Button leftBtn=(Button) findViewById(R.id.leftBtn);
		leftBtn.setBackgroundResource(R.drawable.title_back);
		leftBtn.setVisibility(View.VISIBLE);

		leftBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				finish();
			}
		});
	}

	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		
		case R.id.mTheme0Container:
			
			iconTitles=getResources().getStringArray(R.array.category_theme0);
			iconUrls=getResources().getStringArray(R.array.icon_url_theme0);
			break;
		case R.id.mTheme1Container:
			
			iconTitles=getResources().getStringArray(R.array.category_theme1);
			iconUrls=getResources().getStringArray(R.array.icon_url_theme1);
			break;
		case R.id.mTheme2Container:
			
			iconTitles=getResources().getStringArray(R.array.category_theme2);
			iconUrls=getResources().getStringArray(R.array.icon_url_theme2);
			break;
		case R.id.mTheme3Container:
			
			iconTitles=getResources().getStringArray(R.array.category_theme3);
			iconUrls=getResources().getStringArray(R.array.icon_url_theme3);
			break;
		case R.id.mTheme4Container:
			
			iconTitles=getResources().getStringArray(R.array.category_theme4);
			iconUrls=getResources().getStringArray(R.array.icon_url_theme4);
			break;
		case R.id.mTheme5Container:
			
			iconTitles=getResources().getStringArray(R.array.category_theme5);
			iconUrls=getResources().getStringArray(R.array.icon_url_theme5);
			break;
		case R.id.mTheme6Container:
			
			iconTitles=getResources().getStringArray(R.array.category_theme6);
			iconUrls=getResources().getStringArray(R.array.icon_url_theme6);
			break;
		case R.id.mTheme7Container:
			
			iconTitles=getResources().getStringArray(R.array.category_theme7);
			iconUrls=getResources().getStringArray(R.array.icon_url_theme7);
			break;
		case R.id.mTheme8Container:
			
			iconTitles=getResources().getStringArray(R.array.category_theme8);
			iconUrls=getResources().getStringArray(R.array.icon_url_theme8);
			break;
		case R.id.mTheme9Container:
			
			iconTitles=getResources().getStringArray(R.array.category_theme9);
			iconUrls=getResources().getStringArray(R.array.icon_url_theme9);
			break;
		case R.id.mTheme10Container:
			
			iconTitles=getResources().getStringArray(R.array.category_theme10);
			iconUrls=getResources().getStringArray(R.array.icon_url_theme10);
			break;
		case R.id.mTheme11Container:
			
			iconTitles=getResources().getStringArray(R.array.category_theme11);
			iconUrls=getResources().getStringArray(R.array.icon_url_theme11);
			break;
		case R.id.mTheme12Container:
			
			iconTitles=getResources().getStringArray(R.array.category_theme12);
			iconUrls=getResources().getStringArray(R.array.icon_url_theme12);
			break;
		case R.id.mTheme13Container:
			
			iconTitles=getResources().getStringArray(R.array.category_theme13);
			iconUrls=getResources().getStringArray(R.array.icon_url_theme13);
			break;

		default:
			break;
		}
		
		mThemeSelectBaseAdapter=new ThemeSelectBaseAdapter(ThemeSelectActivity.this, iconUrls, iconTitles);
		mPopGridView.setAdapter(mThemeSelectBaseAdapter);
		
		mPopupWindow.showAsDropDown(mTitleContainer);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode==Constant.RESULT_CODE_ACTIVITY_CREATE){
			
			if(data!=null&&data.getBooleanExtra("ActivityCreateSuccess", false)){
				Intent intent=new Intent();
	        	intent.putExtra("ActivityCreateSuccess", true);
	        	setResult(Constant.RESULT_CODE_ACTIVITY_CREATE, intent);
	        	finish();
			}
		}
	}
	
}
