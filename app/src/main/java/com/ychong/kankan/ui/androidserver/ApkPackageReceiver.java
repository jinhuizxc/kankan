package com.ychong.kankan.ui.androidserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ApkPackageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = intent.getDataString();
        // 安装
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {

        }
        // 覆盖安装
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
        }
        // 移除
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
        }
    }
}
