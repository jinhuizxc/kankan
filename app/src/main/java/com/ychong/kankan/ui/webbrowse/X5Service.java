package com.ychong.kankan.ui.webbrowse;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.tencent.smtt.sdk.QbSdk;

public class X5Service extends IntentService {
    public static final String TAG = X5Service.class.getSimpleName();
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public X5Service(){
        super(TAG);
    }
    public X5Service(String name) {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        initX5Web();

    }

    private void initX5Web() {
        if (!QbSdk.isTbsCoreInited()){
            //设置X5初始化完成的回调接口
            QbSdk.preInit(getApplicationContext(),null);
        }
        QbSdk.initX5Environment(getApplicationContext(),cb);
    }
    QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
        @Override
        public void onCoreInitFinished() {

        }

        @Override
        public void onViewInitFinished(boolean b) {

        }
    };
}
