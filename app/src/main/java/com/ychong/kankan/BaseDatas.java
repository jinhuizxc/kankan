package com.ychong.kankan;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class BaseDatas<T> {
    @Override
    public String toString() {
        return "BaseDatas{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", success=" + success +
                ", data=" + data +
                '}';
    }

    public String code;
    public String msg;
    public boolean success;
    public T data;

    public BaseData<T> toJsonModel(String json) {
        Type type = getClass().getGenericSuperclass();
        Type[] t = ((ParameterizedType) type).getActualTypeArguments();
        Type ty = new ParameterizedTypeImpl(t, BaseData.class);
        return new Gson().fromJson(json, ty );
    }
}
