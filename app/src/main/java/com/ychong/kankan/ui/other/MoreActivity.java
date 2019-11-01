package com.ychong.kankan.ui.other;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ychong.kankan.R;
import com.ychong.kankan.map.baidu.BaiDuMapActivity;
import com.ychong.kankan.ui.base.BaseActivity;
import com.ychong.kankan.utils.widget.dialog.InputDialog;
import com.ychong.kankan.utils.widget.dialog.TipsDialog;
import com.ychong.kankan.utils.widget.dialog.TipsDialogListener;

/**
 * 更多界面
 */
public class MoreActivity extends BaseActivity {
    private TextView addressTv;
    private LinearLayout mapLl;
    private TextView titleTv;
    private ImageView backIv;
    private LinearLayout aboutKankanLl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        backIv.setOnClickListener(view -> finish());
        addressTv.setOnClickListener(view -> {
            showAddressDialog(view,addressTv.getText().toString());
        });
        mapLl.setOnClickListener(view -> BaiDuMapActivity.startActivity(this));
        aboutKankanLl.setOnClickListener(view -> aboutKankan());

    }

    private void aboutKankan() {
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

    }

    private void initView() {
        backIv = findViewById(R.id.left_iv);
        titleTv = findViewById(R.id.title_tv);
        addressTv = findViewById(R.id.address_tv);
        mapLl = findViewById(R.id.map_ll);
        aboutKankanLl = findViewById(R.id.about_kankan_ll);
    }

    private void initLayout() {
        setContentView(R.layout.activity_more);
    }

    private void showAddressDialog(View view,String value){
        TextView textView = (TextView)view;
        new InputDialog(this, value, textView::setText).setTitle("请输入地址")
        .show();
    }
}