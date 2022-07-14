package com.hostate.api.service;

import java.util.List;

import com.hostate.api.vo.TestTableVO;

public interface LoginService {

	List<TestTableVO> selectTest() throws Exception;

}
