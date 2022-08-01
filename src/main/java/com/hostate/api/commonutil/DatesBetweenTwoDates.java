package com.hostate.api.commonutil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

@Component
public class DatesBetweenTwoDates {
	
	//중기예보 호출 함수
	public List<LocalDate> getBetweenDate(String startDate, String endDate) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		
		LocalDate start = LocalDate.parse(startDate, formatter);
		LocalDate end = LocalDate.parse(endDate, formatter);
		
		List<LocalDate> betweenDate = getDatesBetweenTwoDates(start, end);
		
		return betweenDate;
	}
	
	//전체예보 호출 함수
	public List<LocalDate> getBetweenDate(StringBuilder startDate, String endDate) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
		
		LocalDate start = LocalDate.parse(startDate, formatter);
		LocalDate end = LocalDate.parse(endDate, formatter);
		
		List<LocalDate> betweenDate = getDatesBetweenTwoDates(start, end);
		
		return betweenDate;
	}
	
	//금일부터 조회시작날짜의 차이를 구하는 함수 
	public int getDiffDays(String startDate, String endDate) throws ParseException {
		//오늘날짜
		Date now = new Date();
		//조회시작날짜 형태를 포맷
		Date start = new SimpleDateFormat("yyyyMMdd").parse(startDate);

		long diffSec = (start.getTime() - now.getTime()) / 1000; //초 차이
		long diffDays = (diffSec / (24*60*60)); //일자수 차이
		
		//primitive long 타입, int로 cast
		int diffDaysInt = (int) diffDays;
			
		return diffDaysInt;
	}
	
	//조회시작날짜와 끝날짜를 포함하여 두 날짜 사이의 날짜값을 구해 리스트로 반환해준다.
	private List<LocalDate> getDatesBetweenTwoDates(LocalDate start, LocalDate end) {
		//start날짜와 end날짜 사이의 차이를 구한다.
		int numOfDaysBetween = (int) ChronoUnit.DAYS.between(start, end);
		//스트림 생성, 0부터 계산하여 i+1에 의한 결과를 다시 0값(seed)에 위치시켜 반복한다. 
		return IntStream.iterate(0, i -> i + 1) //0, 1, 2...
		    //스트림 자르기, stream요소를 numOfDaysBetween+1개로 제한 즉, 생성할 날짜한도를 정해준다.
			.limit(numOfDaysBetween+1)
			//int값을 plusDays를 사용하여 LocaleDate로 변환, 시작날에 하루 더하기, i를 조회시작날짜의 +1값으로 변환
        	.mapToObj(i -> start.plusDays(i))
		.collect(Collectors.toList()); //스트림 요소를 수집하는 최종 연산, 변환값을 List에 넣어주기
	}
}
