package com.ychong.kankan.ui.androidserver;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ychong.kankan.R;
import com.ychong.kankan.entity.ApkInfoBean;
import com.ychong.kankan.entity.EventBusMessage;
import com.ychong.kankan.ui.base.BaseActivity;
import com.ychong.kankan.utils.BaseContract;
import com.ychong.kankan.utils.BaseUtils;
import com.ychong.kankan.utils.widget.dialog.TipsDialog;
import com.ychong.kankan.utils.widget.dialog.TipsDialogListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AndroidServerActivity extends BaseActivity {
    Unbinder unbinder;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.right_tv)
    TextView moreTv;

    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.recyclerview)
    RecyclerView appRv;
    @BindView(R.id.content_main)
    SwipeRefreshLayout mSwipeRefreshLayout;

    List<ApkInfoBean> appList = new ArrayList<>();
    AndroidServerAdapter adapter;

    private Context context = AndroidServerActivity.this;
    private ObjectAnimator objectAnimator;
    String[] allPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};


    public static void startAct(Context context) {
        Intent intent = new Intent(context, AndroidServerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        unbinder = ButterKnife.bind(this);
        initData();
        initListener();
    }

    private void initListener() {

    }

    private void initData() {
        titleTv.setText("Apk工具");
        moreTv.setText("更多");
        moreTv.setVisibility(View.VISIBLE);
        EventBus.getDefault().register(this);
        initRecyclerView();
    }

    private void initLayout() {
        setContentView(R.layout.activity_android_server);
    }

    private boolean requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean needapply = false;
            for (int i = 0; i < allPermissions.length; i++) {
                int chechpermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                        allPermissions[i]);
                if (chechpermission != PackageManager.PERMISSION_GRANTED) {
                    needapply = true;
                }
            }
            if (needapply) {
                ActivityCompat.requestPermissions(AndroidServerActivity.this, allPermissions, 1);
            }
            return !needapply;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, permissions[i] + "已授权", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, permissions[i] + "授权通过方能使用", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick({R.id.left_iv, R.id.right_tv, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_iv:
                //返回
                onBackPressed();
                break;
            case R.id.right_tv:
                //更多
                showDialog();
                break;
            case R.id.fab:
                startServer();
                break;
            default:
                break;
        }

    }

    private void startServer() {
        if (requestPermission()) {
            objectAnimator = ObjectAnimator.ofFloat(mFab, "translationY", 0, mFab.getHeight() * 2).setDuration(200L);
            objectAnimator.setInterpolator(new AccelerateInterpolator());
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    WebService.start(context);
                    new PopupMenuDialog(context).builder().setCancelable(false)
                            .setCanceledOnTouchOutside(false).show();
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            objectAnimator.start();
        }
    }

    @Override
    protected void onDestroy() {
        if (objectAnimator != null) {
            objectAnimator.cancel();
            objectAnimator = null;
        }
        WebService.stop(this);
        if (unbinder != null) {
            unbinder.unbind();
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusCallBack(EventBusMessage message) {
        String msg = message.msg;
        if (BaseContract.RxBusEventType.POPUP_MENU_DIALOG_SHOW_DISMISS.equals(msg)) {
            int type = message.code;
            if (BaseContract.MSG_DIALOG_DISMISS == type) {
                WebService.stop(this);
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mFab, "translationY", mFab.getHeight() * 2, 0).setDuration(200L);
                objectAnimator.setInterpolator(new AccelerateInterpolator());
                objectAnimator.start();
            }
        } else if (BaseContract.RxBusEventType.LOAD_BOOK_LIST.equals(msg)) {
            appList.clear();
            File dir = BaseContract.DIR;
            if (dir.exists() && dir.isDirectory()) {
                File[] fileNames = dir.listFiles();
                if (fileNames != null) {
                    for (File fileName : fileNames) {
                        handleApk(fileName.getAbsolutePath(), fileName.length());
                    }
                }
            }
            mSwipeRefreshLayout.setRefreshing(false);
            adapter.notifyDataSetChanged();
        }
    }

    //显示确认对话框
    private void showDialog() {
        new TipsDialog(this, "温馨提示：", "确定全部删除吗？", new TipsDialogListener() {
            @Override
            public void onClick(boolean isConfirm) {
                if (isConfirm) {
                    BaseUtils.deleteAll(BaseContract.DIR);
                }
            }
        }).setConfirm("确定")
                .setCancelStr("取消")
                .show();
    }

    void initRecyclerView() {
        adapter = new AndroidServerAdapter(this, appList);
        appRv.setHasFixedSize(true);
        appRv.setLayoutManager(new LinearLayoutManager(this));
        appRv.setAdapter(adapter);
        EventBus.getDefault().post(new EventBusMessage(BaseContract.RxBusEventType.LOAD_BOOK_LIST, 0));

        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        appRv.addItemDecoration(new ItemButtomDecoration(this, 10));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                EventBus.getDefault().post(new EventBusMessage(BaseContract.RxBusEventType.LOAD_BOOK_LIST, 0));
            }
        });

    }

    //获取apk信息
    private void handleApk(String path, long length) {
        ApkInfoBean infoModel = new ApkInfoBean();
        String archiveFilePath = "";
        archiveFilePath = path;
        PackageManager pm = getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, 0);

        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = archiveFilePath;
            appInfo.publicSourceDir = archiveFilePath;
            String packageName = appInfo.packageName;  //得到安装包名称
            String version = info.versionName;       //得到版本信息
            Drawable icon = pm.getApplicationIcon(appInfo);
            String appName = pm.getApplicationLabel(appInfo).toString();
            if (TextUtils.isEmpty(appName)) {
                appName = BaseUtils.getApplicationName(this, packageName);
            }
            if (icon == null) {
                icon = BaseUtils.getIconFromPackageName(packageName, this); // 获得应用程序图标
            }
            infoModel.name = (appName);
            infoModel.packageName = (packageName);
            infoModel.path = (path);
            infoModel.size = (BaseUtils.getFileSize(length));
            infoModel.version = (version);
            infoModel.icon = (icon);
            infoModel.installed = (BaseUtils.isAvilible(this, packageName));
            appList.add(infoModel);
        }
    }
}
