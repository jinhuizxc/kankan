package com.ychong.kankan.ui.androidserver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.ychong.kankan.R;
import com.ychong.kankan.entity.ApkInfoBean;
import com.ychong.kankan.entity.EventBusMessage;
import com.ychong.kankan.ui.base.BaseActivity;
import com.ychong.kankan.utils.BaseUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AndroidServerActivity extends BaseActivity {
    private AsyncHttpServer server = new AsyncHttpServer();
    private AsyncServer mAsyncServer = new AsyncServer();
    private TextView localIpTv;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private AndroidServerAdapter adapter;
    private List<ApkInfoBean> apkInfoBeanList = new ArrayList<>();

    public static void startAct(Context context) {
        Intent intent = new Intent(context, AndroidServerActivity.class);
        context.startActivity(intent);
    }

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
        localIpTv.setText(BaseUtils.getIpStr());
        initRecyclerView();

    }

    private void initRecyclerView() {
        adapter = new AndroidServerAdapter(this, apkInfoBeanList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        refreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        recyclerView.addItemDecoration(new ItemButtomDecoration(this, 10));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        localIpTv = findViewById(R.id.local_ip_tv);

        recyclerView = findViewById(R.id.recyclerView);
        refreshLayout = findViewById(R.id.refreshLayout);

    }

    private void initLayout() {
        setContentView(R.layout.activity_android_server);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusCallBack(EventBusMessage message){
        String msg = message.msg;
        switch (msg){
            case "load":
                adapter.notifyDataSetChanged();
                break;
                default:
                    break;
        }

    }

    private void refreshList(){
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (server != null) {
            server.stop();
        }
        if (mAsyncServer != null) {
            mAsyncServer.stop();
        }
    }
}
