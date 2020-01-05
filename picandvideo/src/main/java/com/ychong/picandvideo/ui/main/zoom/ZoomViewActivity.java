package com.ychong.picandvideo.ui.main.zoom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.ychong.baselib.base.BaseActivity;
import com.ychong.picandvideo.R;
import com.ychong.picandvideo.utils.BigView;

public class ZoomViewActivity extends BaseActivity {
    private BigView bigView;

    public static void startAct(Context context){
        Intent intent = new Intent(context,ZoomViewActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initListener() {

    }

    private void initData() {

    }

    private void initView() {
        bigView = (BigView) findViewById(R.id.bigView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_zoom_view;
    }
}
