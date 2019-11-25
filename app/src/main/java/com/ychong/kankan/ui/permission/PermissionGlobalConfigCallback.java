package com.ychong.kankan.ui.permission;

/**
 * 将申请被拒绝的上述两种情况交给调用者自行处理 框架里不处理
 */
public interface PermissionGlobalConfigCallback {
    abstract public void shouldShowRational(String permission,int ration);
    abstract public void onPermissionReject(String permission,int reject);
}
