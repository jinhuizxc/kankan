package com.ychong.mvvm_demo.data.network

import android.telecom.Call
import android.util.Log
import com.ychong.mvvm_demo.data.network.api.PlaceService
import com.ychong.mvvm_demo.data.network.api.WeatherService
import okhttp3.Response
import javax.security.auth.callback.Callback
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CoolWeatherNetwork {
    suspend fun fetchWeather(weatherId:String) = weatherService.getWeather(weatherId).await()
    private val weatherService = ServiceCreator.create(WeatherService::class.java)

    suspend fun fetchProvinceList() = placeService.getProvinces().await()
    private val placeService = ServiceCreator.create(PlaceService::class.java)

    suspend fun fetchCityList(provinceId:Int) = placeService.getCities(provinceId).await()
    suspend fun fetchCountyList(provinceId:Int,cityId:Int) = placeService.getCounties(provinceId,cityId).await()

    suspend fun fetchBingPic() = weatherService.getBingPic().await()

    private suspend fun <T> retrofit2.Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : retrofit2.Callback<T> {
                override fun onFailure(call: retrofit2.Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: retrofit2.Call<T>, response: retrofit2.Response<T>) {
                    val body = response.body()
                    Log.e("json",body.toString())
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }
            })
        }

    }
    companion object{
        private var network:CoolWeatherNetwork?=null
        fun getInstance():CoolWeatherNetwork{
            if (network ==  null){
                synchronized(CoolWeatherNetwork::class.java){
                    if (network==null){
                        network =  CoolWeatherNetwork()
                    }
                }
            }
          return network!!
        }
    }
}