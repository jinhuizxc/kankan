package com.ychong.kankan.utils.http;

import java.net.ConnectException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

public abstract class FreeObserver <T> implements Observer<T>, SingleObserver<T> {

    private Disposable mDisposable;

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }


    @Override
    public void onNext(T t) {
        try {
            onSuccess(t);
        }catch (Exception e){
            e.printStackTrace();
            try {
                onFailure("数据解析失败");
            }catch (Exception err){
                err.printStackTrace();
            }
        }

    }

    public Disposable getDisposable(){
        return mDisposable;
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof HttpException){
            onFailure("网络连接失败,请检查网络连接");
        }else if (e instanceof ConnectException) {
            onFailure("网络连接失败,请检查网络连接");
        } else if (e instanceof TimeoutException) {
            onFailure("网络连接超时，请重试");
        } else if (e instanceof CustomThrowable) {
            onFailure(e.getMessage());
        }else {
            onFailure("数据请求失败");
        }
    }

    @Override
    public void onComplete() {

    }

    /**
     * 返回成功
     *
     * @param t t
     */
    public abstract void onSuccess(T t);

    /**
     * 错误
     */
    public abstract void onFailure(String message);
}
