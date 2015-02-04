package com.redoct.iclub.adapter;

import com.redoct.iclub.R;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ThemeSelectBaseAdapter extends BaseAdapter {
	
	private Context mContext;
	
	private LayoutInflater mInflater;
	
	private int [] iconIds;
	
	private String [] iconTitles;

	public ThemeSelectBaseAdapter(Context mContext, int count,int[] iconIds,
			String[] iconTitles) {
		super();
		this.mContext = mContext;
		this.iconIds = iconIds;
		this.iconTitles = iconTitles;
		
		mInflater=LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 10;
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
		
		mTitleTv.setText("title"+position);
		
		return convertView;
	}

}
