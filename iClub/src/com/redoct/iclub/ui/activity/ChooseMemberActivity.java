package com.redoct.iclub.ui.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.oct.ga.comm.domain.Contact;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.MemberSortAdapter;
import com.redoct.iclub.adapter.SortAdapter;
import com.redoct.iclub.item.ContactItem;
import com.redoct.iclub.task.GetContactTask;
import com.redoct.iclub.util.CharacterParser;
import com.redoct.iclub.util.PinyinComparator;
import com.redoct.iclub.util.activity.BaseActivityUtil;
import com.redoct.iclub.widget.ClearEditText;
import com.redoct.iclub.widget.SideBar;
import com.redoct.iclub.widget.SideBar.OnTouchingLetterChangedListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ChooseMemberActivity extends Activity {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private MemberSortAdapter adapter;
	private ClearEditText mClearEditText;
	private GetContactTask task;
	private ImageView iv_left;
	private TextView tvRight;
	private ArrayList<ContactItem> list = new ArrayList<ContactItem>();
	
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choosemember);
		tvRight = (TextView) findViewById(R.id.rightBtn);
		tvRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				
				intent.setClass(ChooseMemberActivity.this,ContactDetailActivity.class);
				intent.putExtra("ContactList", list);
				BaseActivityUtil.startActivity(ChooseMemberActivity.this, intent, true, false);
			}
		});
		iv_left= (ImageView) findViewById(R.id.leftBtn);
		iv_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
				
			}
		});
		
		loadData();
		
	}

	private void initViews( final List<ContactItem> listContact ) {
		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		
		pinyinComparator = new PinyinComparator();
		
		sideBar = (SideBar) findViewById(R.id.csidrbar);
		dialog = (TextView) findViewById(R.id.cdialog);
		sideBar.setTextView(dialog);
		
		//设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override 
			public void onTouchingLetterChanged(String s) {
				//该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}
				
			}
		});
		
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		/*sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//这里要利用adapter.getItem(position)来获取当前position所对应的对象
			
				Intent intent = new Intent();
			
				intent.setClass(ChooseMemberActivity.this,ContactDetailActivity.class);
				intent.putExtra("name",((ContactItem)adapter.getItem(position)).getName());
				intent.putExtra("email", ((ContactItem)adapter.getItem(position)).getEmail());
				intent.putExtra("img", ((ContactItem)adapter.getItem(position)).getImageUrl());
				BaseActivityUtil.startActivity(ChooseMemberActivity.this, intent, false, false);
				//Toast.makeText(getApplication(), ((ContactItem)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
			}
		});*/
		
		//SourceDateList = filledData(getResources().getStringArray(R.array.date));
		
		// 根据a-z进行排序源数据
		Collections.sort(listContact, pinyinComparator);
		adapter = new MemberSortAdapter(this, listContact);
		sortListView.setAdapter(adapter);
		
		
		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
		
		//根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString(), listContact);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}


	
	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr,List<ContactItem> listContact){
		List<ContactItem> filterDateList = new ArrayList<ContactItem>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList =  listContact;
		}else{
			filterDateList.clear();
			for(ContactItem sortModel :  listContact){
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}
		
		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}
	private void loadData() {

		task = new GetContactTask() {

			@Override
			public void timeout() {
				// TODO Auto-generated method stub
				super.timeout();

				task.cancel(true);
				Log.e("zyf", "get data time out....");
			}

			@Override
			public void before() {
				// TODO Auto-generated method stub
				super.before();

				Log.e("zyf", "start get data....");
			}

			@Override
			public void callback() {
				// TODO Auto-generated method stub
				super.callback();
				initViews(task.listContact);

			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();
				// ToastUtil.toastshort(ClubListActivity.this,"返回空置");
				loadData();
				Log.e("cc", "get contact data failure....");

			}

			@Override
			public void complete() {
				// TODO Auto-generated method stub
				super.complete();

				Log.e("zyf", "get data complete....");

				// mPullToRefreshListView.onRefreshComplete();
			}
		};
		task.setTimeOutEnabled(true, 10 * 1000);
		task.safeExecute();
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.push_right_in,
				R.anim.push_right_out);
	}
	public void  setContactList(ArrayList<ContactItem> list){
		this.list =  list;
	}

	
}