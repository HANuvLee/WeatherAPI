package com.hostate.api.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hostate.api.service.EncryptService;
import com.hostate.api.service.LoginService;
import com.hostate.api.vo.LoginData;

/**
 * Handles requests for the application home page.
 */
@Controller
public class LoginController {
	
	@Autowired
	LoginService loginService;
	@Autowired
	EncryptService encryptService;
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	
	//로그인 jsp 페이지 이동
	@RequestMapping(value = "/login/login.do", method = RequestMethod.GET)
	public String login(HttpServletRequest res) throws Exception {
		System.out.println("/login/login.do");
		return "/login/login";
	}
	
	//로그인 검증
	@RequestMapping(value = "/login/loginAction.do", method = RequestMethod.POST)
	public String loginAction(LoginData loginData, HttpSession session) throws Exception {	
		System.out.println("/login/loginAction.do");
		int encChk = encryptService.pwEncrypt(loginData);
		
		if(encChk != 0) {
			session.setAttribute("user_id", loginData.getUser_id());
			return "redirect:/main/mainpage.do";
		}else {
			return "redirect:/login/login.do";
		}
	}
	
	//로그아웃
	@RequestMapping(value = "/login/logout.do", method = RequestMethod.GET)
	public String logout(HttpSession session) throws Exception {
		session.invalidate();
		return "redirect:/main/mainpage.do";
	}
}
