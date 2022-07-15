package com.hostate.api.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.hostate.api.vo.TestTableVO;

@Repository
public class LoginDaoImpl implements LoginDao {
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<TestTableVO> selectTest() throws Exception {
		
		return sqlSession.selectList("selectTest");
	}
}
