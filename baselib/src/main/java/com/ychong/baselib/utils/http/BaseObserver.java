package com.ychong.baselib.utils.http;

import com.google.gson.Gson;
import com.ychong.baselib.entity.BaseEntity;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public abstract class BaseObserver<T> implements Observer<ResponseBody> {
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(ResponseBody body) {
        try {
            String json = body.string();
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                Type[] types = ((ParameterizedType) type).getActualTypeArguments();
                Type type1 = new ParameterizedTypeImpl(BaseEntity.class, new Type[]{types[0]});
                BaseEntity<T> baseEntity = new Gson().fromJson(json, type1);
                if (baseEntity.code == 0) {
                    success(baseEntity);
                } else {
                    error(baseEntity.message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            error("数据返回失败");
        }

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        try {
            if (e instanceof HttpException) {
                HttpException httpException = (HttpException) e;
                switch (httpException.code()) {
                    case UNAUTHORIZED:
                        this.error("身份验证未通过");
                        break;
                    case FORBIDDEN:
                        this.error("服务器拒绝请求");
                        break;
                    case NOT_FOUND:
                        this.error("服务器未找到该请求");
                        break;
                    case REQUEST_TIMEOUT:
                        this.error("服务器等候请求时发生超时");
                        break;
                    case INTERNAL_SERVER_ERROR:
                        this.error("服务器遇到错误，无法完成请求");
                        break;
                    case BAD_GATEWAY:
                        this.error("网关或代理错误，从上游服务器收到无效响应");
                        break;
                    case SERVICE_UNAVAILABLE:
                        this.error("服务器连接失败");
                        break;
                    case GATEWAY_TIMEOUT:
                        this.error("请求超时,请检查本地网络");
                        break;
                    default:
                        this.error("网络请求失败");
                }
            } else if (e instanceof ConnectException) {
                this.error("网络连接失败,请检查网络连接");
            } else if (e instanceof TimeoutException) {
                this.error("网络连接超时，请重试");
            } else {
                this.error("网络连接失败");
            }
        } catch (Exception e3) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete() {

    }

    /**
     * 成功
     * @param t
     */
    public abstract void success(BaseEntity<T> t);

    /**
     * 失败
     * @param error
     */
    public abstract void error(String error);
}
