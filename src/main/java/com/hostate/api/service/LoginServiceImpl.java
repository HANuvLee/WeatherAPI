package com.hostate.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hostate.api.dao.LoginDao;
import com.hostate.api.vo.Board;

@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	LoginDao logindao;
	
	@Override
	public List<Board> selectTest() throws Exception {
		// TODO Auto-generated method stub
		return logindao.selectTest();
	}

}
