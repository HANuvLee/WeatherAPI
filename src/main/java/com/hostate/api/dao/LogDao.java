package com.hostate.api.dao;

import java.util.List;

import com.hostate.api.vo.Tb_weather_search_scope_info;

public interface LogDao {

	int searchWeatherLogInsert(Tb_weather_search_scope_info searchInfo) throws Exception;

	List<Tb_weather_search_scope_info> getSearchInfo(Tb_weather_search_scope_info tbWeatherInfo) throws Exception;

	int getTotalCnt(Tb_weather_search_scope_info tbWeatherInfo) throws Exception;

}
