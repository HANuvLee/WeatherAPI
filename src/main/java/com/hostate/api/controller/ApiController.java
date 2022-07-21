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
	@RequestMapping(value = "/api/firsthvilageweather.do", method = RequestMethod.GET)
	public String getVilageFcst(HttpSession session, Tb_weather_search_scope_info searchInfo) throws Exception {
		System.out.println("최초 접속 단기예보호출");	
		
		searchInfo.setUser_name((String)session.getAttribute("user_id"));
		searchInfo.setUser_name((String)session.getAttribute("user_name"));

		
		Date today = new Date(); // 메인페이지 접속 시간
		Locale currentLocale = new Locale("KOREAN", "KOREA"); // 나라
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm", currentLocale);
		StringBuilder time = new StringBuilder(formatter.format(today)); //요청한 시간의 시와 분을 구한다.
		
		System.out.println("time -->" + time);
		StringBuilder startDate = new StringBuilder(time); //yyyymmddhhmm 형태
		System.out.println(startDate);
		
		//메인페이지 접속 시 요청 파리미터 형태를 맞추기 위한포멧 메서드 실행, mm단위는 요청 불가
		apiDateFormat.baseTimeFormat(startDate);

		String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" // https입력 시 Java의 신뢰하는인증서
																								// 목록(keystore)에 사용하고자
																								// 하는 인증기관이 등록되어 있지 않아
																								// 접근이 차단되는현상발생.

				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D" //인증키
				+ "&pageNo=1" //페이지번호
				+ "&numOfRows=870" //결과 수 , default : 24시간 3시간 단위로 설정
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
	
	@RequestMapping(value = "/api/searchShortweather.do", method = RequestMethod.GET)
	public String searchvilageweather(HttpSession session, Tb_weather_search_scope_info searchInfo) throws Exception {
		System.out.println("==================================================조회단기예보호출=====================================================");	
		System.out.println("조회단기예보호출 start date ::>>" + searchInfo.getStart_date());
		System.out.println("조회단기예보호출 end date ::>>" + searchInfo.getEnd_date());
		
		searchInfo.setUser_id((String)session.getAttribute("user_id"));
		searchInfo.setUser_name((String)session.getAttribute("user_name"));
		
		System.out.println("조회단기예보호출 ID => " + (String)session.getAttribute("user_id"));
		
		int chk = logService.searchWeatherLogInsert(searchInfo); //조회기록저장
		
		if(chk == 1) {
			Date today = new Date(); // 메인페이지 접속 시간
			Locale currentLocale = new Locale("KOREAN", "KOREA"); // 나라
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm", currentLocale);
			StringBuilder time = new StringBuilder(formatter.format(today)); //요청한 시간의 시와 분을 구한다.
			
			System.out.println("time -->" + time);
			StringBuilder startDate = new StringBuilder(time); //yyyymmddhhmm 형태
			System.out.println(startDate);
			
			//메인페이지 접속 시 요청 파리미터 형태를 맞추기 위한포멧 메서드 실행, mm단위는 요청 불가
			apiDateFormat.baseTimeFormat(startDate);
	
			String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" // https입력 시 Java의 신뢰하는인증서
																									// 목록(keystore)에 사용하고자
																									// 하는 인증기관이 등록되어 있지 않아
																									// 접근이 차단되는현상발생.
	
					+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D" //인증키
					+ "&pageNo=1" //페이지번호
					+ "&numOfRows=870" //결과 수 , default : 3일로 설정 (금일, 내일, 모레)
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
		}else {
			 return "단기예보조회 호출 실패!!";
		}

	}
	
	// 중기기온예보
	@RequestMapping(value = "/api/searchmidtaweather.do", method = RequestMethod.GET)
	public String restApiSearchMidTaWeather(HttpSession session, Tb_weather_search_scope_info searchInfo) throws Exception {
		System.out.println("중기기온예보");
	
		searchInfo.setUser_name((String)session.getAttribute("user_id"));
		searchInfo.setUser_name((String)session.getAttribute("user_name"));
		
		System.out.println("중기기온예보 ID => " + (String)session.getAttribute("user_id"));
		
		int chk = logService.searchWeatherLogInsert(searchInfo); //조회기록저장

		if(chk == 1) {
		
		Date today = new Date(); // 메인페이지 접속 시간
		Locale currentLocale = new Locale("KOREAN", "KOREA"); // 나라
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm", currentLocale);
		StringBuilder baseTime = new StringBuilder(formatter.format(today)); //요청한 시간의 시와 분을 구한다.
		
		System.out.println(baseTime);
		StringBuilder tmfc = new StringBuilder(baseTime);
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
		}else {
			 return "중기기온예보 호출 실패!!";
		}
	}
	
		// 중기육상예보
		@RequestMapping(value = "/api/searchmidlandweather.do", method = RequestMethod.GET)
		public String restApiSearchMidLandWeather(HttpSession session, Tb_weather_search_scope_info searchInfo) throws Exception {
			System.out.println("중기육상예보");
			
			searchInfo.setUser_name((String)session.getAttribute("user_id"));
			searchInfo.setUser_name((String)session.getAttribute("user_name"));
			
			System.out.println("중기육상예보 ID => " + (String)session.getAttribute("user_id"));
			
			int chk = logService.searchWeatherLogInsert(searchInfo); 
			
			if(chk == 1) {
			
			Date today = new Date(); // 메인페이지 접속 시간
			Locale currentLocale = new Locale("KOREAN", "KOREA"); // 나라
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm", currentLocale); //
			StringBuilder baseTime = new StringBuilder(formatter.format(today));

			System.out.println(baseTime);
			StringBuilder tmfc = new StringBuilder(baseTime);
			apiDateFormat.tmFcDateFormat(tmfc); // api 발표시각 파리미터 형태를 맞추기 위한 포멧 (0600 or 1800)
			String url = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst"
				+"?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D"
				+"&pageNo=1"
				+"&numOfRows=10"
				+"&dataType=JSON"
				+"&regId=11B10101"
				+"&tmFc=" + tmfc;

			HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8", "get", ""); //api요청주소, 인코딩방식, 요청방식
			System.out.println("# RESULT : " + resultMap);
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("result", resultMap);

			return jsonObj.toString();
			}else {
				return "중기육상예보 호출 실패";
			}
		}
	
	//json 데이터 get함수
	public HashMap<String, Object> getDataFromJson(String url, String encoding, String type, String jsonStr)throws Exception {
		boolean isPost = false;

		if ("post".equals(type)) { //post방식이면
			isPost = true;
		} else { //get방식이면
			url = "".equals(jsonStr) ? url : url + "?request=" + jsonStr; //jsonStr값이 존재한다면 요청문자열에 request= +jsonStr+ 삽입
			System.out.println("getDataFromJson url ==>" + url);
		}

		return getStringFromURL(url, encoding, isPost, jsonStr, "application/json");
	}

	public HashMap<String, Object> getStringFromURL(String url, String encoding, boolean isPost, String parameter,String contentType) throws Exception {
		
		
		URL apiURL = new URL(url); //URL클래스 생성 파라미터 url이 지정하는 자원에 대한 URL객체를 생성 후 apiURL에 저장

		HttpURLConnection conn = null; //직접객체생성 불가(생성자가 protected로 선언),  URLConnection을 구현한 클래스 , java.net 클래스에서 제공하는 URL 요청을 위한 클래스
		BufferedReader br = null;
		BufferedWriter bw = null;

		HashMap<String, Object> resultMap = new HashMap<String, Object>(); //요청 결과값을 리턴할 맵 객체 생성

		try {
			/*
			 * URLConnection 클래스와 마찬가지로 생성자가 protected로 선언되어 직접 HttpURLConnection 객체를 생성할 수 없다.  
			 * 하지만 http URL을 사용하는 URL객체의 openConnection()메서드가 리턴하는 URLConnection객체(리턴값)는 HttpURLConnection의 인스턴스가 될 수 있다.
			 * URL u = new URL("http://www.naver.com"); 
			 * HttpURLConnection http = (HttpURLConnection) u.openConnection();
			 * 
			 */
			conn = (HttpURLConnection) apiURL.openConnection(); //리턴된 URLConnection을 HttpURLConnection으로 캐스팅하여 사용.
			conn.setConnectTimeout(5000); // 타임아웃 설정  -  TimeOut 시간 (서버 접속시 연결 소요 시간) 
			conn.setReadTimeout(5000);    // 리드타임아웃 설정  - 데이터 교환 시간 (주고받을 때) 
			conn.setDoOutput(true);  //setDoOutput(true)POST및 PUT요청에 사용됩니다 . 그렇다면 요청 false을 사용하기위한 것입니다 GET.

			if (isPost) { // 만약 post라면
				conn.setRequestMethod("POST"); //요청방식 선택
				conn.setRequestProperty("Content-Type", contentType); //타입설정(text/html) 형식으로 전송(Request Body 전달 시 application/json로 서버에 전달.)
				conn.setRequestProperty("Accept", "*/*"); //서버 Response Data를 JSON 형식의 타입으로 요청. Accept 헤더는 클라이언트에서 서버로 요청 시 요청메시지에 담기는 헤더. 
														  //Accept헤더는 자신에게 이러한 데이터 타입만 허용하겠다는 뜻입니다. 
														  //브라우저가 요청 메시지의 Accept헤더 값을 application/json으로 설정 시 
														  //웹서버에게 나는 json 데이터만 처리가 기능하니 json데이터 형식으로 응답
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
