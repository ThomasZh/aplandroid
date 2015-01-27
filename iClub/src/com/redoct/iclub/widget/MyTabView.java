package com.redoct.iclub.widget;

import com.redoct.iclub.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyTabView extends LinearLayout{
	
	private LayoutInflater mInflater;
	private Context mContext;
	
	private int optionsBgNormal[];
	
	private int optionsBgActive[];
	
	private int OPTION_NUM=4;
	private ImageView [] optionImageViews;
	private TextView [] optionTextViews;
	private LinearLayout [] optionContainers;
	
	public interface MyOnTabClickLister
	{
		void OnTabClick(int choice);
	};
	
	private MyOnTabClickLister mOnTabClickLister;
	
	public void setOnTabClickListener(MyOnTabClickLister onTabClickLister)
	{
		mOnTabClickLister=onTabClickLister;
	}

	public MyTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext=context;
		mInflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

	}
	
	public void setDatas(int []iconNormalIds,int []iconActiveIds){
		optionsBgNormal=iconNormalIds;
		optionsBgActive=iconActiveIds;
		
		initViews();
	}
	
	private void initViews(){
		
		View contentView=mInflater.inflate(R.layout.custom_my_tabview, this);//must write this
		
		optionImageViews=new ImageView[OPTION_NUM];
		optionTextViews=new TextView[OPTION_NUM];
		optionContainers=new LinearLayout[OPTION_NUM];
		
		optionImageViews[0]=(ImageView)contentView.findViewById(R.id.mIcon0ImageView);
		optionImageViews[1]=(ImageView)contentView.findViewById(R.id.mIcon1ImageView);
		optionImageViews[2]=(ImageView)contentView.findViewById(R.id.mIcon2ImageView);
		optionImageViews[3]=(ImageView)contentView.findViewById(R.id.mIcon3ImageView);
		
		optionTextViews[0]=(TextView)contentView.findViewById(R.id.mOptionName0Tv);
		optionTextViews[1]=(TextView)contentView.findViewById(R.id.mOptionName1Tv);
		optionTextViews[2]=(TextView)contentView.findViewById(R.id.mOptionName2Tv);
		optionTextViews[3]=(TextView)contentView.findViewById(R.id.mOptionName3Tv);
		
		optionContainers[0]=(LinearLayout)contentView.findViewById(R.id.mContainer0Layout);
		optionContainers[1]=(LinearLayout)contentView.findViewById(R.id.mContainer1Layout);
		optionContainers[2]=(LinearLayout)contentView.findViewById(R.id.mContainer2Layout);
		optionContainers[3]=(LinearLayout)contentView.findViewById(R.id.mContainer3Layout);
		
		optionContainers[0].setOnClickListener(mOnClickListener);
		optionContainers[1].setOnClickListener(mOnClickListener);
		optionContainers[2].setOnClickListener(mOnClickListener);
		optionContainers[3].setOnClickListener(mOnClickListener);
		
		for(int i=0;i<OPTION_NUM;i++){
			optionImageViews[i].setBackgroundResource(optionsBgNormal[i]);
		}
	}
	
    OnClickListener mOnClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			int choice=0;
			switch (view.getId()) {
			case R.id.mContainer0Layout:
				choice=0;
				break;
			case R.id.mContainer1Layout:
				choice=1;		
				break;
			case R.id.mContainer2Layout:
				choice=2;
				break;
			case R.id.mContainer3Layout:
				choice=3;
				break;
			default:
				break;
			}
			
			handleClickEffect(choice);
			
			if(mOnTabClickLister!=null){
				mOnTabClickLister.OnTabClick(choice);;
			}
		}
	};
	
    private void handleClickEffect(int choice){
		
		for(int i=0;i<OPTION_NUM;i++){
			if(i==choice){
				optionImageViews[i].setBackgroundResource(optionsBgActive[i]);
				
				optionTextViews[i].setTextColor(getResources().getColor(R.color.tabview_color_selected));
			}else{
				optionImageViews[i].setBackgroundResource(optionsBgNormal[i]);
				
				optionTextViews[i].setTextColor(getResources().getColor(R.color.tabview_color_normal));
			}
		}
	}
	
	public void setCurItem(int index){
		handleClickEffect(index);
		if(mOnTabClickLister!=null){
			mOnTabClickLister.OnTabClick(index);;
		}
	}

}
