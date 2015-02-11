package com.redoct.iclub.adapter;

import java.util.ArrayList;

import com.oct.ga.comm.GlobalArgs;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.ActivitiesBaseAdapter.ViewHolder;
import com.redoct.iclub.item.ActivityItem;
import com.redoct.iclub.ui.activity.ActivityDetailsActivity;
import com.redoct.iclub.util.Constant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ClubActivitiesBaseAdapter extends BaseAdapter {
	
    private ArrayList<ActivityItem> activityItems;
	
	private Activity mContext;
	
	private LayoutInflater inflater;

	public ClubActivitiesBaseAdapter(ArrayList<ActivityItem> activityItems,
			Activity mContext) {
		super();
		this.activityItems = activityItems;
		this.mContext = mContext;
		
		inflater=LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return activityItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		final ActivityItem item=activityItems.get(position);
		
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_club_activties_listview, parent, false);
            holder = new ViewHolder();
            holder.mTitleTv = (TextView) convertView.findViewById(R.id.mTitleTv);
            holder.mStartTimeTv= (TextView) convertView.findViewById(R.id.mStartTimeTv);
            holder.mNameTv= (TextView) convertView.findViewById(R.id.mNameTv);
            
            holder.mContentContainer=(RelativeLayout) convertView.findViewById(R.id.mContentContainer);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.mStartTimeTv.setText(item.getStartTime());
        holder.mNameTv.setText(item.getName());
        
        if(activityItems.get(position).isTitle()){
        	holder.mTitleTv.setVisibility(View.VISIBLE);
        	
        	holder.mTitleTv.setText(item.getStartDate());
        }else{
        	holder.mTitleTv.setVisibility(View.GONE);
        }
        
        
        holder.mContentContainer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				Intent intent=new Intent(mContext,ActivityDetailsActivity.class);
				intent.putExtra("id", item.getId());
				intent.putExtra("leaderName", item.getLeaderName());
				intent.putExtra("leaderAvatarUrl", item.getLeaderAvatarUrl());
				((Activity)mContext).startActivityForResult(intent, Constant.RESULT_CODE_ACTIVITY_CREATE);
			}
		});
        
		return convertView;
	}
	
	class ViewHolder {
        TextView mTitleTv;
        TextView mStartTimeTv;
        TextView mNameTv;
        
        RelativeLayout mContentContainer;
    }

}
