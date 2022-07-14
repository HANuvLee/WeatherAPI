package com.hostate.api.dao;

import java.security.MessageDigest;

public interface EncryptDao {

	String pwEncrypt(String user_id, String cryptPw) throws Exception;

}
