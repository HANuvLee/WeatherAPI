package com.hostate.api.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hostate.api.vo.Tb_weather_search_scope_info;

@Repository
public class LogDaoImpl implements LogDao {
	@Autowired
	private SqlSession sqlSession;

	@Override
	public int searchWeatherLogInsert(Tb_weather_search_scope_info searchInfo) throws Exception {
		
		return sqlSession.insert("searchWeatherLogInsert", searchInfo);
	}
}
