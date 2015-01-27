package com.redoct.iclub.util;

import com.redoct.iclub.R;
import com.redoct.iclub.widget.Common2BtnDialog;
import com.redoct.iclub.widget.LoadingProgressDialog;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.view.View.OnClickListener;



public final class DialogUtil
{
    private DialogUtil()
    {
        
    }
    
   
    public static Dialog getWaittingDialog(Context ctx, int iconId)
    {
        return getNoBtnDialog(ctx,
                ctx.getString(R.string.dialog_waitting_title),
                ctx.getString(R.string.dialog_waitting_msg),
                iconId);
    }
    
   
    public static Dialog getNoBtnDialog(Context ctx, String title, String msg,
            int iconId)
    {
        Builder builder = new Builder(ctx);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setIcon(R.drawable.ic_launcher);
        return builder.create();
    }
    
    
    public static LoadingProgressDialog getLoadDialog(Context ctx,
            String message)
    {
        return new LoadingProgressDialog(ctx, message);
    }
    
    public static Common2BtnDialog getCommon2BtnDialog(Context ctx,
            String title, String msg, String leftContent, String rightContent,
            OnClickListener onLeftClick, OnClickListener onRightClick)
    {
        if (ctx != null)
        {
            Common2BtnDialog dialog = new Common2BtnDialog(ctx, title, msg,
                    leftContent, rightContent, onLeftClick, onRightClick);
            //            dialog.setInfo(title, msg, onLeftClick, onRightClick);
            return dialog;
        }
        else
        {
            return null;
        }
    }
}
