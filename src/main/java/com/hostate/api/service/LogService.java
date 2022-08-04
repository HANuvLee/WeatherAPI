package com.hostate.api.service;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.hostate.api.vo.Tb_User_InfoVO;
import com.hostate.api.vo.Tb_weather_search_scope_info;

public interface LogService {

	int searchWeatherLogInsert(Tb_weather_search_scope_info searchInfo) throws Exception;

	JSONObject getFirstApi(Tb_weather_search_scope_info searchInfo)  throws Exception;

	JSONObject getShortWeather(Tb_weather_search_scope_info searchInfo) throws Exception;

	JSONObject getMidWeather(Tb_weather_search_scope_info searchInfo) throws Exception;
	
	JSONObject getAllWeather(Tb_weather_search_scope_info searchInfo) throws Exception;

	HashMap<String, Object> getSearchInfo(Tb_weather_search_scope_info tbWeatherInfo) throws Exception;

	List<Tb_User_InfoVO> getUsersList() throws Exception;

	List<Tb_weather_search_scope_info> getselectAXUser(Tb_weather_search_scope_info tbWeatherInfo) throws Exception;

	

}
