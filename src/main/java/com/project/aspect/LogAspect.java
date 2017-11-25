package com.project.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Claire on 11/25/17.
 */

@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.project.controller.IndexController.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        logger.info("before method");
    }

    @After("execution(* com.project.controller.IndexController.*(..))")
    public void afterMethod(){
        logger.info("after method");
    }
}
