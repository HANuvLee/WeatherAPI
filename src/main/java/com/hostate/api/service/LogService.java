package com.hostate.api.service;


import org.json.JSONObject;

import com.hostate.api.vo.Tb_weather_search_scope_info;

public interface LogService {

	int searchWeatherLogInsert(Tb_weather_search_scope_info searchInfo) throws Exception;

	JSONObject getFirstApi(Tb_weather_search_scope_info searchInfo)  throws Exception;

	JSONObject getShorWeather(Tb_weather_search_scope_info searchInfo) throws Exception;

}
