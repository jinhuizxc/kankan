package com.ychong.mvvm_demo.ui

import androidx.lifecycle.ViewModel
import com.ychong.mvvm_demo.data.WeatherRepository

class MainViewModel(private val repository:WeatherRepository) : ViewModel() {
    fun isWeatherCached() = repository.isWeatherCached()

}