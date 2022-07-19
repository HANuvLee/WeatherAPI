package com.hostate.api.service;

import com.hostate.api.vo.Tb_weather_search_scope_info;

public interface LogService {

	int searchWeatherLogInsert(Tb_weather_search_scope_info searchInfo) throws Exception;

}
