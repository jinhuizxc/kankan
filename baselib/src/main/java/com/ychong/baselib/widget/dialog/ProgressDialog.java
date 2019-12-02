package com.ychong.baselib.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ychong.baselib.R;

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
            Window window = getWindow();
            if (window!=null){
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
    }

    private void initView() {
        tipsTv = findViewById(R.id.tips_tv);
    }

    private void initLayout() {
        setContentView(R.layout.dialog_progress_bar);
    }
}
