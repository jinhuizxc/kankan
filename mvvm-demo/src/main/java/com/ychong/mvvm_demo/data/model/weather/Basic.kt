package com.ychong.mvvm_demo.data.model.weather

class Basic {
    var cityName = ""
    var weatherId = ""
    lateinit var update: Update

    inner class Update{
        var updateTime = ""
        fun time() = updateTime.split(" ")[1]
    }
}