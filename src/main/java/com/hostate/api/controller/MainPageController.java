package com.hostate.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainPageController {
		//���� jsp ������ �̵�
		@RequestMapping(value = "/main/mainpage.do", method = RequestMethod.GET)
		public String login(HttpServletRequest res) throws Exception {
			System.out.println("/main/mainpage.do");
			
		    return "/main/mainpage";
		}
	
}
