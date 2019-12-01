package com.ychong.kankan.ui.LocalVideo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ychong.kankan.R;
import com.ychong.kankan.entity.LocalVideoBean;
import com.ychong.kankan.entity.Material;
import com.ychong.kankan.ui.base.BaseActivity;
import com.ychong.kankan.ui.other.MoreActivity;
import com.ychong.kankan.utils.BaseUtils;
import com.ychong.kankan.utils.http.FreeObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LocalVideoActivity extends BaseActivity {
    private RecyclerView localVideoRv;
    LocalVideoAdapter adapter;
    private List<LocalVideoBean> list = new ArrayList<>();

    public static void startAct(Context context) {
        Intent intent = new Intent(context, LocalVideoActivity.class);
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

        adapter = new LocalVideoAdapter(this,list);
        localVideoRv.setLayoutManager(new LinearLayoutManager(this));
        localVideoRv.setAdapter(adapter);
        showProgressDialog(this, "正在扫描本地视频文件", false);
        Observable.fromCallable(new Callable<List<LocalVideoBean>>() {
            @Override
            public List<LocalVideoBean> call() throws Exception {
                List<LocalVideoBean> list = new ArrayList<>();
                list = BaseUtils.getVideoList(LocalVideoActivity.this);
                return list;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<LocalVideoBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<LocalVideoBean> localVideoBeans) {
                        hideProgressDialog();
                        list.addAll(localVideoBeans);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideProgressDialog();

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void initView() {
        localVideoRv = (RecyclerView) findViewById(R.id.local_video_recycler_view);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_local_video;
    }

    private void qqFile(String path) {
        File qqFile = new File(path);
        if (qqFile.exists()) {
            if (qqFile.isDirectory()) {
                //文件夹
                File[] fileArr = qqFile.listFiles();
                for (int i = 0; i < fileArr.length; i++) {
                    File file = fileArr[i];
                    if (file.isDirectory()) {
                        qqFile(file.getAbsolutePath());
                    } else {
                        if (qqFile.getName().contains(".map4")) {
                            Log.e("文件路径", qqFile.getPath());
                        }
                    }
                }
            } else {
                if (qqFile.getName().contains(".map4")) {
                    Log.e("文件路径", qqFile.getPath());
                }
            }

        }
    }
}
