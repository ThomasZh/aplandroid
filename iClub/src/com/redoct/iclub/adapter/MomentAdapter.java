package com.redoct.iclub.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.redoct.iclub.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class MomentAdapter extends BaseAdapter {
	
	private Context mContext;
	
	private LayoutInflater mInflater;
	
	private ArrayList<String> imageUrls;
	
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;

	public MomentAdapter(Context mContext, ArrayList<String> imageUrls) {
		super();
		this.mContext = mContext;
		this.imageUrls = imageUrls;
		
		mInflater=LayoutInflater.from(mContext);
		
		mImageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				/*.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)*/
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(10)).build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageUrls.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(convertView==null){
			
			convertView=mInflater.inflate(R.layout.item_activity_details_gallery, parent,false);
		}
		
		ImageView image=(ImageView)convertView.findViewById(R.id.image);
		//image.setBackgroundResource(R.drawable.test);
		
		mImageLoader.displayImage(imageUrls.get(position), image, options);
		
		return convertView;
	}

}
