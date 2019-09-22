package com.ychong.kankan;

import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public abstract class BaseObsever<T> implements Observer<ResponseBody> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(ResponseBody responseBody) {
        try {
            BaseData<List<T>> baseData=new  BaseData<List<T>>().toJsonModel(responseBody.string());
           if (baseData.success){
               success(baseData.data);
           }else {
               error(baseData.msg);
           }
        } catch (Exception e) {
            e.printStackTrace();
            error("请求失败" + e.toString());
        }

    }

    @Override
    public void onError(Throwable e) {
        error(e.toString());
    }

    @Override
    public void onComplete() {

    }
    public abstract void success(List<T> list);

    public abstract void error(String error);
}
