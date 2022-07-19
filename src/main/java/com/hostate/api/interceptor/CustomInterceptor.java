package com.hostate.api.interceptor;

import java.io.IOException;

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

import com.sun.org.apache.bcel.internal.generic.RETURN;




public class CustomInterceptor extends HandlerInterceptorAdapter {
	 
	//��Ʈ�ѷ� ��û �� ���� �Ǵ� �޼��� 
	//return true : preHandle �޼��� ���� �� ���� ��û�� ��Ʈ�ѷ� ����
	//return false : ��Ʈ�ѷ��� ��û�� ���� �ʴ´�.
	 public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException
	            {
	        
	        System.out.println("preHandle1");
	        System.out.println(request.getRequestURI());
	        
	        	System.out.println("preHandle2");
	        	HttpSession session = request.getSession();
	        	
				if (session.getAttribute("user_id") != null && !session.getAttribute("user_id").toString().equals("")) {
					//�ش� ��û ��Ʈ�Ѷ� �̵�
					System.out.println("preHandle3");
					return true;
				}else {
					if(request.getRequestURI() == "/login/login.do") {
						System.out.println("preHandle4");
						return true;
					}else {
						System.out.println("preHandle5");
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
