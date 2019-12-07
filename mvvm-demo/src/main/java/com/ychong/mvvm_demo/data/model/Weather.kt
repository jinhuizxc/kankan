package com.ychong.mvvm_demo.data.model

class Weather {
    var status = ""
    lateinit var basic:Basic
    lateinit var aqi:AQI
    lateinit var now:Now
    lateinit var suggestion:Suggestion
    lateinit var forecastList:List<Forecast>
}