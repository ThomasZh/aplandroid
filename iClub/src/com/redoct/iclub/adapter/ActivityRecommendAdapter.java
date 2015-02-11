package com.redoct.iclub.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.MessageBaseAdapter.ViewHolder;
import com.redoct.iclub.item.ContactItem;
import com.redoct.iclub.item.MessageItem;
import com.redoct.iclub.util.DateUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityRecommendAdapter extends BaseAdapter {
	
	private Context mContext;
	
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	
	private ArrayList<ContactItem> contactItems;

	public ActivityRecommendAdapter(Context mContext,ArrayList<ContactItem> contactItems) {
		super();
		this.mContext = mContext;
		this.contactItems=contactItems;
		
		mInflater = LayoutInflater.from(mContext);
		
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
		
		if(contactItems!=null){
			return contactItems.size();
		}
		
		return 0;
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
		
		ContactItem item=contactItems.get(position);
		
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_activity_recommend_listview, parent, false);
            holder = new ViewHolder();
            holder.mNameTv = (TextView) convertView.findViewById(R.id.mNameTv);
            holder.mAvatarView= (ImageView) convertView.findViewById(R.id.mAvatarView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.mNameTv.setText(item.getName());
        mImageLoader.displayImage(item.getImageUrl(), holder.mAvatarView, options);
  
		return convertView;
	}
	
	class ViewHolder {
		
        TextView mNameTv;
        
        ImageView mAvatarView;
    }

}
