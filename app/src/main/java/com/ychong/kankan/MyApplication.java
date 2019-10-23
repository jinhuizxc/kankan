package com.ychong.kankan;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

<<<<<<< HEAD
import com.ychong.kankan.entity.DaoMaster;
import com.ychong.kankan.entity.DaoSession;
=======
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
>>>>>>> c74adde5facfb5a4c92559e10ebc30ce1e883bfd

public class MyApplication extends Application {
    public static final String DB_NAME= "kankan.db";
    private static DaoSession mDaoSession;
    @Override
    public void onCreate() {
        super.onCreate();
        initBaiDuMap();
       // initGreenDao();
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
}
