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
		System.out.println("TmfcFormat" + tmFc);
		int hour = Integer.parseInt(tmFc.substring(9,10)); // 현재 시간
		int day =  Integer.parseInt(tmFc.substring(0,8)) - 1; // 다음날 00시부터 05시59분까지는 전날 18시 업데이트 정보를 불러와야함.

		if(tmFc.substring(8,9).equals("0")) { //현재시간이 오전 00시 ~ 09시59분	
			if(hour>=0 && hour<=5) { //오전 00시 ~ 05시59분까지
				tmFc.replace(8, 12, "1800");
				tmFc.replace(0, 8, String.valueOf(day));
			}else { 			   //오전 06시 ~ 09시59분까지
				tmFc.replace(8, 12, "0600"); 
			}
		}else {								  //현재 시간이 오전 10시 ~ 23시 59분
			hour = Integer.parseInt(tmFc.substring(8,10)); //현재시간	
			if(hour >= 10 && hour <= 17) { //오전 10시 ~ 오후 17시59분
				tmFc.replace(8, 12, "0600"); 
			}else {						 //오후 18시 ~ 오후 23시59분
				tmFc.replace(8, 12, "1800"); 
			}
		}
		
		return tmFc;
	}
	

	private StringBuilder BaseTimeFormat(StringBuilder startDate) {
		System.out.println("BaseTimeFormat" + startDate);
		switch (startDate.substring(8,10)) {	
			case "03": case "04": case "05":
				startDate.replace(8, 12, "0200");
				break;
			case "06": case "07": case "08":
				startDate.replace(8, 12, "0500");
				break;
			case "09": case "10": case "11":
				startDate.replace(8, 12, "0800");
				break;
			case "12": case "13": case "14":
				startDate.replace(8, 12, "1100");
				break;
			case "15": case "16": case "17":
				startDate.replace(8, 12, "1400");
				break;
			case "18": case "19": case "20":
				startDate.replace(8, 12, "1700");
				break;
			case "21": case "22": case "23":
				startDate.replace(8, 12, "2000");
				break;
			case "00": case "01": case "02":
				int year = Integer.parseInt(startDate.substring(4, 8)) -1; // 금일 시간 -1
				startDate.replace(5, 9,String.valueOf(year));
				startDate.replace(8, 12, "2300");
				break;
			default:
				break;
			}
		System.out.println("AfterBaseTimeFormat" + startDate);
		return startDate;
	}
}
