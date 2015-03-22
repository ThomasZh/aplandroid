package com.redoct.iclub.adapter;

import java.util.ArrayList;

import com.redoct.iclub.R;
import com.redoct.iclub.adapter.ChatMemberAdapter.ViewHolder;
import com.redoct.iclub.item.MemberItem;
import com.redoct.iclub.item.MessageItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatMessageAdapter extends BaseAdapter {
	
	private ArrayList<MessageItem> mMessageItems;
	
	private Context mContext;
	
	private LayoutInflater inflater;

	public ChatMessageAdapter(ArrayList<MessageItem> mMessageItems,
			Context mContext) {
		super();
		this.mMessageItems = mMessageItems;
		this.mContext = mContext;
		
		inflater=LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mMessageItems.size();
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
		
		ViewHolder holder;
		final MessageItem item=mMessageItems.get(position);
		
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_chat_message, parent, false);
            holder = new ViewHolder();
            holder.mContentTv= (TextView) convertView.findViewById(R.id.mContentTv);              

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        holder.mContentTv.setText(item.getContent());
        
		return convertView;
	}
	
	class ViewHolder {
        TextView mContentTv;
    }

}
