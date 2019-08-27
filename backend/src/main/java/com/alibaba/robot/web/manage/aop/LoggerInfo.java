package com.alibaba.robot.web.manage.aop;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.robot.web.manage.entity.Request;

@Aspect
@Component
public class LoggerInfo {
	
	private Logger LOGGER = Logger.getLogger(LoggerInfo.class);
	
	@Pointcut("execution(* com.alibaba.robot.web.manage.controller.*.*(..))")
	public void logerInffo(){
		
	}
	
	@Around("logerInffo()")
	public Object around(ProceedingJoinPoint joinPoint){
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
		HttpServletRequest request = servletRequestAttributes.getRequest();
		String method = request.getMethod();
		String requestURI = request.getRequestURI();
		
		Object[] args = joinPoint.getArgs();
		for (Object bean : args) {
			Request req = (Request) bean;
			LOGGER.info("request-" + method + "-" + requestURI + ":" + bean);
		}
		
		Object response = null;
		try {
			response = joinPoint.proceed();
			LOGGER.info("response-" + requestURI + ":" + response);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		return response;
		
	}
	
	
}
