package com.redoct.iclub.widget;


import com.redoct.iclub.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;



public class Common2BtnDialog extends Dialog
{
    
    private Button btLeft;
    
    private Button btRight;
    
    private TextView tvTilte;
    
    private TextView tvMsg;
    
    private String title;
    
    private String msg;
    
    private String leftContent;
    
    private String rightContent;
    
    private View.OnClickListener onLeftClick;
    
    private View.OnClickListener onRightClick;
    
    public Common2BtnDialog(Context context, boolean cancelable,
            OnCancelListener cancelListener)
    {
        super(context, cancelable, cancelListener);
    }
    
    public Common2BtnDialog(Context context, int theme)
    {
        super(context, theme);
    }
    
    public Common2BtnDialog(Context context, String title, String msg,
            String leftContent, String rightCotent,
            View.OnClickListener onLeftClick, View.OnClickListener onRightClick)
    {
        super(context);
        this.title = title;
        this.msg = msg;
        this.leftContent = leftContent;
        this.rightContent = rightCotent;
        this.onLeftClick = onLeftClick;
        this.onRightClick = onRightClick;
    }
    
    //    public void setInfo(String title,String msg,View.OnClickListener onLeftClick,View.OnClickListener onRightClick)
    //    {
    //        tvTilte.setText(title);
    //        tvMsg.setText(msg);
    //        
    //        if (onLeftClick != null)
    //        {
    //            btLeft.setOnClickListener(onLeftClick);
    //        }
    //        
    //        if (onRightClick != null)
    //        {
    //            btRight.setOnClickListener(onRightClick);
    //        }
    //    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_comm);
        
        tvTilte = (TextView) findViewById(R.id.tv_dialog_title);
        tvTilte.setText(title);
        
        tvMsg = (TextView) findViewById(R.id.tv_dialog_msg);
        tvMsg.setText(msg);
        
        btLeft = (Button) findViewById(R.id.bt_dialog_left);
        if (onLeftClick != null)
        {
            btLeft.setOnClickListener(onLeftClick);
        }
        else
        {
            btLeft.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Common2BtnDialog.this.dismiss();
                }
            });
        }
        
        btRight = (Button) findViewById(R.id.bt_dialog_right);
        if (onRightClick != null)
        {
            btRight.setOnClickListener(onRightClick);
        }
        else
        {
            btRight.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Common2BtnDialog.this.dismiss();
                }
            });
        }
        
        if (TextUtils.isEmpty(leftContent) == false)
        {
            btLeft.setText(leftContent);
        }
        
        if (TextUtils.isEmpty(rightContent) == false)
        {
            btRight.setText(rightContent);
        }
        
    }
    
}
