package com.hostate.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hostate.api.commonutil.SHA512;
import com.hostate.api.dao.LoginDao;
import com.hostate.api.vo.Tb_User_InfoVO;
import com.hostate.api.vo.TestTableVO;


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
	public Tb_User_InfoVO userChk(Tb_User_InfoVO loginData) throws Exception {

		//유저 아이디 값으로 데이터베이스 상의 해당되는 아이디의 salt값 가져오기
		String userSalt = logindao.getUserSalt(loginData);
	
		if (userSalt == null) {
			System.out.println("계정정보가 존재하지 않습니다.");
			return null;
		}else {
			//DB에서 읽은 솔트값을 로그인 요청 시 넘어온 LoginData객체에 set
			loginData.setUser_salt(userSalt);
			
			//비밀번호 암호화 진행
			String encPw = sha512.encrypt(loginData);
			
			//암호화된 비밀번호 LoginData객체에 set
			loginData.setUser_pw(encPw);
			
			//암호화된 패스워드와 DB애 저장되어있는 패스워드 비교
			Tb_User_InfoVO chkUser = logindao.chkUser(loginData);
			
			if(chkUser == null) {
				System.out.println("계정 정보가 없습니다.");
				return null;
			}else {
				System.out.println("계정 정보가 존재합니다.");
				return chkUser;
			}
		}
	}

}
