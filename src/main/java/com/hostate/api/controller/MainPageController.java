package com.hostate.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hostate.api.service.LogService;
import com.hostate.api.vo.Tb_weather_search_scope_info;

@Controller
public class MainPageController {
	
	
	@Autowired
	LogService logservice;
	
	//메인 jsp 페이지 이동
	@RequestMapping(value = "/main/mainpage.do", method = RequestMethod.GET)
	public String login(HttpServletRequest res) throws Exception {
		System.out.println("/main/mainpage.do");
			
		return "/main/mainpage";
	}
		
	//axgrid의 조회이력저장테이블 조회 ajax 매핑 컨트롤러
	@RequestMapping(value = "/main/selectSearchList.do", method = RequestMethod.GET)
	public String selectSearchTb(HttpServletRequest res, Tb_weather_search_scope_info info) throws Exception {
		System.out.println("조회서비스를 호출하셨습니다.");
		
		info = logservice.getSearchInfoList();
		
		return "/main/mainpage";
	}

}
