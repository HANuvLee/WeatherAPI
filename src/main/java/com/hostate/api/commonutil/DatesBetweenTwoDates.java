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
	
	//조회시작날짜와 끝날짜를 포함하여 두 날짜 사이의 날짜값까지 구해온다.
	private List<LocalDate> getDatesBetweenTwoDates(LocalDate start, LocalDate end) {
		int numOfDaysBetween = (int) ChronoUnit.DAYS.between(start, end);
		
		return IntStream.iterate(0, i -> i + 1)
        	.limit(numOfDaysBetween+1)
        	.mapToObj(i -> start.plusDays(i))
		.collect(Collectors.toList());
	}
	
	//금일에서 조회시작날짜의 차이를 구하는 함수 
	public int getDiffDays(String startDate, String endDate) throws ParseException {
		
		Date now = new Date();
		Date start = new SimpleDateFormat("yyyyMMdd").parse(startDate);
		Date end = new SimpleDateFormat("yyyyMMdd").parse(endDate);

		long diffSec = (start.getTime() - now.getTime()) / 1000; //초 차이
		long diffDays = (diffSec / (24*60*60)); //일자수 차이
		
		//primitive long 타입, int로 cast
		int diffDaysInt = (int) diffDays;
			
		return diffDaysInt;
	}
	
}
