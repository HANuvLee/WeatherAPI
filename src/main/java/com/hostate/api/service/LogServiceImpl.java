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
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.xml.crypto.Data;

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
import com.hostate.api.vo.Tb_User_InfoVO;
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

	@Autowired
	JsonParsing jsonParsing;

	// 날짜 데이터변환
	Date today = new Date(); // 메인페이지 접속 시간
	Locale currentLocale = new Locale("KOREAN", "KOREA"); // 나라
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm", currentLocale);
	// 요청한 시간의 시와 분을 구한다.(단기예보조회 시 사용)
	StringBuilder startDate = new StringBuilder(formatter.format(today));
	// 요청한 시간의 시와 분을 구한다.(중기예보조회 시 사용)
	StringBuilder baseTime = new StringBuilder(formatter.format(today));
	// 중기조회 시 발표시각 세팅
	StringBuilder tmfc = new StringBuilder(baseTime);

	@Override
	public int searchWeatherLogInsert(Tb_weather_search_scope_info searchInfo) throws Exception {

		return logdao.searchWeatherLogInsert(searchInfo);
	}

	// 최초접속 API
	@Override
	public JSONObject getFirstApi(Tb_weather_search_scope_info searchInfo) throws Exception {
		System.out.println("getFirstApi serviceimple start");
		// api 데이터를 담을 HashMap 생성
		HashMap<String, Object> resultData = new HashMap<String, Object>();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", currentLocale);
		String startDate = new String(formatter.format(today)); // 요청한 시간의 시와 분을 구한다.

		// 최초접속이므로 조회시작날짜와 끝날짜를 오늘로 맞춰준다.
		searchInfo.setStart_date(startDate);
		searchInfo.setEnd_date(startDate);

		String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" // https입력 시 Java의 신뢰하는인증서
																								// 목록(keystore)에 사용하고자
																								// 하는 인증기관이 등록되어 있지 않아
																								// 접근이 차단되는현상발생.
				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D" // 인증키
				+ "&pageNo=1" // 페이지번호
				+ "&numOfRows=254" // 결과 수 , default : 하루치 데이터 단위로 설정
				+ "&dataType=JSON" // XML, JSON
				+ "&base_date=" + startDate.substring(0, 8) // 발표일자
				+ "&base_time=0200" // 발표시각 0200인 이유는 tmx와 tmn (오늘 최고최저기온값을 가져온다.)
				+ "&nx=60" + "&ny=127";

		resultData = getDataFromJson(url, "UTF-8", "get", "");
		System.out.println("# RESULT : " + resultData);

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", resultData);

		jsonObj = apiJsonFormat.shortWeather(jsonObj, searchInfo);

		return jsonObj;
	}

	// 단기예보조회 서비스
	@Override
	public JSONObject getShortWeather(Tb_weather_search_scope_info searchInfo) throws Exception {
		System.out.println("getShortWeather serviceimple start");
		// api데이터를 담을 HashMap 선언
		HashMap<String, Object> resultData = new HashMap<String, Object>();

		System.out.println("LogServiceImpl getShorWeather START");

		String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" // https입력 시 Java의 신뢰하는인증서
																								// 목록(keystore)에 사용하고자
																								// 하는 인증기관이 등록되어 있지 않아
																								// 접근이 차단되는현상발생.
				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D" // 인증키
				+ "&pageNo=1" // 페이지번호
				+ "&numOfRows=1000" // 결과 수 , default : 하루치 데이터 단위로 설정
				+ "&dataType=JSON" // XML, JSON
				+ "&base_date=" + startDate.substring(0, 8) // 발표일자
				+ "&base_time=0200" // 발표시각 0200인 이유는 tmx와 tmn (오늘 최고최저기온값을 가져온다.)
				+ "&nx=60" + "&ny=127";
		// 위의 url정보로 api로부터 단기예보 데이터를 전달받는다.
		resultData = getDataFromJson(url, "UTF-8", "get", "");
		System.out.println("# RESULT : " + resultData);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", resultData);

		// 전달받은 단기예보 데이터를 가공하는 서비스 호출
		jsonObj = apiJsonFormat.shortWeather(jsonObj, searchInfo);

		return jsonObj;
	}

	// 중기예보조회 서비스
	@Override
	public JSONObject getMidWeather(Tb_weather_search_scope_info searchInfo) throws Exception {
		System.out.println("getMidWeather serviceimple start");
		// 중기기온예보조회 api 데이터를 담을 HashMap 생성
		HashMap<String, Object> resultData1 = new HashMap<String, Object>();
		// 중기육상예보조회api 데이터를 담을 HashMap 생성
		HashMap<String, Object> resultData2 = new HashMap<String, Object>();

		apiDateFormat.tmFcDateFormat(tmfc); // api 발표시각 파리미터 형태를 맞추기 위한 포멧 (0600 or 1800)

		// 중기기온조회 요청URL
		String url = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa"

				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D"
				// 일반인증키
				+ "&pageNo=1" // 페이지번호
				+ "&numOfRows=10" // 페이지 rows
				+ "&dataType=JSON" // JSON, XNL
				+ "&regId=11B10101" // 예보구역코드 기본값 서울
				+ "&tmFc=" + tmfc; // 발표시각

		// 중기육상예보조회 요청URL
		String url2 = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst"
				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D"
				+ "&pageNo=1" + "&numOfRows=10" + "&dataType=JSON" + "&regId=11B00000" + "&tmFc=" + tmfc;
		// 위의 url정보로 api로부터 단기예보 데이터를 전달받는다.
		resultData1 = getDataFromJson(url, "UTF-8", "get", "");
		resultData2 = getDataFromJson(url2, "UTF-8", "get", "");

		System.out.println("# RESULT : " + resultData1);
		System.out.println("# RESULT : " + resultData2);

		JSONObject jsonObj = new JSONObject();
		JSONObject jsonObj2 = new JSONObject();

		jsonObj.put("result", resultData1);
		jsonObj2.put("result", resultData2);

		// 전달받은 중기예보 데이터를 가공하는 서비스 호출
		jsonObj = apiJsonFormat.midWeather(jsonObj, jsonObj2, searchInfo);

		return jsonObj;
	}

	@Override
	public JSONObject getAllWeather(Tb_weather_search_scope_info searchInfo) throws Exception {
		System.out.println("getAllWeather serviceimple start");

		// api 데이터를 담을 HashMap 생성(단기예보)
		HashMap<String, Object> resultData = new HashMap<String, Object>();
		// api 데이터를 담을 HashMap 생성(중기기온예보)
		HashMap<String, Object> resultData2 = new HashMap<String, Object>();
		// api 데이터를 담을 HashMap 생성(중기육상예보)
		HashMap<String, Object> resultData3 = new HashMap<String, Object>();

		// 오늘날짜로부터 단기끝날짜
		String sWeatherEnd = formatter.format((new Date(today.getTime() + ((60 * 60 * 24 * 1000) * 2))));
		// 오늘날짜로부터 중기시작날짜
		String mWeatherStart = formatter.format((new Date(today.getTime() + ((60 * 60 * 24 * 1000) * 3))));
		// 요청시 받은 시작날짜
		String orgStDate = searchInfo.getStart_date();
		// 단기예보 조회 시 파리미터로 들어갈 시작날짜
		String shortStDate = "";
		// 단기예보 조회 시 파리미터로 들어갈 끝날짜
		String shortEdDate = "";
		// 중기예보 조회 시 파리미터로 들어갈 시작날짜, 형태 포맷
		String midStDate = mWeatherStart.substring(0, 8);
		// 중기예보 조회 시 파람미터로 들어갈 끝날짜
		String midEdDate = searchInfo.getEnd_date();

		// 금일로부터 단기조회날짜 범위의 날짜들(오늘포함 3일)을 리스트에 담는다
		List<LocalDate> shortDateScope = datesBetween.getBetweenDate(startDate, sWeatherEnd);

		for (int i = 0; i < shortDateScope.size(); i++) {
			// 오늘포함 3일이내의 날짜들이 리스트에 존재하며 요청으로 들어온 조회시작날짜가 리스트중에 있다면
			if (orgStDate.equals(shortDateScope.get(i).toString().replaceAll("[^0-9]", ""))) {
				// 그 값을 단기예보시작날짜변수에 대입
				shortStDate = shortDateScope.get(i).toString().replaceAll("[^0-9]", "");
				break;
			}
		} // 단기중기 모두 조회시 shorDateScope리스트의 마지막번째 요소는 단기예보조회 시 끝날자변수이기에 단기조회 끝날짜 변수에 대입
		int lastIdx = shortDateScope.size() - 1;
		// 끝날짜 형태를 포맷 후 대입
		shortEdDate = shortDateScope.get(lastIdx).toString().replaceAll("[^0-9]", "");

		// 중기예보 발표일자 형태 format
		apiDateFormat.tmFcDateFormat(tmfc);

		// 단기기온조회 요청url
		String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" // https입력 시 Java의 신뢰하는인증서
				// 목록(keystore)에 사용하고자
				// 하는 인증기관이 등록되어 있지 않아
				// 접근이 차단되는현상발생.
				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D" // 인증키
				+ "&pageNo=1" // 페이지번호
				+ "&numOfRows=1000" // 결과 수 , default : 하루치 데이터 단위로 설정
				+ "&dataType=JSON" // XML, JSON
				+ "&base_date=" + startDate.substring(0, 8) // 발표일자
				+ "&base_time=0200" // 발표시각 0200인 이유는 tmx와 tmn (오늘 최고최저기온값을 가져온다.)
				+ "&nx=60" + "&ny=127";

		// 중기기온조회 요청URL
		String url2 = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa"

				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D"
				// 일반인증키
				+ "&pageNo=1" // 페이지번호
				+ "&numOfRows=10" // 페이지 rows
				+ "&dataType=JSON" // JSON, XNL
				+ "&regId=11B10101" // 예보구역코드 기본값 서울
				+ "&tmFc=" + tmfc; // 발표시각

		// 중기육상예보조회 요청URL
		String url3 = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst"

				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D"
				+ "&pageNo=1" + "&numOfRows=10" + "&dataType=JSON" + "&regId=11B00000" + "&tmFc=" + tmfc;

		// 단기예보 api요청
		resultData = getDataFromJson(url, "UTF-8", "get", "");
		// 중기예보 api요청
		resultData2 = getDataFromJson(url2, "UTF-8", "get", "");
		resultData3 = getDataFromJson(url3, "UTF-8", "get", "");

		System.out.println("# RESULT : " + resultData);
		System.out.println("# RESULT : " + resultData2);
		System.out.println("# RESULT : " + resultData3);

		// 응답받은 api json을 넣을 json객체 인스턴스
		JSONObject jsonObj = new JSONObject();
		JSONObject jsonObj2 = new JSONObject();
		JSONObject jsonObj3 = new JSONObject();

		// 단기예보 응답 데이터를 JsonObject에 대입
		jsonObj.put("result", resultData);
		// 중기예보 응답 데이터를 JsonObject에 대입
		jsonObj2.put("result", resultData2);
		jsonObj3.put("result", resultData3);

		// 단기예보 데이터 가공 전 시작날짜와 끝날자 set
		searchInfo.setStart_date(shortStDate);
		searchInfo.setEnd_date(shortEdDate);
		// 단기에보 데이터 가공
		jsonObj = apiJsonFormat.shortWeather(jsonObj, searchInfo);

		// 중기예보 데이터 가공 전 시작날짜와 끝날자 다시 set
		searchInfo.setStart_date(midStDate);
		searchInfo.setEnd_date(midEdDate);
		// 중기예보 데이터 가공
		jsonObj2 = apiJsonFormat.midWeather(jsonObj2, jsonObj3, searchInfo);

		// 가공된 Json데이터들을 하나의 Json객채에 담아 프론트에 응답해주기 위해 Jsonarray로 파싱
		JSONArray parseItem = jsonParsing.parse2(jsonObj);
		JSONArray parseItem2 = jsonParsing.parse2(jsonObj2);

		// Jsonarray로 파싱된 데이터들을 타입이 jsonObject인 리스트에 담아준다.
		List<JSONObject> list = new ArrayList<JSONObject>();
		int len = parseItem.length();
		int len2 = parseItem2.length();

		for (int i = 0; i < len; i++) {
			list.add((JSONObject) parseItem.get(i));
		}
		for (int i = 0; i < len2; i++) {
			list.add((JSONObject) parseItem2.get(i));
		}

		// 리스트 안의 json데이터들을 JsonArray로 감싸쭌다.
		JSONArray data = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			data.put(list.get(i));
		}

		// JsonArray를 JsonObj로 다시한번 감싸준다.
		JSONObject toTalObj = new JSONObject();
		toTalObj.put("list", data);

		return toTalObj;
	}

	// 프론트 grid테이블 값을 새로 업데이트 해주기 위한 서비스
	@Override
	public HashMap<String, Object> getSearchInfo(Tb_weather_search_scope_info tbWeatherInfo) throws Exception {
		// DB에서 조회이력테이블을 select한 값을 담은 list
		List<Tb_weather_search_scope_info> getList = logdao.getSearchInfo(tbWeatherInfo);

		// getList의 값들을 가공하여 프론트에 보내질 list
		List<Tb_weather_search_scope_info> newList = new ArrayList<Tb_weather_search_scope_info>();

		// getList 데이터 가공 시 Json형식으로 변경된 데이터들을 담을 리스트
		List<HashMap<String, String>> dataInfo = new ArrayList<HashMap<String, String>>();

		// 최종적으로 컨트롤러로 응답할 HashMap
		HashMap<String, Object> result = new HashMap<String, Object>();

		// 조회이력저장 데이터 총 갯수
		int totalCnt = logdao.getTotalCnt(tbWeatherInfo);

		// 페이징을 위한설정
		if (totalCnt != 0) {
			// Json형식으로 값을 담기 위한 HashMap 객체 선언
			HashMap<String, String> param = new HashMap<String, String>();
			// 한 페이지에 보여줄 데이터 갯수
			int listCnt = tbWeatherInfo.getListCount();
			// 페이지번호
			int pageno = tbWeatherInfo.getPageNo();
			// 총페이지 갯수
			int pageCnt = totalCnt / listCnt;

			if (totalCnt % listCnt > 0) {
				pageCnt++;
			}

			// 페이지 사이즈
			int pagesize = tbWeatherInfo.getPageSize();

			// hashmap에 넣어주기 위해 String으로 캐스팅
			String listCount = String.valueOf(listCnt);
			String pageNo = String.valueOf(pageno);
			String pageCount = String.valueOf(pageCnt);
			String pageSize = String.valueOf(pagesize);

			param.put("pageNo", pageNo);
			param.put("listCount", listCount);
			param.put("pageCount", pageCount);
			param.put("pageSize", pageSize);

			result.put("page", param);
		}

		for (int i = 0; i < getList.size(); i++) {
			Tb_weather_search_scope_info info = new Tb_weather_search_scope_info();

			// 조회이력저장테이블로부터 받은 데이터들을 순서대로 data 객체에 set해준 후 리스에 추가해준다.
			info.setNo(String.valueOf(getList.get(i).getNumrow()));
			info.setUser_id(getList.get(i).getUser_id());
			info.setUser_name(getList.get(i).getUser_name());

			// DB데이터의 날짜형식을 포멧시키기 위한 조건절 수행
			if (!getList.get(i).getStart_date().equals("") || getList.get(i).getStart_date() != null) {
				String startDate = getList.get(i).getStart_date();
				startDate = startDate.substring(0, 4) + "." + startDate.substring(4, 6) + "."
						+ startDate.substring(6, 8);
				info.setStart_date(startDate);
			}
			if (!getList.get(i).getEnd_date().equals("") || getList.get(i).getEnd_date() != null) {
				String endDate = getList.get(i).getEnd_date();
				endDate = endDate.substring(0, 4) + "." + endDate.substring(4, 6) + "." + endDate.substring(6, 8);
				info.setEnd_date(endDate);
			}
			if (!getList.get(i).getCreate_date().equals("") || getList.get(i).getCreate_date() != null) {
				String createDate = getList.get(i).getCreate_date();
				createDate = createDate.substring(0, 10).replaceAll("-", ".");
				info.setCreate_date(createDate);
			}

			// 날짜가 가공된 조회이력데이터를 새로운 리스트에 넣어 준다.
			newList.add(info);
		}

		for (int i = 0; i < newList.size(); i++) {
			// Json형식으로 값을 담기 위한 HashMap 객체 선언
			HashMap<String, String> param = new HashMap<String, String>();
			// 프론트로 보낼 json형식의 키와 값을 설정해준다
			param.put("no", newList.get(i).getNo());
			param.put("id", newList.get(i).getUser_id());
			param.put("name", newList.get(i).getUser_name());
			param.put("stDate", newList.get(i).getStart_date());
			param.put("edDate", newList.get(i).getEnd_date());
			param.put("crDate", newList.get(i).getCreate_date());

			dataInfo.add(param);

		}
		result.put("list", dataInfo);

		/*
		 * 2022-08-01 사내 개인 노트북 인코딩 문제로 인한 JsonObject타입을 리턴하지 않고 HashMap으로 리턴타입을변경 문제
		 * 해결을 위해서는 Json객체로 변환 시 인코딩 설정이 필요하다. => Json변환 시 인코딩 설정 확인해볼것 JSONArray data =
		 * new JSONArray(); for(int i = 0; i < resultData.size(); i++) {
		 * data.put(resultData.get(i)); }
		 * 
		 * //jsonArray를 jsonObject에 담아 컨트롤러로 보내준다. JSONObject result = new JSONObject();
		 * result.put("list", data);
		 * 
		 * System.out.println(result);
		 */

		return result;
	}

	// 메인페이지 select태그 option값 설정
	@Override
	public List<Tb_User_InfoVO> getUsersList() throws Exception {
		System.out.println("getUsersList serviceImpl start");

		// 읽는 리스트
		List<Tb_User_InfoVO> usersList = logdao.getUsersList();
		// 쓰는 리스트
		List<Tb_User_InfoVO> resultList = new ArrayList<Tb_User_InfoVO>();

		for (int i = 0; i < usersList.size(); i++) {
			Tb_User_InfoVO data = new Tb_User_InfoVO();
			data.setUser_id(usersList.get(i).getUser_id());
			// 테이블 컬럼이 동명이인이라면
			if (("1").equals(usersList.get(i).getUser_sn())) {

				String userName = usersList.get(i).getUser_name();
				String no = usersList.get(i).getNo();

				data.setUser_name(userName + no);
				// 동명이인이 아니라면
			} else {
				String userName = usersList.get(i).getUser_name();
				data.setUser_name(userName);
			}
			resultList.add(data);
		}

		return resultList;
	}

	// 메인페이지 그리드2 응답데이터 설정
	@Override
	public List<Tb_weather_search_scope_info> getselectAXUser(Tb_weather_search_scope_info tbWeatherInfo)
			throws Exception {
		// TODO Auto-generated method stub
		/*
		 * SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); Calendar
		 * cal = Calendar.getInstance();
		 * 
		 * int year = Integer.parseInt(tbWeatherInfo.getEnd_date().substring(0,4)); int
		 * month = Integer.parseInt(tbWeatherInfo.getEnd_date().substring(6,7)); int day
		 * = Integer.parseInt(tbWeatherInfo.getEnd_date().substring(9));
		 * 
		 * //cal객체에 파라미터로 받은 날짜를 set cal.set(year, month-1); int firstDate =
		 * cal.getMinimum(Calendar.DAY_OF_MONTH); int lastDate =
		 * cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		 * 
		 * System.out.println("lastDate =>" + lastDate); //시작일을 cal객체에 set cal.set(year,
		 * month-1, firstDate);
		 * tbWeatherInfo.setStart_date(dateFormat.format(cal.getTime()));
		 */

		String userID = tbWeatherInfo.getUser_id();

		// 전체 조회
		if (userID.equals("all")) {
			tbWeatherInfo.setUser_id("");
			// 요청 id 조회
		} else {
			// 요청 파라미터 객체에 아이디 set
			tbWeatherInfo.setUser_id(userID);
		}

		// 객체 피라미터에는 사용자 아이디, 조회시작날짜, 끝날짜를 가지고 있다
		List<Tb_weather_search_scope_info> getselectAXUser = logdao.getselectAXUser(tbWeatherInfo);
		// 응답 리스트
		List<Tb_weather_search_scope_info> resultList = new ArrayList<Tb_weather_search_scope_info>();

		int allTotalCnt = 0;
		
		System.out.println("size() --> " + getselectAXUser.size());
		if(getselectAXUser.size() != 0) {			
			int lastIndex = getselectAXUser.size() - 1;
			for (int i = 0; i < getselectAXUser.size(); i++) {
				Tb_weather_search_scope_info data = new Tb_weather_search_scope_info();
				
				data.setCreate_date(getselectAXUser.get(i).getCreate_date());
				data.setTotalCnt(getselectAXUser.get(i).getTotalCnt());
				
				allTotalCnt += getselectAXUser.get(i).getTotalCnt();
				
				// 테이블 컬럼이 동명이인이라면
				if (("1").equals(getselectAXUser.get(i).getUser_sn())) {
					String userName = getselectAXUser.get(i).getUser_name();
					String no = getselectAXUser.get(i).getNo();
					data.setUser_name(userName + no);
				} // 동명이인이 아니라면
				else {
					String userName = getselectAXUser.get(i).getUser_name();
					data.setUser_name(userName);
				}
				resultList.add(data);
				// 리스트 마지막번째 요소라면
				if (i == lastIndex) {
					data.setName("총합계");
					data.setAllTotalCnt(String.valueOf(allTotalCnt));
				}
			}
			return resultList;
		}
		else {
			return null;
		}
	}

	public HashMap<String, Object> getDataFromJson(String url, String encoding, String type, String jsonStr)
			throws Exception {
		boolean isPost = false;

		// 만약 post방식이면
		if ("post".equals(type)) {
			isPost = true;
			// get방식
		} else {
			url = "".equals(jsonStr) ? url : url + "?request=" + jsonStr;

			System.out.println("getDataFromJson url ==>" + url);
		}

		return getStringFromURL(url, encoding, isPost, jsonStr, "application/json");
	}

	public HashMap<String, Object> getStringFromURL(String url, String encoding, boolean isPost, String parameter,
			String contentType) throws Exception {

		// 파라미터로 받은 url문자열로 url객체 생성
		URL apiURL = new URL(url);

		HttpURLConnection conn = null;

		BufferedReader br = null;
		BufferedWriter bw = null;

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {

			conn = (HttpURLConnection) apiURL.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
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
