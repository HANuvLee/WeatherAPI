package com.hostate.api.dao;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EncryptDaoImpl implements EncryptDao {

	@Autowired
	private SqlSession sqlsession;

	@Override
	public String pwEncrypt(String user_id, String cryptPw) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> param = new HashMap<>();
		
		param.put("user_id", user_id);
		param.put("user_pw", cryptPw);
		
		return sqlsession.selectOne("pwEncryptuUpdate", param);
	}
}
