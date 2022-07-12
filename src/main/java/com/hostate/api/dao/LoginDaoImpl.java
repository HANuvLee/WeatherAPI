package com.hostate.api.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hostate.api.vo.Board;

@Repository
public class LoginDaoImpl implements LoginDao {
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<Board> selectTest() throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectList("selectTest");
	}

}
