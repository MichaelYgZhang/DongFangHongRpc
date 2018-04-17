package com.home.DongFangHongRpc.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.UUID;

@Slf4j
@Aspect
@Component
public class LogAspect {
    private static final ThreadLocal<Long> TIME_TREAD_LOCAL = new ThreadLocal<Long>();
    private static final String SESSION_ID_KEY = "sessionId";

    //@Pointcut("execution(* com.iqianjin.robot.controller.api..*.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    @Pointcut("execution(* com.home.DongFangHongRpc..*.*(..))"// +
//            " || execution(* com.iqianjin.robot.controller.app..*.*(..))" +
//            " || execution(* com.iqianjin.robot.advice..*.*(..))"
            )
    public void log() {
    }

    @Before("log()")
    public void before(JoinPoint joinPoint) {
        Long startTime = TIME_TREAD_LOCAL.get();
        if (startTime == null) {
            MDC.put(SESSION_ID_KEY, UUID.randomUUID().toString());

            TIME_TREAD_LOCAL.set(System.currentTimeMillis());

            beforePrint();
        }
    }


    @After("log()")
    public void after() {
        //log.info("aop的after()方法");
    }

    @AfterReturning(returning = "result", pointcut = "log()")
    public Object afterReturn(Object result) {
        afterPrint(result);

        return result;
    }

    /**
     * 获取请求参数
     * @param request
     * @return
     */
    private String getReqParameter(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Enumeration<String> enumeration = request.getParameterNames();
        JSONArray jsonArray = new JSONArray();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            String value = request.getParameter(key);
            JSONObject json = new JSONObject();
            json.put(key, value);
            jsonArray.add(json);
        }
        return jsonArray.toString();
    }

    /**
     * 打印开始日志信息
     */
    private void beforePrint() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String requestParam = getReqParameter(request);
        String requestMethod = request.getMethod();
        String uri = request.getRequestURI();
        log.info("request.请求url={},请求方法={},请求参数={}", uri, requestMethod, requestParam);
    }

    /**
     * 打印返回日志信息
     * @param result
     */
    private void afterPrint(Object result) {
        long startTime = TIME_TREAD_LOCAL.get();
        double callTime = (System.currentTimeMillis() - startTime);

        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String requestParam = getReqParameter(request);
        String requestMethod = request.getMethod();
        String uri = request.getRequestURI();
        String resultStr = JSON.toJSONString(result);
        log.info("response.请求uri={},请求方法={},请求参数={},响应={},处理时间={}毫秒", uri, requestMethod, requestParam, resultStr, callTime);
    }

}
