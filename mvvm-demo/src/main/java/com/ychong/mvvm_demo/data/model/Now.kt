package com.ychong.mvvm_demo.data.model

class Now {
    var temperature = ""
    lateinit var more:More

    fun degree() = "$temperature℃"

    inner class More{
        var info = ""
    }
}