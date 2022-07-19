package com.hostate.api.service;

import java.util.List;

import com.hostate.api.vo.LoginData;
import com.hostate.api.vo.Tb_User_InfoVO;
import com.hostate.api.vo.TestTableVO;

public interface LoginService {

	List<TestTableVO> selectTest() throws Exception;

	Tb_User_InfoVO userChk(LoginData loginData) throws Exception;

}
