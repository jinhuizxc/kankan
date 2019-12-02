package com.ychong.kankan.utils;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiService {
    /**
     * 登录
     * @param body
     * @return
     */
    @POST("kankan/user/login")
    Observable<ResponseBody> login(@Body RequestBody body);

    /**
     * 注册
     * @param body
     * @return
     */
    @POST("kankan/user/register")
    Observable<ResponseBody> register(@Body RequestBody body);
    @POST("kankan/image/insert")
    Observable<ResponseBody> insertImage(@Body RequestBody body);
    @GET("kankan/image/query")
    Observable<ResponseBody> queryAllImages();

    //测试
    //http://192.168.0.12:8033/GPSMetroApi/PatrolManages/PatrolTaskDown
    @POST("GPSMetroApi/PatrolManages/PatrolTaskDown")
    Observable<ResponseBody> PatrolTaskDown(@Body RequestBody body);

    @POST("api/ProjectManage/"+"GetIllegalProjectGrid")
    Observable<ResponseBody> GetIllegalProjectGrid(@Body RequestBody body);

    @GET
    Observable<ResponseBody> getBeautys(@Url String url);
}
