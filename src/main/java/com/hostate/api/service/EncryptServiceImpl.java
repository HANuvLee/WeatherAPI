package com.hostate.api.service;

import java.security.MessageDigest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hostate.api.dao.EncryptDao;

@Service
public class EncryptServiceImpl implements EncryptService {
	
	@Autowired
	EncryptDao encryptDao;
	
	private String bytesToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for(byte b : bytes) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}

	@Override
	public int pwEncrypt(String user_id, String user_pw) throws Exception {
	
		MessageDigest md = MessageDigest.getInstance("SHA-256"); //"SHA-256 형식으로 암호화를 위한 인스턴스 생성"
		md.update(user_pw.getBytes()); //해쉬값 업데이트
		
		String cryptPw = bytesToHex(md.digest()); //md.digest() 메서드로 생성되는 해시 값은 ByteArray형, 관리의 편의를 위해 Hex String으로 변환해주는 bytesToHex() 메서드 사용
		
		return encryptDao.pwEncrypt(user_id, cryptPw);
	}
}
