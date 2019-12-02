package com.ychong.baselib.utils;

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

    public static final String LOGIN_STATUS = "Login_Status";

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


    public static final int HTTP_PORT = 10086;
    public static final int MSG_DIALOG_DISMISS = 0;
    public static final class RxBusEventType {
        public static final String POPUP_MENU_DIALOG_SHOW_DISMISS = "POPUP MENU DIALOG SHOW DISMISS";
        public static final String WIFI_CONNECT_CHANGE_EVENT = "WIFI CONNECT CHANGE EVENT";
        public static final String LOAD_BOOK_LIST = "LOAD BOOK LIST";
    }

    public static final String ACTION_START_WEB_SERVICE = "com.baidusoso.wifitransfer.action.START_WEB_SERVICE";
    public static final String ACTION_STOP_WEB_SERVICE = "com.baidusoso.wifitransfer.action.STOP_WEB_SERVICE";

    public static final String TEXT_CONTENT_TYPE = "text/html;charset=utf-8";
    public static final String CSS_CONTENT_TYPE = "text/css;charset=utf-8";
    public static final String BINARY_CONTENT_TYPE = "application/octet-stream";
    public static final String JS_CONTENT_TYPE = "application/javascript";
    public static final String PNG_CONTENT_TYPE = "application/x-png";
    public static final String JPG_CONTENT_TYPE = "application/jpeg";
    public static final String SWF_CONTENT_TYPE = "application/x-shockwave-flash";
    public static final String WOFF_CONTENT_TYPE = "application/x-font-woff";
    public static final String TTF_CONTENT_TYPE = "application/x-font-truetype";
    public static final String SVG_CONTENT_TYPE = "image/svg+xml";
    public static final String EOT_CONTENT_TYPE = "image/vnd.ms-fontobject";
    public static final String MP3_CONTENT_TYPE = "audio/mp3";
    public static final String MP4_CONTENT_TYPE = "video/mpeg4";

    public static final String FLAG_OF_JIGUANG_ALIAS = "FLAG_OF_JI_GUANG_ALIAS"; //极光别名设置状态


    public static final String PREFERENCES_NAME_CONFIG= "preferences_name_config";

    public static final String SERVER_HOST_URL_KEY = "server_host_url_key";


}
