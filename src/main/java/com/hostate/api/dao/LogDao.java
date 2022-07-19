package com.hostate.api.dao;

import com.hostate.api.vo.Tb_weather_search_scope_info;

public interface LogDao {

	int searchWeatherLogInsert(Tb_weather_search_scope_info searchInfo) throws Exception;

}
