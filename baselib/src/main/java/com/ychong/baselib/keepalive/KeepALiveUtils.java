package com.ychong.baselib.keepalive;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

public class KeepALiveUtils {
    private Activity mActivity;
    public static final int INTENT_CODE =123;

    public static KeepALiveUtils newInstance(Activity activity) {
        return new KeepALiveUtils(activity);
    }

    private KeepALiveUtils(Activity activity) {
        mActivity = activity;
    }

    /**
     * 判断应用是否在白名单中
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isIgnoringBatteryOptimizations() {
        boolean isIgnoring = false;
        PowerManager powerManager = (PowerManager) mActivity.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            isIgnoring = powerManager.isIgnoringBatteryOptimizations(mActivity.getPackageName());
        }
        return isIgnoring;
    }

    /**
     * 如果不在白名单中，申请加入白名单
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestIgnoreBatteryOptimizations(){
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:"+mActivity.getPackageName()));
            mActivity.startActivityForResult(intent,INTENT_CODE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 跳转到指定应用的首页
     */
    public void showActivity(String packageName){
        Intent intent = mActivity.getPackageManager().getLaunchIntentForPackage(packageName);
        mActivity.startActivity(intent);
    }
    /**
     * 跳转到指定应用的指定页面
     */
    public void showActivity(String packageName,String activityDir){
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName,activityDir));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(intent);
    }

    /**
     * 跳转到手机管家自启动界面
     */
    public void selfStartUp(){
        if (isHuawei()){
            goHuaweiSetting();
        }
        if (isXiaomi()){
            goXiaomiSetting();
        }
        if (isOPPO()){
            goOPPOSetting();
        }
        if (isVIVO()){
            goVIVOSetting();
        }
        if (isMeizu()){
            goMeizuSetting();
        }
        if (isSmartisan()){
            goSmartisanSetting();
        }
        if (isSamsung()){
            goSamsungSetting();
        }
        if (isLeTV()){
            goLetvSetting();
        }

    }

    /**
     * 华为
     */
    public boolean isHuawei(){
        if (Build.BRAND == null){
            return false;
        }else {
            return Build.BRAND.toLowerCase().equals("huawei")||Build.BRAND.toLowerCase().equals("honor");
        }
    }
    /**
     * 跳转到华为手机管家的自启动管理页面
     */
    public void goHuaweiSetting() {
        try {
            showActivity("com.huawei.systemmanager",
                    "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
        } catch (Exception e) {
            showActivity("com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.bootstart.BootStartActivity");
        }
    }
    /**
     * 小米
     */
    public  boolean isXiaomi() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("xiaomi");
    }
    /**
     * 跳转到小米安全中心自启动管理页面
     */
    public void goXiaomiSetting() {
        showActivity("com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity");
    }

    /**
     * oppo
     */
    public  boolean isOPPO() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("oppo");
    }

    /**
     * 跳转到oppo手机管家
     */
    public void goOPPOSetting() {
        try {
            showActivity("com.coloros.phonemanager");
        } catch (Exception e1) {
            try {
                showActivity("com.oppo.safe");
            } catch (Exception e2) {
                try {
                    showActivity("com.coloros.oppoguardelf");
                } catch (Exception e3) {
                    showActivity("com.coloros.safecenter");
                }
            }
        }
    }

    /**
     * vivo
     */
    public static boolean isVIVO() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("vivo");
    }

    /**
     * 跳转到vivo手机管家
     */
    public void goVIVOSetting() {
        showActivity("com.iqoo.secure");
    }

    /**
     * 魅族
     */
    public static boolean isMeizu() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("meizu");
    }

    /**
     * 跳转到魅族手机管家
     */
    public void goMeizuSetting() {
        showActivity("com.meizu.safe");
    }
    /**
     * 三星
     */
    public static boolean isSamsung() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("samsung");
    }

    /**
     * 跳转到三星智能管理器
     */
    public void goSamsungSetting() {
        try {
            showActivity("com.samsung.android.sm_cn");
        } catch (Exception e) {
            showActivity("com.samsung.android.sm");
        }
    }

    /**
     * 乐视
     */
    public static boolean isLeTV() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("letv");
    }

    /**
     * 跳转到乐视手机管家
     */
    public void goLetvSetting() {
        showActivity("com.letv.android.letvsafe",
                "com.letv.android.letvsafe.AutobootManageActivity");
    }

    /**
     * 锤子
     */
    public static boolean isSmartisan() {
        return Build.BRAND != null && Build.BRAND.toLowerCase().equals("smartisan");
    }

    /**
     * 跳转到锤子手机管理
     */
    public void goSmartisanSetting() {
        showActivity("com.smartisanos.security");
    }


}
