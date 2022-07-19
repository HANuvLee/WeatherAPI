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
		
		//���� ���̵� ������ �����ͺ��̽� ���� �ش�Ǵ� ���̵��� salt�� ��������
		String userSalt = logindao.getUserSalt(loginData);
		
		if (userSalt.equals("") || userSalt == null) {
			System.out.println("���� ������ �����ϴ�.");
			return result;
		}else {
			//������ ��Ʈ�� �α��� ��û �� �Ѿ�� LoginData��ü�� set
			loginData.setUser_salt(userSalt);
			//��й�ȣ ��ȣȭ ����
			String encPw = sha512.encrypt(loginData);
			loginData.setUser_pw(encPw);
			//��ȣȭ�� �н������ DB���� �н����� ��
			Tb_User_InfoVO chkUser = logindao.chkUser(loginData);
			System.out.println(chkUser.getUser_id());
			
			return chkUser;
		}
	}

}
