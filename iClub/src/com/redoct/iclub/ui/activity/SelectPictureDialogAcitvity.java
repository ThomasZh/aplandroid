package com.redoct.iclub.ui.activity;

import java.io.File;
import java.io.FileOutputStream;

import com.redoct.iclub.R;
import com.redoct.iclub.iClubApplication;
import com.redoct.iclub.config.AppConfig;
import com.redoct.iclub.util.activity.BaseActivityUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * @author chengcai
 * @date 2015-01-31
 */
public class SelectPictureDialogAcitvity extends Activity {
	private Bitmap mBitmap;  //裁剪后的bitmap
	private TextView tv_quit, tv_photo, tv_camera;
	public static final int OPTION_CANCEL = 0; // 用户选择了“取消”按钮

	public static final int OPTION_CAPTURE = 1; // 用户选择了“相册”
	public static final int OPTION_GALLERY = 2; // 用户选择了“拍照”
	public static final int CODE_RESULT_REQUEST = 3; // 裁剪请求
	// 用于暂时保存通过相机拍摄的照片
	private final String IMAGE_FILE_NAME = "faceImage.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectpiciture_dialog);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		tv_quit = (TextView) findViewById(R.id.tv_myshop_cancel);
		tv_quit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				BaseActivityUtil
						.setDownTransition(SelectPictureDialogAcitvity.this);
			}
		});
		tv_photo = (TextView) findViewById(R.id.tv_myshop_photo);
		tv_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentFromGallery = new Intent(Intent.ACTION_PICK,
						Media.EXTERNAL_CONTENT_URI);
				intentFromGallery.setType("image/*");
				startActivityForResult(intentFromGallery, OPTION_GALLERY);
			}
		});
		tv_camera = (TextView) findViewById(R.id.tv_myshop_camera);
		tv_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentFromCapture = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				intentFromCapture.putExtra(
						MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Environment
								.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
				startActivityForResult(intentFromCapture, OPTION_CAPTURE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_CANCELED) {

			return;
		}

		if (requestCode == OPTION_GALLERY) { // 用户从相册中选择了照片，下一步裁剪操作

			startCropPic(data.getData());

		} else if (requestCode == OPTION_CAPTURE) { // 用户调用相机拍摄了照片，下一步裁剪操作

			File tempFile = new File(Environment.getExternalStorageDirectory(),
					IMAGE_FILE_NAME);
			startCropPic(Uri.fromFile(tempFile));

		}
		else if (requestCode == CODE_RESULT_REQUEST) { // 
			
			if (data!=null) {
				handleCropedPic(data);
			}
	} 
	
	

	}
	//裁剪图片方法实现 
    public void startCropPic(Uri uri) {  
    	if(uri==null){
			Log.e("zyf", "the uri is not exist.");
		}
        Intent intent = new Intent("com.android.camera.action.CROP");  
        intent.setDataAndType(uri, "image/*");  
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CODE_RESULT_REQUEST);  
    }
    
    
    //保存裁剪之后的图片数据
    private void handleCropedPic(Intent data) {  
        Bundle extras = data.getExtras();  
        if (extras != null) {
        	
        	mBitmap= extras.getParcelable("data");	 
        	//String filePath=Environment.getExternalStorageDirectory().getPath();
        	String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+ "//";

			File f=new File(filePath); 
			if(!f.exists()){
				f.mkdir(); 
			}
			
			try { 
				File newFile=new File(f,"img.png");
				if(newFile.exists()){
					newFile.delete();
					
				}
				FileOutputStream out = new FileOutputStream(newFile); 
				mBitmap.compress(Bitmap.CompressFormat.PNG, 90, out); 
				out.flush(); 
				out.close(); 
				Activity activity = iClubApplication.getActivity();
				ReturnBitmap returnBitmap = (ReturnBitmap) activity;
				returnBitmap.returnMapandUrl(mBitmap, filePath+"/img.png");
				finish();
				Log.e("zyf", "file save success......"); 
			} catch (Exception e) {
				Log.e("zyf", "file save exception: "+e.toString());
			}

        }  
    }
    interface  ReturnBitmap{
    	public void returnMapandUrl(Bitmap bitmapk,String path);
    }
    
    
  
    

}
