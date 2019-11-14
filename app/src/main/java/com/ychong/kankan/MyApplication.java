package com.ychong.kankan;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.ychong.kankan.entity.DaoMaster;
import com.ychong.kankan.entity.DaoSession;
import com.ychong.kankan.ui.webbrowse.X5Service;

public class MyApplication extends Application {
    public static final String DB_NAME= "kankan.db";
    private static DaoSession mDaoSession;
    private static  Context mContext;
    private RefWatcher refWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initLeakCanary();
        initBaiDuMap();
       // initGreenDao();
    }

    public static Context getAppContext(){
        return mContext;
    }

    private void initGreenDao(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,DB_NAME);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster master = new DaoMaster(db);
        mDaoSession = master.newSession();
    }
    public static DaoSession getmDaoSession(){
        return mDaoSession;
    }


    private void initBaiDuMap(){
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }


    private void initLeakCanary(){
        refWatcher = setUpLeakCanary();

    }

    private RefWatcher setUpLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)){
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }
    public static RefWatcher getRefWatcher(Context context){
        MyApplication application = (MyApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    private void initX5(){
        Intent intent = new Intent(this, X5Service.class);
        startService(intent);
    }
}
