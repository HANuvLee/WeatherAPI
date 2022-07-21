package com.hostate.api.service;

import javax.servlet.http.HttpSession;

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
		
		System.out.println("searchWeatherLogInsert insert¹® ½ÇÇà" + searchInfo.getUser_id());
		return logdao.searchWeatherLogInsert(searchInfo);
	}
}
