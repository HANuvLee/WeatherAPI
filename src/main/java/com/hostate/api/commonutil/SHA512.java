package com.hostate.api.commonutil;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import com.hostate.api.vo.LoginData;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@Component
public class SHA512 {
	
	public String getSalt(LoginData loginData) throws NoSuchAlgorithmException {
		
		return GetSalt(loginData);
	}
	
	public String encrypt(LoginData loginData) throws NoSuchAlgorithmException {
		
		return Encrypt(loginData);
	}
	
	private String GetSalt(LoginData loginData) throws NoSuchAlgorithmException {
		
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		byte[] bytes = new byte[16];
		random.nextBytes(bytes);
		String salt = new String(Base64.encode(bytes));
		return salt;
	}
	
	private String Encrypt(LoginData loginData) throws NoSuchAlgorithmException {
		
		MessageDigest md = MessageDigest.getInstance("SHA-512"); //"SHA-512 �������� ��ȣȭ�� ���� �ν��Ͻ� ����"
		md.update(loginData.getUser_salt().getBytes()); //�ؽ��� ������Ʈ
		md.update(loginData.getUser_pw().getBytes()); //�ؽ��� ������Ʈ
		
		String hex = String.format("%064x", new BigInteger(1, md.digest()));
		return hex;
	}
	
}
