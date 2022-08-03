package com.hostate.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
		
	
	//axgrid�� ajax ȣ�� �� ���� ��Ʈ�ѷ�
	@ResponseBody
	@RequestMapping(value = "/main/selectSearchList.do", method = RequestMethod.GET)
	public HashMap<String, Object> selectSearchTb(HttpServletRequest res, Tb_weather_search_scope_info tbWeatherInfo, String sortBy) throws Exception { 
		System.out.println("��ȸ���񽺸� ȣ���ϼ̽��ϴ�.");
		System.out.println("sortBy ���ɴϱ�? ==> " + sortBy);
		HashMap<String, Object> result = new HashMap<String,Object>(); 
		
		 //���� ���� �� pagno�� 0�̹Ƿ� 1�� ���� 
		 if(tbWeatherInfo.getPageNo() == 0) {
			 tbWeatherInfo.setPageNo(1);
		  }//���� ���� �� listcount�� 0�̹Ƿ� 1�� ���� 
		 if(tbWeatherInfo.getListCount() == 0) { 
			 tbWeatherInfo.setListCount(10);
		 }
		
		//������ȸ�̷� select ���� ȣ��
		result = logservice.getSearchInfo(tbWeatherInfo); 
		
		/* 2022-08-01 �系 ���γ�Ʈ�� ���ڵ� ������ ���� JsonObjectŸ���� �������� �ʰ� HashMap���� ����Ÿ��������
		 * ���� �ذ��� ���ؼ��� Json��ü�� ��ȯ �� ���ڵ� ������ �ʿ��ϴ�. => Json��ȯ �� ���ڵ� ���� Ȯ���غ���
		 * list.put("result","ok"); */
		result.put("result", "ok");
		
		System.out.println("result :: >> " + result);
		
		return result; 
	}
	
	
}
