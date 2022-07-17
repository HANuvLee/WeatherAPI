package com.hostate.api.commonutil;

import org.springframework.stereotype.Component;

@Component
public class ApiDateFormat {	
	
	public StringBuilder tmFcDateFormat(StringBuilder tmFc) {
		
		return TmfcFormat(tmFc);
	}
	
	public StringBuilder  baseTimeFormat(StringBuilder baseTime) {
		
		return BaseTimeFormat(baseTime);
	}

	private StringBuilder TmfcFormat(StringBuilder tmFc) {
		
		int hour = Integer.parseInt(tmFc.substring(9,10)); // ���� �ð�
		int day =  Integer.parseInt(tmFc.substring(0,8)) - 1; // ������ 00�ú��� 05��59�б����� ���� 18�� ������Ʈ ������ �ҷ��;���.

		if(tmFc.substring(8,9).equals("0")) { //����ð��� ���� 00�� ~ 09��59��	
			if(hour>=0 && hour<=5) { //���� 00�� ~ 05��59�б���
				tmFc.replace(8, 12, "1800");
				tmFc.replace(0, 8, String.valueOf(day));
			}else { 			   //���� 06�� ~ 09��59�б���
				tmFc.replace(8, 12, "0600"); 
			}
		}else {								  //���� �ð��� ���� 10�� ~ 23�� 59��
			hour = Integer.parseInt(tmFc.substring(8,10)); //����ð�	
			if(hour >= 10 && hour <= 17) { //���� 10�� ~ ���� 17��59��
				tmFc.replace(8, 12, "0600"); 
			}else {						 //���� 18�� ~ ���� 23��59��
				tmFc.replace(8, 12, "1800"); 
			}
		}	
		return tmFc;
	}
	

	private StringBuilder BaseTimeFormat(StringBuilder baseTime) {
		
		switch (baseTime.substring(8,10)) {	
			case "03": case "04": case "05":
				baseTime.replace(8, 12, "0200");
				break;
			case "06": case "07": case "08":
				baseTime.replace(8, 12, "0500");
				break;
			case "09": case "10": case "11":
				baseTime.replace(8, 12, "0800");
				break;
			case "12": case "13": case "14":
				baseTime.replace(8, 12, "1100");
				break;
			case "15": case "16": case "17":
				baseTime.replace(8, 12, "1400");
				break;
			case "18": case "19": case "20":
				baseTime.replace(8, 12, "1700");
				break;
			case "21": case "22": case "23":
				baseTime.replace(8, 12, "2000");
				break;
			case "00": case "01": case "02":
				int year = Integer.parseInt(baseTime.substring(4, 8)) -1; // ���� �ð�
				baseTime.replace(5, 9,String.valueOf(year));
				baseTime.replace(8, 12, "2300");
				break;
			default:
				break;
			}
		return baseTime;
	}
}
