package com.ychong.mvvm_demo.data.db

import android.preference.PreferenceManager
import com.google.gson.Gson
import com.ychong.mvvm_demo.CoolWeatherApplication
import com.ychong.mvvm_demo.data.model.Weather

class WeatherDao{
    fun cacheWeatherInfo(weather: Weather?){
        if (weather == null)return
    }

    fun getCachedWeatherInfo():Weather?{
        val weatherInfo = PreferenceManager.getDefaultSharedPreferences(CoolWeatherApplication.context)
                .getString("weather",null)
        if (weatherInfo!=null){
            return Gson().fromJson(weatherInfo,Weather::class.java)
        }
        return null
    }
}