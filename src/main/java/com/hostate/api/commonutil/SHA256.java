package com.hostate.api.commonutil;

import java.security.MessageDigest;

import org.springframework.stereotype.Component;

@Component
public class SHA256 {
	
	public String encrypt(String text) throws Exception {
	
		MessageDigest md = MessageDigest.getInstance("SHA-256"); //"SHA-256 형식으로 암호화를 위한 인스턴스 생성"
		md.update(text.getBytes()); //해쉬값 업데이트
		
		return bytesToHex(md.digest()); //md.digest() 메서드로 생성되는 해시 값은 ByteArray형, 관리의 편의를 위해 Hex String으로 변환해주는 bytesToHex() 메서드 사용
	}
	
	private String bytesToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for(byte b : bytes) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}

}
