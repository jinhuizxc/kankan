package com.ychong.kankan.utils.fastclick;

import android.view.View;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * Aspect AOP处理类
 */
@Aspect
public class SingleClickAspect {
    private static final long DEFAULT_TIME_INTERVAL = 5000;
    /**
     * 定义一个切面方法 包裹切点方法
     */
    @Around("execution(@com.ychong.kankan.utils.fastclick.SingleClick * *(..))")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable{
        //取出方法内的参数
        View view = null;
        for (Object o:joinPoint.getArgs()){
            if (o instanceof View){
                view = (View)o;
                break;
            }
        }
        if (view == null){
            return;
        }
        //取出方法的注解
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        if (!method.isAnnotationPresent(SingleClick.class)){
            return;
        }
        SingleClick singleClick = method.getAnnotation(SingleClick.class);
        //判断是否快速点击
        if (!XClickUtils.isFastDoubleClick(view,singleClick.value())){
            //不是快速点击，执行原方法
            joinPoint.proceed();
        }
    }
}
