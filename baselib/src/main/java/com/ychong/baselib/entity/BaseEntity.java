package com.ychong.baselib.entity;

/**
 * 响应体基类
 * @param <T>
 */
public class BaseEntity<T> {
    public String message;//响应提示
    public int code;//响应码
    public T data;//响应数据
}
