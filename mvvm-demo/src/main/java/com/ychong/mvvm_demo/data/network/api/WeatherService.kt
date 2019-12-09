package com.ychong.mvvm_demo.data.network.api

import com.ychong.mvvm_demo.data.model.HeWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("api/weather")
    fun getWeather(@Query("cityid") weatherId:String): Call<HeWeather>

    @GET("api/bing_pic")
    fun getBingPic():Call<String>
}