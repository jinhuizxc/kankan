package com.ychong.baselib.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static void showShort(Context context,String text, boolean isBug){
        if (isBug){
            Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
        }
    }
    public static void showLong(Context context,String text,boolean isBug){
        if (isBug){
            Toast.makeText(context,text,Toast.LENGTH_LONG).show();
        }
    }
}
