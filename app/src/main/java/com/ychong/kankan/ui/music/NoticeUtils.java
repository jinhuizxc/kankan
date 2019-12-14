package com.ychong.kankan.ui.music;

import android.app.NotificationManager;

public class NoticeUtils {
    private static NoticeUtils Instance;
    public static NoticeUtils getInstance(){
        if (Instance==null){
            synchronized (NoticeUtils.class){
                if (Instance==null){
                    Instance = new NoticeUtils();
                }
            }
        }
        return Instance;
    }

    private NoticeUtils(){
    }
}
