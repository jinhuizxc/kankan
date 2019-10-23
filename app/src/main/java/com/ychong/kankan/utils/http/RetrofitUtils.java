package com.ychong.kankan.utils.http;

import com.ychong.kankan.utils.BaseContract;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {
    private static RetrofitUtils Intance;
    private final Retrofit retrofit;

    public static RetrofitUtils getInstance(){
        if (Intance == null){
            synchronized (RetrofitUtils.class){
                if (Intance == null){
                    Intance= new RetrofitUtils();
                }
            }
        }
        return Intance;
    }
    public RetrofitUtils(){
         retrofit = new Retrofit.Builder()
                .baseUrl(BaseContract.SERVER_HOST_URL) //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public ApiService getApiService(){
        return retrofit.create(ApiService.class);
    }
}
