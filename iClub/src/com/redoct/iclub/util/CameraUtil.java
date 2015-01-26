package com.redoct.iclub.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;


public class CameraUtil
{
    @SuppressLint("NewApi") public static boolean checkCheckCamera(Context context)
    {
        int nums = Camera.getNumberOfCameras();
        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) == false || nums < 1)
        {
            return false;
        }
        
        return true;
    }
}
