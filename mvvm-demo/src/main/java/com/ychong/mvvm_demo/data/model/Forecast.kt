package com.ychong.mvvm_demo.data.model

class Forecast {
    var date:String = ""
    lateinit var temperature:Temperature
    lateinit var more:More

    inner class Temperature{
        var max  = ""
        var min = ""
    }
    inner class More{
        var info = ""
    }
}