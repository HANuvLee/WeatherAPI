package com.hostate.api.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hostate.api.commonutil.SHA512;
import com.hostate.api.dao.LoginDao;
import com.hostate.api.vo.LoginData;
import com.hostate.api.vo.Tb_User_InfoVO;
import com.hostate.api.vo.TestTableVO;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	LoginDao logindao;
	@Autowired
	SHA512 sha512;
	
	@Override
	public List<TestTableVO> selectTest() throws Exception {
		// TODO Auto-generated method stub
		return logindao.selectTest();
	}

	@Override
	public Tb_User_InfoVO userChk(LoginData loginData) throws Exception {
		Tb_User_InfoVO result = new Tb_User_InfoVO();
		
		//유저 아이디 값으로 데이터베이스 상의 해당되는 아이디의 salt값 가져오기
		String userSalt = logindao.getUserSalt(loginData);
		
		if (userSalt.equals("") || userSalt == null) {
			System.out.println("계정 정보가 없습니다.");
			return result;
		}else {
			//가져온 솔트를 로그인 요청 시 넘어온 LoginData객체에 set
			loginData.setUser_salt(userSalt);
			//비밀번호 암호화 진행
			String encPw = sha512.encrypt(loginData);
			loginData.setUser_pw(encPw);
			//암호화된 패스워드와 DB상의 패스워드 비교
			Tb_User_InfoVO chkUser = logindao.chkUser(loginData);
			System.out.println(chkUser.getUser_id());
			
			return chkUser;
		}
	}

}
