package com.ychong.mvvm_demo.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.ychong.mvvm_demo.R
import com.ychong.mvvm_demo.ui.area.ChooseAreaFragment
import com.ychong.mvvm_demo.util.InjectorUtil
import com.ychong.mvvm_demo.ui.weather.WeatherActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModel = ViewModelProviders.of(this,InjectorUtil.getMainModelFactory()).get(MainViewModel::class.java)
        if (viewModel.isWeatherCached()){
            val intent = Intent(this, WeatherActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            supportFragmentManager.beginTransaction().replace(R.id.container, ChooseAreaFragment()).commit()
        }
    }
}
