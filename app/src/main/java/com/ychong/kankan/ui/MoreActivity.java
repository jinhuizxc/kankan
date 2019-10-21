package com.ychong.kankan.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ychong.kankan.R;

public class MoreActivity extends BaseActivity {
    private TextView addressTv;
    private ImageView backIv;
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

    }

    private void initData() {

    }

    private void initView() {
        backIv = findViewById(R.id.left_iv);
        addressTv = findViewById(R.id.address_tv);


    }

    private void initLayout() {
        setContentView(R.layout.activity_more);
    }

    private void showAddressDialog(View addressView,String value){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_input,null);
        TextView titleTv = view.findViewById(R.id.title_tv);
        EditText addressEt = view.findViewById(R.id.address_et);
        TextView sureTv = view.findViewById(R.id.sure_tv);
        TextView cancelTv = view.findViewById(R.id.cancel_tv);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();
        TextView addressTv = (TextView) addressView;
        titleTv.setText("请输入地址");
        addressEt.setText(value);
        sureTv.setOnClickListener(view1 -> {
            addressTv.setText(addressEt.getText().toString());
            dialog.dismiss();
        });
        cancelTv.setOnClickListener(view12 -> dialog.dismiss());
    }
}
