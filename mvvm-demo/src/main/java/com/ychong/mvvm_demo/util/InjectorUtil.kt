package com.ychong.mvvm_demo.util

import com.ychong.mvvm_demo.data.PlaceRepository
import com.ychong.mvvm_demo.ui.MainModelFactory
import com.ychong.mvvm_demo.data.WeatherRepository
import com.ychong.mvvm_demo.data.db.CoolWeatherDatabase
import com.ychong.mvvm_demo.data.network.CoolWeatherNetwork
import com.ychong.mvvm_demo.ui.area.ChooseAreaModelFactory
import com.ychong.mvvm_demo.ui.weather.WeatherModelFactory

object InjectorUtil {
    fun getMainModelFactory() = MainModelFactory(getWeatherRepository())
    private fun getWeatherRepository() = WeatherRepository.getInstance(CoolWeatherDatabase.getWeatherDao(), CoolWeatherNetwork.getInstance())

    fun getChooseAreaModelFactory() = ChooseAreaModelFactory(getPlaceRepository())
    private fun getPlaceRepository() = PlaceRepository.getInstance(CoolWeatherDatabase.getPlaceDao(),CoolWeatherNetwork.getInstance())

    fun getWeatherModelFactory() = WeatherModelFactory(getWeatherRepository())
}