package com.redoct.iclub.fragment;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oct.ga.comm.domain.account.AccountDetailInfo;
import com.redoct.iclub.R;
import com.redoct.iclub.config.AppConfig;
import com.redoct.iclub.task.GetAccountTask;
import com.redoct.iclub.ui.activity.AccountActivity;
import com.redoct.iclub.util.PersistentUtil;
import com.redoct.iclub.util.UserInformationLocalManagerUtil;
import com.redoct.iclub.util.activity.BaseActivityUtil;
import com.redoct.iclub.widget.CircleBitmapDisplayer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

public class MySelfFragment extends Fragment implements OnClickListener{
	private View contentView;
	private TextView tv_title,tv_alterpass,tv_quit,tv_friends,tv_connectors,tv_name;
	private RelativeLayout rl_myinformation;
	private GetAccountTask getTask;
	private AccountDetailInfo act;
	private ImageView ivMyhead;
	private DisplayImageOptions options; // 配置图片加载及显示选项  
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_myself, container,
				false);
		initView();
		
		return contentView;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		tv_name.setText(new UserInformationLocalManagerUtil(getActivity()).ReadUserInformation().getName());
		ImageLoader.getInstance().displayImage(new UserInformationLocalManagerUtil(getActivity()).ReadUserInformation().getImageUrl(), ivMyhead,options); 
	}

	

	private void initView() {
		// TODO Auto-generated method stub
		options = new DisplayImageOptions.Builder()
	        .cacheInMemory(true)
	        .cacheOnDisk(true)
	        .showImageOnLoading(R.drawable.ic_launcher)
	        .showImageOnFail(R.drawable.ic_launcher)
	        .displayer( new CircleBitmapDisplayer() )
	        .build();
		tv_title = (TextView) contentView.findViewById(R.id.mTitleView);
		tv_title.setText(R.string.me);
		tv_alterpass = (TextView) contentView.findViewById(R.id.alterpass);
		tv_alterpass.setOnClickListener(this);
		tv_quit = (TextView) contentView.findViewById(R.id.quit);
		tv_quit.setOnClickListener(this);
		tv_friends = (TextView) contentView.findViewById(R.id.friends);
		tv_friends.setOnClickListener(this);
		tv_connectors = (TextView) contentView.findViewById(R.id.connctor);
		tv_connectors.setOnClickListener(this);
		rl_myinformation = (RelativeLayout) contentView.findViewById(R.id.myself_information);
		rl_myinformation.setOnClickListener(this);
		tv_name = (TextView) contentView.findViewById(R.id.myname);
		ivMyhead = (ImageView) contentView.findViewById(R.id.myhead);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.alterpass:
			
			break;
		case R.id.quit:
			
			break;
		case R.id.connctor:
			
			break;
		case R.id.friends:
			
			break;
		case R.id.myself_information:
			BaseActivityUtil.startActivity(getActivity(),AccountActivity.class, false);
			break;

	
		}
	}
}
