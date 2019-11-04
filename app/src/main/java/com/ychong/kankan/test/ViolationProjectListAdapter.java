package com.ychong.kankan.test;

import android.content.Context;
import android.view.ViewGroup;

import java.util.List;

public class ViolationProjectListAdapter extends RecyclerAdapter<ViolationResponse> {


    public ViolationProjectListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder<ViolationResponse> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return new TestViewHolder(parent);
    }
}
