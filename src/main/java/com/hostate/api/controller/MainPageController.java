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
import com.hostate.api.vo.PageVo;
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
	@RequestMapping(value = "/main/selectSearchList.do", method = RequestMethod.POST)
	public HashMap<String, Object> selectSearchTb(HttpServletRequest res, PageVo pageVo) throws Exception { 
		System.out.println("��ȸ���񽺸� ȣ���ϼ̽��ϴ�.");
		
		List<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();  
		HashMap<String, Object> data = new HashMap<String,Object>(); 
		
		//���� ���� �� pagno�� 0�̹Ƿ� 1�� ����
		if(pageVo.getPageNo() == 0) {
			pageVo.setPageNo(1);
		}
	
		result = logservice.getSearchInfo(pageVo); 
		

		/* 2022-08-01 �系 ���γ�Ʈ�� ���ڵ� ������ ���� JsonObjectŸ���� �������� �ʰ� HashMap���� ����Ÿ��������
		 * ���� �ذ��� ���ؼ��� Json��ü�� ��ȯ �� ���ڵ� ������ �ʿ��ϴ�. => Json��ȯ �� ���ڵ� ���� Ȯ���غ���
		 * list.put("result","ok"); */
		System.out.println("mainpageController searchInfo List => " + result);
		data.put("list", result);
		
		System.out.println("mainpageController searchInfo List => " + data);
		
		return data; 
	}
	
		//axgrid�� paging ȣ�� �� ���� ��Ʈ�ѷ�
		@ResponseBody
		@RequestMapping(value = "/main/searchPaging.do", method = RequestMethod.GET)
		public HashMap<String, Object> selectPaging(HttpServletRequest res) throws Exception { 
			System.out.println("����¡ ���񽺸� ȣ���ϼ̽��ϴ�.");
			String pageNo = res.getParameter("pageNo");
			System.out.println("����¡���� �������ѹ� => " + pageNo);
			
			return null; 
		}
	
}
