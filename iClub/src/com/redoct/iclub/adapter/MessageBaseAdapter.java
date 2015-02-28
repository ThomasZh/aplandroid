package com.redoct.iclub.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.oct.ga.comm.GlobalArgs;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.ActivitiesBaseAdapter.ViewHolder;
import com.redoct.iclub.item.ActivityItem;
import com.redoct.iclub.item.MessageItem;
import com.redoct.iclub.ui.activity.ActivityDetailsActivity;
import com.redoct.iclub.util.DateUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageBaseAdapter extends BaseAdapter {
	
	private ArrayList<MessageItem> messageItems;
	
	private Context mContext;
	
	private LayoutInflater inflater;
	
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;

	public MessageBaseAdapter(ArrayList<MessageItem> messageItems,
			Context mContext) {
		super();
		this.messageItems = messageItems;
		this.mContext = mContext;
		
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
		return messageItems.size();
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
		// TODO Auto-generated method stub
		
		ViewHolder holder;
		final MessageItem item=messageItems.get(position);
		final int pos=position;
		
		if(item.getUserName()==null){
			item.setUserName("");
		}
		
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_message_listview, parent, false);
            holder = new ViewHolder();
            holder.mNameTv = (TextView) convertView.findViewById(R.id.mNameTv);
            holder.mTimeTv= (TextView) convertView.findViewById(R.id.mTimeTv);
            holder.mAcceptBtn= (Button) convertView.findViewById(R.id.mAcceptBtn);
            holder.mInfoTv= (TextView) convertView.findViewById(R.id.mInfoTv);
            
            holder.mLeaderAvatarView= (ImageView) convertView.findViewById(R.id.mLeaderAvatarView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        if(!item.isFeedback()){ //别人对自己的邀请
        	
        	holder.mAcceptBtn.setVisibility(View.VISIBLE);
        	
        	holder.mInfoTv.setText(item.getUserName()+"希望成为您的好友");
        	
        	if(!item.isAccept()){   //尚未同意请求
        		
        		holder.mAcceptBtn.setText("接受");
        		holder.mAcceptBtn.setClickable(true);
        		holder.mAcceptBtn.setOnClickListener(new OnClickListener() {
        			
        			@Override
        			public void onClick(View v) {
        				
        				Log.e("zyf", "accept name: "+item.getUserName());
        				accept(item.getInviteId(),pos);
        			}
        		});
        	}else{
        		
        		holder.mAcceptBtn.setClickable(false);
        		holder.mAcceptBtn.setText("已接受");
        	}
        	
        }else{  //别人对自己邀请的回复
        	
        	holder.mAcceptBtn.setVisibility(View.GONE);
        	
        	holder.mInfoTv.setText(item.getUserName()+"接受了您的邀请");
        }
        
        mImageLoader.displayImage(item.getUserAvatarUrl(), holder.mLeaderAvatarView, options);
        
        holder.mNameTv.setText(item.getUserName());
        holder.mTimeTv.setText(DateUtils.getMessageFormatDate(item.getTimestamp()));
  
		return convertView;
	}
	
	public void accept(String id,int position){
		
	}
	
	class ViewHolder {
		
        TextView mTimeTv;
        TextView mNameTv;
        TextView mInfoTv;
        
        Button mAcceptBtn;
        
        ImageView mLeaderAvatarView;
    }

}
