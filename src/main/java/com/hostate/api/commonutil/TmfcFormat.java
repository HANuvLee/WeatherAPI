package com.hostate.api.commonutil;

import org.springframework.stereotype.Component;

@Component
public class TmfcFormat {
	
	public StringBuilder tmFcDateFormat(StringBuilder tmFc) {
		
		return tmFcFormat(tmFc);
	}
	
	private StringBuilder tmFcFormat(StringBuilder tmFc) {
		
		if(tmFc.substring(8,9).equals("0")) { //����ð��� ���� 00�� ~ 09��59��
			int num = Integer.parseInt(tmFc.substring(9,10)); //���� �ð�
			if(num>=0 && num<=5) { //���� 00�� ~ 05��59�б���
				tmFc.replace(8, 12, "1800"); 
			}else { 			   //���� 06�� ~ 09��59�б���
				tmFc.replace(8, 12, "0600"); 
			}
		}else {								  //���� �ð��� ���� 10�� ~ 23�� 59��
			int num = Integer.parseInt(tmFc.substring(8,10)); //����ð�
			if(num >= 10 && num <= 17) { //���� 10�� ~ ���� 17��59��
				tmFc.replace(8, 12, "0600"); 
			}else {						 //���� 18�� ~ ���� 23��59��
				tmFc.replace(8, 12, "1800"); 
			}
		}	
		return tmFc;
	}
}
