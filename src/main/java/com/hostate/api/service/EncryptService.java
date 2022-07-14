package com.hostate.api.service;

public interface EncryptService {

	int pwEncrypt(String user_id, String user_pw) throws Exception;

}
