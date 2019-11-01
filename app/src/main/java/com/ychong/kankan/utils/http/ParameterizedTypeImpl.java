package com.ychong.kankan.utils.http;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ParameterizedTypeImpl implements ParameterizedType {
    private  Class raw;
    private  Type[] args;
    public ParameterizedTypeImpl(Class raw,Type[] args){
        this.raw = raw;
        this.args = args !=null?args:new Type[0];
    }
    @NonNull
    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }

    @NonNull
    @Override
    public Type getRawType() {
        return raw;
    }

    @Nullable
    @Override
    public Type getOwnerType() {
        return null;
    }
}
