package com.redoct.iclub.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.redoct.iclub.R;
import com.redoct.iclub.item.ActivityItem;
import com.redoct.iclub.ui.activity.ActivityDetaisActivity;
import com.redoct.iclub.ui.activity.SplashActivity;

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
		.showStubImage(R.drawable.ic_launcher)
		.showImageForEmptyUri(R.drawable.ic_launcher)
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
		
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_activities_listview, parent, false);
            holder = new ViewHolder();
            holder.mTitleTv = (TextView) convertView.findViewById(R.id.mTitleTv);
            holder.mStartTimeTv= (TextView) convertView.findViewById(R.id.mStartTimeTv);
            holder.mLeaderNameTv= (TextView) convertView.findViewById(R.id.mLeaderNameTv);
            holder.mNameTv= (TextView) convertView.findViewById(R.id.mNameTv);
            holder.mLocDescTv= (TextView) convertView.findViewById(R.id.mLocDescTv);
            
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
        
        holder.mContentContainer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				Intent intent=new Intent(mContext,ActivityDetaisActivity.class);
				intent.putExtra("id", item.getId());
				intent.putExtra("leaderName", item.getLeaderName());
				mContext.startActivity(intent);
			}
		});
        
		return convertView;
	}
	
	class ViewHolder {
        TextView mTitleTv;
        TextView mStartTimeTv;
        TextView mLeaderNameTv;
        TextView mNameTv;
        TextView mLocDescTv;
        
        ImageView mLeaderAvatarView;
        
        LinearLayout mLocDescContainer;
        LinearLayout mContentContainer;
    }

}
