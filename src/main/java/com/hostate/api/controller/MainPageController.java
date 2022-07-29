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
	
	//���� jsp ������ �̵�
	@RequestMapping(value = "/main/mainpage.do", method = RequestMethod.GET)
	public String login(HttpServletRequest res) throws Exception {
		System.out.println("/main/mainpage.do");
			
		return "/main/mainpage";
	}
		
	//axgrid�� ��ȸ�̷��������̺� ��ȸ ajax ���� ��Ʈ�ѷ�
	@RequestMapping(value = "/main/selectSearchList.do", method = RequestMethod.GET)
	public String selectSearchTb(HttpServletRequest res, Tb_weather_search_scope_info info) throws Exception {
		System.out.println("��ȸ���񽺸� ȣ���ϼ̽��ϴ�.");
		
		info = logservice.getSearchInfoList();
		
		return "/main/mainpage";
	}

}
