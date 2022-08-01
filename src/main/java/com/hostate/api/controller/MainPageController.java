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
	
	//메인 jsp 페이지 이동
	@RequestMapping(value = "/main/mainpage.do", method = RequestMethod.GET)
	public String login(HttpServletRequest res) throws Exception {
		System.out.println("/main/mainpage.do");
			
		return "/main/mainpage";
	}
		
	
	//axgrid의 ajax 호출 시 매핑 컨트롤러
	@ResponseBody
	@RequestMapping(value = "/main/selectSearchList.do", method = RequestMethod.POST)
	public HashMap<String, Object> selectSearchTb(HttpServletRequest res, PageVo pageVo) throws Exception { 
		System.out.println("조회서비스를 호출하셨습니다.");
		
		List<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();  
		HashMap<String, Object> data = new HashMap<String,Object>(); 
		
		//최초 접속 시 pagno는 0이므로 1을 대입
		if(pageVo.getPageNo() == 0) {
			pageVo.setPageNo(1);
		}
	
		result = logservice.getSearchInfo(pageVo); 
		

		/* 2022-08-01 사내 개인노트북 인코딩 문제로 인한 JsonObject타입을 리턴하지 않고 HashMap으로 리턴타입을변경
		 * 문제 해결을 위해서는 Json객체로 변환 시 인코딩 설정이 필요하다. => Json변환 시 인코딩 설정 확인해볼것
		 * list.put("result","ok"); */
		System.out.println("mainpageController searchInfo List => " + result);
		data.put("list", result);
		
		System.out.println("mainpageController searchInfo List => " + data);
		
		return data; 
	}
	
		//axgrid의 paging 호출 시 매핑 컨트롤러
		@ResponseBody
		@RequestMapping(value = "/main/searchPaging.do", method = RequestMethod.GET)
		public HashMap<String, Object> selectPaging(HttpServletRequest res) throws Exception { 
			System.out.println("페이징 서비스를 호출하셨습니다.");
			String pageNo = res.getParameter("pageNo");
			System.out.println("페이징서비스 페이지넘버 => " + pageNo);
			
			return null; 
		}
	
}
