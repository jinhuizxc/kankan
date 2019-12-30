package com.ychong.picandvideo.ui.main.picture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.ychong.baselib.base.BaseFragment;
import com.ychong.baselib.utils.http.FreeObserver;
import com.ychong.baselib.utils.http.RetrofitUtils;
import com.ychong.picandvideo.R;
import com.ychong.picandvideo.entity.BeautyBean;
import com.ychong.picandvideo.ui.main.common.PreViewActivity;
import com.ychong.picandvideo.utils.ApiService;
import com.ychong.picandvideo.utils.BaseContract;
import com.ychong.xrecyclerview.ProgressStyle;
import com.ychong.xrecyclerview.XRecyclerView;

import org.greenrobot.greendao.annotation.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class BeautyFragment extends BaseFragment {
    private XRecyclerView mRecyclerView;
    private List<BeautyBean> listData = new ArrayList<>();
    private BeautyAdapter adapter;
    private int pages = 1;
    private Context context;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, BeautyFragment.class);
        context.startActivity(intent);
    }

    public static Fragment newInstance() {
        return new BeautyFragment();
    }

    public int getLayoutId() {
        return R.layout.fragment_beauty;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    public void initListener() {
        adapter.setClickCallBack(new BeautyAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int pos) {
                PreViewActivity.startAct(getContext(), BaseContract.PIC_TYPE, listData.get(pos).url);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(getContext().getApplicationContext()).resumeRequests();//恢复Glide加载图片
                } else {
                    Glide.with(context.getApplicationContext()).pauseRequests();//禁止Glide加载图片
                }
            }
        });

    }

    public void initData() {
        if (context == null) {
            context = getContext();
        }
        GridLayoutManager layoutManager = new GridLayoutManager(context.getApplicationContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);//避免多次绘制ui
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);//关闭默认动画

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        showProgressDialog(context, "加载数据", false);
        getBeautys(10, 1);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                listData.clear();
                getBeautys(10, 1);
                adapter.notifyDataSetChanged();
                mRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                ++pages;
                getBeautys(10, pages);
            }
        });
        adapter = new BeautyAdapter(context, listData);

        mRecyclerView.setAdapter(adapter);
    }

    private void initView(View view) {
        mRecyclerView =  view.findViewById(R.id.beauty_recycler_view);
    }

    private void getBeautys(int rows, int pages) {
        RetrofitUtils.getInstance(context).getRetrofit().create(ApiService.class)
                .getBeautys("http://gank.io/api/data/福利/" + rows + "/" + pages)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FreeObserver<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody responseBody) {
                        hideProgressDialog();
                        Snackbar.make(mRecyclerView,"数据加载完成",Snackbar.LENGTH_SHORT).show();
                        try {
                            String json = responseBody.string();
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray resultsArr = jsonObject.getJSONArray("results");
                            if (resultsArr.length() == 0) {
                                adapter.notifyDataSetChanged();
                                mRecyclerView.setNoMore(true);
                            } else {
                                for (int i = 0; i < resultsArr.length(); i++) {
                                    String beautyJson = resultsArr.getString(i);
                                    BeautyBean bean = new Gson().fromJson(beautyJson, BeautyBean.class);
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
                        hideProgressDialog();
                        Snackbar.make(mRecyclerView,"数据加载失败",Snackbar.LENGTH_SHORT).show();

                    }
                });
    }
}
