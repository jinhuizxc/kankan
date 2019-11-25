package com.ychong.kankan.ui.permission;

import android.content.Context;

/**
 * 封装一个门面类  提供api调用
 */
public class GPermission {
    //权限申请回调
    private PermissionCallback mCallback;
    //需要的申请的权限
    private String[] permissions;
    private Context context;
    private static PermissionGlobalConfigCallback globalConfigCallback;

    public GPermission(Context context) {
        this.context = context;
    }

    public static void init(PermissionGlobalConfigCallback callback) {
        globalConfigCallback = callback;
    }

    public static PermissionGlobalConfigCallback getGlobalConfigCallback() {
        return globalConfigCallback;
    }

    public static GPermission with(Context context) {
        GPermission permission = new GPermission(context);
        return permission;
    }

    public GPermission permission(String[] permissions) {
        this.permissions = permissions;
        return this;
    }

    public GPermission callBack(PermissionCallback callback) {
        this.mCallback = callback;
        return this;
    }

    public void request() {
        if (permissions == null || permissions.length == 0) {
            return;
        }
        PermissionActivity.request(context, permissions, mCallback);
    }
}
