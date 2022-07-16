package com.hostate.api.commonutil;

import org.springframework.stereotype.Component;

@Component
public class TmfcFormat {
	
	public StringBuilder tmFcDateFormat(StringBuilder tmFc) {
		
		return tmFcFormat(tmFc);
	}
	
	private StringBuilder tmFcFormat(StringBuilder tmFc) {
		
		if(tmFc.substring(8,9).equals("0")) { //현재시간이 오전 00시 ~ 09시59분
			int num = Integer.parseInt(tmFc.substring(9,10)); //현재 시간
			if(num>=0 && num<=5) { //오전 00시 ~ 05시59분까지
				tmFc.replace(8, 12, "1800"); 
			}else { 			   //오전 06시 ~ 09시59분까지
				tmFc.replace(8, 12, "0600"); 
			}
		}else {								  //현재 시간이 오전 10시 ~ 23시 59분
			int num = Integer.parseInt(tmFc.substring(8,10)); //현재시간
			if(num >= 10 && num <= 17) { //오전 10시 ~ 오후 17시59분
				tmFc.replace(8, 12, "0600"); 
			}else {						 //오후 18시 ~ 오후 23시59분
				tmFc.replace(8, 12, "1800"); 
			}
		}	
		return tmFc;
	}
}
