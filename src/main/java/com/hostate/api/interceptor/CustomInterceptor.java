package com.hostate.api.interceptor;

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


public class CustomInterceptor extends HandlerInterceptorAdapter {
	
	 //컨트롤러 요청 전 수행 되는 메서드 
	 public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	            throws Exception {
	        
	        System.out.println("preHandle1");
	       
	        HttpSession session = request.getSession();
			//session.getAttribute("user_id");
	        System.out.println("preHandle2");
															// 공백 : .toString().equals("") ***!!!*** 
			if (session.getAttribute("user_id") == null || session.getAttribute("user_id").toString().equals("") ) {
				// 로그인 페이지 이동 / 현재 로그인상태가 아님
				System.out.println("preHandle3");
				ModelAndView mv = new ModelAndView("redirect:/login/login.do");
				System.out.println("preHandle4");
				throw new ModelAndViewDefiningException(mv);
			}else {
				// 로그인상태임으로 메인페이지로 이동
				System.out.println("preHandle5");
				ModelAndView mv = new ModelAndView("redirect:/main/mainpage.do");
				 System.out.println("preHandle6");
				throw new ModelAndViewDefiningException(mv);
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
