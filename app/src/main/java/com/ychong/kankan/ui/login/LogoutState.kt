package com.ychong.kankan.ui.login

import android.content.Context
import android.content.Intent

/**
 * 未登录状态   在执行操作前 先跳转到登录
 */
class LogoutState :UserState{
    override fun login(context: Context?) {
        context?.run {
           val intent = Intent(context,LoginActivity::class.java)
            startActivity(intent)
        }
    }

}