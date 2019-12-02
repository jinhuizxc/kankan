package com.ychong.baselib.utils.http;

import android.content.Context;
import android.content.SharedPreferences;

import com.ychong.baselib.utils.BaseContract;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {
    private static RetrofitUtils Instance;
    private final Retrofit retrofit;

    public static RetrofitUtils getInstance(Context context){
        if (Instance == null){
            synchronized (RetrofitUtils.class){
                if (Instance == null){
                    Instance= new RetrofitUtils(context.getApplicationContext());
                }
            }
        }
        return Instance;
    }
    public RetrofitUtils(Context context){
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
         retrofit = new Retrofit.Builder()
                .baseUrl(sp.getString("server_host_url", BaseContract.SERVER_HOST_URL)) //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
    public Retrofit getRetrofit(){
        return retrofit;
    }
}
