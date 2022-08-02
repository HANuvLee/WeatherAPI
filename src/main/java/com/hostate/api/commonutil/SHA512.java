package com.hostate.api.commonutil;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import com.hostate.api.vo.LoginData;
import com.hostate.api.vo.Tb_User_InfoVO;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@Component
public class SHA512 {
	
	public String getSalt(Tb_User_InfoVO loginData) throws NoSuchAlgorithmException {
		
		return GetSalt(loginData);
	}
	
	public String encrypt(Tb_User_InfoVO loginData) throws NoSuchAlgorithmException {
		
		return Encrypt(loginData);
	}
	
	private String GetSalt(Tb_User_InfoVO loginData) throws NoSuchAlgorithmException {
		
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		byte[] bytes = new byte[16];
		random.nextBytes(bytes);
		String salt = new String(Base64.encode(bytes));

		return salt;
	}
	
	private String Encrypt(Tb_User_InfoVO loginData) throws NoSuchAlgorithmException {
		
		MessageDigest md = MessageDigest.getInstance("SHA-512"); //"SHA-512 형식으로 암호화를 위한 인스턴스 생성"
		md.update(loginData.getUser_salt().getBytes()); //해쉬값 업데이트
		md.update(loginData.getUser_pw().getBytes()); //해쉬값 업데이트
		
		String hex = String.format("%064x", new BigInteger(1, md.digest()));
		return hex;
	}
	
}
