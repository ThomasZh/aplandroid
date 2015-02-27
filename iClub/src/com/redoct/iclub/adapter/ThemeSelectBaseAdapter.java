package com.redoct.iclub.adapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.redoct.iclub.R;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ThemeSelectBaseAdapter extends BaseAdapter {
	
	private Context mContext;
	
	private LayoutInflater mInflater;
	
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	
	private String [] iconUrls;
	
	private String [] iconTitles;

	public ThemeSelectBaseAdapter(Context mContext, String[] iconUrls,
			String[] iconTitles) {
		super();
		this.mContext = mContext;
		this.iconUrls = iconUrls;
		this.iconTitles = iconTitles;
		
		mInflater=LayoutInflater.from(mContext);
		
		mImageLoader=ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.icon_default)
		.showImageForEmptyUri(R.drawable.icon_default)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new RoundedBitmapDisplayer(100)) 
		.build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return iconTitles.length;
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
		
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.item_theme_select_pop_gridview, null);
		}
		
		ImageView mImageView=(ImageView) convertView.findViewById(R.id.mImageView);
		TextView mTitleTv=(TextView) convertView.findViewById(R.id.mTitleTv);
		
		mTitleTv.setText(iconTitles[position]);
		
		mImageLoader.displayImage(iconUrls[position], mImageView, options);
		
		return convertView;
	}

}
