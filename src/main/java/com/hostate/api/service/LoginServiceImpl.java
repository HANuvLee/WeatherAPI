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

		//���� ���̵� ������ �����ͺ��̽� ���� �ش�Ǵ� ���̵��� salt�� ��������
		String userSalt = logindao.getUserSalt(loginData);
	
		if (userSalt == null) {
			System.out.println("���������� �������� �ʽ��ϴ�.");
			return null;
		}else {
			//DB���� ���� ��Ʈ���� �α��� ��û �� �Ѿ�� LoginData��ü�� set
			loginData.setUser_salt(userSalt);
			
			//��й�ȣ ��ȣȭ ����
			String encPw = sha512.encrypt(loginData);
			
			//��ȣȭ�� ��й�ȣ LoginData��ü�� set
			loginData.setUser_pw(encPw);
			
			//��ȣȭ�� �н������ DB�� ����Ǿ��ִ� �н����� ��
			Tb_User_InfoVO chkUser = logindao.chkUser(loginData);
			
			if(chkUser == null) {
				System.out.println("���� ������ �����ϴ�.");
				return null;
			}else {
				System.out.println("���� ������ �����մϴ�.");
				return chkUser;
			}
		}
	}

}
