package com.redoct.iclub.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.redoct.iclub.R;
import com.redoct.iclub.item.MessageItem;
import com.redoct.iclub.util.UserInformationLocalManagerUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatMessageReceiveAdapter extends BaseAdapter {
	
	private ArrayList<MessageItem> mMessageItems;
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	private Context mContext;
	
	private LayoutInflater inflater;

	public ChatMessageReceiveAdapter(ArrayList<MessageItem> mMessageItems,
			Context mContext) {
		super();
		this.mMessageItems = mMessageItems;
		this.mContext = mContext;
		
		inflater=LayoutInflater.from(mContext);
		
		mImageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.me_normal)
				.showImageForEmptyUri(R.drawable.me_normal)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(10)).build();
	

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mMessageItems.size();
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
		final MessageItem item=mMessageItems.get(position);
		
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_chat_receive_message, parent, false);
            holder = new ViewHolder();
            holder.mContentTv= (TextView) convertView.findViewById(R.id.msgcontent);              
            holder.mHeadImge  = (ImageView) convertView.findViewById(R.id.iv_chat_headimg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.mContentTv.setText(item.getLastContent());
        mImageLoader.displayImage(item.getUserAvatarUrl(), holder.mHeadImge, options);
		return convertView;
	}
	
	class ViewHolder {
        TextView mContentTv;
        ImageView  mHeadImge;
    }

}
