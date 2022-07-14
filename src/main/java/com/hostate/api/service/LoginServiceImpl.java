package com.hostate.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hostate.api.dao.LoginDao;
import com.hostate.api.vo.TestTableVO;

@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	LoginDao logindao;
	
	@Override
	public List<TestTableVO> selectTest() throws Exception {
		// TODO Auto-generated method stub
		return logindao.selectTest();
	}

}
