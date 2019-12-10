package com.ychong.mvvm_demo.util

import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.ychong.mvvm_demo.R
import com.ychong.mvvm_demo.data.model.weather.Weather
import com.ychong.mvvm_demo.databinding.ForecastBinding
import com.ychong.mvvm_demo.databinding.ItemForecastBinding

@BindingAdapter("loadBingPic")
fun ImageView.loadBingPic(url:String?){
    if (url !=null){
        Glide.with(context).load(url).into(this)
    }
}
@BindingAdapter("colorSchemeResources")
fun SwipeRefreshLayout.colorSchemeResources(resId:Int){
    setColorSchemeResources(resId)
}
@BindingAdapter("showForecast")
fun LinearLayout.showForecast(weather: Weather?)=weather?.let { {
    removeAllViews()
    for (forecast in it.forecastList){
        val view = LayoutInflater.from(context).inflate(R.layout.item_forecast,this,false)
        DataBindingUtil.bind<ItemForecastBinding>(view)?.forecast  = forecast
        addView(view)
    }
} }