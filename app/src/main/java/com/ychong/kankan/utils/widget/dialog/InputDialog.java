package com.ychong.kankan.utils.widget.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.DialogTitle;

import com.ychong.kankan.R;

/**
 * 自定义输入弹窗
 */
@SuppressLint("RestrictedApi")
public class InputDialog extends Dialog {
    private String content;
    private DialogListener listener;
    private String title;
    private String confirmStr;
    private String cancelStr;

    private TextView titleTv;
    private EditText contentEt;
    private TextView confirmTv;
    private TextView cancelTv;

    public InputDialog(Context context,String defaultValue,DialogListener listener){
        super(context);
        this.content = defaultValue;
        this.listener = listener;

    }
    public InputDialog(Context context,String title,String defaultValue,DialogListener listener){
        super(context);
        this.title = title;
        this.content = defaultValue;
        this.listener = listener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        confirmTv.setOnClickListener(view -> confirmDialog());
        cancelTv.setOnClickListener(view -> cancelDialog());
    }

    private void cancelDialog() {
        dismiss();
    }

    private void confirmDialog() {
        this.content = contentEt.getText().toString();
        listener.confirm(content);
        dismiss();
    }

    private void initData() {
        if (!TextUtils.isEmpty(title)){
            titleTv.setText(title);
        }
        if (!TextUtils.isEmpty(content)){
            contentEt.setText(content);
        }
        if (!TextUtils.isEmpty(confirmStr)){
            confirmTv.setText(confirmStr);
        }
        if (!TextUtils.isEmpty(cancelStr)){
            cancelTv.setText(cancelStr);
        }
//
//        //获取当前Activity所在的窗体
//        Window dialogWindow = getWindow();
//        //设置Dialog从窗体底部弹出
//        dialogWindow.setGravity(Gravity.BOTTOM);
//        //获得窗体的属性
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.y = 40;//设置Dialog距离底部的距离
//
//        //将属性设置给窗体
//        dialogWindow.setAttributes(lp);
    }

    private void initView() {
        titleTv = findViewById(R.id.title_tv);
        contentEt = findViewById(R.id.content_et);
        confirmTv = findViewById(R.id.confirm_tv);
        cancelTv = findViewById(R.id.cancel_tv);
    }

    private void initLayout() {
        setContentView(R.layout.dialog_input);
    }

    public InputDialog setTitle(String title){
        this.title = title;
        return this;
    }
    public InputDialog setConfirm(String confirmStr){
        this.confirmStr = confirmStr;
        return this;
    }
    public InputDialog setCancelStr(String cancelStr){
        this.cancelStr = cancelStr;
        return this;
    }
}
