package com.hostate.api.dao;

import java.security.MessageDigest;

public interface EncryptDao {

	int pwEncrypt(String user_id, String cryptPw) throws Exception;

}
