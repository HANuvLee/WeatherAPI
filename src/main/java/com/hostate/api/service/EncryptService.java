package com.hostate.api.service;

import com.hostate.api.vo.LoginData;

public interface EncryptService {

	int pwEncrypt(LoginData loginData) throws Exception;


}
