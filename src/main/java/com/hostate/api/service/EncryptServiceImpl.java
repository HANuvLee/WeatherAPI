package com.hostate.api.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hostate.api.dao.EncryptDao;
import com.hostate.api.vo.LoginData;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@Service
public class EncryptServiceImpl implements EncryptService {
	
	@Autowired
	EncryptDao encryptDao;

	@Override
	public int pwEncrypt(LoginData loginData) throws Exception {
		
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		byte[] bytes = new byte[16];
		random.nextBytes(bytes);
		String salt = new String(Base64.encode(bytes));
	
		MessageDigest md = MessageDigest.getInstance("SHA-512"); //"SHA-256 형식으로 암호화를 위한 인스턴스 생성"
		md.update(salt.getBytes()); //해쉬값 업데이트
		md.update(loginData.getUser_pw().getBytes()); //해쉬값 업데이트
		
		String hex = String.format("%064x", new BigInteger(1, md.digest()));
		
		Map<String, String> param = new HashMap<>();
		System.out.println("salt => "+ salt);
		System.out.println("hex =>" + hex);
	
		param.put("user_id", loginData.getUser_id()); 
		param.put("user_pw", hex);
		param.put("salt", salt);
		
		int userLoginChk = encryptDao.userLoginChk(param);
		
		return userLoginChk;
	}
	
}
