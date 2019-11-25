package com.ychong.kankan.ui.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Retention 需要Runtime 否则运行时没有这个注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Permission {
    String[] permissions();
    int[] rationales() default {};
    int[] rejects() default {};
}
