package com.ychong.mvvm_demo.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ychong.mvvm_demo.data.WeatherRepository

@Suppress("UNCHECKED_CAST")
class WeatherModelFactory (private val repository:WeatherRepository): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass : Class<T>):T{
        return WeatherViewModel(repository) as T
    }
}