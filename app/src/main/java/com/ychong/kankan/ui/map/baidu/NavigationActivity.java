package com.ychong.kankan.ui.map.baidu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
public class NavigationActivity extends Activity {
    private WalkNavigateHelper mWalkNavigationHelper;
    private BikeNavigateHelper mBikeNavigationHelper;
    private int navigationType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initView();
        initData();
        initListener();
    }

    private void initData() {
        navigationType = getIntent().getIntExtra("navigationType",-1);
        switch (navigationType){
            case 0:
                //获取WalkNavigateHelper实例
                mWalkNavigationHelper = WalkNavigateHelper.getInstance();
                //获取诱导页面地图展示View
                View walkView = mWalkNavigationHelper.onCreate(this);
                if (walkView != null) {
                    setContentView(walkView);
                }
                mWalkNavigationHelper.startWalkNavi(NavigationActivity.this);
                //步行
                break;
            case 1:
                //获取BikeNavigateHelper实例
                mBikeNavigationHelper = BikeNavigateHelper.getInstance();
                //获取诱导页面地图展示View
                View bikeView = mBikeNavigationHelper.onCreate(this);
                if (bikeView != null) {
                    setContentView(bikeView);
                }
                mBikeNavigationHelper.startBikeNavi(NavigationActivity.this);
                break;
                default:
                    break;
        }

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
        if (navigationType==BaiDuMapActivity.WALK_TYPE){
            //步行
            mWalkNavigationHelper.resume();
        }else if (navigationType == BaiDuMapActivity.RADING_TYPE){
            //骑行
            mBikeNavigationHelper.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (navigationType==BaiDuMapActivity.WALK_TYPE){
            //步行
            mWalkNavigationHelper.pause();
        }else if (navigationType == BaiDuMapActivity.RADING_TYPE){
            //骑行
            mBikeNavigationHelper.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (navigationType==BaiDuMapActivity.WALK_TYPE){
            //步行
            mWalkNavigationHelper.quit();
        }else if (navigationType == BaiDuMapActivity.RADING_TYPE){
            //骑行
            mBikeNavigationHelper.quit();
        }
    }

}
