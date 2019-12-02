package com.ychong.picandvideo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.ychong.baselib.base.BaseActivity;
import com.ychong.picandvideo.ui.main.SectionsPagerAdapter;

public class PVMainActivity extends BaseActivity {
    private ImageView backIv;
    private TextView titleTv;
    private ViewPager viewPager;
    private TabLayout tabs;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, PVMainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        backIv.setOnClickListener(v -> onBackPressed());
    }

    private void initData() {
        titleTv.setText("图片与视频");
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);
    }

    private void initView() {
        backIv = (ImageView) findViewById(R.id.left_iv);
        titleTv = (TextView) findViewById(R.id.title_tv);
        viewPager = (ViewPager) findViewById(R.id.pv_view_pager);
         tabs = (TabLayout) findViewById(R.id.tabs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main_pv;
    }
}