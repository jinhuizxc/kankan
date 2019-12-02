package com.ychong.baselib.utils.http;


/**
 * 自定义异常，用于提示自定义错误信息
 */
public class CustomThrowable extends Throwable {
    public CustomThrowable(String message){
        super(message);
    }

}
