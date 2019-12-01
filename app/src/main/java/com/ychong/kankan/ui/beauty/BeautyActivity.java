package com.ychong.kankan.ui.beauty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ychong.kankan.R;
import com.ychong.kankan.entity.BeautyBean;
import com.ychong.kankan.ui.base.BaseActivity;
import com.ychong.kankan.ui.common.PreViewActivity;
import com.ychong.kankan.ui.other.MoreActivity;
import com.ychong.kankan.utils.BaseContract;
import com.ychong.kankan.utils.http.FreeObserver;
import com.ychong.kankan.utils.http.RetrofitUtils;
import com.ychong.xrecyclerview.ProgressStyle;
import com.ychong.xrecyclerview.XRecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class BeautyActivity extends BaseActivity {
    private XRecyclerView mRecyclerView;
    private List<BeautyBean> listData = new ArrayList<>();
    private BeautyAdapter adapter;
    private int pages = 1;

    public static void startAct(Context context) {
        Intent intent = new Intent(context,BeautyActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_beauty;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        adapter.setClickCallBack(new BeautyAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int pos) {
                PreViewActivity.startAct(BeautyActivity.this, BaseContract.PIC_TYPE,listData.get(pos).url);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(getApplicationContext()).resumeRequests();//恢复Glide加载图片
                }else {
                    Glide.with(getApplicationContext()).pauseRequests();//禁止Glide加载图片
                }
            }
        });

    }

    private void initData() {

        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);//避免多次绘制ui
        ((SimpleItemAnimator)mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);//关闭默认动画

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        getBeautys(10,1);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                listData.clear();
                getBeautys(10,1);
                adapter.notifyDataSetChanged();
                mRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                ++pages;
                getBeautys(10,pages);
            }
        });
        adapter = new BeautyAdapter(this,listData);

        mRecyclerView.setAdapter(adapter);
    }

    private void initView() {
        mRecyclerView = (XRecyclerView)this.findViewById(R.id.beauty_recycler_view);
    }

    private void getBeautys(int rows,int pages){
        RetrofitUtils.getInstance()
                .getApiService()
                .getBeautys("http://gank.io/api/data/福利/"+rows+"/"+pages)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new FreeObserver<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray resultsArr = jsonObject.getJSONArray("results");
                    if (resultsArr.length()==0){
                        adapter.notifyDataSetChanged();
                        mRecyclerView.setNoMore(true);
                    }else {
                        for (int i=0;i<resultsArr.length();i++){
                            String beautyJson = resultsArr.getString(i);
                            BeautyBean bean = new Gson().fromJson(beautyJson,BeautyBean.class);
                            listData.add(bean);
                        }
                        mRecyclerView.loadMoreComplete();
                        adapter.notifyDataSetChanged();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}
