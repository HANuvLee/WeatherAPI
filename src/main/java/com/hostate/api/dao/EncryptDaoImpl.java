package com.hostate.api.dao;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EncryptDaoImpl implements EncryptDao {

	@Autowired
	private SqlSession sqlsession;
	
	@Override
	public int userLoginChk(Map<String, String> param) throws Exception {

		return sqlsession.selectOne("userLoginChk", param); //���� ���� ��ȸ
	}
}
