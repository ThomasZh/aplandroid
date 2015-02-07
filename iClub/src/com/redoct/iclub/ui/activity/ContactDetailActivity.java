package com.redoct.iclub.ui.activity;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.redoct.iclub.R;
import com.redoct.iclub.util.activity.BaseActivityUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactDetailActivity extends Activity {
	private Button tvTitleLeftBtn;
	private TextView tvTitle, tvName, tvEmail;
	private EditText etPhone;
	private ImageView ivHead;
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_detail);
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

		tvTitle.setText(getIntent().getStringExtra("name"));
		tvName.setText(getIntent().getStringExtra("name"));
		tvEmail.setText(getIntent().getStringExtra("email"));
		mImageLoader.displayImage(getIntent().getStringExtra("img"), ivHead,
				options);

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
