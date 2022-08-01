package com.hostate.api.service;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.hostate.api.vo.PageVo;
import com.hostate.api.vo.Tb_weather_search_scope_info;

public interface LogService {

	int searchWeatherLogInsert(Tb_weather_search_scope_info searchInfo) throws Exception;

	JSONObject getFirstApi(Tb_weather_search_scope_info searchInfo)  throws Exception;

	JSONObject getShortWeather(Tb_weather_search_scope_info searchInfo) throws Exception;

	JSONObject getMidWeather(Tb_weather_search_scope_info searchInfo) throws Exception;
	
	JSONObject getAllWeather(Tb_weather_search_scope_info searchInfo) throws Exception;

	List<HashMap<String, String>> getSearchInfo(PageVo pageVo) throws Exception;

	

}
