package com.hostate.api.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hostate.api.commonutil.ApiDateFormat;
import com.hostate.api.service.LogService;

//@RestController : 기본 하위의 메소드들은 모두 @responsebody를 갖는다
//@RequestBody : 클라이언트 요청 xml/json을 자바 객체로 변환하여 전달 받을 수 있다.
//@ResponseBody : 자바 객체를 xml/json으로 변환시켜 응답객체의 Body에 실어 전송가능하다.

@RestController
public class ApiController {

	@Autowired
	ApiDateFormat apiDateFormat;

	@Autowired
	LogService logService;
	
	/*
	 * @API LIST ~
	 * 
	 * getVilageFcst 단기예보조회 getMidTa 중기기온조회 getMidLandFcst 중기육상예보조회
	 */

	// 단기예보
	@RequestMapping(value = "/api/searchvilageweather.do", method = RequestMethod.GET)
	public String getVilageFcst(HttpSession session, String startdate, String enddate) throws Exception {
		System.out.println("단기예보호출");
		
		String user_id = (String) session.getAttribute("user_id");
		int recordChk = logService.searchWeatherLogInsert(user_id, startdate, enddate); //조회기록저장
		
		Date today = new Date(); // 메인페이지 접속 시간
		Locale currentLocale = new Locale("KOREAN", "KOREA"); // 나라
		SimpleDateFormat formatter = new SimpleDateFormat("HHmm", currentLocale); //
		StringBuilder baseTime = new StringBuilder(formatter.format(today)); //요청한 시간의 시와 분을 구한다.
		
		StringBuilder startDate = new StringBuilder(startdate+baseTime);
		apiDateFormat.baseTimeFormat(startDate); // base_time (발표시각) 파리미터 형태를 맞추기 위한포멧

		String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" // https입력 시 Java의 신뢰하는인증서
																								// 목록(keystore)에 사용하고자
																								// 하는 인증기관이 등록되어 있지 않아
																								// 접근이 차단되는현상발생.

				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D" // 인증키
				+ "&pageNo=1" // 페이지번호
				+ "&numOfRows=834" // 헌 패아자 결과 수 (금일, 내일, 모레)
				+ "&dataType=JSON" // XML, JSON
				+ "&base_date=" + startDate.substring(0, 8) // 발표일자
				+ "&base_time=" + startDate.substring(8) // 발표시각
				+ "&nx=60" 
				+ "&ny=127";

		HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8", "get", "");
		System.out.println("# RESULT : " + resultMap);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", resultMap);

		return jsonObj.toString();

	}

	// 중기기온예보
	@RequestMapping(value = "/api/searchmidtaweather.do", method = RequestMethod.GET)
	public String restApiSearchMidTaWeather(HttpSession session, String startdate, String enddate) throws Exception {
		System.out.println("중기기온예보");
		System.out.println(startdate);
		System.out.println(enddate);

		Date today = new Date(); // 메인페이지 접속 시간
		Locale currentLocale = new Locale("KOREAN", "KOREA"); // 나라
		SimpleDateFormat formatter = new SimpleDateFormat("HHmm", currentLocale); //
		StringBuilder baseTime = new StringBuilder(formatter.format(today));

		StringBuilder tmfc = new StringBuilder(startdate + baseTime);
		apiDateFormat.tmFcDateFormat(tmfc); // api 발표시각 파리미터 형태를 맞추기 위한 포멧 (0600 or 1800)
		String url = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa" // https입력 시 Java의 신뢰하는 인증서
																					// 목록(keystore)에 사용하고자 하는 인증기관이
																					// 등록되어 있지 않아 접근이 차단되는 현상.

				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D" // 일반인증키
				+ "&pageNo=1" // 페이지번호
				+ "&numOfRows=10" // 페이지 rows
				+ "&dataType=JSON" // JSON, XNL
				+ "&regId=11B10101" // 예보구역코드 기본값 서울
				+ "&tmFc=" + tmfc; // 발표시각

		HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8", "get", "");
		System.out.println("# RESULT : " + resultMap);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", resultMap);

		return jsonObj.toString();
	}
	
		// 중기육상예보
		@RequestMapping(value = "/api/searchmidlandweather.do", method = RequestMethod.GET)
		public String restApiSearchMidLandWeather(HttpSession session, String startdate, String enddate) throws Exception {
			System.out.println("중기육상예보");
			System.out.println(startdate);
			System.out.println(enddate);

			Date today = new Date(); // 메인페이지 접속 시간
			Locale currentLocale = new Locale("KOREAN", "KOREA"); // 나라
			SimpleDateFormat formatter = new SimpleDateFormat("HHmm", currentLocale); //
			StringBuilder baseTime = new StringBuilder(formatter.format(today));

			StringBuilder tmfc = new StringBuilder(startdate + baseTime);
			apiDateFormat.tmFcDateFormat(tmfc); // api 발표시각 파리미터 형태를 맞추기 위한 포멧 (0600 or 1800)
			String url = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst"
				+"?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D"
				+"&pageNo=1"
				+"&numOfRows=10"
				+"&dataType=JSON"
				+"&regId=11B10101"
				+"&tmFc=" + tmfc;

			HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8", "get", "");
			System.out.println("# RESULT : " + resultMap);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("result", resultMap);

			return jsonObj.toString();
		}
	

	public HashMap<String, Object> getDataFromJson(String url, String encoding, String type, String jsonStr)throws Exception {
		boolean isPost = false;

		if ("post".equals(type)) { //post방식
			isPost = true;
		} else { //get방식
			url = "".equals(jsonStr) ? url : url + "?request=" + jsonStr;
			System.out.println("getDataFromJson url ==>" + url);
		}

		return getStringFromURL(url, encoding, isPost, jsonStr, "application/json");
	}

	public HashMap<String, Object> getStringFromURL(String url, String encoding, boolean isPost, String parameter,String contentType) throws Exception {

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

			if (isPost) {
				conn.setRequestMethod("POST");
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
