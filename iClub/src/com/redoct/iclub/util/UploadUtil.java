package com.redoct.iclub.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.util.Log;

import com.upyun.UpYun;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liwenzhi on 14-11-13.
 */
public class UploadUtil {

    private static final String OPERATOR_FACE = "tripc2c";
    private static final String OPERATOR_CLUB = "tripc2cclub";
    private static final String OPERATOR_PROJECT = "tripc2cproject";

    private static final String PASSWORD = "upi8aegg";



    public static boolean upyUploadClubImage(String localFilePath, String remoteFilePath){
        String bucketName = "tripc2c-club-title";
        UpYun upyun = new UpYun(bucketName, OPERATOR_CLUB, PASSWORD);
        upyun.setDebug(false);

        File localFile = new File(localFilePath);
        try {
            Log.d("URL", "to upload file: "+localFilePath+" to: "+remoteFilePath);
            boolean result = upyun.writeFile(remoteFilePath, localFile, true);
            Log.d("URL", "upload success @ http://"+bucketName+".b0.upaiyun.com"+remoteFilePath);

            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean upyUploadProjectImage(String localFilePath, String remoteFilePath){
        String bucketName = "tripc2c-project-pic";
        UpYun upyun = new UpYun(bucketName, OPERATOR_PROJECT, PASSWORD);
        upyun.setDebug(false);

        File localFile = new File(localFilePath);
        try {
            Log.d("URL", "to upload file: "+localFilePath+" to: "+remoteFilePath);
            boolean result = upyun.writeFile(remoteFilePath, localFile, true);
            Log.d("URL", "upload success @ http://"+bucketName+".b0.upaiyun.com"+remoteFilePath);

            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * upload to upyun
     *
     * @param localFilePath
     * @param remoteFilePath
     * @return
     */
    public static boolean upyUploadFaceImage(String localFilePath, String remoteFilePath){
        String bucketName = "tripc2c-person-face";
        UpYun upyun = new UpYun(bucketName, OPERATOR_FACE, PASSWORD);
        upyun.setDebug(false);

        File localFile = new File(localFilePath);
        try {
//            upyun.setContentMD5(UpYun.md5(localFile));
            boolean result = upyun.writeFile(remoteFilePath, localFile, true);

            Log.d("URL", "http://"+bucketName+".b0.upaiyun.com"+remoteFilePath);

            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }



    /**
     * upload to upyun with secret
     *
     * @param bucketName, bucket name configured in upyun
     * @param operator, operator user
     * @param password, operator password
     * @param localFilePath, upload file path include file name
     * @param remoteFilePath, remote fil path include file name
     * @param secret
     * @return
     */
    public static boolean upyUploadRawSecret(String bucketName, String operator, String password, String localFilePath, String remoteFilePath, String secret){

        UpYun upyun = new UpYun(bucketName, operator, password);
        upyun.setDebug(false);

        File localFile = new File(localFilePath);
        try {
            upyun.setContentMD5(UpYun.md5(localFile));
            upyun.setFileSecret(secret);
            boolean result = upyun.writeFile(remoteFilePath, localFile, true);

            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     *
     *
     * @param bucketName
     * @param operator
     * @param password
     * @param localFilePath
     * @param remoteFilePath
     *
     * @return
     */
    public static boolean upCreateThumbnail(String bucketName, String operator, String password, String localFilePath, String remoteFilePath){

        // 设置缩略图的参数
        Map<String, String> params = new HashMap<String, String>();

        // 设置缩略图类型，必须搭配缩略图参数值（KEY_VALUE）使用，否则无效
        params.put(UpYun.PARAMS.KEY_X_GMKERL_TYPE.getValue(),
                UpYun.PARAMS.VALUE_FIX_BOTH.getValue());

        // 设置缩略图参数值，必须搭配缩略图类型（KEY_TYPE）使用，否则无效
        params.put(UpYun.PARAMS.KEY_X_GMKERL_VALUE.getValue(), "150x150");

        // 设置缩略图的质量，默认 95
        params.put(UpYun.PARAMS.KEY_X_GMKERL_QUALITY.getValue(), "95");

        // 设置缩略图的锐化，默认锐化（true）
        params.put(UpYun.PARAMS.KEY_X_GMKERL_UNSHARP.getValue(), "true");

        // 若在 upyun 后台配置过缩略图版本号，则可以设置缩略图的版本名称
        // 注意：只有存在缩略图版本名称，才会按照配置参数制作缩略图，否则无效
        params.put(UpYun.PARAMS.KEY_X_GMKERL_THUMBNAIL.getValue(), "small");

        // 上传文件，并自动创建父级目录（最多10级）

        UpYun upyun = new UpYun(bucketName, operator, password);
        upyun.setDebug(false);

        File localFile = new File(localFilePath);
        try {
            upyun.setContentMD5(UpYun.md5(localFile));
            upyun.setFileSecret("bac");
            boolean result = upyun.writeFile(remoteFilePath, localFile, true, params);

            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


    }
   


}
