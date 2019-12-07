package com.ychong.mvvm_demo.data

import com.ychong.mvvm_demo.data.db.WeatherDao
import com.ychong.mvvm_demo.data.model.Weather
import com.ychong.mvvm_demo.data.network.CoolWeatherNetwork

class WeatherRepository private constructor(private val weatherDao:WeatherDao,private val network: CoolWeatherNetwork){
    suspend fun getWeather(weatherId:String):Weather{
        var weather =  weatherDao.getCachedWeatherInfo()
        if (weather == null){
            weather = requestWeather(weatherId)
        }
        return weather
    }

    private suspend fun requestWeather(weatherId: String){
        val heWeather = network.fetchWeather(weatherId)
        

    }

}