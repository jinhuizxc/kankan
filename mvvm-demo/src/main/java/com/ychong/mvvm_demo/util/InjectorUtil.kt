package com.ychong.mvvm_demo.util

import androidx.lifecycle.ViewModelProvider
import com.ychong.mvvm_demo.MainModelFactory
import com.ychong.mvvm_demo.data.WeatherRepository

object InjectorUtil {
    fun getMainModelFactory() = MainModelFactory(getWeatherRepository())
    private fun getWeatherRepository() = WeatherRepository.getInstance(CoolWeatherDatabase.getWeatherDao(),CoolWeatherNetwork.getInstance())


}