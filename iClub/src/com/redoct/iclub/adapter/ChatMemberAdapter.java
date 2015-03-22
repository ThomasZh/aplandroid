package com.redoct.iclub.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.ActivitiesBaseAdapter.ViewHolder;
import com.redoct.iclub.item.ActivityItem;
import com.redoct.iclub.item.MemberItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatMemberAdapter extends BaseAdapter {
	
	private ArrayList<MemberItem> mMemberItems;
	
	private Context mContext;
	
	private LayoutInflater inflater;
	
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;

	public ChatMemberAdapter(ArrayList<MemberItem> mMemberItems,
			Context mContext) {
		super();
		this.mMemberItems = mMemberItems;
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

		return mMemberItems.size();
	}

	@Override
	public Object getItem(int position) {

		return position;
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		final MemberItem item=mMemberItems.get(position);
		
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_chat_member, parent, false);
            holder = new ViewHolder();
            holder.mNameTv= (TextView) convertView.findViewById(R.id.mNameTv);
            
            holder.mLeaderAvatarView= (ImageView) convertView.findViewById(R.id.mLeaderAvatarView);               

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.mNameTv.setText(item.getUserName());
        mImageLoader.displayImage(item.getImageUrl(), holder.mLeaderAvatarView, options);
        
		return convertView;
	}
	
	class ViewHolder {
        TextView mNameTv;
        ImageView mLeaderAvatarView;
    }

}
