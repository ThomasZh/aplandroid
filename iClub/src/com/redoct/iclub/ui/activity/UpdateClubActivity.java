package com.redoct.iclub.ui.activity;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.redoct.iclub.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

public class UpdateClubActivity extends Activity{
	private EditText et_clubname;
	private EditText et_clubdesc;
	private ImageView iv_clubimg;
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	private String clubName,clubDesc,clubImg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updateclub);
		
		mImageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(100)).build();
		initView();
	}
	public void initView(){
		et_clubdesc = (EditText) findViewById(R.id.et_updateclub_clubdesc);
		et_clubdesc.setText(getIntent().getStringExtra("desc"));
		et_clubname = (EditText) findViewById(R.id.et_updateclub_clubname);
		et_clubname.setText(getIntent().getStringExtra("name"));
		iv_clubimg  = (ImageView) findViewById(R.id.iv_update_clubimg);
		mImageLoader.displayImage(getIntent().getStringExtra("img"),
				iv_clubimg, options);
	}

}
