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
	
	//날짜 데이터변환
	Date today = new Date(); // 메인페이지 접속 시간
	Locale currentLocale = new Locale("KOREAN", "KOREA"); // 나라
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm", currentLocale);
	// 요청한 시간의 시와 분을 구한다.(단기예보조회 시 사용)
	StringBuilder startDate = new StringBuilder(formatter.format(today));
	// 요청한 시간의 시와 분을 구한다.(중기예보조회 시 사용)
	StringBuilder baseTime = new StringBuilder(formatter.format(today));
	// 중기조회 시 발표시각 세팅
	StringBuilder tmfc = new StringBuilder(baseTime);

}
