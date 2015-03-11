package com.redoct.iclub.ui.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.oct.ga.comm.domain.account.AccountMasterInfo;
import com.redoct.iclub.R;
import com.redoct.iclub.adapter.MemberSortAdapter;
import com.redoct.iclub.item.ContactItem;
import com.redoct.iclub.task.ClubSubscribersAddTask;
import com.redoct.iclub.task.ClubSubscribersRemoveTask;
import com.redoct.iclub.task.GetContactTask;
import com.redoct.iclub.task.ModifyAcitivityMembersTask;
import com.redoct.iclub.util.CharacterParser;
import com.redoct.iclub.util.Constant;
import com.redoct.iclub.util.MyProgressDialogUtils;
import com.redoct.iclub.util.PinyinComparator;
import com.redoct.iclub.util.ToastUtil;
import com.redoct.iclub.util.activity.BaseActivityUtil;
import com.redoct.iclub.widget.ClearEditText;
import com.redoct.iclub.widget.MyToast;
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
	private ArrayList<String> listId = new ArrayList<String>();
	private List<String> id_list=new ArrayList<String>();
	private ClubSubscribersAddTask task_add;
	private String id;
	
	
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	
	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	
	private boolean isFromActivityDetails;
	private ArrayList<ContactItem> selectedContactItems;
	private String activityId;

	private String [] id_string;

	
	private boolean isFromActivityCreate;
	private boolean isFromActivityModify;
	
	public ModifyAcitivityMembersTask mModifyAcitivityMembersTask;
	private MyProgressDialogUtils mProgressDialogUtils;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choosemember);
		
		isFromActivityDetails=getIntent().getBooleanExtra("isFromActivityDetails", false);
		isFromActivityCreate=getIntent().getBooleanExtra("isFromActivityCreate", false);
		isFromActivityModify=getIntent().getBooleanExtra("isFromActivityModify", false);
		selectedContactItems=(ArrayList<ContactItem>) getIntent().getSerializableExtra("selectedContactItems");
		activityId=getIntent().getStringExtra("activityId");
		id_string = getIntent().getStringArrayExtra("membering");
		id = getIntent().getStringExtra("id");
		
		tvRight = (TextView) findViewById(R.id.rightBtn);
		tvRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(isFromActivityDetails){    //推荐好友
					
					Intent intent = new Intent();
					intent.putExtra("ContactItems", list);
					intent.putExtra("activityId", activityId);
					intent.setClass(ChooseMemberActivity.this,ActivityRecommendActivity.class);
					
					BaseActivityUtil.startActivity(ChooseMemberActivity.this, intent, true, false);
<<<<<<< HEAD
				}else{
					
=======
					
				}else if(isFromActivityCreate){   //活动的创建
					
					Intent intent = new Intent();
					intent.putExtra("ContactItems", list);
					setResult(Constant.RESULT_CODE_MEMBER_SELECT, intent);
					
					finish();
					
				}else if(isFromActivityModify){    //活动的编辑
					
					modifyActityMembers();
					
				}else{
>>>>>>> origin/master
					
					addMember();
					
				}
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
	
	private void modifyActityMembers(){
		String [] subscriberIds=new String[list.size()];
		for(int i=0;i<list.size();i++){
			subscriberIds[i]=list.get(i).getId();
		}
		mModifyAcitivityMembersTask=new ModifyAcitivityMembersTask(activityId, subscriberIds){

			@Override
			public void before() {
				
				Log.e("zyf", "activity members modify start......");
				
				mProgressDialogUtils=new MyProgressDialogUtils(R.string.progress_dialog_activity_members_modify, ChooseMemberActivity.this);
				mProgressDialogUtils.showDialog();
			}

			@Override
			public void callback() {
				
				Log.e("zyf", "activity members modify success......");
				
				mProgressDialogUtils.dismissDialog();
				
				MyToast.makeText(ChooseMemberActivity.this, true, R.string.activity_members_modify_success, MyToast.LENGTH_LONG).show();
				
				Intent intent = new Intent();
				intent.putExtra("ContactItems", list);
				setResult(Constant.RESULT_CODE_MEMBER_SELECT, intent);
				
				finish();
			}

			@Override
			public void failure() {
				
				Log.e("zyf", "activity members modify failed......");
				
				MyToast.makeText(ChooseMemberActivity.this, true, R.string.activity_members_modify_failed, MyToast.LENGTH_LONG).show();
				
				mProgressDialogUtils.dismissDialog();
			}

			@Override
			public void timeout() {
				
				Log.e("zyf", "activity members modify time out......");
				
				MyToast.makeText(ChooseMemberActivity.this, true, R.string.activity_members_modify_failed, MyToast.LENGTH_LONG).show();
				
				mProgressDialogUtils.dismissDialog();
			}
		};
		mModifyAcitivityMembersTask.setTimeOutEnabled(true, 10*1000);
		mModifyAcitivityMembersTask.safeExecute();
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
		
		sortListView = (ListView) findViewById(R.id.lv_chosemember_list);
		
		//用于判断是否处于选中状态
		if(selectedContactItems!=null&&selectedContactItems.size()>0){
			for(int i=0;i<listContact.size();i++){
				
				for(int j=0;j<selectedContactItems.size();j++){
					
					if(listContact.get(i).getId().equals(selectedContactItems.get(j).getId())){
						
						Log.e("zyf", listContact.get(i).getName()+"  default selected......");
						listContact.get(i).setSelected(true);
						
						break;
					}
				}
			}
		}else{
			Log.e("zyf", "selected members is null or size is 0......");
		}
		/*for(int i=0;i<selectedContactItems.size();i++){
			Log.e("zyf", "selected member: name: "+selectedContactItems.get(i).getName()+"  id: "+selectedContactItems.get(i).getId());
		}
		
		Log.e("zyf", "------------------------------------------------------------------");
		
		for(int i=0;i<listContact.size();i++){
			Log.e("zyf", "selected member: name: "+listContact.get(i).getName()+"  id: "+listContact.get(i).getId());
		}*/
		
		// 根据a-z进行排序源数据
		Collections.sort(listContact, pinyinComparator);
		adapter = new MemberSortAdapter(this, listContact,id_string);
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
		/*sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//这里要利用adapter.getItem(position)来获取当前position所对应的对象
				Checkable check = (Checkable) view.findViewById(R.id.checkBox1);
				if(check.isChecked()){
					listId.add(((ContactItem)adapter.getItem(position)).getId());
				}else{
					listId.remove(((ContactItem)adapter.getItem(position)).getId());
				}
			
				
			}
		});*/
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
	
	public void addMember(){
		
		
		for (int i = 0; i < list.size(); i++) {
			ContactItem item = list.get(i);
			id_list.add(item.getId());
		}
		id_string = new String[id_list.size()];
		id_string =id_list.toArray(id_string);
		task_add = new ClubSubscribersAddTask(id, id_string) {

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
				ToastUtil.toastshort(ChooseMemberActivity.this, "succeful!");
				Intent intent = new Intent();
				setResult(1, intent);
				
				finish();

			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();
				ToastUtil.toastshort(ChooseMemberActivity.this, "failure");

				// loadData();
				Log.e("zyf", "get data failure....");

			}

			@Override
			public void complete() {
				// TODO Auto-generated method stub
				super.complete();

				Log.e("zyf", "get data complete....");

				// mPullToRefreshListView.onRefreshComplete();
			}
		};
		task_add.setTimeOutEnabled(true, 10 * 1000);
		task_add.safeExecute();
	}
	

	
}
