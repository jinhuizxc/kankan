package com.ychong.kankan.ui.other;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ychong.kankan.R;
import com.ychong.kankan.ui.map.baidu.BaiDuMapActivity;
import com.ychong.kankan.ui.androidserver.AndroidServerActivity;
import com.ychong.kankan.ui.base.BaseActivity;
import com.ychong.kankan.ui.webbrowse.WebBrowseActivity;
import com.ychong.kankan.utils.BaseContract;
import com.ychong.kankan.utils.SPUtils;
import com.ychong.kankan.utils.widget.dialog.InputDialog;
import com.ychong.kankan.utils.widget.dialog.TipsDialog;
import com.ychong.kankan.utils.widget.dialog.TipsDialogListener;

/**
 * 更多界面
 */
public class MoreActivity extends BaseActivity {
    private TextView addressTv;
    private LinearLayout mapLl;
    private LinearLayout updateAddressLl;
    private TextView titleTv;
    private ImageView backIv;
    private LinearLayout androidServerLl;
    private LinearLayout aboutKankanLl;
    private LinearLayout webBrowseLl;
    private SPUtils mSPUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_more;
    }

    private void initListener() {
        backIv.setOnClickListener(view -> finish());
        updateAddressLl.setOnClickListener(view -> {
            showAddressDialog(addressTv,addressTv.getText().toString());
        });
        mapLl.setOnClickListener(view -> BaiDuMapActivity.startActivity(this));
        androidServerLl.setOnClickListener(view -> AndroidServerActivity.startAct(this));
        aboutKankanLl.setOnClickListener(view -> aboutClick());
        webBrowseLl.setOnClickListener(v -> webBrowseClick());

    }

    /**
     * Web浏览
     */
    private void webBrowseClick() {
        startActivity(new Intent(this,WebBrowseActivity.class));
    }

    private void aboutClick() {
        new TipsDialog(this, "您确定要进入关于界面吗?", new TipsDialogListener() {
            @Override
            public void onClick(boolean isConfirm) {
                if (isConfirm){
                    startActivity(new Intent(MoreActivity.this,AboutActivity.class));
                }
            }
        }).setConfirm("是的")
                .setCancelStr("没有啊")
                .show();
    }

    private void initData() {
        titleTv.setText("更多");
        mSPUtils = SPUtils.getInstance(BaseContract.PREFERENCES_NAME_CONFIG,MODE_PRIVATE);
       addressTv.setText(mSPUtils.getString(BaseContract.SERVER_HOST_URL_KEY,BaseContract.SERVER_HOST_URL));
    }

    private void initView() {
        backIv = findViewById(R.id.left_iv);
        titleTv = findViewById(R.id.title_tv);
        addressTv = findViewById(R.id.address_tv);
        mapLl = findViewById(R.id.map_ll);
        updateAddressLl = findViewById(R.id.update_address_ll);
        androidServerLl = findViewById(R.id.android_server_ll);
        aboutKankanLl = findViewById(R.id.about_kankan_ll);
        webBrowseLl = findViewById(R.id.web_browse_ll);
    }

    private void showAddressDialog(View view,String value){
        TextView textView = (TextView)view;
        new InputDialog(this, value, text -> {
            mSPUtils.put(BaseContract.SERVER_HOST_URL_KEY,text);
            textView.setText(text);
        }).setTitle("请输入地址")
        .show();
    }
}
