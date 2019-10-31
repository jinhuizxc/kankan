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
import com.ychong.kankan.utils.widget.dialog.ProgressDialog;

/**
 * Activity 基类
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    private LinearLayout mBaseView;
    private View actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initStatusBar() {
        //改变statusbar的颜色
        actionBar = mBaseView.findViewById(R.id.action_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            actionBar.setVisibility(ViewGroup.VISIBLE);
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,    //设置StatusBar透明
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int id = 0;
            id = getResources().getIdentifier("status_bar_height", "dimen",    //获取状态栏的高度
                    "android");
            if (id > 0) {
                actionBar.getLayoutParams().height = getResources()    //设置状态栏的高度
                        .getDimensionPixelOffset(id);
            }
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mBaseView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.actionbar_main_layout, null);//状态栏
        LayoutInflater.from(this).inflate(layoutResID, mBaseView);    //把状态栏添加到主视图
        setContentView(mBaseView);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initStatusBar();
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
}
