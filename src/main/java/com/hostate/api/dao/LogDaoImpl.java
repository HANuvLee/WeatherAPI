package com.hostate.api.dao;

import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LogDaoImpl implements LogDao {
	@Autowired
	private SqlSession sqlSession;

	@Override
	public int searchWeatherLogInsert(HashMap<String, String> param) throws Exception {
		
		return sqlSession.insert("searchWeatherLogInsert", param);
	}
}
