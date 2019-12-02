package com.ychong.kankan.utils.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.ychong.kankan.utils.BaseContract;
import com.ychong.baselib.utils.SPUtils;
import com.ychong.baselib.utils.ToastUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;


/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-Example";
    private boolean isLogin;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            isLogin = SPUtils.getInstance("config",Context.MODE_PRIVATE).getBoolean(BaseContract.LOGIN_STATUS);
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Logger.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
                if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                    String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                    Logger.i(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                    //send the Registration Id to your server...
                    String channelID = JPushInterface.getRegistrationID(context);
                    if (TextUtils.isEmpty(channelID)) {
                        channelID = JPushInterface.getRegistrationID(context);
                    }
                    boolean flag = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(BaseContract.FLAG_OF_JIGUANG_ALIAS, false);
                    if (!TextUtils.isEmpty(channelID) && !flag) {
                        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
                        tagAliasBean.action = TagAliasOperatorHelper.ACTION_SET;
                        TagAliasOperatorHelper.sequence++;
                        tagAliasBean.alias = channelID;
                        tagAliasBean.isAliasAction = true;
                        TagAliasOperatorHelper.getInstance().handleAction(context, TagAliasOperatorHelper.sequence, tagAliasBean);
                    }
                } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                    Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                    //int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                    //judgeAccount(context, bundle, notificationId);
                } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                    Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                    int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                    Logger.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notificationId);
                } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                    Logger.d(TAG, "[MyReceiver] 用户点击打开了通知   " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                    //判断是否登录
                    if (isLogin) {

                    } else {
                        ToastUtils.showShort(context,"未登录",true);
                    }
                } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                    Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                    //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
                } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                    boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                    int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                    Logger.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
                } else {
                    Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            switch (key) {
                case JPushInterface.EXTRA_NOTIFICATION_ID:
                    sb.append("\nkey:").append(key).append(", value:").append(bundle.getInt(key));
                    break;
                case JPushInterface.EXTRA_CONNECTION_CHANGE:
                    sb.append("\nkey:").append(key).append(", value:").append(bundle.getBoolean(key));
                    break;
                case JPushInterface.EXTRA_EXTRA:
                    if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                        Logger.i(TAG, "This message has no Extra data");
                        continue;
                    }

                    try {
                        JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                        Iterator<String> it = json.keys();

                        while (it.hasNext()) {
                            String myKey = it.next();
                            sb.append("\nkey:").append(key).append(", value: [").append(myKey).append(" - ").append(json.optString(myKey)).append("]");
                        }
                    } catch (JSONException e) {
                        Logger.e(TAG, "Get message extra JSON error!");
                    }

                    break;
                default:
                    sb.append("\nkey:").append(key).append(", value:").append(bundle.get(key));
                    break;
            }
        }
        return sb.toString();
    }

    public static class MessageInfo implements Serializable {
        public String extrasKey;
    }
}