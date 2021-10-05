package com.example.singleapp.aspect;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.UUID;

@Aspect
public class ActivityAspect {

    private static final String TAG = "ActivityAspect";

    @Pointcut("execution(* androidx.appcompat.app.AppCompatActivity.onCreate(..))")
    public void onCreateCutPoint() {

    }

    @Pointcut("execution(@com.example.loginapp.aspect.Intercept * *(..))")
    public void onClickCutPoint() {

    }

    @After("execution(* androidx.appcompat.app.AppCompatActivity.on**(..))")
    public void onXxMethod(JoinPoint joinPoint) {
        Log.e(TAG, "onXxMethod(): " + joinPoint.getSignature());
    }

    @Around("onCreateCutPoint()")
    public void actionOnCreate(ProceedingJoinPoint joinPoint) throws Throwable {
        joinPoint.proceed();
        Signature signature = joinPoint.getSignature();
        Log.e(TAG, "actionOnCreate(): " + signature);
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            String name = methodSignature.getName();
            Log.e(TAG, "actionOnCreate(): " + name);
        }
    }

    @Around("execution(* androidx.appcompat.app.AppCompatActivity.onResume(..))")
    public Object actionOnResume(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        Log.e(TAG, "actionOnResume(): " + signature);
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            String name = methodSignature.getName();
            Log.e(TAG, "actionOnResume(): " + name);
        }
        return joinPoint.proceed();
    }

    @Around("onClickCutPoint()")
    public Object handleClick(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        Log.e(TAG, "handleClick(): " + signature);
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            Log.e(TAG, "handleClick(): return type = " + methodSignature.getReturnType());
        }
        Object[] args = joinPoint.getArgs();
        args[0] = UUID.randomUUID().toString();
        return joinPoint.proceed(args);
    }
}