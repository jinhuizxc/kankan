package com.ychong.kankan.ui.webbrowse;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.ychong.kankan.R;
import com.ychong.kankan.ui.base.BaseActivity;

public class WebBrowseActivity  extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initView();
        initData();
        initListener();
    }

    private void initListener() {

    }

    private void initData() {

    }

    private void initView() {

    }

    private void initLayout() {
        setContentView(R.layout.activity_web_browse);
    }
}
