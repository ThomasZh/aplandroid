package com.redoct.iclub.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.oct.ga.comm.GlobalArgs;
import com.redoct.iclub.R;
import com.redoct.iclub.item.ActivityItem;
import com.redoct.iclub.ui.activity.ActivityDetailsActivity;
import com.redoct.iclub.ui.activity.SplashActivity;
import com.redoct.iclub.util.Constant;

public class ActivitiesBaseAdapter extends BaseAdapter {
	
	private ArrayList<ActivityItem> activityItems;
	
	private Context mContext;
	
	private LayoutInflater inflater;
	
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;

	public ActivitiesBaseAdapter(Context mContext,ArrayList<ActivityItem> activityItems) {
		super();
		this.mContext = mContext;
		this.activityItems=activityItems;
		
		inflater=LayoutInflater.from(mContext);
		
		mImageLoader=ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.me_normal)
		.showImageForEmptyUri(R.drawable.me_normal)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new RoundedBitmapDisplayer(100)) 
		.build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return activityItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		final ActivityItem item=activityItems.get(position);
		final int pos=position;
		
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_activities_listview, parent, false);
            holder = new ViewHolder();
            holder.mTitleTv = (TextView) convertView.findViewById(R.id.mTitleTv);
            holder.mStartTimeTv= (TextView) convertView.findViewById(R.id.mStartTimeTv);
            holder.mLeaderNameTv= (TextView) convertView.findViewById(R.id.mLeaderNameTv);
            holder.mNameTv= (TextView) convertView.findViewById(R.id.mNameTv);
            holder.mLocDescTv= (TextView) convertView.findViewById(R.id.mLocDescTv);
            holder.mMemberNumTv=(TextView) convertView.findViewById(R.id.mMemberNumTv);
            holder.mStateTv=(TextView) convertView.findViewById(R.id.mStateTv);
            
            holder.mLeaderAvatarView= (ImageView) convertView.findViewById(R.id.mLeaderAvatarView);
            
            holder.mLocDescContainer= (LinearLayout)convertView.findViewById(R.id.mLocDescContainer);
            holder.mContentContainer= (LinearLayout)convertView.findViewById(R.id.mContentContainer);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.mLeaderNameTv.setText(item.getLeaderName());
        holder.mStartTimeTv.setText(item.getStartTime());
        holder.mNameTv.setText(item.getName());
        holder.mMemberNumTv.setText(mContext.getString(R.string.invitation)+item.getMemberNum()+mContext.getString(R.string.people));
        
        
        if(activityItems.get(position).isTitle()){
        	holder.mTitleTv.setVisibility(View.VISIBLE);
        	
        	holder.mTitleTv.setText(item.getStartDate());
        }else{
        	holder.mTitleTv.setVisibility(View.GONE);
        }
        
        if(item.getLocDesc()!=null&&item.getLocDesc().length()>0){
        	
        	holder.mLocDescContainer.setVisibility(View.VISIBLE);
        	holder.mLocDescTv.setText(item.getLocDesc());
        }else {
			holder.mLocDescContainer.setVisibility(View.GONE);
		}
        
        mImageLoader.displayImage(item.getLeaderAvatarUrl(), holder.mLeaderAvatarView, options);
        
        short state=Short.parseShort(item.getState());
        if(state==GlobalArgs.CLUB_ACTIVITY_STATE_CANCELED){  //活动已取消
        	
        	holder.mStateTv.setTextColor(Color.GRAY);
        	holder.mStateTv.setText(mContext.getResources().getString(R.string.activity_canceled));
        	
        }else if(state==GlobalArgs.CLUB_ACTIVITY_STATE_COMPLETED){ //活动已完成
        	
        	holder.mStateTv.setTextColor(Color.GRAY);
        	holder.mStateTv.setText(mContext.getResources().getString(R.string.activity_competed));
        	
        }else if(state==GlobalArgs.CLUB_ACTIVITY_STATE_OPENING){
        	
        	holder.mStateTv.setTextColor(Color.parseColor("#a2a2a2"));
        	
        	short memberRank=item.getMemberRank();
        	
        	if(memberRank==GlobalArgs.MEMBER_RANK_NONE){   //未参加
        		
        		holder.mStateTv.setText(mContext.getResources().getString(R.string.activity_not_joined));
        	}else{   //已参加
        		
        		holder.mStateTv.setText(mContext.getResources().getString(R.string.activity_joined));
        	}
        }
        
        holder.mContentContainer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				/*Intent intent=new Intent(mContext,ActivityDetailsActivity.class);
				intent.putExtra("position", pos);
				intent.putExtra("id", item.getId());
				intent.putExtra("leaderName", item.getLeaderName());
				intent.putExtra("leaderAvatarUrl", item.getLeaderAvatarUrl());
				
				((Activity)mContext).startActivityForResult(intent, Constant.RESULT_CODE_ACTIVITY_READ);*/
				
				gotoDetails(item,pos);
			}
		});
        
		return convertView;
	}
	
	public void gotoDetails(ActivityItem item,int position){
		
	}
	
	class ViewHolder {
        TextView mTitleTv;
        TextView mStartTimeTv;
        TextView mLeaderNameTv;
        TextView mNameTv;
        TextView mLocDescTv;
        TextView mMemberNumTv;
        TextView mStateTv;
        
        ImageView mLeaderAvatarView;
        
        LinearLayout mLocDescContainer;
        LinearLayout mContentContainer;
    }

}
