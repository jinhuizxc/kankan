package com.ychong.kankan.test;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ychong.kankan.R;
import com.ychong.kankan.utils.ToastUtils;

public class TestViewHolder extends BaseViewHolder<ViolationResponse> {
    private TextView nameTv;
    public TestViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    public TestViewHolder(ViewGroup parent){
        super(parent, R.layout.item_test);
    }

    @Override
    public void onInitializeView() {
        super.onInitializeView();
        nameTv = findViewById(R.id.test_tv);
    }

    @Override
    public void setData(ViolationResponse data) {
        super.setData(data);
        nameTv.setText(data.ProjectName);
    }

    @Override
    public void onItemViewClick(ViolationResponse data) {
        super.onItemViewClick(data);
        ToastUtils.showShort(data.ProjectName,true);
    }
}
