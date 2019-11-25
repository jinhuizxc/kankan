package com.ychong.kankan.ui.permission;

/**
 * 权限申请回调
 */
public interface PermissionCallback {
    void onPermissionGranted();
    void shouldShowRational(String permission);
    void onPermissionReject(String permission);

}
