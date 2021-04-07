package com.simple.ratelimiter.valve;

import com.simple.ratelimiter.annotation.DoRateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

public interface IValveService {
    Object access(ProceedingJoinPoint jp, Method method, DoRateLimiter doRateLimiter, Object[] args) throws Throwable;
}
