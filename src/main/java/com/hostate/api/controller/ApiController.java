package com.hostate.api.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.hostate.api.commonutil.ApiDateFormat;
import com.hostate.api.service.LogService;
import com.hostate.api.vo.Tb_weather_search_scope_info;

//@RestController : 기본 하위의 메소드들은 모두 @responsebody를 갖는다
//@RequestBody : 클라이언트 요청 xml/json을 자바 객체로 변환하여 전달 받을 수 있다.
//@ResponseBody : 자바 객체를 xml/json으로 변환시켜 응답객체의 Body에 실어 전송가능하다.

@RestController
public class ApiController {

	@Autowired
	ApiDateFormat apiDateFormat;

	@Autowired
	LogService logService;

	/*
	 * @API LIST ~
	 * 
	 * getVilageFcst 단기예보조회 getMidTa 중기기온조회 getMidLandFcst 중기육상예보조회
	 */

	// 단기예보
	@RequestMapping(value = "/api/searchShortweather.do", method = RequestMethod.GET)
	public String searchShortWeather(HttpSession session, Tb_weather_search_scope_info searchInfo) throws Exception {
		System.out.println("단기예보호출");
		
		if ("".equals(searchInfo.getStart_date()) || searchInfo.getStart_date() == null || "".equals(searchInfo.getEnd_date()) || searchInfo.getStart_date() == null) {
			JSONObject jsonObj = logService.getFirstApi(searchInfo);
			return jsonObj.toString();
		}else {
			System.out.println("searchShortweather 2");
			searchInfo.setUser_id((String) session.getAttribute("user_id"));
			searchInfo.setUser_name((String) session.getAttribute("user_name"));
			
			//조회기록저장
			logService.searchWeatherLogInsert(searchInfo);
			

			JSONObject jsonObj = logService.getShortWeather(searchInfo);
		
			return jsonObj.toString();
		}
	}

	//중기예보
	@RequestMapping(value = "/api/searchmidtaweather.do", method = RequestMethod.GET)
	public String searchMidWeahter(HttpSession session, Tb_weather_search_scope_info searchInfo) throws Exception {
		System.out.println("중기기온예보");
		
		//조회이력시 DB에 이력저장을 위한 사용자아이디와 이름을 set
		searchInfo.setUser_id((String) session.getAttribute("user_id"));
		searchInfo.setUser_name((String) session.getAttribute("user_name"));
		
		// 조회기록저장
		logService.searchWeatherLogInsert(searchInfo);

		JSONObject jsonObj = logService.getMidWeather(searchInfo);
		
		return jsonObj.toString();
	}
	
	//단기예보 + 중기예보
	@RequestMapping(value = "/api/searchAllweather.do", method = RequestMethod.GET)
	public String searchAllWeahter(HttpSession session, Tb_weather_search_scope_info searchInfo) throws Exception {
		System.out.println("모든예보호출");

		searchInfo.setUser_id((String) session.getAttribute("user_id"));
		searchInfo.setUser_name((String) session.getAttribute("user_name"));
		
		//조회기록저장
		logService.searchWeatherLogInsert(searchInfo);
		
		JSONObject jsonObj = logService.getAllWeather(searchInfo);

		return jsonObj.toString();
	}

}
