package com.redoct.iclub.ui.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.oct.ga.comm.domain.Contact;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.SortAdapter;
import com.redoct.iclub.item.ContactItem;
import com.redoct.iclub.item.SortModel;
import com.redoct.iclub.task.GetClubListTask;
import com.redoct.iclub.task.GetContactTask;
import com.redoct.iclub.util.CharacterParser;
import com.redoct.iclub.util.PinyinComparator;
import com.redoct.iclub.util.ToastUtil;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ContactActivity extends Activity {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	private GetContactTask task;
	private ImageView iv_right,iv_left;
	private RadioButton rbFriends,rbConacts;
	
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		iv_right = (ImageView) findViewById(R.id.rightBtn);
		iv_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BaseActivityUtil.startActivity(ContactActivity.this, AddContactActivity.class, false);
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
		rbFriends = (RadioButton) findViewById(R.id.rb_contact_friends);
		rbFriends.setChecked(true);
		loadData();
		
	}

	private void initViews( final List<ContactItem> listContact ) {
		//实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		
		pinyinComparator = new PinyinComparator();
		
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
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
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//这里要利用adapter.getItem(position)来获取当前position所对应的对象
				Intent intent = new Intent();
				intent.setClass(ContactActivity.this,ContactDetailActivity.class);
				intent.putExtra("name",((ContactItem)adapter.getItem(position)).getName());
				intent.putExtra("email", ((ContactItem)adapter.getItem(position)).getEmail());
				intent.putExtra("img", ((ContactItem)adapter.getItem(position)).getImageUrl());
				BaseActivityUtil.startActivity(ContactActivity.this, intent, false, false);
				//Toast.makeText(getApplication(), ((ContactItem)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
			}
		});
		
		//SourceDateList = filledData(getResources().getStringArray(R.array.date));
		
		// 根据a-z进行排序源数据
		Collections.sort(listContact, pinyinComparator);
		adapter = new SortAdapter(this, listContact);
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
	 * 为ListView填充数据
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(String [] date){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<date.length; i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			//汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		return mSortList;
		
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

	
}
