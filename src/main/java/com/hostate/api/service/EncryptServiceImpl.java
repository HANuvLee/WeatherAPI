package com.hostate.api.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hostate.api.commonutil.SHA256;
import com.hostate.api.dao.EncryptDao;
import com.hostate.api.vo.LoginData;

@Service
public class EncryptServiceImpl implements EncryptService {
	
	@Autowired
	EncryptDao encryptDao;
	@Autowired
	SHA256 sha256;

	@Override
	public int pwEncrypt(LoginData loginData) throws Exception {
		
		Map<String, String> param = new HashMap<>();
 		
		String pwEncrypt = sha256.encrypt(loginData.getUser_pw()); //USER_PW 암호화 
		
		param.put("user_id", loginData.getUser_id()); 
		param.put("user_pw", pwEncrypt);
		
		int userLoginChk = encryptDao.userLoginChk(param);
		
		if(userLoginChk == 0) { //DB 암호화 패스워드 업데이트 
			userLoginChk = encryptDao.userLoginUpdate(param);
			return userLoginChk;
		}else{ //사용자 아이디가 DB 존재하면 1 리턴
			return userLoginChk;
		}
	}
	
}
