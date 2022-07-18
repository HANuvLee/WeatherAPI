package com.hostate.api.dao;

import java.util.Map;

public interface EncryptDao {
	
	int userLoginChk(Map<String, String> param) throws Exception;

	int userLoginUpdate(Map<String, String> param) throws Exception;

}
