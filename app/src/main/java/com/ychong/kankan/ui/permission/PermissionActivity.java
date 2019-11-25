package com.ychong.kankan.ui.permission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.ychong.kankan.R;

public class PermissionActivity extends AppCompatActivity {
    public static final String KEY_PERMISSIONS = "permissions";
    private static final int RC_REQUEST_PERMISSION = 100;
    private static PermissionCallback mCallback;

    public static void request(Context context,String[] permissions,PermissionCallback callback){
        mCallback = callback;
        Intent intent = new Intent(context,PermissionActivity.class);
        intent.putExtra(KEY_PERMISSIONS,permissions);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        Intent intent = getIntent();
        if (!intent.hasExtra(KEY_PERMISSIONS)){
            return;
        }
        //当api大于23 才进行权限申请
        String[] permissions = getIntent().getStringArrayExtra(KEY_PERMISSIONS);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            requestPermissions(permissions,RC_REQUEST_PERMISSION);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode!=RC_REQUEST_PERMISSION){
            return;
        }
        //处理了申请结果
        boolean[] shouldShowRequestPermissionRationale = new boolean[permissions.length];
        for (int i=0;i<permissions.length;++i){
            shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i]);
        }
        this.onRequestPermissionsResult(permissions,grantResults,shouldShowRequestPermissionRationale);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onRequestPermissionsResult(String[] permissions,int[] grantResults,boolean[] shouldShowRequestPermissionRationale){
        int length = permissions.length;
        int granted = 0;
        for (int i=0;i<length;i++){
            if (grantResults[i]!= PackageManager.PERMISSION_GRANTED){
                if (shouldShowRequestPermissionRationale[i]){
                    mCallback.shouldShowRational(permissions[i]);
                }else {
                    mCallback.onPermissionReject(permissions[i]);
                }
            }else {
                granted++;
            }
        }
        if (granted==length){
            mCallback.onPermissionGranted();
        }
        onBackPressed();
    }
}
