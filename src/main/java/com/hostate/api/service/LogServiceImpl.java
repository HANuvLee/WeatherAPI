package com.hostate.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hostate.api.dao.LogDao;
import com.hostate.api.vo.Tb_weather_search_scope_info;


@Service
public class LogServiceImpl implements LogService {
	@Autowired
	LogDao logdao;
	
	@Override
	public int searchWeatherLogInsert(Tb_weather_search_scope_info searchInfo) throws Exception {
		
		return logdao.searchWeatherLogInsert(searchInfo);
	}
}
