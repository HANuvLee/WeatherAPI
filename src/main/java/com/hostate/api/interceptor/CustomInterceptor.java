package com.hostate.api.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CustomInterceptor extends HandlerInterceptorAdapter {

	 public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	            throws Exception {
	        
	        System.out.println("preHandle1");
	       
	        HttpSession session = request.getSession();
			//session.getAttribute("user_id");
															// ���� : .toString().equals("") ***!!!*** 
			if (session.getAttribute("user_id") == null || session.getAttribute("user_id").toString().equals("") ) {
				// �α��� ������ �̵� / ���� �α��λ��°� �ƴ�
				ModelAndView mv = new ModelAndView("redirect:/login/login.do");
				throw new ModelAndViewDefiningException(mv);
			}else {
				// �α��λ��������� ������������ �̵�
				ModelAndView mv = new ModelAndView("redirect:/main/mainpage.do");
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
