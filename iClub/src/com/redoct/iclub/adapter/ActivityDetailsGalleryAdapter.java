package com.redoct.iclub.adapter;

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

public class ActivityDetailsGalleryAdapter extends BaseAdapter {
	
	private Context mContext;
	
	private LayoutInflater mInflater;
	
	private JSONArray mJsonArray;
	
	private ImageLoader mImageLoader;
	
	private DisplayImageOptions options;
	

	public ActivityDetailsGalleryAdapter(Context mContext, JSONArray mJsonArray,
			ImageLoader mImageLoader,DisplayImageOptions options) {
		super();
		this.mContext = mContext;
		this.mJsonArray = mJsonArray;
		this.mImageLoader=mImageLoader;
		this.options=options;
		
		mInflater=LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mJsonArray==null)
			return 0;
		return mJsonArray.length();
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
		
		try {
			
			JSONObject jsonObject=mJsonArray.getJSONObject(position);
			
			if (imageView == null) {
				imageView = (ImageView) mInflater.inflate(R.layout.item_activity_details_gallery, parent, false);
			}
			mImageLoader.displayImage(jsonObject.getString("imageUrl"), imageView, options);
			
			Log.e("zyf", "gallery get view......");
			
		} catch (Exception e) {
			
			Log.e("zyf", "gallery get view exception: "+e.toString());
		}
		
		
		return imageView;
	}

}
