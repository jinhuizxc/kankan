package com.ychong.mvvm_demo.data.model.place

import org.litepal.crud.LitePalSupport

class County(val countyName:String,val weatherId:String):LitePalSupport(){
    val id = 0
    var cityId = 0

}