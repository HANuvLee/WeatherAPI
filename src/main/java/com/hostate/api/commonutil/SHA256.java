package com.hostate.api.commonutil;

import java.security.MessageDigest;

import org.springframework.stereotype.Component;

@Component
public class SHA256 {
	
	public String encrypt(String text) throws Exception {
	
		MessageDigest md = MessageDigest.getInstance("SHA-256"); //"SHA-256 �������� ��ȣȭ�� ���� �ν��Ͻ� ����"
		md.update(text.getBytes()); //�ؽ��� ������Ʈ
		
		return bytesToHex(md.digest()); //md.digest() �޼���� �����Ǵ� �ؽ� ���� ByteArray��, ������ ���Ǹ� ���� Hex String���� ��ȯ���ִ� bytesToHex() �޼��� ���
	}
	
	private String bytesToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for(byte b : bytes) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}

}
