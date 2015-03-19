package com.redoct.iclub.ui.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import com.redoct.iclub.R;
import com.redoct.iclub.adapter.SortAdapter;
import com.redoct.iclub.item.ContactItem;
import com.redoct.iclub.task.GetContactTask;
import com.redoct.iclub.task.Invite2Task;
import com.redoct.iclub.util.CharacterParser;
import com.redoct.iclub.util.PinyinComparator;
import com.redoct.iclub.util.ToastUtil;
import com.redoct.iclub.util.activity.BaseActivityUtil;
import com.redoct.iclub.widget.ClearEditText;
import com.redoct.iclub.widget.SideBar;
import com.redoct.iclub.widget.SideBar.OnTouchingLetterChangedListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
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
	private ImageView iv_right, iv_left;
	private RadioButton rbFriends, rbConacts;
	private Invite2Task taskI;
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
		setContentView(R.layout.activity_contact);
		

		initTitleViews();

		loadData();

	}

	private void initTitleViews() {

		TextView mTitleView = (TextView) findViewById(R.id.mTitleView);
		mTitleView.setText(getResources().getString(R.string.contacts));

		Button leftBtn = (Button) findViewById(R.id.leftBtn);
		leftBtn.setBackgroundResource(R.drawable.title_back);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});

		Button rightBtn = (Button) findViewById(R.id.rightBtn);
		rightBtn.setBackgroundResource(R.drawable.title_add);
		;
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				BaseActivityUtil.startActivity(ContactActivity.this,
						AddContactActivity.class, false);
			}
		});
		findViewById(R.id.rightBtn1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					getKey();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void initViews(final List<ContactItem> listContact) {
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				Intent intent = new Intent();
				intent.setClass(ContactActivity.this,
						ContactDetailActivity.class);
				intent.putExtra("name",
						((ContactItem) adapter.getItem(position)).getName());
				intent.putExtra("email",
						((ContactItem) adapter.getItem(position)).getEmail());
				intent.putExtra("img",
						((ContactItem) adapter.getItem(position)).getImageUrl());
				BaseActivityUtil.startActivity(ContactActivity.this, intent,
						false, false);
				// Toast.makeText(getApplication(),
				// ((ContactItem)adapter.getItem(position)).getName(),
				// Toast.LENGTH_SHORT).show();
			}
		});

		// SourceDateList =
		// filledData(getResources().getStringArray(R.array.date));

		// 根据a-z进行排序源数据
		Collections.sort(listContact, pinyinComparator);
		adapter = new SortAdapter(this, listContact);
		sortListView.setAdapter(adapter);

		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
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
	 * 


	 */
	private void filterData(String filterStr, List<ContactItem> listContact) {
		List<ContactItem> filterDateList = new ArrayList<ContactItem>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = listContact;
		} else {
			filterDateList.clear();
			for (ContactItem sortModel : listContact) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
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
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}

	public void getKey() throws IOException {

		taskI = new Invite2Task("", (short) 161, (short) 0) {

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
				try {
					showShare(taskI.key);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ToastUtil.toastshort(ContactActivity.this, "请重新分享");
				}

			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();
				// ToastUtil.toastshort(ClubListActivity.this,"返回空置");

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
		taskI.setTimeOutEnabled(true, 10 * 1000);
		taskI.safeExecute();

	}

	private void showShare(final String key) throws IOException {

		File pathFile = android.os.Environment.getExternalStorageDirectory();

		File myCaptureFile = new File(pathFile, "te.jpg");
		if(!myCaptureFile.exists()){
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(myCaptureFile));
			InputStream is = getResources().openRawResource(R.drawable.a);
			
			Bitmap mBitmap = BitmapFactory.decodeStream(is);
			
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
		}

		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
         oks.setTitle("加我");
		// oks.setTitle(mNameTv.getText().toString());
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://www.sina.com");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("嗨，推荐一个做活动的工具《A计划》，用起来非常搞笑简洁，我们一起来用吧！");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数

		oks.setImagePath("/sdcard/te.jpg");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://www.tripc2c.com/m/index.php?action=signup&ekey="+key);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {

			@Override
			public void onShare(Platform platform, ShareParams paramsToShare) {
				// TODO Auto-generated method stub
				Log.i("cc", platform.getName().toString());
				if ("ShortMessage".equals(platform.getName())) {
					paramsToShare.setText("嗨,推荐一个做活动的工具《A计划》，用起来非常简洁高效，我们一起来用吧！http://www.tripc2c.com/m/index.php?action=signup&ekey="+key);
					paramsToShare.setTitle(null);
					paramsToShare.setImagePath(null);
				}
				if ("Email".equals(platform.getName())) {
					paramsToShare.setText("嗨,我推荐一个《A计划》上的活动，希望你能加入！活动 :众筹旅行      发起人: kevin    开始时间: 04-05 09:00      结束时间:04-06 10:30     在手机上打开邮件，几步就可以开始使用了！    1), 还没有安装点击安装http://www.tripc2c.com/m/index.php?action=signup&ekey="+key+", 已经在使用了就 点击开启aplan://fm?ekey="+key);
					paramsToShare.setTitle("我在这里建立了一个朋友圈");
					paramsToShare.setImagePath(null);
					

					return;
				}
			}
		});
 // P@ssword01!!!
  //C#checai12@@@
  //Cheng@cai1234
		// 启动分享GUI
		oks.show(this);

	}

}
