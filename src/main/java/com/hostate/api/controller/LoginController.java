package com.hostate.api.controller;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
import com.hostate.api.vo.Tb_User_InfoVO;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

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
	public String loginAction(Tb_User_InfoVO loginData, HttpSession session) throws Exception {	
		
		Tb_User_InfoVO userChk = loginService.userChk(loginData);
		
		if(userChk.getUser_id()!=null && !userChk.getUser_id().equals("")) {
			session.setAttribute("user_id", userChk.getUser_id());
			session.setAttribute("user_name", userChk.getUser_name());
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
