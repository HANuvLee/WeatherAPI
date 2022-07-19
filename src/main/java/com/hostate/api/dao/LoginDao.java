package com.hostate.api.dao;

import java.util.List;

import com.hostate.api.vo.LoginData;
import com.hostate.api.vo.Tb_User_InfoVO;
import com.hostate.api.vo.TestTableVO;

public interface LoginDao {

	List<TestTableVO> selectTest() throws Exception;

	String getUserSalt(LoginData loginData) throws Exception;

	Tb_User_InfoVO chkUser(LoginData loginData) throws Exception;
	
}
