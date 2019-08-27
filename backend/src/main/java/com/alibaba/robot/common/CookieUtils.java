package com.alibaba.robot.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {
	
	public static Cookie getCookie(HttpServletRequest request, String cookieName){
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals(cookieName)){
					return cookie;
				}
			}
		}
		return null;
	}
	
	
	public static void addCookie(HttpServletResponse response, String cookieName,	String value){
		Cookie cookie = new Cookie(cookieName, value);
		cookie.setMaxAge(60*60*2);
		response.addCookie(cookie);
	}
	
}
