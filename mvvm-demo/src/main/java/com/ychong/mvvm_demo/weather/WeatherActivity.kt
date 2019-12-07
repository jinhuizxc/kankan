package com.ychong.mvvm_demo.weather

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

class WeatherActivity : AppCompatActivity() {
    val viewModel by lazy { ViewModelProviders.of(this).get(WeatherViewModel::class.java) }

}