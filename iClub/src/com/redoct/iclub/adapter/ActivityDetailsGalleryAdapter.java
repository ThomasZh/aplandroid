package com.redoct.iclub.adapter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.redoct.iclub.R;
import com.redoct.iclub.item.MemberItem;

public class ActivityDetailsGalleryAdapter extends BaseAdapter {
	
	private Context mContext;
	
	private LayoutInflater mInflater;

	
	private ImageLoader mImageLoader;
	
	private DisplayImageOptions options;
	
	private ArrayList<MemberItem> mMemberItems;
	

	public ActivityDetailsGalleryAdapter(Context mContext, ArrayList<MemberItem> mMemberItems,
			ImageLoader mImageLoader,DisplayImageOptions options) {
		super();
		this.mContext = mContext;
		this.mMemberItems = mMemberItems;
		this.mImageLoader=mImageLoader;
		this.options=options;
		
		mInflater=LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mMemberItems==null){
			return 0;
		}else{
			Log.e("zyf", "gallery adapter size: "+mMemberItems.size());
			return mMemberItems.size();
		}
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
		
		ImageView imageView = (ImageView) convertView;
		
		MemberItem item=mMemberItems.get(position);
		
		try {
			
			if (imageView == null) {
				imageView = (ImageView) mInflater.inflate(R.layout.item_activity_details_gallery, parent, false);
			}
			mImageLoader.displayImage(item.getImageUrl(), imageView, options);
			
			Log.e("zyf", "gallery get view......");
			
		} catch (Exception e) {
			
			Log.e("zyf", "gallery get view exception: "+e.toString());
		}
		
		
		return imageView;
	}

}
