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
	
		MessageDigest md = MessageDigest.getInstance("SHA-256"); //"SHA-256 �������� ��ȣȭ�� ���� �ν��Ͻ� ����"
		md.update(user_pw.getBytes()); //�ؽ��� ������Ʈ
		
		String cryptPw = bytesToHex(md.digest()); //md.digest() �޼���� �����Ǵ� �ؽ� ���� ByteArray��, ������ ���Ǹ� ���� Hex String���� ��ȯ���ִ� bytesToHex() �޼��� ���
		
		return encryptDao.pwEncrypt(user_id, cryptPw);
	}
}
