package com.ychong.kankan;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.ychong.kankan.entity.DaoMaster;
import com.ychong.kankan.entity.DaoSession;

public class MyApplication extends Application {
    public static final String DB_NAME= "kankan.db";
    private static DaoSession mDaoSession;
    @Override
    public void onCreate() {
        super.onCreate();
        initGreenDao();
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
}
