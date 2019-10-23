package com.ychong.kankan.utils.http;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

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
}
