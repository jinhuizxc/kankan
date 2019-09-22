package com.ychong.kankan;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ParameterizedTypeImpl implements ParameterizedType {
    Type[] type;
    Class raw;

    @Override
    public Type[] getActualTypeArguments() {
        // TODO 自动生成的方法存根
        return type;
    }

    @Override
    public Type getOwnerType() {
        // TODO 自动生成的方法存根
        return null;
    }

    public ParameterizedTypeImpl(Type[] args, Class raw) {
        super();
        this.type = args != null ? args : new Type[0];
        this.raw = raw;
    }

    @Override
    public Type getRawType() {
        // TODO 自动生成的方法存根
        return raw;
    }

}
