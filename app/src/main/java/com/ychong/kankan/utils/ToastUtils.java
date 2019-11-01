package com.ychong.kankan.utils;

import android.widget.Toast;

import com.ychong.kankan.MyApplication;

public class ToastUtils {
    public static void showShort(String text,boolean isBug){
        if (isBug){
            Toast.makeText(MyApplication.getAppContext(),text,Toast.LENGTH_SHORT).show();
        }
    }
    public static void showLong(String text,boolean isBug){
        if (isBug){
            Toast.makeText(MyApplication.getAppContext(),text,Toast.LENGTH_LONG).show();
        }
    }
}
