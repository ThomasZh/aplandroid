package com.redoct.iclub.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.f;
import com.redoct.iclub.R;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.adapter.ThemeSelectBaseAdapter;
import com.redoct.iclub.util.activity.BaseActivityUtil;

public class ThemeSelectActivity extends Activity {
	
	private RelativeLayout mTitleContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_theme_select);
		
		initTitle();
		
		mTitleContainer=(RelativeLayout)findViewById(R.id.mTitleContainer);
	}
	
	private GridView mPopGridView;
	
	private LinearLayout mPopOtherContainer;
	
	private PopupWindow mPopupWindow;
	
	private View mPopContentView;
	
	private ThemeSelectBaseAdapter mThemeSelectBaseAdapter;
	
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
				
				LayoutInflater mLayoutInflater=LayoutInflater.from(ThemeSelectActivity.this);
				mPopContentView=mLayoutInflater.inflate(R.layout.popupwindow_theme_second, null);
				mPopOtherContainer=(LinearLayout)mPopContentView.findViewById(R.id.mPopOtherContainer);
				mPopOtherContainer.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						mPopupWindow.dismiss();
						mPopupWindow=null;
						mPopGridView=null;
						mThemeSelectBaseAdapter=null;
						mPopContentView=null;
					}
				});
				mPopGridView=(GridView)mPopContentView.findViewById(R.id.mPopGridView);
				
				//if(mThemeSelectBaseAdapter==null){
				mThemeSelectBaseAdapter=new ThemeSelectBaseAdapter(ThemeSelectActivity.this, count,null, null);
				mPopGridView.setAdapter(mThemeSelectBaseAdapter);
				mPopupWindow=new PopupWindow(mPopContentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
				
				Log.e("zyf", "popup window is null.......");
				/*}else{
					mThemeSelectBaseAdapter.notifyDataSetChanged();
				}*/

				mPopupWindow.showAsDropDown(mTitleContainer);
				
				count--;
			}
		});
	}
}
