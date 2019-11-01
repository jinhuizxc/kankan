package com.ychong.kankan.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.leakcanary.RefWatcher;
import com.ychong.kankan.MyApplication;

/**
 * Fragment基类
 */
public abstract class BaseFragment extends Fragment {
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    protected abstract void initListener();

    protected abstract void initData();

    @Override
    public void onDestroy() {
        super.onDestroy();
        Context context = getContext();
        if (context!=null){
            RefWatcher refWatcher = MyApplication.getRefWatcher(context);
            refWatcher.watch(context);
        }
    }
}
