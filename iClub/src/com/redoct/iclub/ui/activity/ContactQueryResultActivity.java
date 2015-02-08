package com.redoct.iclub.ui.activity;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.oct.ga.comm.domain.account.AccountDetailInfo;
import com.redoct.iclub.R;
import com.redoct.iclub.task.FoundFriendsTask;
import com.redoct.iclub.task.InviteTask;
import com.redoct.iclub.util.ToastUtil;
import com.redoct.iclub.util.activity.BaseActivityUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Administrator
 *163  //系统外
 *147 系统内的
 */
public class ContactQueryResultActivity extends Activity {
	
	private Button tvTitleLeftBtn;
	private TextView tvTitle, tvName, tvEmail;
	private EditText etPhone;
	private ImageView ivHead;
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	private String  flag;
	private InviteTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_result);
		flag = getIntent().getStringExtra("flag");
		initView();

	}

	@SuppressLint("NewApi")
	private void initView() {
		
		
		mImageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(100)).build();
		
		if("no".equals(flag)){
			findViewById(R.id.line_name).setVisibility(View.GONE);
			findViewById(R.id.relative_img).setVisibility(View.GONE);
		}else{
			findViewById(R.id.tv_result_show).setVisibility(View.GONE);
		}
		
		
		

		tvTitleLeftBtn = (Button) findViewById(R.id.leftBtn);

		tvName = (TextView) findViewById(R.id.tv_accountdetail_name);
		tvEmail = (TextView) findViewById(R.id.tv_accountdetail_mail);
		tvTitle = (TextView) findViewById(R.id.mTitleView);
		etPhone = (EditText) findViewById(R.id.et_accountdetail_phone);
		ivHead = (ImageView) findViewById(R.id.iv_accoutdetai_headimg);

		tvTitleLeftBtn.setBackground(getResources().getDrawable(
				R.drawable.title_back));
		tvTitleLeftBtn.setVisibility(View.VISIBLE);
		tvTitleLeftBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});
		findViewById(R.id.btn_addcontact_req).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SendRequest(flag,getIntent().getStringExtra("email"),getIntent().getStringExtra("id"));
				
			}
		});

		tvName.setText(getIntent().getStringExtra("name"));
		tvEmail.setText(getIntent().getStringExtra("email"));
		mImageLoader.displayImage(getIntent().getStringExtra("img"), ivHead,
				options);

	}
	protected void SendRequest(String flag, String mail,String id) {
		// TODO Auto-generated method stub
                  task=new InviteTask(id,mail,flag){
			
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
				Log.e("zyf", "hahahhahahahhahah");
				ToastUtil.toastshort(ContactQueryResultActivity.this, "邀请成功！");
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
				
				
			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();
				
				Log.e("zyf", "get data failure....");
				
			    ToastUtil.toastshort(ContactQueryResultActivity.this,"邀请失败！");
				
			}

			@Override
			public void complete() {
				// TODO Auto-generated method stub
				super.complete();
				
			}
		};
		task.setTimeOutEnabled(true, 10*1000);
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
