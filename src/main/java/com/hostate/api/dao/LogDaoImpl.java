package com.hostate.api.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hostate.api.vo.Tb_User_InfoVO;
import com.hostate.api.vo.Tb_weather_search_scope_info;

@Repository
public class LogDaoImpl implements LogDao {
	@Autowired
	private SqlSession sqlSession;

	@Override
	public int searchWeatherLogInsert(Tb_weather_search_scope_info searchInfo) throws Exception {
		
		return sqlSession.insert("searchWeatherLogInsert", searchInfo);
	}

	@Override
	public List<Tb_weather_search_scope_info> getSearchInfo(Tb_weather_search_scope_info tbWeatherInfo) throws Exception {
		
		return sqlSession.selectList("selectSearchInfo", tbWeatherInfo);
	}

	@Override
	public int getTotalCnt(Tb_weather_search_scope_info tbWeatherInfo) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("selectTotalCnt", tbWeatherInfo);
	}

	@Override
	public List<Tb_User_InfoVO> getUsersList() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("getUsersList daoImpl start");
		return sqlSession.selectList("getUsersList");
	}
}
