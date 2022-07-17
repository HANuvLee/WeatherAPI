package com.hostate.api.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hostate.api.dao.LogDao;
import com.hostate.api.dao.LoginDao;

@Service
public class LogServiceImpl implements LogService {
	@Autowired
	LogDao logdao;

	@Override
	public int searchWeatherLogInsert(String user_id, String startdate, String enddate) throws Exception {
		// TODO Auto-generated method stub
	
		HashMap<String, String> param = new HashMap<>();
		param.put("user_id", user_id);
		param.put("startdate", startdate);
		param.put("enddate", enddate);
		
		return logdao.searchWeatherLogInsert(param);
	}
}
