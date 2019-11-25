package com.ychong.kankan.test;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.ychong.kankan.R;
import com.ychong.kankan.ui.base.BaseActivity;
import com.ychong.kankan.utils.BaseUtils;
import com.ychong.kankan.utils.http.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * RecyclerView刷新仿照  https://github.com/xinzhazha/RecyclerView
 */
public class TestActivity extends BaseActivity {

    private ViolationProjectListAdapter mAdapter;
    private TestRecyclerView testRecyclerView;
    private int page = 1;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initView();
        initData();
        initListener();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    private void initListener() {

    }

    private void initData() {
        mHandler = new Handler();
        mAdapter = new ViolationProjectListAdapter(this);
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
        testRecyclerView.addRefreshAction(() -> getData(true));
        testRecyclerView.addLoadMoreAction(() -> {
            getData(false);
            page++;
        });

        testRecyclerView.post(() -> {
            testRecyclerView.showSwipeRefresh();
            getData(true);
        });


    }

    private void getData(boolean isRefresh) {
            if (isRefresh) {
                page = 1;
                getIllegalProjectGrid();
            } else if (page == 3) {
                mAdapter.showLoadMoreError();
            } else {
                getIllegalProjectGrid();
                if (page >= 5) {
                    testRecyclerView.showNoMore();
                }
            }
    }

    private void initView() {
        testRecyclerView = (TestRecyclerView) findViewById(R.id.test_recycler_view);


    }

    private void initLayout() {
        setContentView(R.layout.activity_test);

    }

    public void getIllegalProjectGrid() {

        HashMap<String,Object> map = new HashMap<>();
        map.put("PublicKey","60168879-A4FB-4975-ADE0-AFFB9518D435");
        HashMap<String,Object> dataMap = new HashMap<>();
        dataMap.put("page",page);
        dataMap.put("rows",1);
        map.put("Data",dataMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(map));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://PC-20170901ZKBV:8301") //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService.GetIllegalProjectGrid(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    JSONArray rowsArr = new JSONObject(json).getJSONObject("Data").getJSONArray("rows");
                    List<ViolationResponse> list = new ArrayList<>();
                    for (int i=0;i<rowsArr.length();i++){
                        ViolationResponse response = new Gson().fromJson(rowsArr.getString(i),ViolationResponse.class);
                        list.add(response);
                    }
                    mAdapter.clear();
                    mAdapter.addAll(list);
                    testRecyclerView.dismissSwipeRefresh();
                    testRecyclerView.getRecyclerView().scrollToPosition(0);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e("error", e.toString());
                hideProgressDialog();
                showText("登录失败");
            }

            @Override
            public void onComplete() {
                hideProgressDialog();
            }
        });
    }

}
