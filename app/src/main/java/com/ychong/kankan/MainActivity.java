package com.ychong.kankan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Administrator
 */
public class MainActivity extends BaseActivity {
    private ImageView ivPhoto;
    private String createTime;
    private LinearLayout headLl;
    private ImageView addIv;
    private boolean isHideBar;
    private RecyclerView recyclerView;
    private MainRecyclerAdapter adapter;
    private List<ImageBean> list = new ArrayList<>();
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initView();
        intiData();
        initListener();
    }

    private void initListener() {
        addIv.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AddActivity.class)));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    Glide.with(MainActivity.this).resumeRequests();
                }else {
                    Glide.with(MainActivity.this).pauseRequests();
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e("dx/dy",dx+"/"+dy);
//                if (dy>0&&!isHideBar){
//                    isHideBar=true;
//                    //上拉
//                    //隐藏标题栏
//                    hideTitleBar();
//                }else if (dy<0&&isHideBar){
//                    isHideBar = false;
//                    //下拉
//                    //显示标题栏
//                    showTitleBar();
//                }
            }
        });
        adapter.setOnClickListener(new MainRecyclerAdapter.OnClickListener() {
            @Override
            public void onClick(String path) {
                if ("mp4".equals(path.substring(path.lastIndexOf(".")+1))){
                    display(path);
                }
            }
        });
    }

    private void showTitleBar() {
        Log.e("titleBar","显示");
        ObjectAnimator animator = ObjectAnimator.ofFloat(headLl, "translationY", height+100);
        animator.setDuration(1000);
        animator.start();
    }

    private void hideTitleBar() {
        Log.e("titleBar","隐藏");
        ObjectAnimator animator = ObjectAnimator.ofFloat(headLl, "translationY", -height-100);
        animator.setDuration(1000);
        animator.start();
    }

    private void intiData() {
        height = headLl.getHeight();

        adapter = new MainRecyclerAdapter(this, list);
        GridLayoutManager manager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        addIv = findViewById(R.id.right_iv);
        headLl = findViewById(R.id.head_ll);
        ivPhoto = findViewById(R.id.iv_photo);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void initLayout() {
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    @SuppressLint("CheckResult")
    private void refreshData() {
        showLoadingDialog(this);
        queryAllImage();
//        Observable.create((ObservableOnSubscribe<List<ImageBean>>) emitter -> {
//            //执行耗时操作
//            List<ImageBean> imageBeans =MyApplication.getmDaoSession().getImageBeanDao().loadAll();
//            Collections.reverse(imageBeans);
//            emitter.onNext(imageBeans);
//            emitter.onComplete();
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<ImageBean>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<ImageBean> imageBeans) {
//                            list.addAll(imageBeans);
//                            adapter.notifyDataSetChanged();
//                            hideLoadingDialog();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.e("dddd","onComplete");
//                    }
//                });
    }

    private void queryAllImage() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.100:10086/") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService.queryAllImages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String json = responseBody.string();
                            Log.e("MainActivity",json);
                            JSONObject jsonObject = new JSONObject(json);
                            String msg = jsonObject.getString("msg");
                            boolean success = jsonObject.getBoolean("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ImageBean bean = new ImageBean();
                                JSONObject jsonObject1 = new JSONObject(jsonArray.getString(i));
                                bean.name = jsonObject1.getString("name");
                                bean.path = jsonObject1.getString("path");
                                bean.uploadTime = jsonObject1.getString("uploadTime");
                                bean.userId = jsonObject1.getInt("userId");
                                list.add(bean);
                            }
                            adapter.notifyDataSetChanged();

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoadingDialog();
                    }

                    @Override
                    public void onComplete() {
                        hideLoadingDialog();
                    }
                });
    }
    private void display(String path) {
        showLoadingDialog(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_video_display,null);
        VideoView displayVv = view.findViewById(R.id.display_vv);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        Uri uri = Uri.parse(path);
        displayVv.setOnCompletionListener(mediaPlayer -> {
            hideLoadingDialog();
            dialog.dismiss();
        });
        displayVv.setMediaController(new MediaController(this));
        displayVv.setVideoURI(uri);
        displayVv.start();
    }
}
