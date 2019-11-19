package com.ychong.kankan.ui.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ychong.kankan.R;
import com.ychong.kankan.utils.BarUtils;
import com.ychong.kankan.utils.widget.dialog.ProgressDialog;

/**
 * Activity 基类
 */
@SuppressLint("Registered")
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        BarUtils.setStatusBarColor(this,getResources().getColor(R.color.green));
    }


    public void showText(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }


    ProgressDialog dialog;
    public void showProgressDialog(Context context,String tips, boolean isCancel){
        if (dialog == null){
            dialog = new ProgressDialog(context,tips);
            dialog.setCanceledOnTouchOutside(isCancel);
            dialog.setCancelable(isCancel);
            dialog.show();
        }
    }
    public void hideProgressDialog(){
        if (dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
    }
    public abstract int getLayoutId();
}
