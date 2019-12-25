package com.ychong.kankan.ui.other;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ychong.kankan.R;
import com.ychong.kankan.ui.ColumnSelectActivity;
import com.ychong.kankan.ui.map.baidu.BaiDuMapActivity;
import com.ychong.kankan.ui.androidserver.AndroidServerActivity;
import com.ychong.baselib.base.BaseActivity;
import com.ychong.kankan.ui.music.MusicActivity;
import com.ychong.kankan.ui.webbrowse.WebBrowseActivity;
import com.ychong.kankan.utils.BaseContract;
import com.ychong.baselib.utils.SPUtils;
import com.ychong.baselib.widget.dialog.InputDialog;
import com.ychong.picandvideo.PVMainActivity;
import com.ychong.picandvideo.ui.main.common.PreViewActivity;
import com.ychong.picandvideo.ui.main.zoom.ZoomViewActivity;

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
    private LinearLayout columnSelectLayout;
    private LinearLayout pvLayout;
    private LinearLayout musicLayout;
    private LinearLayout zoomViewLayout;
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
            showAddressDialog(addressTv, addressTv.getText().toString());
        });
        mapLl.setOnClickListener(view -> BaiDuMapActivity.startActivity(this));
        androidServerLl.setOnClickListener(view -> AndroidServerActivity.startAct(this));
        aboutKankanLl.setOnClickListener(view -> aboutClick());
        webBrowseLl.setOnClickListener(v -> webBrowseClick());
        columnSelectLayout.setOnClickListener(view -> ColumnSelectActivity.startAct(this));
        pvLayout.setOnClickListener(v -> {
            PVMainActivity.startAct(this);
        });
        musicLayout.setOnClickListener(v -> MusicActivity.startAct(this));
        zoomViewLayout.setOnClickListener(v -> ZoomViewActivity.startAct(this));

    }

    /**
     * Web浏览
     */
    private void webBrowseClick() {
        startActivity(new Intent(this, WebBrowseActivity.class));
    }

    private void aboutClick() {
        startActivity(new Intent(MoreActivity.this, AboutActivity.class));
    }

    private void initData() {
        titleTv.setText("更多");
        mSPUtils = SPUtils.getInstance(BaseContract.PREFERENCES_NAME_CONFIG, MODE_PRIVATE);
        addressTv.setText(mSPUtils.getString(BaseContract.SERVER_HOST_URL_KEY, BaseContract.SERVER_HOST_URL));
    }

    private void initView() {
        backIv = (ImageView) findViewById(R.id.left_iv);
        titleTv = (TextView) findViewById(R.id.title_tv);
        addressTv = (TextView) findViewById(R.id.address_tv);
        mapLl = (LinearLayout) findViewById(R.id.map_ll);
        updateAddressLl = (LinearLayout) findViewById(R.id.update_address_ll);
        androidServerLl = (LinearLayout) findViewById(R.id.android_server_ll);
        aboutKankanLl = (LinearLayout) findViewById(R.id.about_kankan_ll);
        webBrowseLl = (LinearLayout) findViewById(R.id.web_browse_ll);
        columnSelectLayout = (LinearLayout) findViewById(R.id.column_select_layout);
        pvLayout = (LinearLayout) findViewById(R.id.pv_layout);
        musicLayout = (LinearLayout) findViewById(R.id.music_layout);
        zoomViewLayout = (LinearLayout) findViewById(R.id.zoom_view_layout);
    }

    private void showAddressDialog(View view, String value) {
        TextView textView = (TextView) view;
        new InputDialog(this, value, text -> {
            mSPUtils.put(BaseContract.SERVER_HOST_URL_KEY, text);
            textView.setText(text);
        }).setTitle("请输入地址")
                .show();
    }
}
