package com.ychong.mvvm_demo.data.model.weather

class Suggestion {
    lateinit var comfort: Comfort
    lateinit var carWash: CarWash
    lateinit var sport: Sport

    inner class Comfort{
        var info = ""
    }
    inner class CarWash{
        var info = ""
    }
    inner class Sport{
        var info = ""
    }
}