package com.ychong.mvvm_demo.data.model.place

import org.litepal.crud.LitePalSupport

class City(val cityName:String,val cityCode:Int):LitePalSupport(){
    val id = 0
    var provinceId = 0;
}