package com.alibaba.robot.web.manage.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.robot.web.manage.entity.Request;
import com.alibaba.robot.web.manage.entity.Response;
import com.alibaba.robot.web.manage.service.YunQiService;

@RestController
@RequestMapping("/yunqi")
public class YunQiUserLogin {

	@Autowired
	private YunQiService yunQiService;
	
	private Logger LOGGER = Logger.getLogger(YunQiUserLogin.class);
	
	//用户登录系统
	@RequestMapping("/user_login")
	@ResponseBody
	public Response login(@RequestBody Request request, HttpServletRequest hRequest, HttpServletResponse hResponse){
		Response response = yunQiService.login(request,hRequest,hResponse);
		return response;
	}
}
