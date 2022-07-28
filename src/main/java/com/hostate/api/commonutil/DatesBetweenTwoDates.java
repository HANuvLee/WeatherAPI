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
	
	//�߱⿹�� ȣ�� �Լ�
	public List<LocalDate> getBetweenDate(String startDate, String endDate) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		
		LocalDate start = LocalDate.parse(startDate, formatter);
		LocalDate end = LocalDate.parse(endDate, formatter);
		
		List<LocalDate> betweenDate = getDatesBetweenTwoDates(start, end);
		
		return betweenDate;
	}
	
	//��ü���� ȣ�� �Լ�
	public List<LocalDate> getBetweenDate(StringBuilder startDate, String endDate) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
		
		LocalDate start = LocalDate.parse(startDate, formatter);
		LocalDate end = LocalDate.parse(endDate, formatter);
		
		List<LocalDate> betweenDate = getDatesBetweenTwoDates(start, end);
		
		return betweenDate;
	}
	
	//��ȸ���۳�¥�� ����¥�� �����Ͽ� �� ��¥ ������ ��¥������ ���ؿ´�.
	private List<LocalDate> getDatesBetweenTwoDates(LocalDate start, LocalDate end) {
		int numOfDaysBetween = (int) ChronoUnit.DAYS.between(start, end);
		
		return IntStream.iterate(0, i -> i + 1)
        	.limit(numOfDaysBetween+1)
        	.mapToObj(i -> start.plusDays(i))
		.collect(Collectors.toList());
	}
	
	//���Ͽ��� ��ȸ���۳�¥�� ���̸� ���ϴ� �Լ� 
	public int getDiffDays(String startDate, String endDate) throws ParseException {
		
		Date now = new Date();
		Date start = new SimpleDateFormat("yyyyMMdd").parse(startDate);
		Date end = new SimpleDateFormat("yyyyMMdd").parse(endDate);

		long diffSec = (start.getTime() - now.getTime()) / 1000; //�� ����
		long diffDays = (diffSec / (24*60*60)); //���ڼ� ����
		
		//primitive long Ÿ��, int�� cast
		int diffDaysInt = (int) diffDays;
			
		return diffDaysInt;
	}
	
}
