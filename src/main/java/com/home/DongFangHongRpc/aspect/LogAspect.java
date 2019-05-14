package com.home.DongFangHongRpc.aspect;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {
    @Pointcut("execution(public * com.home.DongFangHongRpc.*.*.*(..))")
    public void log() {}

    @Around("log()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long begin = System.currentTimeMillis();
        Object obj = pjp.proceed();
        long end = System.currentTimeMillis();

        String args = JSON.toJSONString(pjp.getArgs());
        String rets = JSON.toJSONString(obj);
        log.info("调用方法：{}, 耗时：{}, 参数：{}, 返回值: {}",pjp.getSignature().toString(), (end - begin),
                args.length() > 100 ? "..." : args, rets.length() > 100 ? "..." : rets);
        return obj;
    }
}
