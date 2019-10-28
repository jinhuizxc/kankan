package com.ychong.kankan.map.baidu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.ychong.kankan.ui.BaseActivity;

public class NavigationActivity extends Activity {
    private WalkNavigateHelper mNaviHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initView();
        initData();
        initListener();
    }

    private void initData() {
        //获取WalkNavigateHelper实例
         mNaviHelper = WalkNavigateHelper.getInstance();
        //获取诱导页面地图展示View
        View view = mNaviHelper.onCreate(this);
        if (view != null) {
            setContentView(view);
        }
        mNaviHelper.startWalkNavi(NavigationActivity.this);
    }

    private void initListener() {

    }

    private void initView() {

    }

    private void initLayout() {

    }
    @Override
    protected void onResume() {
        super.onResume();
        mNaviHelper.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNaviHelper.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNaviHelper.quit();
    }

}
