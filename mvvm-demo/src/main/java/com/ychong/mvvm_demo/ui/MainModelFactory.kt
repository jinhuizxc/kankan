package com.ychong.mvvm_demo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ychong.mvvm_demo.data.WeatherRepository

@Suppress("UNCHECKED_CAST")
class MainModelFactory(private val repository: WeatherRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}