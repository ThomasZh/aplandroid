package com.redoct.iclub.fragment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.oct.ga.comm.domain.account.AccountDetailInfo;
import com.redoct.iclub.R;
import com.redoct.iclub.task.GetAccountTask;
import com.redoct.iclub.ui.activity.AccountActivity;
import com.redoct.iclub.ui.activity.ClubListActivity;
import com.redoct.iclub.ui.activity.ContactActivity;
import com.redoct.iclub.ui.activity.LoginActivity;
import com.redoct.iclub.ui.activity.RegisterActivity;
import com.redoct.iclub.ui.activity.UpdatePasswordActivity;
import com.redoct.iclub.util.ToastUtil;
import com.redoct.iclub.util.UserInformationLocalManagerUtil;
import com.redoct.iclub.util.activity.BaseActivityUtil;
import com.redoct.iclub.widget.CircleBitmapDisplayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MySelfFragment extends Fragment implements OnClickListener{
	private View contentView;
	private TextView tv_title,tv_alterpass,tv_quit,tv_friends,tv_connectors,tv_name;
	private RelativeLayout rl_myinformation;
	private GetAccountTask getTask;
	private AccountDetailInfo act;
	private ImageView ivMyhead;
	private DisplayImageOptions options; // ?????????????????  
	private TextView tAlterPwd;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.fragment_myself, container,
				false);
		initView();
		
		return contentView;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		tv_name.setText(new UserInformationLocalManagerUtil(getActivity()).ReadUserInformation().getName());
		if(!TextUtils.isEmpty(new UserInformationLocalManagerUtil(getActivity()).ReadUserInformation().getImageUrl())){
			
			ImageLoader.getInstance().displayImage(new UserInformationLocalManagerUtil(getActivity()).ReadUserInformation().getImageUrl(), ivMyhead,options); 
		}
	}

	

	private void initView() {
		// TODO Auto-generated method stub
		options = new DisplayImageOptions.Builder()
	        .cacheInMemory(true)
	        .cacheOnDisk(true)
	        
	        .showImageOnLoading(R.drawable.ic_launcher)
	        .showImageOnFail(R.drawable.ic_launcher)
	        .displayer( new CircleBitmapDisplayer() )
	        .build();
		tv_alterpass = (TextView) contentView.findViewById(R.id.alterpass);
		tv_alterpass.setOnClickListener(this);
		tv_title = (TextView) contentView.findViewById(R.id.mTitleView);
		tv_title.setText(R.string.me);
		tv_alterpass = (TextView) contentView.findViewById(R.id.alterpass);
		tv_alterpass.setOnClickListener(this);
		tv_quit = (TextView) contentView.findViewById(R.id.quit);
		tv_quit.setOnClickListener(this);
		tv_friends = (TextView) contentView.findViewById(R.id.friends);
		tv_friends.setOnClickListener(this);
		tv_connectors = (TextView) contentView.findViewById(R.id.connctor);
		tv_connectors.setOnClickListener(this);
		rl_myinformation = (RelativeLayout) contentView.findViewById(R.id.myself_information);
		rl_myinformation.setOnClickListener(this);
		tv_name = (TextView) contentView.findViewById(R.id.myname);
		ivMyhead = (ImageView) contentView.findViewById(R.id.myhead);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.alterpass:
        	BaseActivityUtil.startActivity(getActivity(),UpdatePasswordActivity.class, false);

			break;
		case R.id.quit:
			/*Intent intent = new Intent(getActivity(),
					RegisterActivity.class);
			 
			startActivity(intent);
			BaseActivityUtil.setUpTransition(getActivity());
			getActivity().finish();*/
			//System.exit(0);
			 try {
				showShare();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
		case R.id.connctor:
        	BaseActivityUtil.startActivity(getActivity(),ContactActivity.class, false);

			break;
		case R.id.friends:
        	BaseActivityUtil.startActivity(getActivity(),ClubListActivity.class, false);

			break;
		case R.id.myself_information:
			BaseActivityUtil.startActivity(getActivity(),AccountActivity.class, false);
			break;

	
		}
	}
	private void showShare() throws IOException {
		File pathFile = android.os.Environment.getExternalStorageDirectory();
		
		
        File myCaptureFile = new File(pathFile,"test.jpg");  
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));  
        InputStream is = getResources().openRawResource(R.drawable.ic_launcher);  

        Bitmap mBitmap = BitmapFactory.decodeStream(is);
        
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);  
        bos.flush();  
        bos.close();  
		
		 ShareSDK.initSDK(getActivity());
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 

		// 分享时Notification的图标和文字
		// oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle("每天努力一点多了而已吧，关于生活的快乐你们看见都是内啊。。。。。。。");
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		  oks.setTitleUrl("http://www.sina.com");
		 // text是分享文本，所有平台都需要这个字段
		  oks.setText("我是程才！！！");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 
		 
		 
		 oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		   oks.setUrl("http://www.baidu.com");
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment("我是测试评论文本");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		 oks.show(getActivity());
		 }
}
