package com.ychong.baselib.utils.fastclick;

import android.view.View;

public class XClickUtils {
    /**
     * 最近一次点击的时间
     */
    private static long mLastClickTime;
    /**
     * 最近一次点击的控件ID
     */
    private static int mLastClickViewId;
    /**
     * 是否是快速点击
     */
    public static boolean isFastDoubleClick(View v,long intervalMillis){
        int viewId = v.getId();
        long time = System.currentTimeMillis();
        long timeInterval = Math.abs(time-mLastClickTime);
        if (timeInterval<intervalMillis&&viewId == mLastClickViewId){
            return true;
        }else {
            mLastClickTime = time;
            mLastClickViewId = viewId;
            return false;
        }
    }

}
