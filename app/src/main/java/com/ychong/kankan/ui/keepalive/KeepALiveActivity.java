package com.ychong.kankan.ui.keepalive;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ychong.baselib.base.BaseActivity;
import com.ychong.baselib.keepalive.KeepALiveUtils;
import com.ychong.kankan.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class KeepALiveActivity extends BaseActivity {
    private KeepALiveUtils mKeepALiveUtils;
    private TextView switchSuspensionWindowTv;
    private TextView switchBatteryOptimizationWhiteListTv;
    private TextView switchBackgroundOperationPermissionTv;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, KeepALiveActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_keep_alive;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        switchSuspensionWindowTv.setOnClickListener(v -> {
            requestSuspensionWindow();
        });
        switchBatteryOptimizationWhiteListTv.setOnClickListener(v -> {
            if (mKeepALiveUtils != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mKeepALiveUtils.requestIgnoreBatteryOptimizations();
                }
            }
        });
        switchBackgroundOperationPermissionTv.setOnClickListener(v -> {

            if (mKeepALiveUtils != null) {
                mKeepALiveUtils.selfStartUp();
            }
        });

    }

    private void initData() {

        initSwitch();

    }

    private void initView() {
        switchSuspensionWindowTv = (TextView) findViewById(R.id.switch_suspension_window_tv);
        switchBatteryOptimizationWhiteListTv = (TextView) findViewById(R.id.switch_battery_optimization_white_list_tv);
        switchBackgroundOperationPermissionTv = (TextView) findViewById(R.id.switch_background_operation_permission_tv);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KeepALiveUtils.INTENT_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                initSwitch();
            }
        } else if (requestCode == 124) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                initSwitch();
            }
        }
    }

    /**
     * 判断 悬浮窗口权限是否打开
     *
     * @param context
     * @return true 允许  false禁止
     */
    public static boolean getAppOps(Context context) {
        try {
            @SuppressLint("WrongConstant") Object object = context.getSystemService("appops");
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = Integer.valueOf(24);
            arrayOfObject1[1] = Integer.valueOf(Binder.getCallingUid());
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1)).intValue();
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void requestSuspensionWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivityForResult(intent, 124);
        }
    }

    private void initSwitch() {
        mKeepALiveUtils = KeepALiveUtils.newInstance(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getAppOps(this)) {
                switchSuspensionWindowTv.setText("已开启");
                switchSuspensionWindowTv.setBackground(null);
                switchSuspensionWindowTv.setEnabled(false);
            }

            if (mKeepALiveUtils.isIgnoringBatteryOptimizations()) {
                switchBatteryOptimizationWhiteListTv.setText("已开启");
                switchBatteryOptimizationWhiteListTv.setBackground(null);
                switchBatteryOptimizationWhiteListTv.setEnabled(false);
            }
        }
    }

}
