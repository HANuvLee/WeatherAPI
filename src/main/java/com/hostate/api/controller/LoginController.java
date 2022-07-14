package com.hostate.api.controller;

import java.security.MessageDigest;

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
	public String login() throws Exception {
		
		return "/login/login";
	}
	
	//로그인 검증 메소드
	@RequestMapping(value = "/login/loginAction.do", method = RequestMethod.POST)
	public String loginAction(LoginData loginData) throws Exception {
		
		 int EncChk = encryptService.pwEncrypt(loginData.getUser_id() ,loginData.getUser_pw()); 
		 System.out.println(EncChk);
		
		
		return "/main/mainpage";
	}
	
s
}
