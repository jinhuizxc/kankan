package com.ychong.mvvm_demo.data.model.place

import org.litepal.crud.LitePalSupport

class Province(val provinceName:String,val provinceCode:Int):LitePalSupport(){
    val id = 0
}