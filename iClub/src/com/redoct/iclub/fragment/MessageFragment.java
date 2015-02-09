package com.redoct.iclub.fragment;

import java.util.ArrayList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.MessageBaseAdapter;
import com.redoct.iclub.item.MessageItem;

public class MessageFragment extends Fragment{
	
	private PullToRefreshListView mPullToRefreshListView;
	private MessageBaseAdapter mBaseAdapter;
	private ArrayList<MessageItem> messageItems=new ArrayList<MessageItem>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View contentView=inflater.inflate(R.layout.fragment_message, container, false);
		
		initViews(contentView);
		
		return contentView;
	}
	
	private void initViews(View convertView){
		
		TextView mTitleView=(TextView) convertView.findViewById(R.id.mTitleView);
		mTitleView.setText(getResources().getString(R.string.message));
		
		mPullToRefreshListView=(PullToRefreshListView)convertView.findViewById(R.id.mPullToRefreshListView);
		mPullToRefreshListView.setMode(Mode.PULL_FROM_START);
		mBaseAdapter=new MessageBaseAdapter(messageItems, getActivity()){

			@Override
			public void accept(String id) {
				// TODO Auto-generated method stub
				super.accept(id);
				
				
			}
		};
		mPullToRefreshListView.setAdapter(mBaseAdapter);
		mPullToRefreshListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				
				Log.e("zyf", "refresh refresh refresh......");
			}
		});
	}
}
