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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hostate.api.commonutil.ApiDateFormat;
import com.hostate.api.commonutil.ApiJsonFormat;
import com.hostate.api.commonutil.DatesBetweenTwoDates;
import com.hostate.api.commonutil.JsonParsing;
import com.hostate.api.dao.LogDao;
import com.hostate.api.vo.Tb_weather_search_scope_info;
import com.mysql.cj.xdevapi.JsonArray;

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
	
	@Autowired
	JsonParsing jsonParsing;
	
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

	@Override
	public int searchWeatherLogInsert(Tb_weather_search_scope_info searchInfo) throws Exception {

		return logdao.searchWeatherLogInsert(searchInfo);
	}
	
	//최초접속 API
	@Override
	public JSONObject getFirstApi(Tb_weather_search_scope_info searchInfo) throws Exception {
		System.out.println("getFirstApi serviceimple start");
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
		System.out.println("getShortWeather serviceimple start");
		//api데이터를 담을 HashMap 선언
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
		//위의 url정보로 api로부터 단기예보 데이터를 전달받는다.
		resultData = getDataFromJson(url, "UTF-8", "get", "");
		System.out.println("# RESULT : " + resultData);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", resultData);
		
		//전달받은 단기예보 데이터를 가공하는 서비스 호출
		jsonObj = apiJsonFormat.shortWeather(jsonObj, searchInfo);

		return jsonObj;
	}
	
	//중기예보조회 서비스
	@Override
	public JSONObject getMidWeather(Tb_weather_search_scope_info searchInfo) throws Exception {	
		System.out.println("getMidWeather serviceimple start");
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
		//위의 url정보로 api로부터 단기예보 데이터를 전달받는다.
		resultData1 = getDataFromJson(url, "UTF-8", "get", "");
		resultData2 = getDataFromJson(url2, "UTF-8", "get", "");
		
		System.out.println("# RESULT : " + resultData1);
		System.out.println("# RESULT : " + resultData2);
		
		JSONObject jsonObj = new JSONObject();
		JSONObject jsonObj2 = new JSONObject();
		
		jsonObj.put("result", resultData1);
		jsonObj2.put("result", resultData2);
		
		//전달받은 중기예보 데이터를 가공하는 서비스 호출
		jsonObj = apiJsonFormat.midWeather(jsonObj,jsonObj2,searchInfo);
		
		return jsonObj;
	}
	
	@Override
	public JSONObject getAllWeather(Tb_weather_search_scope_info searchInfo) throws Exception {
		System.out.println("getAllWeather serviceimple start");
		
		//api 데이터를 담을 HashMap 생성(단기예보)
		HashMap<String, Object> resultData = new HashMap<String, Object>();
		//api 데이터를 담을 HashMap 생성(중기기온예보)
		HashMap<String, Object> resultData2 = new HashMap<String, Object>();
		//api 데이터를 담을 HashMap 생성(중기육상예보)
		HashMap<String, Object> resultData3 = new HashMap<String, Object>();
		
		//오늘날짜로부터 단기끝날짜
		String sWeatherEnd = formatter.format((new Date(today.getTime() + ( (60 * 60 * 24 * 1000) * 2 ))));
		//오늘날짜로부터 중기시작날짜
		String mWeatherStart = formatter.format((new Date(today.getTime() + ( (60 * 60 * 24 * 1000) * 3))));
		//요청시 받은 시작날짜
		String orgStDate = searchInfo.getStart_date();
	    //단기예보 조회 시 파리미터로 들어갈 시작날짜
		String shortStDate = "";
		//단기예보 조회 시 파리미터로 들어갈 끝날짜
	    String shortEdDate = "";
	    //중기예보 조회 시 파리미터로 들어갈 시작날짜, 형태 포맷
	    String midStDate = mWeatherStart.substring(0,8);
	    //중기예보 조회 시 파람미터로 들어갈 끝날짜
	    String midEdDate = searchInfo.getEnd_date();

	    //금일로부터 단기조회날짜 범위의 날짜들(오늘포함 3일)을 리스트에 담는다 
	    List<LocalDate> shortDateScope = datesBetween.getBetweenDate(startDate, sWeatherEnd);
	  
	    for(int i=0; i<shortDateScope.size(); i++) {
	    	//오늘포함 3일이내의 날짜들이 리스트에 존재하고 요청으로 들어온 조회시작날짜가 리스트중에 있다면
	    	if(orgStDate.equals(shortDateScope.get(i).toString().replaceAll("[^0-9]",""))) {
	    		 //그 값을 단기예보시작날짜변수에 대입
	    		shortStDate = shortDateScope.get(i).toString().replaceAll("[^0-9]","");
	    		break;
	    	}
	    } //단기중기 모두 조회시 리스트의 마지막번째 요소는 단기예보조회 시 끝날자변수에 대입
	      int lastIdx = shortDateScope.size() - 1;
	      shortEdDate = shortDateScope.get(lastIdx).toString().replaceAll("[^0-9]","");
	      
	      //중기예보 발표일자 형태 format
	      apiDateFormat.tmFcDateFormat(tmfc);
	      
	      //단기기온조회 요청url
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
	           
	      //중기기온조회 요청URL
		  String url2 = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa"

					+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D"
					// 일반인증키
					+ "&pageNo=1" // 페이지번호
					+ "&numOfRows=10" // 페이지 rows
					+ "&dataType=JSON" // JSON, XNL
					+ "&regId=11B10101" // 예보구역코드 기본값 서울
					+ "&tmFc=" + tmfc; // 발표시각
			
		  //중기육상예보조회 요청URL
		  String url3 = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst"
				  
					  +"?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D"
					  +"&pageNo=1"
					  +"&numOfRows=10" 
					  +"&dataType=JSON" 
					  +"&regId=11B00000" 
					  +"&tmFc="+ tmfc;
		  
		  //단기예보 api요청
		  resultData = getDataFromJson(url, "UTF-8", "get", "");
		  //중기예보 api요청
		  resultData2 = getDataFromJson(url2, "UTF-8", "get", "");
		  resultData3 = getDataFromJson(url3, "UTF-8", "get", "");
	
		  System.out.println("# RESULT : " + resultData);
		  System.out.println("# RESULT : " + resultData2);
		  System.out.println("# RESULT : " + resultData3);
		  
		  //응답받은 api json을 넣을 json객체 인스턴스
		  JSONObject jsonObj = new JSONObject();
		  JSONObject jsonObj2 = new JSONObject();
		  JSONObject jsonObj3 = new JSONObject();
		  
		  //단기예보 응답 데이터를 JsonObject에 대입
		  jsonObj.put("result", resultData);
		  //중기예보 응답 데이터를 JsonObject에 대입
		  jsonObj2.put("result", resultData2);
		  jsonObj3.put("result", resultData3);
		  
		  //단기예보 데이터 가공 전 시작날짜와 끝날자 set
		  searchInfo.setStart_date(shortStDate);
		  searchInfo.setEnd_date(shortEdDate);
		  //단기에보 데이터 가공
		  jsonObj = apiJsonFormat.shortWeather(jsonObj, searchInfo);
		  
		  //중기예보 데이터 가공 전 시작날짜와 끝날자 다시 set
		  searchInfo.setStart_date(midStDate);
		  searchInfo.setEnd_date(midEdDate);
		  //중기예보 데이터 가공
		  jsonObj2 = apiJsonFormat.midWeather(jsonObj2,jsonObj3,searchInfo);
		  	  
		  //가공된 Json데이터들을 하나의 Json객채에 담아 프론트에 응답해주기 위해 Jsonarray로 파싱
		  JSONArray parse_item = jsonParsing.parse2(jsonObj);
		  JSONArray parse_item2 = jsonParsing.parse2(jsonObj2);
		  
		  //파싱된 Jsonarray의 데이터들을 타입이 JSON Object인 리스트에 담아준다.
		  List<JSONObject> list = new ArrayList<JSONObject>();
		  int len = parse_item.length();
		  int len2 = parse_item2.length();
		  
		  for (int i = 0; i<len; i++) {
			  list.add((JSONObject) parse_item.get(i));
		  }
		  for (int i = 0; i<len2; i++) {
			  list.add((JSONObject) parse_item2.get(i));
		  }
		
		  //리스트 안의 json데이터들을 JsonArray에 대입
		  JSONArray data = new JSONArray();
	      for(int i = 0; i < list.size(); i++) {
	    	  data.put(list.get(i));
	      }
	      
	      //JsonArray를 JsonObj에 대입
	      JSONObject toTalObj = new JSONObject();
	      toTalObj.put("list", data);
		
		return toTalObj;
	}
	
	@Override
	public JSONObject getSearchInfo() throws Exception {
		//DB에서 조회이력테이블을 select한 값을 담은 list
		List<Tb_weather_search_scope_info> getList = logdao.getSearchInfo();
		//날짜 형식을 가공하여 getList의 값을 담을 list
		List<Tb_weather_search_scope_info> newList = new ArrayList<Tb_weather_search_scope_info>();
		//jsonArray형식으로 변환할 ArrayList 선언
		List<HashMap<String, String>> resultData = new ArrayList<HashMap<String,String>>();
				
		for(int i=0; i<getList.size(); i++) {
			Tb_weather_search_scope_info data = new Tb_weather_search_scope_info();
			
			data.setNo(getList.get(i).getNo());
			data.setUser_id(getList.get(i).getUser_id());
			data.setUser_name(getList.get(i).getUser_name());
			//DB데이터의 날짜형식을 포멧시키기 위한 조건절
			if(!getList.get(i).getStart_date().equals("") || getList.get(i).getStart_date() != null) {
				String startDate = getList.get(i).getStart_date();
				startDate = startDate.substring(0,4) + "." + startDate.substring(4,6) + "." + startDate.substring(6,8);
				System.out.println("startDate ==> " + startDate);
				data.setStart_date(startDate);
			}else {
				//값이 비어있을 때 쓸 문자를 추가, 대응책필요
			}
			if(!getList.get(i).getEnd_date().equals("") || getList.get(i).getEnd_date() != null) {
				String endDate = getList.get(i).getEnd_date();
				endDate = endDate.substring(0,4) + "." + endDate.substring(4,6) + "." + endDate.substring(6,8);
				System.out.println("endDate ==> " + endDate);
				data.setEnd_date(endDate);
			}else {
				//값이 비어있을 때 쓸 문자를 추가, 대응책필요
			}
			if(!getList.get(i).getCreate_date().equals("") || getList.get(i).getCreate_date() != null) {
				String createDate = getList.get(i).getCreate_date();
				createDate = createDate.substring(0, 10).replaceAll("-",".");
				System.out.println("createDate ==> " + createDate);
				data.setCreate_date(createDate);
			}else {
				//값이 비어있을 때 쓸 문자를 추가, 대응책필요
			}
			
			newList.add(data);	

		}
		
		for(int i=0; i<newList.size(); i++) {
			HashMap<String, String> param = new HashMap<String, String>();
			//프론트로 보낼 json형식의 키와 값을 설정해준다 
			param.put("no", newList.get(i).getNo());
			param.put("id", newList.get(i).getUser_id());
			param.put("name", newList.get(i).getUser_name());
			param.put("stDate", newList.get(i).getStart_date());
			param.put("edDate", newList.get(i).getEnd_date());
			param.put("crDate", newList.get(i).getCreate_date());
			
			resultData.add(param);
		}
		
		JSONArray data = new JSONArray();
	    for(int i = 0; i < resultData.size(); i++) {
	    	data.put(resultData.get(i));
	    }
		
	    //jsonArray를 jsonObject에 담아 컨트롤러로 보내준다.
	    JSONObject result = new JSONObject();
	    result.put("list", data);
		
	    System.out.println(result);
	    
		return result;
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
