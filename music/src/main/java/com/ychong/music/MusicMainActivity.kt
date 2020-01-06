package com.ychong.music

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout

class MusicMainActivity : AppCompatActivity() {
     lateinit var frameLayout : FrameLayout

    companion object{
        fun startAct(context:Context){
           val intent = Intent(context,MusicMainActivity().javaClass)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_main)
        initView()
        initData()
        initListener()
    }

    private fun initView(){
        frameLayout = findViewById(R.id.frame_layout);

    }
    private fun initData(){

    }
    private fun initListener(){

    }
}
