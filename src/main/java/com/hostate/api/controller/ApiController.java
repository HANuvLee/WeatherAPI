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
import com.hostate.api.vo.Tb_weather_search_scope_info;

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
	@RequestMapping(value = "/api/searchShortweather.do", method = RequestMethod.GET)
	public String getVilageFcst(HttpSession session, Tb_weather_search_scope_info searchInfo) throws Exception {
		System.out.println("단기예보호출");	

		if("".equals(searchInfo.getStart_date()) || searchInfo.getStart_date() == null || "".equals(searchInfo.getEnd_date()) || searchInfo.getStart_date() == null) {
			JSONObject jsonObj = logService.getShorWeather(searchInfo);
			return jsonObj.toString();
		}else{
			System.out.println("searchShortweather 2");
			searchInfo.setUser_id((String)session.getAttribute("user_id"));
			searchInfo.setUser_name((String)session.getAttribute("user_name"));

			logService.searchWeatherLogInsert(searchInfo); //조회기록저장

			JSONObject jsonObj = logService.getShorWeather(searchInfo);

			return jsonObj.toString();
		}
	}
}
	/*
	 * @RequestMapping(value = "/api/searchShortweather.do", method =
	 * RequestMethod.GET) public String searchvilageweather(HttpSession session,
	 * Tb_weather_search_scope_info searchInfo) throws Exception {
	 * System.out.println("조회단기예보호출");
	 * 
	 * 
	 * 
	 * searchInfo.setUser_id((String)session.getAttribute("user_id"));
	 * searchInfo.setUser_name((String)session.getAttribute("user_name"));
	 * 
	 * int chk = logService.searchWeatherLogInsert(searchInfo); //조회기록저장
	 * 
	 * if(chk == 1) { Date today = new Date(); // 메인페이지 접속 시간 Locale currentLocale =
	 * new Locale("KOREAN", "KOREA"); // 나라 SimpleDateFormat formatter = new
	 * SimpleDateFormat("yyyyMMddHHmm", currentLocale); //시간형태를 포멧 StringBuilder
	 * time = new StringBuilder(formatter.format(today)); //요청한 시간의 시와 분을 구한다.
	 * 
	 * System.out.println("time -->" + time); StringBuilder startDate = new
	 * StringBuilder(time); //yyyymmddhhmm 형태
	 * 
	 * //메인페이지 접속 시 요청 파리미터 형태를 맞추기 위한포멧 메서드 실행, mm단위는 요청 불가
	 * apiDateFormat.baseTimeFormat(startDate);
	 * 
	 * String url =
	 * "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" //
	 * https입력 시 Java의 신뢰하는인증서 // 목록(keystore)에 사용하고자 // 하는 인증기관이 등록되어 있지 않아 // 접근이
	 * 차단되는현상발생.
	 * 
	 * +
	 * "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D"
	 * //인증키 + "&pageNo=1" //페이지번호 + "&numOfRows=834" //결과 수 , default : 3일로 설정 (금일,
	 * 내일, 모레) + "&dataType=JSON" // XML, JSON + "&base_date=" +
	 * startDate.substring(0, 8) // 발표일자 + "&base_time=0200" // 발표시각 + "&nx=60" +
	 * "&ny=127";
	 * 
	 * HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8", "get", "");
	 * System.out.println("# RESULT : " + resultMap); JSONObject jsonObj = new
	 * JSONObject(); jsonObj.put("result", resultMap); return jsonObj.toString();
	 * }else { return "단기예보조회 호출 실패!!"; }
	 * 
	 * }
	 */
	

	/*
	 * // 중기기온예보
	 * 
	 * @RequestMapping(value = "/api/searchmidtaweather.do", method =
	 * RequestMethod.GET) public String restApiSearchMidTaWeather(HttpSession
	 * session, Tb_weather_search_scope_info searchInfo) throws Exception {
	 * System.out.println("중기기온예보");
	 * 
	 * searchInfo.setUser_id((String)session.getAttribute("user_id"));
	 * searchInfo.setUser_name((String)session.getAttribute("user_name"));
	 * 
	 * System.out.println("중기기온예보 ID => " +
	 * (String)session.getAttribute("user_id"));
	 * 
	 * int chk = logService.searchWeatherLogInsert(searchInfo); //조회기록저장
	 * 
	 * if(chk == 1) {
	 * 
	 * Date today = new Date(); // 메인페이지 접속 시간 Locale currentLocale = new
	 * Locale("KOREAN", "KOREA"); // 나라 SimpleDateFormat formatter = new
	 * SimpleDateFormat("yyyyMMddHHmm", currentLocale); StringBuilder baseTime = new
	 * StringBuilder(formatter.format(today)); //요청한 시간의 시와 분을 구한다.
	 * 
	 * System.out.println(baseTime); StringBuilder tmfc = new
	 * StringBuilder(baseTime); apiDateFormat.tmFcDateFormat(tmfc); // api 발표시각 파리미터
	 * 형태를 맞추기 위한 포멧 (0600 or 1800) String url =
	 * "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa" // https입력 시
	 * Java의 신뢰하는 인증서 // 목록(keystore)에 사용하고자 하는 인증기관이 // 등록되어 있지 않아 접근이 차단되는 현상.
	 * 
	 * +
	 * "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D"
	 * // 일반인증키 + "&pageNo=1" // 페이지번호 + "&numOfRows=10" // 페이지 rows +
	 * "&dataType=JSON" // JSON, XNL + "&regId=11B10101" // 예보구역코드 기본값 서울 + "&tmFc="
	 * + tmfc; // 발표시각
	 * 
	 * HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8", "get", "");
	 * System.out.println("# RESULT : " + resultMap); JSONObject jsonObj = new
	 * JSONObject(); jsonObj.put("result", resultMap);
	 * 
	 * return jsonObj.toString(); }else { return "중기기온예보 호출 실패!"; } }
	 * 
	 * 
	 * // 중기육상예보
	 * 
	 * @RequestMapping(value = "/api/searchmidlandweather.do", method =
	 * RequestMethod.GET) public String restApiSearchMidLandWeather(HttpSession
	 * session, Tb_weather_search_scope_info searchInfo) throws Exception {
	 * System.out.println("중기육상예보");
	 * 
	 * searchInfo.setUser_id((String)session.getAttribute("user_id"));
	 * searchInfo.setUser_name((String)session.getAttribute("user_name"));
	 * 
	 * System.out.println("중기육상예보 ID => " +
	 * (String)session.getAttribute("user_id"));
	 * 
	 * int chk = logService.searchWeatherLogInsert(searchInfo);
	 * 
	 * if(chk == 1) {
	 * 
	 * Date today = new Date(); // 메인페이지 접속 시간 Locale currentLocale = new
	 * Locale("KOREAN", "KOREA"); // 나라 SimpleDateFormat formatter = new
	 * SimpleDateFormat("yyyyMMddHHmm", currentLocale); // StringBuilder baseTime =
	 * new StringBuilder(formatter.format(today));
	 * 
	 * System.out.println(baseTime); StringBuilder tmfc = new
	 * StringBuilder(baseTime); apiDateFormat.tmFcDateFormat(tmfc); // api 발표시각 파리미터
	 * 형태를 맞추기 위한 포멧 (0600 or 1800) String url =
	 * "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst"
	 * +"?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D"
	 * +"&pageNo=1" +"&numOfRows=10" +"&dataType=JSON" +"&regId=11B00000" +"&tmFc="
	 * + tmfc;
	 * 
	 * HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8", "get", "");
	 * //api요청주소, 인코딩방식, 요청방식 System.out.println("# RESULT : " + resultMap);
	 * JSONObject jsonObj = new JSONObject(); jsonObj.put("result", resultMap);
	 * 
	 * return jsonObj.toString(); }else { return "중기육사예보 호출 실패"; } }
	 * 
	 */

