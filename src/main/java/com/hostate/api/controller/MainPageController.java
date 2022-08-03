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
	
	//메인 jsp 페이지 이동
	@RequestMapping(value = "/main/mainpage.do", method = RequestMethod.GET)
	public String login(HttpServletRequest res) throws Exception {
		System.out.println("/main/mainpage.do");
			
		return "/main/mainpage";
	}
		
	
	//axgrid의 ajax 호출 시 매핑 컨트롤러
	@ResponseBody
	@RequestMapping(value = "/main/selectSearchList.do", method = RequestMethod.GET)
	public HashMap<String, Object> selectSearchTb(HttpServletRequest res, Tb_weather_search_scope_info tbWeatherInfo, String sortBy) throws Exception { 
		System.out.println("조회서비스를 호출하셨습니다.");
		System.out.println("sortBy 나옵니까? ==> " + sortBy);
		HashMap<String, Object> result = new HashMap<String,Object>(); 
		
		 //최초 접속 시 pagno는 0이므로 1을 대입 
		 if(tbWeatherInfo.getPageNo() == 0) {
			 tbWeatherInfo.setPageNo(1);
		  }//최초 접속 시 listcount는 0이므로 1을 대입 
		 if(tbWeatherInfo.getListCount() == 0) { 
			 tbWeatherInfo.setListCount(10);
		 }
		
		//날씨조회이력 select 서비스 호츨
		result = logservice.getSearchInfo(tbWeatherInfo); 
		
		/* 2022-08-01 사내 개인노트북 인코딩 문제로 인한 JsonObject타입을 리턴하지 않고 HashMap으로 리턴타입을변경
		 * 문제 해결을 위해서는 Json객체로 변환 시 인코딩 설정이 필요하다. => Json변환 시 인코딩 설정 확인해볼것
		 * list.put("result","ok"); */
		result.put("result", "ok");
		
		System.out.println("result :: >> " + result);
		
		return result; 
	}
	
	
}
