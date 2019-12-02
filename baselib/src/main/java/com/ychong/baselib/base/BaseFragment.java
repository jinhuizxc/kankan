package com.ychong.baselib.base;

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
import com.ychong.baselib.widget.dialog.ProgressDialog;

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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
