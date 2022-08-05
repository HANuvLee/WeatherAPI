package com.hostate.api.commonutil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetJsonData {
	
	@Autowired
	ApiDateFormat apiDateFormat;
	
	//��¥ �����ͺ�ȯ
	Date today = new Date(); // ���������� ���� �ð�
	Locale currentLocale = new Locale("KOREAN", "KOREA"); // ����
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm", currentLocale);
	// ��û�� �ð��� �ÿ� ���� ���Ѵ�.(�ܱ⿹����ȸ �� ���)
	StringBuilder startDate = new StringBuilder(formatter.format(today));
	// ��û�� �ð��� �ÿ� ���� ���Ѵ�.(�߱⿹����ȸ �� ���)
	StringBuilder baseTime = new StringBuilder(formatter.format(today));
	// �߱���ȸ �� ��ǥ�ð� ����
	StringBuilder tmfc = new StringBuilder(baseTime);

}
