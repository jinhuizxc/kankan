package com.ychong.kankan.ui.androidserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import com.ychong.baselib.entity.EventBusMessage;
import org.greenrobot.eventbus.EventBus;

public class WifiConnectChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
            Parcelable parcelableExtra = intent
                    .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                //通知 wifi连接改变成功
                EventBus.getDefault().post(new EventBusMessage(PopupMenuDialog.WIFI_CONNECT_CHANGE_EVENT,networkInfo.getState()));
            }
        }
    }
}
