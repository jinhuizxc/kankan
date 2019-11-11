package com.ychong.kankan.utils;

import android.os.Environment;

import java.io.File;

public class BaseContract {
    public static final String SERVER_HOST_URL = "http://192.168.0.101:10086/";
    public static final String FTP_HOST_URL = "http://47.98.147.72:9999/file/ychong/";


    public static final String PATH = "path";
    public static final String PREVIEW_TYPE = "PreView_Type";
    public static final int VIDEO_TYPE = 0;
    public static final int PIC_TYPE= 1;
    public static final int FILE_TYPE = 2;

    /**
     * 手机号正则
     */
    public static final String REG_PHONE_CHINA = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    /**
     * 邮箱正则
     */
    public static final String REG_EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

    public static final String DIR_IN_SDCARD = "KanKan";
    public static final File DIR = new File(Environment.getExternalStorageDirectory() + File.separator + DIR_IN_SDCARD);


    /**
     * 成功
     */
    public static final int SUCCESS = 1;
    /**
     * 失败
     */
    public static final int ERROR = 0;


}
