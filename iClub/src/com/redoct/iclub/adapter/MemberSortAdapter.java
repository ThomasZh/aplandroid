package com.redoct.iclub.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.redoct.iclub.R;
import com.redoct.iclub.item.ContactItem;
import com.redoct.iclub.ui.activity.ChooseMemberActivity;

public class MemberSortAdapter extends BaseAdapter implements SectionIndexer{
	private List<ContactItem> list = null;
	private ChooseMemberActivity mContext;
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	private ArrayList<ContactItem> list_num = new ArrayList<ContactItem>();
	private String [] id_string;
	private List<String> containList = new ArrayList<String>();
	
	public MemberSortAdapter(ChooseMemberActivity mContext, List<ContactItem> list,String [] id_string) {
		this.mContext = mContext;
		this.list = list;
		this.id_string=id_string;
		for(int i = 0;i<id_string.length;i++){
			for(int j=0;j<list.size();j++){
				if(id_string[i]!=null&&list!=null){
					
					if(id_string[i].equals(list.get(j).getId())){
						containList.add(id_string[i]);
						
					}
				}
			}
		}
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
	
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<ContactItem> list){
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final ContactItem mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_choose_member, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_contactactivity_name);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.ivHeadImg = (ImageView) view.findViewById(R.id.iv_contactactivity_headimg);
			viewHolder.box = (CheckBox) view.findViewById(R.id.checkBox1);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
		}else{
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
	
		viewHolder.tvTitle.setText(this.list.get(position).getName());
		 mImageLoader.displayImage(this.list.get(position).getImageUrl(), viewHolder.ivHeadImg, options);
		 viewHolder.box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					list_num.add(mContent);
				    mContext.setContactList(list_num);
				}else{
					list_num.remove(mContent);
					mContext.setContactList(list_num);
				}
				
			}
		});

	
		if(containList!=null){
			if(containList.contains(list.get(position).getId())){
				 viewHolder.box.setVisibility(View.INVISIBLE);
			}
		}

		 
		 if(mContent.isSelected()){
			 viewHolder.box.setChecked(true);
		 }else{
			 viewHolder.box.setChecked(false);
		 }
		

		return view;

	}
	


	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		ImageView ivHeadImg;
		CheckBox box;
	}


	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}