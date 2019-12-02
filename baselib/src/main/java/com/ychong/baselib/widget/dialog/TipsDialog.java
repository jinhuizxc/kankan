package com.ychong.baselib.widget.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.ychong.baselib.R;

/**
 * 自定义输入弹窗
 */
@SuppressLint("RestrictedApi")
public class TipsDialog extends Dialog {
    private String tips;
    private TipsDialogListener listener;
    private String title;
    private String confirmStr;
    private String cancelStr;

    private TextView titleTv;
    private TextView tipsTv;
    private TextView confirmTv;
    private TextView cancelTv;

    public TipsDialog(Context context, String tips, TipsDialogListener listener) {
        super(context);
        this.tips = tips;
        this.listener = listener;

    }

    public TipsDialog(Context context, String title, String tips, TipsDialogListener listener) {
        super(context);
        this.title = title;
        this.tips = tips;
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
        listener.onClick(false);
        dismiss();
    }

    private void confirmDialog() {
        listener.onClick(true);
        dismiss();
    }

    private void initData() {
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }
        if (!TextUtils.isEmpty(tips)) {
            tipsTv.setText(tips);
        }
        if (!TextUtils.isEmpty(confirmStr)) {
            confirmTv.setText(confirmStr);
        }
        if (!TextUtils.isEmpty(cancelStr)) {
            cancelTv.setText(cancelStr);
        }
    }

    private void initView() {
        titleTv = findViewById(R.id.title_tv);
        tipsTv = findViewById(R.id.tips_tv);
        confirmTv = findViewById(R.id.confirm_tv);
        cancelTv = findViewById(R.id.cancel_tv);
    }

    private void initLayout() {
        setContentView(R.layout.dialog_tips);
    }

    public TipsDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public TipsDialog setConfirm(String confirmStr) {
        this.confirmStr = confirmStr;
        return this;
    }

    public TipsDialog setCancelStr(String cancelStr) {
        this.cancelStr = cancelStr;
        return this;
    }
}
