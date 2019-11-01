package com.ychong.kankan.test;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ychong.kankan.R;
import com.ychong.kankan.ui.base.BaseActivity;
import com.ychong.kankan.utils.BaseUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView刷新仿照  https://github.com/xinzhazha/RecyclerView
 */
public class TestActivity extends BaseActivity{

    private TestAdapter mAdapter;
    private TestRecyclerView testRecyclerView;
    private int page=1;
    private Handler mHandler;

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
        mHandler = new Handler();
        mAdapter = new TestAdapter(this);
        //添加Header
        final TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, BaseUtils.dip2px(48)));
        textView.setTextSize(16);
        textView.setGravity(Gravity.CENTER);
        textView.setText("顶部视图");
        mAdapter.setHeader(textView);
        //添加footer
        final TextView footer = new TextView(this);
        footer.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, BaseUtils.dip2px(48)));
        footer.setTextSize(16);
        footer.setGravity(Gravity.CENTER);
        footer.setText("底部视图");
        mAdapter.setFooter(footer);

        testRecyclerView.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        testRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        testRecyclerView.setAdapter(mAdapter);
        testRecyclerView.addRefreshAction(()->getData(true));
        testRecyclerView.addLoadMoreAction(()->{
            getData(false);
            page++;
        });

        testRecyclerView.post(()->{
            testRecyclerView.showSwipeRefresh();
            getData(true);
        });


    }

    private void getData(boolean isRefresh) {
        mHandler.postDelayed(() -> {
            if (isRefresh) {
                page = 1;
                mAdapter.clear();
                mAdapter.addAll(getVirtualData());
                testRecyclerView.dismissSwipeRefresh();
                testRecyclerView.getRecyclerView().scrollToPosition(0);
            } else if (page == 3) {
                mAdapter.showLoadMoreError();
            } else {
                mAdapter.addAll(getVirtualData());
                if (page >= 5) {
                    testRecyclerView.showNoMore();
                }
            }
        }, 1500);
    }

    private String[] getVirtualData() {
        return new String[]{
                "1",
                "1",
                "1",
                "1",
                "1",
                "1",
                "1",
                "1",
                "1",
                "1",
                "1"

        };
    }

    private void initView() {
        testRecyclerView = findViewById(R.id.test_recycler_view);


    }

    private void initLayout() {
        setContentView(R.layout.activity_test);

    }

}
