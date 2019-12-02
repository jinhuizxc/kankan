package com.ychong.picandvideo.ui.main.video;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ychong.baselib.base.BaseActivity;
import com.ychong.baselib.base.BaseFragment;
import com.ychong.baselib.utils.http.FreeObserver;
import com.ychong.picandvideo.R;
import com.ychong.picandvideo.entity.LocalVideoBean;
import com.ychong.picandvideo.utils.BaseUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LocalVideoFragment extends BaseFragment {
    private RecyclerView localVideoRv;
    private LocalVideoAdapter adapter;
    private Context context;
    private List<LocalVideoBean> list = new ArrayList<>();

    public static void startAct(Context context) {
        Intent intent = new Intent(context, LocalVideoFragment.class);
        context.startActivity(intent);
    }

    public static Fragment newInstance() {
        return new LocalVideoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_video, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    @Override
    protected void initListener() {

    }

    public void initData() {
        if (context==null){
            context = getContext();
        }

        adapter = new LocalVideoAdapter(context, list);
        localVideoRv.setLayoutManager(new LinearLayoutManager(context));
        localVideoRv.setAdapter(adapter);
        showProgressDialog(context, "正在扫描本地视频文件", false);
        Observable.fromCallable(new Callable<List<LocalVideoBean>>() {
            @Override
            public List<LocalVideoBean> call() throws Exception {
                return BaseUtils.getVideoList(context.getApplicationContext());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FreeObserver<List<LocalVideoBean>>() {
                    @Override
                    public void onSuccess(List<LocalVideoBean> localVideoBeans) {
                        hideProgressDialog();
                        list.addAll(localVideoBeans);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String message) {
                        hideProgressDialog();
                    }
                });

    }

    private void initView(View view) {
        localVideoRv =  view.findViewById(R.id.local_video_recycler_view);
    }

    public int getLayoutId() {
        return R.layout.fragment_local_video;
    }
}
