package com.hostate.api.dao;

import java.util.List;

import com.hostate.api.vo.TestTableVO;

public interface LoginDao {

	List<TestTableVO> selectTest() throws Exception;

}
