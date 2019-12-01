package com.ychong.kankan.ui.login

import android.app.Activity
import android.preference.Preference
import com.tencent.mmkv.MMKV
import java.security.Key
import java.util.prefs.Preferences

object UserContext {
    //持久化存储  登录地址
    private var isLogin:Boolean = false;
    //设置默认状态
    var mState:UserState  = if (isLogin)LoginState()else LogoutState()

    //登录
    fun login(context:Activity){
        mState.login(context)
    }

    //切换成  登录状态
    fun  setLoginState(){
        //改变sharedPreferences  isLogin值
        isLogin = true;
        mState = LoginState()
    }

    //切换成  未登录状态
    fun setLogoutState(){
        //改变 sharedPreferences  isLogin值
        isLogin = false;
        mState = LogoutState();
    }

}