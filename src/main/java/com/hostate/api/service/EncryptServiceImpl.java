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
 		
		String pwEncrypt = sha256.encrypt(loginData.getUser_pw()); //USER_PW ��ȣȭ 
		
		param.put("user_id", loginData.getUser_id()); 
		param.put("user_pw", pwEncrypt);
		
		int userLoginChk = encryptDao.userLoginChk(param);
		
		if(userLoginChk == 0) { //����� ������ DB �������� ������ 0 ���� 
			return userLoginChk;
		}else{ //����� ���̵� DB �����ϸ� 1 ����
			return userLoginChk;
		}
	}
	
}
