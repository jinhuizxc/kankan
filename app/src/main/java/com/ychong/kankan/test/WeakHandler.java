package com.ychong.kankan.test;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;

public class WeakHandler extends Handler {
    private WeakReference<IHandler> mHandler;
    public WeakHandler(IHandler handler){
        this.mHandler = new WeakReference<>(handler);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        IHandler handler = mHandler.get();
        if (handler!=null){
            handler.handMsg(msg);
        }
    }
}
