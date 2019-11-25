package com.ychong.kankan.ui.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ychong.kankan.R;
import com.ychong.kankan.ui.login.LoginActivity;
import com.ychong.kankan.ui.permission.GPermission;
import com.ychong.kankan.ui.permission.PermissionGlobalConfigCallback;
import com.ychong.kankan.ui.permission.PermissionPageUtils;
import com.ychong.kankan.utils.BarUtils;
import com.ychong.kankan.utils.widget.dialog.ProgressDialog;
import com.ychong.kankan.utils.widget.dialog.TipsDialog;
import com.ychong.kankan.utils.widget.dialog.TipsDialogListener;
import com.ychong.swipebacklayout.SwipeBackActivity;
import com.ychong.swipebacklayout.SwipeBackLayout;

/**
 * Activity 基类
 */
@SuppressLint("Registered")
public abstract class BaseActivity extends SwipeBackActivity {

    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 可以调用该方法，设置是否允许滑动退出
        setSwipeBackEnable(true);

        mSwipeBackLayout = getSwipeBackLayout();

        // 设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        // 滑动退出的效果只能从边界滑动才有效果，如果要扩大touch的范围，可以调用这个方法
        mSwipeBackLayout.setEdgeSize(200);
        setContentView(getLayoutId());
        BarUtils.setStatusBarColor(this,getResources().getColor(R.color.bar_grey_90));
        initPermission();
    }


    public void showText(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }


    ProgressDialog dialog;
    public void showProgressDialog(Context context,String tips, boolean isCancel){
        if (dialog == null){
            dialog = new ProgressDialog(context,tips);
            dialog.setCanceledOnTouchOutside(isCancel);
            dialog.setCancelable(isCancel);
            dialog.show();
        }
    }
    public void hideProgressDialog(){
        if (dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
    }
    public abstract int getLayoutId();

    public void initPermission(){
        GPermission.init(new PermissionGlobalConfigCallback() {
            @Override
            public void shouldShowRational(String permission, int ration) {
                Log.e("权限日志","权限申请" + permission);
            }

            @Override
            public void onPermissionReject(String permission, int reject) {
                new TipsDialog(BaseActivity.this, "权限未同意，跳转到设置界面，自行设置权限", new TipsDialogListener() {
                    @Override
                    public void onClick(boolean isConfirm) {
                        if (isConfirm){
                            new PermissionPageUtils(BaseActivity.this).jumpPermissionPage();
                        }else {
                            System.exit(0);
                        }
                    }
                }).setConfirm("跳转")
                        .setCancelStr("拒绝")
                        .setTitle("温馨提示")
                        .show();
            }
        });
    }
}
