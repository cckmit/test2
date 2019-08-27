package com.alibaba.robot.web.manage.aop;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.robot.common.ErrorCode;
import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;
import com.alibaba.robot.web.manage.mapper.UserDao;

@Aspect
@Component
public class TokenVerifyAspect {

	@Autowired
	private UserDao userDao;
	
	@Pointcut("execution(* com.alibaba.robot.web.manage.controller.*Controller.*(..)) !execution(* com.alibaba.robot.web.manage.controller.YunQiController.login(..))")
	public void verifyToken() {
	}
	

	@Around("verifyToken()")
	public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		for (Object bean : args) {
			Request request = (Request) bean;
			String token = request.getToken();

			if (token != null && token.length() > 0) {
				if (token.contains("_")) {
					String[] split = token.split("_");
					if (split != null && split.length > 0) {
						String userId = split[0];
						String tokenReq = split[1];
						// 判断userId是否为数字
						Pattern pattern = Pattern.compile("[0-9]*");
						Matcher isNum = pattern.matcher(userId);
						if (isNum.matches()) {
							int id = Integer.parseInt(userId);
							String userToken = userDao.selectTokenById(id);
							if (userToken != null && userToken.length() > 0) {
								if (userToken.equals(tokenReq)) {
									return joinPoint.proceed();
								}
							}
						}
					}
				}

			}

			return new Response<>(request.getSn(), "访问不合法,请登录！", ErrorCode.ACCESS_DENIED, null);

		}

		return null;
	}
	
	
}
