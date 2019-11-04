package com.ychong.kankan.test;

import android.content.Context;
import android.view.ViewGroup;

import java.util.List;

public class TestAdapter extends RecyclerAdapter<String> {
    public TestAdapter(Context context) {
        super(context);
    }

    public TestAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public BaseViewHolder<String> onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return null;
    }
}
