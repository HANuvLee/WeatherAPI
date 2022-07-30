package com.hostate.api.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hostate.api.service.LogService;

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
		
	
	//axgrid�� ajax ȣ�� �� ���� ��Ʈ�ѷ�
	@RequestMapping(value = "/main/selectSearchList.do", method = RequestMethod.GET)
	@ResponseBody
	public String selectSearchTb(HttpServletRequest res) throws Exception { 
		System.out.println("��ȸ���񽺸� ȣ���ϼ̽��ϴ�.");
		  
		JSONObject list = logservice.getSearchInfo(); 
	
	
	return list.toString(); 
	}
	 

}
