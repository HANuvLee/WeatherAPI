package com.hostate.api.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
/*
 * ���ͼ��� : Ư�� url ��û �� ��Ʈ�ѷ��� ���� ��û�� ����æ��.
 * ���ͼ��͸� ����Ͽ� ��� ��û���� ��Ʈ�ѷ����� session���� �α��� ���� ���� ���θ� Ȯ���ϴ� �ߺ��ڵ带 �ٿ��ش�.
 *  
 */


public class CustomInterceptor extends HandlerInterceptorAdapter {
	
	 //��Ʈ�ѷ� ��û �� ���� �Ǵ� �޼��� 
	 public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	            throws Exception {
	        
	        System.out.println("preHandle1");
	       
	        HttpSession session = request.getSession();
			//session.getAttribute("user_id");
	        System.out.println("preHandle2");
															// ���� : .toString().equals("") ***!!!*** 
			if (session.getAttribute("user_id") == null || session.getAttribute("user_id").toString().equals("") ) {
				// �α��� ������ �̵� / ���� �α��λ��°� �ƴ�
				System.out.println("preHandle3");
				ModelAndView mv = new ModelAndView("redirect:/login/login.do");
				System.out.println("preHandle4");
				throw new ModelAndViewDefiningException(mv);
			}else {
				// �α��λ��������� ������������ �̵�
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
