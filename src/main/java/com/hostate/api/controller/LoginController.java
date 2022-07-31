package com.hostate.api.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hostate.api.service.EncryptService;
import com.hostate.api.service.LoginService;
import com.hostate.api.vo.Tb_User_InfoVO;


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
	
	//�α��� jsp ������ �̵�
	@RequestMapping(value = "/login/login.do", method = RequestMethod.GET)
	public String login(HttpServletRequest res) throws Exception {
		System.out.println("/login/login.do");
		return "/login/login";
	}
	
	//�α��� ����
	@ResponseBody
	@RequestMapping(value = "/login/loginAction.do", method = RequestMethod.POST)
	public HashMap<String, String> loginAction(Tb_User_InfoVO loginData, HttpSession session, ModelAndView mv) throws Exception {	
		
		Tb_User_InfoVO userChk = loginService.userChk(loginData);
		//�α��� �������� ������� ��� ���� result ����
		HashMap<String, String> result = new HashMap<String, String>(); 
		
		if(userChk != null) {
			session.setAttribute("user_id", userChk.getUser_id());
			session.setAttribute("user_name", userChk.getUser_name());
			result.put("result", "success");
			result.put("url", "/"); //�ֻ��� ��û�ּ�
			return result;
		}else{
			result.put("result", "fail");
			return result;
		}
	}
	
	//�α׾ƿ�
	@RequestMapping(value = "/login/logout.do", method = RequestMethod.GET)
	public String logout(HttpSession session) throws Exception {
		session.invalidate();
		return "redirect:/login/login.do";
	}
}
