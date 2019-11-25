package com.ychong.kankan.ui.permission;

import android.app.Service;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Aspect
public class PermissionAspect {

    @Around("execution(@com.ychong.kankan.ui.permission.Permission * *(..))")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint){
        try {
            //获取方法注解
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            Permission annotation = method.getAnnotation(Permission.class);
            //获取注解参数，这里我们有3个参数需要获取
            String[] permissions =  annotation.permissions();
            int[] rationales = annotation.rationales();
            int[] rejects = annotation.rejects();
            List<String> permissionList = Arrays.asList(permissions);
            //获取上下文
            Object object = joinPoint.getThis();
            Context context = null;
            if (object instanceof FragmentActivity){
                context = (FragmentActivity) object;
            }else if (object instanceof Fragment){
                context = ((Fragment)object).getContext();
            }else if (object instanceof Service){
                context = (Service)object;
            }
            //申请权限
            GPermission.with(context)
                    .permission(permissions)
                    .callBack(new PermissionCallback() {
                        @Override
                        public void onPermissionGranted() {
                            try {
                                //权限申请通过  执行原方法
                                joinPoint.proceed();
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }

                        @Override
                        public void shouldShowRational(String permission) {
                            //申请权限被拒绝  但是没有勾选 “不在提醒” 这里我们让外部自行处理
                            int index =  permissionList.indexOf(permission);
                            int rationale = -1;
                            if (rationales.length>index){
                                rationale = rationales[index];
                            }
                            GPermission.getGlobalConfigCallback().shouldShowRational(permission,rationale);

                        }

                        @Override
                        public void onPermissionReject(String permission) {
                            //申请权限被拒绝  且勾选了  “不在提醒” 这里让外部自行解决
                            int index = permissionList.indexOf(permission);
                            int reject = -1;
                            if (rejects.length>index){
                                reject = rejects[index];
                            }
                            GPermission.getGlobalConfigCallback().onPermissionReject(permission,reject);
                        }
                    }).request();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
