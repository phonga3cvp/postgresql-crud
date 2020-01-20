package com.phongdq.postgresqlcrud.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class PerformanceMonitorAspect {
  private static final Logger logger = LoggerFactory.getLogger(PerformanceMonitorAspect.class);
  @Pointcut("execution(public* com.phongdq.postgresqlcrud.controller.*.*(..))")
  public void allControllerPublicMethod(){}

  @Around("allControllerPublicMethod() && @annotation(Profiler)")
  public Object profileMethodExecution(ProceedingJoinPoint jp) throws Throwable {
    MethodSignature signature = (MethodSignature) jp.getSignature();

    String methodName = signature.getName();
    String className = signature.getDeclaringType().getSimpleName();

    final StopWatch stopWatch = new StopWatch(methodName);
    stopWatch.start();
    Object object = jp.proceed();
    stopWatch.stop();
    logger.info(String.format("StopWatch %s.%s: running time = %d ms", className, methodName, stopWatch.getTotalTimeMillis()));
    return object;
  }
}
