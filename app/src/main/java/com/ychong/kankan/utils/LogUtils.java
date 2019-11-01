package com.ychong.kankan.utils;

import android.util.Log;

/**
 * 日志打印工具类
 */
public class LogUtils {
    private static final boolean IS_DUBUG = true;

    public static void e(String tag,String content){
        if (!IS_DUBUG){
            return;
        }
        Log.e(tag,content);
    }
    public static void i(String tag,String content){
        if (!IS_DUBUG){
            return;
        }
        Log.i(tag,content);
    }
    public static void d(String tag,String content){
        if (!IS_DUBUG){
            return;
        }
        Log.d(tag,content);
    }
}
