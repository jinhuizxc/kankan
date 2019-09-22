package com.ychong.kankan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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

    Dialog dialog;

    public void showLoadingDialog(Activity activity) {
        if (dialog==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.dialog);
            View view = LayoutInflater.from(activity).inflate(R.layout.dialog_progress_bar, null);
            builder.setView(view);
            dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        }
    }
    public void hideLoadingDialog(){
        if (dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public void showText(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }
}
