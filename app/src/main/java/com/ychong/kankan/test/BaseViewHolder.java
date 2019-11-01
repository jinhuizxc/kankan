package com.ychong.kankan.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    private static final String TAG = BaseViewHolder.class.getSimpleName();
    private T mData;
    public BaseViewHolder(ViewGroup parent,int layoutId){
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false));
    }
    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        onInitializeView();
        itemView.setOnClickListener(view -> onItemViewClick(mData));
    }
    public void onInitializeView(){}

    public <T extends View> T findViewById(@IdRes int resId){
        if (itemView !=null){
            return (T)itemView.findViewById(resId);
        }else {
            return null;
        }
    }
    public void setData(T data){
        if (data == null){
            return;
        }
        mData = data;
    }
    public T getData(){return mData;}
    public void onItemViewClick(T data){

    }
}
