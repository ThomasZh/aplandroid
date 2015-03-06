package com.redoct.iclub.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.oct.ga.comm.domain.account.AccountMasterInfo;
import com.redoct.iclub.R;
import com.redoct.iclub.item.MemberItem;
import com.redoct.iclub.ui.activity.AddMenberActivity;
import com.redoct.iclub.util.ViewHolder;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MembersListAdapter extends BaseAdapter {
	List<AccountMasterInfo> list = null;
	public List<AccountMasterInfo> temp_list = null;
	public List<AccountMasterInfo> getTemp_list() {
		return temp_list;
	}

	public void setTemp_list(List<AccountMasterInfo> temp_list) {
		this.temp_list = temp_list;
	}

	private AddMenberActivity activity;
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;

	public MembersListAdapter(List<AccountMasterInfo> list, Activity activity) {
		super();
		this.list = list;
		temp_list = new ArrayList<AccountMasterInfo>();
		this.activity = (AddMenberActivity) activity;
		mImageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(10)).build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
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
		if (list == null) {
			return null;
		}
		if (convertView == null) {
			convertView = activity.getLayoutInflater().inflate(
					R.layout.item_member, null);
		}
		final AccountMasterInfo  item = list.get(position);
		ImageView iv_headImge = ViewHolder.get(convertView,R.id.iv_member_headimg);
		mImageLoader.displayImage(item.getImageUrl(), iv_headImge, options);

		TextView tvName = ViewHolder.get(convertView,
				R.id.tv_member_name);
		tvName.setText(item.getName());
		CheckBox check = ViewHolder.get(convertView,R.id.checkBox2);
        if(position==0){
        	check.setVisibility(View.INVISIBLE);
        }
        check.setOnCheckedChangeListener(new  OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					temp_list.remove(item);
				}else{
					temp_list.add(item);
				}
				activity.setCompleteVisible(temp_list.size());   //设置完成按钮的显示和消失
		
			}
        	
        });
		return convertView;
	}

}
