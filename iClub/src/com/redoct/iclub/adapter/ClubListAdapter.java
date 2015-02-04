package com.redoct.iclub.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.redoct.iclub.R;
import com.redoct.iclub.item.ClubItem;
import com.redoct.iclub.util.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ClubListAdapter extends BaseAdapter {
	Activity context;
	List<ClubItem> list = null;
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	
	public ClubListAdapter(Activity context) {
		// TODO Auto-generated constructor stub
		this.context= context;
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

	public List<ClubItem> getList() {
		return list;
	}

	public void setList(List<ClubItem> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
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
		// TODO Auto-generated method stub
		if (list == null) {
			return null;
		}
		if (convertView == null) {
			convertView = context.getLayoutInflater().inflate(R.layout.listview_item_club,
					null);
		}
		ClubItem club = list.get(position);
		ImageView iv_headImge = ViewHolder.get(convertView, R.id.iv_clublistactivity_clubimg);
        mImageLoader.displayImage(club.getTitleBkImage(), iv_headImge, options);
       

		TextView tvName = ViewHolder.get(convertView, R.id.tv_clublistacitvity_name);
		tvName.setText(club.getName());
		TextView tv_level = ViewHolder.get(convertView, R.id.tv_clublistactivity_num);
		tv_level.setText(club.getSubscriberNum()+"人");
		
	return convertView;
	
	}

}