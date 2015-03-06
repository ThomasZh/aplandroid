package com.redoct.iclub.ui.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.redoct.iclub.R;
import com.redoct.iclub.task.GetClubDetailTask;
import com.redoct.iclub.util.activity.BaseActivityUtil;

/**
 * @author created by chengcai 15-02-05
 *
 */
public class ClubDetailActivity extends Activity {

	private ImageLoader mImageLoader;
	private DisplayImageOptions options;

	private GetClubDetailTask task;
	private String id;
	private TextView tvClubName, tvClubDesc, tvJoinPeople, tvActivity;
	private ImageView ivClubPic;
	private RelativeLayout lineActivity;
	private String clubName, clubDesc, clubImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clubdetial);
		id = getIntent().getStringExtra("id");
		mImageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				//.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.icon_club_temp)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				//.displayer(new RoundedBitmapDisplayer(100))
				.build();

		initView();
		loadData();

	}

	private void loadData() {

		task = new GetClubDetailTask(id) {

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
					JSONObject json = new JSONObject(task.getJsonClubDetail());
					tvActivity.setText(json.optInt("activityNum") + "");
					clubName = json.optString("name");
					clubDesc = json.optString("desc");
					clubImg = json.optString("titleBkImage");
					tvClubDesc.setText(json.optString("desc"));
					tvClubName.setText(json.optString("name"));
					tvJoinPeople.setText(json.optInt("subscriberNum") + "");
					mImageLoader.displayImage(json.optString("titleBkImage"),
							ivClubPic, options);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e("cc", "club json  huo qu shi bai!");
				}

			}

			@Override
			public void failure() {
				// TODO Auto-generated method stub
				super.failure();
				// ToastUtil.toastshort(ClubListActivity.this,"返回空置");
				loadData();
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
		task.setTimeOutEnabled(true, 10 * 1000);
		task.safeExecute();
	}

	private void initView() {

		/*
		 * TextView mTitleView = (TextView) findViewById(R.id.mTitleView);
		 * mTitleView.setText(getResources().getString(R.string.friends));
		 */
		tvActivity = (TextView) findViewById(R.id.tv_clubdetailactivity_actnum);
		tvClubDesc = (TextView) findViewById(R.id.tv_clubdetial_detail);
		tvClubName = (TextView) findViewById(R.id.tv_clubdetailactivity_clubname);
		tvJoinPeople = (TextView) findViewById(R.id.tv_clubdetailactivity_peonum);
		ivClubPic = (ImageView) findViewById(R.id.iv_clubdetailactivity_clubimg);
		lineActivity = (RelativeLayout) findViewById(R.id.line_activity);
		lineActivity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// BaseActivityUtil.startActivity(ClubDetailActivity.this,
				// ActivityDetailActivity.class, false);

				Intent intent = new Intent(ClubDetailActivity.this,ClubActivityListActivity.class);
				intent.putExtra("id", id);
				BaseActivityUtil.startActivity(ClubDetailActivity.this, intent, false, false);
				
			}
		});
		findViewById(R.id.leftBtn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});
		
		ImageView btnRigthButton = (ImageView) findViewById(R.id.rightBtn);
		btnRigthButton.setVisibility(View.VISIBLE);
		
		btnRigthButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent();
				in.setClass(ClubDetailActivity.this, UpdateClubActivity.class);
				in.putExtra("name", clubName);
				in.putExtra("img", clubImg);
				in.putExtra("desc", clubDesc);
				in.putExtra("id", id);
				startActivityForResult(in, 1);
				BaseActivityUtil.setStartTransition(ClubDetailActivity.this);

			}
		});
		findViewById(R.id.rl_clubdetail_menber).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(ClubDetailActivity.this,AddMenberActivity.class);
				intent.putExtra("id", id);
				startActivityForResult(intent, 2);
				BaseActivityUtil.setStartTransition(ClubDetailActivity.this);
				
				
			}
		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		int code = requestCode;
		switch (code) {
		case 2:
			if(data!=null){
				tvJoinPeople.setText(data.getStringExtra("joinpeople"));
			}
			
			break;

		default:
			break;
		}
	}

}
