package com.hostate.api.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
/*
 * 인터셉터 : 특정 url 요청 시 컨트롤러로 가는 요청을 가로챈다.
 * 인터셉터를 사용하여 모든 요청마다 컨트롤러에서 session으로 로그인 정보 존재 여부를 확인하는 중복코드를 줄여준다.
 *  
 */

import com.mysql.cj.Session;
import com.sun.org.apache.bcel.internal.generic.RETURN;




public class CustomInterceptor extends HandlerInterceptorAdapter {
	 
	//컨트롤러 요청 전 수행 되는 메서드 
	//return true : preHandle 메서드 수행 후 본래 요청한 컨트롤러 수행
	//return false : 컨트롤러로 요청이 가지 않는다.
	 public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException
	            {
	        
		 		System.out.println("preHandle =====> " + request.getRequestURI());
		 		System.out.println("preHandle1");
	        	HttpSession session = request.getSession();
	        	System.out.println("preHandle get session =====> " + session.getAttribute("user_id"));
	        	
				if (session.getAttribute("user_id") != null && !session.getAttribute("user_id").toString().equals("")) { //세션이 있다면
					//해당 요청 컨트롤러 수행
					System.out.println("preHandle2");
					return true;
				}else{//세션이 없고
					if(request.getRequestURI().equals("/login/login.do") || (request.getRequestURI().equals("/login/loginAction.do"))) { //로그인 페이지 요청 시 응답
						System.out.println("preHandle3");
						return true;
					}else { //로그인페이지가 아닐 시 로그인 페이지로 응답
						System.out.println("preHandle4");
						response.sendRedirect("/login/login.do");
						return false;
					}
				}
			}
		/*
		 * @Override public void postHandle(HttpServletRequest request,
		 * HttpServletResponse response, Object handler, ModelAndView modelAndView)
		 * throws Exception {
		 * 
		 * System.out.println("postHandle1");
		 * 
		 * 
		 * }
		 * 
		 * @Override public void afterCompletion(HttpServletRequest request,
		 * HttpServletResponse response, Object handler, Exception ex) throws Exception
		 * {
		 * 
		 * System.out.println("afterCompletion1");
		 * 
		 * 
		 * }
		 */   
	 
}
