package com.hostate.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainPageController {
		//���� jsp ������ �̵�
		@RequestMapping(value = "/main/mainpage.do", method = RequestMethod.GET)
		public String login() throws Exception {
			
			return "/main/mainpage";
		}
}
