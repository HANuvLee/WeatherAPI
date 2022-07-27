package com.hostate.api.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hostate.api.commonutil.ApiDateFormat;
import com.hostate.api.commonutil.ApiJsonFormat;
import com.hostate.api.commonutil.DatesBetweenTwoDates;
import com.hostate.api.dao.LogDao;
import com.hostate.api.vo.Tb_weather_search_scope_info;

@Service
public class LogServiceImpl implements LogService {
	@Autowired
	LogDao logdao;

	@Autowired
	ApiDateFormat apiDateFormat;
	
	@Autowired
	ApiJsonFormat apiJsonFormat;
	
	@Autowired
	DatesBetweenTwoDates datesBetween;

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
	//(단기+중기예보조회 시 사용), 오늘날짜로부터 2틀후인 날을 가져온다.
	String next = formatter.format((new Date(today.getTime() + ( (60 * 60 * 24 * 1000) * 2 ))));

	@Override
	public int searchWeatherLogInsert(Tb_weather_search_scope_info searchInfo) throws Exception {

		return logdao.searchWeatherLogInsert(searchInfo);
	}
	
	//최초접속 API
	@Override
	public JSONObject getFirstApi(Tb_weather_search_scope_info searchInfo) throws Exception {
		//api 데이터를 담을 HashMap 생성
		HashMap<String, Object> resultData = new HashMap<String, Object>();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", currentLocale);
		String startDate = new String(formatter.format(today)); //요청한 시간의 시와 분을 구한다.
		
		//최초접속이므로 조회시작날짜와 끝날짜를 오늘로 맞춰준다.
		searchInfo.setStart_date(startDate);
		searchInfo.setEnd_date(startDate);
		
		String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" // https입력 시 Java의 신뢰하는인증서
																							  // 목록(keystore)에 사용하고자
																							  // 하는 인증기관이 등록되어 있지 않아
																							  // 접근이 차단되는현상발생.
				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D" // 인증키
				+ "&pageNo=1" //페이지번호
				+ "&numOfRows=254" //결과 수 , default : 하루치 데이터 단위로 설정
				+ "&dataType=JSON" // XML, JSON
				+ "&base_date=" + startDate.substring(0,8) // 발표일자
				+ "&base_time=0200" //발표시각 0200인 이유는 tmx와 tmn (오늘 최고최저기온값을 가져온다.)
				+ "&nx=60" + "&ny=127";
			
			resultData = getDataFromJson(url, "UTF-8", "get", "");
			System.out.println("# RESULT : " + resultData);
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("result", resultData);
			
			jsonObj = apiJsonFormat.shortWeather(jsonObj, searchInfo);
			
			return jsonObj;
		}

	
	//단기예보조회 서비스
	@Override
	public JSONObject getShortWeather(Tb_weather_search_scope_info searchInfo) throws Exception {
		
		//api 데이터를 담을 HashMap 생성
		HashMap<String, Object> resultData = new HashMap<String, Object>();
		
		System.out.println("LogServiceImpl getShorWeather START");

		String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" //https입력 시 Java의 신뢰하는인증서
																								// 목록(keystore)에 사용하고자
																								// 하는 인증기관이 등록되어 있지 않아
																								// 접근이 차단되는현상발생.
				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D" // 인증키
				+ "&pageNo=1" //페이지번호
				+ "&numOfRows=1000" //결과 수 , default : 하루치 데이터 단위로 설정
				+ "&dataType=JSON" // XML, JSON
				+ "&base_date=" + startDate.substring(0,8) // 발표일자
				+ "&base_time=0200" //발표시각 0200인 이유는 tmx와 tmn (오늘 최고최저기온값을 가져온다.)
				+ "&nx=60" + "&ny=127";

		resultData = getDataFromJson(url, "UTF-8", "get", "");
		System.out.println("# RESULT : " + resultData);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", resultData);
		
		jsonObj = apiJsonFormat.shortWeather(jsonObj, searchInfo);

		return jsonObj;
	}
	
	//중기예보조회 서비스
	@Override
	public JSONObject getMidWeather(Tb_weather_search_scope_info searchInfo) throws Exception {	
		//중기기온예보조회 api 데이터를 담을 HashMap 생성
		HashMap<String, Object> resultData1 = new HashMap<String, Object>();
		//중기육상예보조회api 데이터를 담을 HashMap 생성
		HashMap<String, Object> resultData2 = new HashMap<String, Object>();
		
		apiDateFormat.tmFcDateFormat(tmfc); // api 발표시각 파리미터 형태를 맞추기 위한 포멧 (0600 or 1800)

		//중기기온조회 요청URL
		String url = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa"

				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D"
				// 일반인증키
				+ "&pageNo=1" // 페이지번호
				+ "&numOfRows=10" // 페이지 rows
				+ "&dataType=JSON" // JSON, XNL
				+ "&regId=11B10101" // 예보구역코드 기본값 서울
				+ "&tmFc=" + tmfc; // 발표시각
		
		//중기육상예보조회 요청URL
		String url2 = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst"
				  +"?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D"
				  +"&pageNo=1"
				  +"&numOfRows=10" 
				  +"&dataType=JSON" 
				  +"&regId=11B00000" 
				  +"&tmFc="+ tmfc;

		resultData1 = getDataFromJson(url, "UTF-8", "get", "");
		resultData2 = getDataFromJson(url2, "UTF-8", "get", "");
		
		System.out.println("# RESULT : " + resultData1);
		System.out.println("# RESULT : " + resultData2);
		
		JSONObject jsonObj = new JSONObject();
		JSONObject jsonObj2 = new JSONObject();
		
		jsonObj.put("result", resultData1);
		jsonObj2.put("result", resultData2);
		
		jsonObj = apiJsonFormat.midWeather(jsonObj,jsonObj2,searchInfo);
		
		return jsonObj;
	}
	
	@Override
	public JSONObject getAllWeather(Tb_weather_search_scope_info searchInfo) throws Exception {
		System.out.println("getAllWeather serviceimple start");
	    System.out.println(startDate);
	    System.out.println(next);
	    System.out.println(searchInfo.getStart_date());
	    System.out.println(searchInfo.getEnd_date());
	    
	    List<LocalDate> shortDateScope = datesBetween.getBetweenDate(startDate, next);
	    
	    for(int i=0; i<shortDateScope.size(); i++) {
	    	System.out.println(shortDateScope.get(i).getClass());
	    	System.out.println(shortDateScope.get(i).toString());
	    	System.out.println(shortDateScope.get(i).toString().getClass());
	    	System.out.println(shortDateScope.get(i).toString().replaceAll("[^0-9]",""));
	    	System.out.println(searchInfo.getStart_date().equals(shortDateScope.get(i).toString().replaceAll("[^0-9]","")));
	    }
	  
	    
		return null;
	}
	

	public HashMap<String, Object> getDataFromJson(String url, String encoding, String type, String jsonStr)
			throws Exception {
		boolean isPost = false;

		if ("post".equals(type)) {
			isPost = true;
		} else {
			url = "".equals(jsonStr) ? url : url + "?request=" + jsonStr;
																			
			System.out.println("getDataFromJson url ==>" + url);
		}

		return getStringFromURL(url, encoding, isPost, jsonStr, "application/json");
	}

	public HashMap<String, Object> getStringFromURL(String url, String encoding, boolean isPost, String parameter,
			String contentType) throws Exception {

		URL apiURL = new URL(url);

		HttpURLConnection conn = null; 
										
		BufferedReader br = null;
		BufferedWriter bw = null;

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
		
			conn = (HttpURLConnection) apiURL.openConnection(); 
			conn.setConnectTimeout(5000); 
			conn.setReadTimeout(5000); 
			conn.setDoOutput(true);

			if (isPost) { // 만약 post라면
				conn.setRequestMethod("POST"); // 요청방식 선택
				conn.setRequestProperty("Content-Type", contentType);
				conn.setRequestProperty("Accept", "*/*");
														
			} else {
				conn.setRequestMethod("GET");
			}

			conn.connect();

			if (isPost) {
				bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));

				bw.write(parameter);
				bw.flush();
				bw = null;
			}

			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));

			String line = null;
			StringBuffer result = new StringBuffer();

			while ((line = br.readLine()) != null)
				result.append(line);

			ObjectMapper mapper = new ObjectMapper();

			resultMap = mapper.readValue(result.toString(), HashMap.class);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(url + " interface failed" + e.toString());
		} finally {
			if (conn != null)
				conn.disconnect();
			if (br != null)
				br.close();
			if (bw != null)
				bw.close();
		}

		return resultMap;
	}
}
