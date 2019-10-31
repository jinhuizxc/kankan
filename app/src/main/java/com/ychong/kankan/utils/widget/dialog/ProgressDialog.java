package com.ychong.kankan.utils.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;

import com.ychong.kankan.R;

/**
 * 加载弹窗
 */
public class ProgressDialog extends Dialog {
    private TextView tipsTv;
    private String tips;
    public ProgressDialog(Context context,String tips){
        super(context);
        this.tips = tips;
    }

    public ProgressDialog(@NonNull Context context) {
        super(context);
    }

    protected ProgressDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
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

    }

    private void initData() {
        tipsTv.setText(tips);
    }

    private void initView() {
        tipsTv = findViewById(R.id.tips_tv);
    }

    private void initLayout() {
        setContentView(R.layout.dialog_progress_bar);
    }
}
