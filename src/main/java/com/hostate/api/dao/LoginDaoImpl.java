package com.hostate.api.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hostate.api.vo.LoginData;
import com.hostate.api.vo.Tb_User_InfoVO;
import com.hostate.api.vo.TestTableVO;

@Repository
public class LoginDaoImpl implements LoginDao {
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<TestTableVO> selectTest() throws Exception {
		
		return sqlSession.selectList("selectTest");
	}

	@Override
	public String getUserSalt(Tb_User_InfoVO loginData) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("getUserSalt", loginData);
	}

	@Override
	public Tb_User_InfoVO chkUser(Tb_User_InfoVO loginData) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("chkUser", loginData);
	}
}

