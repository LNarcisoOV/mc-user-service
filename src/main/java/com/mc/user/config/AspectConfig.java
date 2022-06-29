package com.mc.user.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class AspectConfig {

    private Logger LOGGER = LoggerFactory.getLogger(AspectConfig.class);
    
    @Before(value = "execution(* com.mc.user..*.*(..))")
    public void logStatementBefore(JoinPoint joinPoint) {
        LOGGER.info("Executing before: {}", joinPoint);
    }
    
    @After(value = "execution(* com.mc.user..*.*(..))")
    public void logStatementAfter(JoinPoint joinPoint) {
        LOGGER.info("Complete after: {}", joinPoint);
    }
}
