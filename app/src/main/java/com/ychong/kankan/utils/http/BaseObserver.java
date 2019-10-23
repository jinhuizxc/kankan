package com.ychong.kankan.utils.http;

import com.google.gson.Gson;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public abstract class BaseObserver<T> implements Observer<ResponseBody> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(ResponseBody body) {

    }

    @Override
    public void onError(Throwable e) {
        error("请求失败");
    }

    @Override
    public void onComplete() {

    }

    public abstract void success(T data);

    public abstract void error(String error);
}
