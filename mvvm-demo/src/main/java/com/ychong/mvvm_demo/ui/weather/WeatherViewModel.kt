package com.ychong.mvvm_demo.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ychong.mvvm_demo.data.WeatherRepository
import com.ychong.mvvm_demo.data.model.Weather
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) :ViewModel(){
    var weather = MutableLiveData<Weather>()

    var bingPicUrl = MutableLiveData<String>()

    var refreshing = MutableLiveData<Boolean>()

    var weatherInitialized = MutableLiveData<Boolean>()

    var weatherId = ""

    fun getWeather(){
        launch({
            weather.value = repository.getWeather(weatherId)
            weatherInitialized.value = true
        },{

        })
        getBingPic(false)
    }

    fun refreshWeather(){
        refreshing.value = true
        launch({
            weather.value = repository.refreshWeather(weatherId)
            refreshing.value = false
            weatherInitialized.value = true
        },{
            refreshing.value = false
        })
    }
    fun isWeatherCached() = repository.isWeatherCached()

    fun getCachedWeather() = repository.getCachedWeather()

    fun onRefresh(){
        refreshWeather()
    }

    private fun getBingPic(refresh:Boolean){
        launch({
            bingPicUrl.value = if (refresh) repository.refreshBingPic() else repository.getBingPic()
        },{

        })
    }

    private fun launch(block:suspend () -> Unit,error:suspend (Throwable)->Unit) = viewModelScope.launch {
        try {
            block()
        }catch (e:Throwable){
            error(e)
        }
    }

}