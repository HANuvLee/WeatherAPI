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

	// ��¥ �����ͺ�ȯ
	Date today = new Date(); // ���������� ���� �ð�
	Locale currentLocale = new Locale("KOREAN", "KOREA"); // ����
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm", currentLocale);
	// ��û�� �ð��� �ÿ� ���� ���Ѵ�.(�ܱ⿹����ȸ �� ���)
	StringBuilder startDate = new StringBuilder(formatter.format(today));
	// ��û�� �ð��� �ÿ� ���� ���Ѵ�.(�߱⿹����ȸ �� ���)
	StringBuilder baseTime = new StringBuilder(formatter.format(today));
	// �߱���ȸ �� ��ǥ�ð� ����
	StringBuilder tmfc = new StringBuilder(baseTime);

	@Override
	public int searchWeatherLogInsert(Tb_weather_search_scope_info searchInfo) throws Exception {

		return logdao.searchWeatherLogInsert(searchInfo);
	}

	// �������� API
	@Override
	public JSONObject getFirstApi(Tb_weather_search_scope_info searchInfo) throws Exception {
		System.out.println("getFirstApi serviceimple start");
		// api �����͸� ���� HashMap ����
		HashMap<String, Object> resultData = new HashMap<String, Object>();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", currentLocale);
		String startDate = new String(formatter.format(today)); // ��û�� �ð��� �ÿ� ���� ���Ѵ�.

		// ���������̹Ƿ� ��ȸ���۳�¥�� ����¥�� ���÷� �����ش�.
		searchInfo.setStart_date(startDate);
		searchInfo.setEnd_date(startDate);

		String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" // https�Է� �� Java�� �ŷ��ϴ�������
																								// ���(keystore)�� ����ϰ���
																								// �ϴ� ��������� ��ϵǾ� ���� �ʾ�
																								// ������ ���ܵǴ�����߻�.
				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D" // ����Ű
				+ "&pageNo=1" // ��������ȣ
				+ "&numOfRows=254" // ��� �� , default : �Ϸ�ġ ������ ������ ����
				+ "&dataType=JSON" // XML, JSON
				+ "&base_date=" + startDate.substring(0, 8) // ��ǥ����
				+ "&base_time=0200" // ��ǥ�ð� 0200�� ������ tmx�� tmn (���� �ְ�������°��� �����´�.)
				+ "&nx=60" + "&ny=127";

		resultData = getDataFromJson(url, "UTF-8", "get", "");
		System.out.println("# RESULT : " + resultData);

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", resultData);

		jsonObj = apiJsonFormat.shortWeather(jsonObj, searchInfo);

		return jsonObj;
	}

	// �ܱ⿹����ȸ ����
	@Override
	public JSONObject getShortWeather(Tb_weather_search_scope_info searchInfo) throws Exception {
		System.out.println("getShortWeather serviceimple start");
		// api�����͸� ���� HashMap ����
		HashMap<String, Object> resultData = new HashMap<String, Object>();

		System.out.println("LogServiceImpl getShorWeather START");

		String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" // https�Է� �� Java�� �ŷ��ϴ�������
																								// ���(keystore)�� ����ϰ���
																								// �ϴ� ��������� ��ϵǾ� ���� �ʾ�
																								// ������ ���ܵǴ�����߻�.
				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D" // ����Ű
				+ "&pageNo=1" // ��������ȣ
				+ "&numOfRows=1000" // ��� �� , default : �Ϸ�ġ ������ ������ ����
				+ "&dataType=JSON" // XML, JSON
				+ "&base_date=" + startDate.substring(0, 8) // ��ǥ����
				+ "&base_time=0200" // ��ǥ�ð� 0200�� ������ tmx�� tmn (���� �ְ�������°��� �����´�.)
				+ "&nx=60" + "&ny=127";
		// ���� url������ api�κ��� �ܱ⿹�� �����͸� ���޹޴´�.
		resultData = getDataFromJson(url, "UTF-8", "get", "");
		System.out.println("# RESULT : " + resultData);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", resultData);

		// ���޹��� �ܱ⿹�� �����͸� �����ϴ� ���� ȣ��
		jsonObj = apiJsonFormat.shortWeather(jsonObj, searchInfo);

		return jsonObj;
	}

	// �߱⿹����ȸ ����
	@Override
	public JSONObject getMidWeather(Tb_weather_search_scope_info searchInfo) throws Exception {
		System.out.println("getMidWeather serviceimple start");
		// �߱��¿�����ȸ api �����͸� ���� HashMap ����
		HashMap<String, Object> resultData1 = new HashMap<String, Object>();
		// �߱����󿹺���ȸapi �����͸� ���� HashMap ����
		HashMap<String, Object> resultData2 = new HashMap<String, Object>();

		apiDateFormat.tmFcDateFormat(tmfc); // api ��ǥ�ð� �ĸ����� ���¸� ���߱� ���� ���� (0600 or 1800)

		// �߱�����ȸ ��ûURL
		String url = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa"

				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D"
				// �Ϲ�����Ű
				+ "&pageNo=1" // ��������ȣ
				+ "&numOfRows=10" // ������ rows
				+ "&dataType=JSON" // JSON, XNL
				+ "&regId=11B10101" // ���������ڵ� �⺻�� ����
				+ "&tmFc=" + tmfc; // ��ǥ�ð�

		// �߱����󿹺���ȸ ��ûURL
		String url2 = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst"
				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D"
				+ "&pageNo=1" + "&numOfRows=10" + "&dataType=JSON" + "&regId=11B00000" + "&tmFc=" + tmfc;
		// ���� url������ api�κ��� �ܱ⿹�� �����͸� ���޹޴´�.
		resultData1 = getDataFromJson(url, "UTF-8", "get", "");
		resultData2 = getDataFromJson(url2, "UTF-8", "get", "");

		System.out.println("# RESULT : " + resultData1);
		System.out.println("# RESULT : " + resultData2);

		JSONObject jsonObj = new JSONObject();
		JSONObject jsonObj2 = new JSONObject();

		jsonObj.put("result", resultData1);
		jsonObj2.put("result", resultData2);

		// ���޹��� �߱⿹�� �����͸� �����ϴ� ���� ȣ��
		jsonObj = apiJsonFormat.midWeather(jsonObj, jsonObj2, searchInfo);

		return jsonObj;
	}

	@Override
	public JSONObject getAllWeather(Tb_weather_search_scope_info searchInfo) throws Exception {
		System.out.println("getAllWeather serviceimple start");

		// api �����͸� ���� HashMap ����(�ܱ⿹��)
		HashMap<String, Object> resultData = new HashMap<String, Object>();
		// api �����͸� ���� HashMap ����(�߱��¿���)
		HashMap<String, Object> resultData2 = new HashMap<String, Object>();
		// api �����͸� ���� HashMap ����(�߱����󿹺�)
		HashMap<String, Object> resultData3 = new HashMap<String, Object>();

		// ���ó�¥�κ��� �ܱⳡ��¥
		String sWeatherEnd = formatter.format((new Date(today.getTime() + ((60 * 60 * 24 * 1000) * 2))));
		// ���ó�¥�κ��� �߱���۳�¥
		String mWeatherStart = formatter.format((new Date(today.getTime() + ((60 * 60 * 24 * 1000) * 3))));
		// ��û�� ���� ���۳�¥
		String orgStDate = searchInfo.getStart_date();
		// �ܱ⿹�� ��ȸ �� �ĸ����ͷ� �� ���۳�¥
		String shortStDate = "";
		// �ܱ⿹�� ��ȸ �� �ĸ����ͷ� �� ����¥
		String shortEdDate = "";
		// �߱⿹�� ��ȸ �� �ĸ����ͷ� �� ���۳�¥, ���� ����
		String midStDate = mWeatherStart.substring(0, 8);
		// �߱⿹�� ��ȸ �� �Ķ����ͷ� �� ����¥
		String midEdDate = searchInfo.getEnd_date();

		// ���Ϸκ��� �ܱ���ȸ��¥ ������ ��¥��(�������� 3��)�� ����Ʈ�� ��´�
		List<LocalDate> shortDateScope = datesBetween.getBetweenDate(startDate, sWeatherEnd);

		for (int i = 0; i < shortDateScope.size(); i++) {
			// �������� 3���̳��� ��¥���� ����Ʈ�� �����ϸ� ��û���� ���� ��ȸ���۳�¥�� ����Ʈ�߿� �ִٸ�
			if (orgStDate.equals(shortDateScope.get(i).toString().replaceAll("[^0-9]", ""))) {
				// �� ���� �ܱ⿹�����۳�¥������ ����
				shortStDate = shortDateScope.get(i).toString().replaceAll("[^0-9]", "");
				break;
			}
		} // �ܱ��߱� ��� ��ȸ�� shorDateScope����Ʈ�� ��������° ��Ҵ� �ܱ⿹����ȸ �� �����ں����̱⿡ �ܱ���ȸ ����¥ ������ ����
		int lastIdx = shortDateScope.size() - 1;
		// ����¥ ���¸� ���� �� ����
		shortEdDate = shortDateScope.get(lastIdx).toString().replaceAll("[^0-9]", "");

		// �߱⿹�� ��ǥ���� ���� format
		apiDateFormat.tmFcDateFormat(tmfc);

		// �ܱ�����ȸ ��ûurl
		String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" // https�Է� �� Java�� �ŷ��ϴ�������
				// ���(keystore)�� ����ϰ���
				// �ϴ� ��������� ��ϵǾ� ���� �ʾ�
				// ������ ���ܵǴ�����߻�.
				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D" // ����Ű
				+ "&pageNo=1" // ��������ȣ
				+ "&numOfRows=1000" // ��� �� , default : �Ϸ�ġ ������ ������ ����
				+ "&dataType=JSON" // XML, JSON
				+ "&base_date=" + startDate.substring(0, 8) // ��ǥ����
				+ "&base_time=0200" // ��ǥ�ð� 0200�� ������ tmx�� tmn (���� �ְ�������°��� �����´�.)
				+ "&nx=60" + "&ny=127";

		// �߱�����ȸ ��ûURL
		String url2 = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa"

				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D"
				// �Ϲ�����Ű
				+ "&pageNo=1" // ��������ȣ
				+ "&numOfRows=10" // ������ rows
				+ "&dataType=JSON" // JSON, XNL
				+ "&regId=11B10101" // ���������ڵ� �⺻�� ����
				+ "&tmFc=" + tmfc; // ��ǥ�ð�

		// �߱����󿹺���ȸ ��ûURL
		String url3 = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst"

				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D"
				+ "&pageNo=1" + "&numOfRows=10" + "&dataType=JSON" + "&regId=11B00000" + "&tmFc=" + tmfc;

		// �ܱ⿹�� api��û
		resultData = getDataFromJson(url, "UTF-8", "get", "");
		// �߱⿹�� api��û
		resultData2 = getDataFromJson(url2, "UTF-8", "get", "");
		resultData3 = getDataFromJson(url3, "UTF-8", "get", "");

		System.out.println("# RESULT : " + resultData);
		System.out.println("# RESULT : " + resultData2);
		System.out.println("# RESULT : " + resultData3);

		// ������� api json�� ���� json��ü �ν��Ͻ�
		JSONObject jsonObj = new JSONObject();
		JSONObject jsonObj2 = new JSONObject();
		JSONObject jsonObj3 = new JSONObject();

		// �ܱ⿹�� ���� �����͸� JsonObject�� ����
		jsonObj.put("result", resultData);
		// �߱⿹�� ���� �����͸� JsonObject�� ����
		jsonObj2.put("result", resultData2);
		jsonObj3.put("result", resultData3);

		// �ܱ⿹�� ������ ���� �� ���۳�¥�� ������ set
		searchInfo.setStart_date(shortStDate);
		searchInfo.setEnd_date(shortEdDate);
		// �ܱ⿡�� ������ ����
		jsonObj = apiJsonFormat.shortWeather(jsonObj, searchInfo);

		// �߱⿹�� ������ ���� �� ���۳�¥�� ������ �ٽ� set
		searchInfo.setStart_date(midStDate);
		searchInfo.setEnd_date(midEdDate);
		// �߱⿹�� ������ ����
		jsonObj2 = apiJsonFormat.midWeather(jsonObj2, jsonObj3, searchInfo);

		// ������ Json�����͵��� �ϳ��� Json��ä�� ��� ����Ʈ�� �������ֱ� ���� Jsonarray�� �Ľ�
		JSONArray parseItem = jsonParsing.parse2(jsonObj);
		JSONArray parseItem2 = jsonParsing.parse2(jsonObj2);

		// Jsonarray�� �Ľ̵� �����͵��� Ÿ���� jsonObject�� ����Ʈ�� ����ش�.
		List<JSONObject> list = new ArrayList<JSONObject>();
		int len = parseItem.length();
		int len2 = parseItem2.length();

		for (int i = 0; i < len; i++) {
			list.add((JSONObject) parseItem.get(i));
		}
		for (int i = 0; i < len2; i++) {
			list.add((JSONObject) parseItem2.get(i));
		}

		// ����Ʈ ���� json�����͵��� JsonArray�� �������.
		JSONArray data = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			data.put(list.get(i));
		}

		// JsonArray�� JsonObj�� �ٽ��ѹ� �����ش�.
		JSONObject toTalObj = new JSONObject();
		toTalObj.put("list", data);

		return toTalObj;
	}

	// ����Ʈ grid���̺� ���� ���� ������Ʈ ���ֱ� ���� ����
	@Override
	public HashMap<String, Object> getSearchInfo(Tb_weather_search_scope_info tbWeatherInfo) throws Exception {
		// DB���� ��ȸ�̷����̺��� select�� ���� ���� list
		List<Tb_weather_search_scope_info> getList = logdao.getSearchInfo(tbWeatherInfo);

		// getList�� ������ �����Ͽ� ����Ʈ�� ������ list
		List<Tb_weather_search_scope_info> newList = new ArrayList<Tb_weather_search_scope_info>();

		// getList ������ ���� �� Json�������� ����� �����͵��� ���� ����Ʈ
		List<HashMap<String, String>> dataInfo = new ArrayList<HashMap<String, String>>();

		// ���������� ��Ʈ�ѷ��� ������ HashMap
		HashMap<String, Object> result = new HashMap<String, Object>();

		// ��ȸ�̷����� ������ �� ����
		int totalCnt = logdao.getTotalCnt(tbWeatherInfo);

		// ����¡�� ���Ѽ���
		if (totalCnt != 0) {
			// Json�������� ���� ��� ���� HashMap ��ü ����
			HashMap<String, String> param = new HashMap<String, String>();
			// �� �������� ������ ������ ����
			int listCnt = tbWeatherInfo.getListCount();
			// ��������ȣ
			int pageno = tbWeatherInfo.getPageNo();
			// �������� ����
			int pageCnt = totalCnt / listCnt;

			if (totalCnt % listCnt > 0) {
				pageCnt++;
			}

			// ������ ������
			int pagesize = tbWeatherInfo.getPageSize();

			// hashmap�� �־��ֱ� ���� String���� ĳ����
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

			// ��ȸ�̷��������̺�κ��� ���� �����͵��� ������� data ��ü�� set���� �� ������ �߰����ش�.
			info.setNo(String.valueOf(getList.get(i).getNumrow()));
			info.setUser_id(getList.get(i).getUser_id());
			info.setUser_name(getList.get(i).getUser_name());

			// DB�������� ��¥������ �����Ű�� ���� ������ ����
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

			// ��¥�� ������ ��ȸ�̷µ����͸� ���ο� ����Ʈ�� �־� �ش�.
			newList.add(info);
		}

		for (int i = 0; i < newList.size(); i++) {
			// Json�������� ���� ��� ���� HashMap ��ü ����
			HashMap<String, String> param = new HashMap<String, String>();
			// ����Ʈ�� ���� json������ Ű�� ���� �������ش�
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
		 * 2022-08-01 �系 ���� ��Ʈ�� ���ڵ� ������ ���� JsonObjectŸ���� �������� �ʰ� HashMap���� ����Ÿ�������� ����
		 * �ذ��� ���ؼ��� Json��ü�� ��ȯ �� ���ڵ� ������ �ʿ��ϴ�. => Json��ȯ �� ���ڵ� ���� Ȯ���غ��� JSONArray data =
		 * new JSONArray(); for(int i = 0; i < resultData.size(); i++) {
		 * data.put(resultData.get(i)); }
		 * 
		 * //jsonArray�� jsonObject�� ��� ��Ʈ�ѷ��� �����ش�. JSONObject result = new JSONObject();
		 * result.put("list", data);
		 * 
		 * System.out.println(result);
		 */

		return result;
	}

	// ���������� select�±� option�� ����
	@Override
	public List<Tb_User_InfoVO> getUsersList() throws Exception {
		System.out.println("getUsersList serviceImpl start");

		// �д� ����Ʈ
		List<Tb_User_InfoVO> usersList = logdao.getUsersList();
		// ���� ����Ʈ
		List<Tb_User_InfoVO> resultList = new ArrayList<Tb_User_InfoVO>();

		for (int i = 0; i < usersList.size(); i++) {
			Tb_User_InfoVO data = new Tb_User_InfoVO();
			data.setUser_id(usersList.get(i).getUser_id());
			// ���̺� �÷��� ���������̶��
			if (("1").equals(usersList.get(i).getUser_sn())) {

				String userName = usersList.get(i).getUser_name();
				String no = usersList.get(i).getNo();

				data.setUser_name(userName + no);
				// ���������� �ƴ϶��
			} else {
				String userName = usersList.get(i).getUser_name();
				data.setUser_name(userName);
			}
			resultList.add(data);
		}

		return resultList;
	}

	// ���������� �׸���2 ���䵥���� ����
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
		 * //cal��ü�� �Ķ���ͷ� ���� ��¥�� set cal.set(year, month-1); int firstDate =
		 * cal.getMinimum(Calendar.DAY_OF_MONTH); int lastDate =
		 * cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		 * 
		 * System.out.println("lastDate =>" + lastDate); //�������� cal��ü�� set cal.set(year,
		 * month-1, firstDate);
		 * tbWeatherInfo.setStart_date(dateFormat.format(cal.getTime()));
		 */

		String userID = tbWeatherInfo.getUser_id();

		// ��ü ��ȸ
		if (userID.equals("all")) {
			tbWeatherInfo.setUser_id("");
			// ��û id ��ȸ
		} else {
			// ��û �Ķ���� ��ü�� ���̵� set
			tbWeatherInfo.setUser_id(userID);
		}

		// ��ü �Ƕ���Ϳ��� ����� ���̵�, ��ȸ���۳�¥, ����¥�� ������ �ִ�
		List<Tb_weather_search_scope_info> getselectAXUser = logdao.getselectAXUser(tbWeatherInfo);
		// ���� ����Ʈ
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
				
				// ���̺� �÷��� ���������̶��
				if (("1").equals(getselectAXUser.get(i).getUser_sn())) {
					String userName = getselectAXUser.get(i).getUser_name();
					String no = getselectAXUser.get(i).getNo();
					data.setUser_name(userName + no);
				} // ���������� �ƴ϶��
				else {
					String userName = getselectAXUser.get(i).getUser_name();
					data.setUser_name(userName);
				}
				resultList.add(data);
				// ����Ʈ ��������° ��Ҷ��
				if (i == lastIndex) {
					data.setName("���հ�");
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

		// ���� post����̸�
		if ("post".equals(type)) {
			isPost = true;
			// get���
		} else {
			url = "".equals(jsonStr) ? url : url + "?request=" + jsonStr;

			System.out.println("getDataFromJson url ==>" + url);
		}

		return getStringFromURL(url, encoding, isPost, jsonStr, "application/json");
	}

	public HashMap<String, Object> getStringFromURL(String url, String encoding, boolean isPost, String parameter,
			String contentType) throws Exception {

		// �Ķ���ͷ� ���� url���ڿ��� url��ü ����
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

			if (isPost) { // ���� post���
				conn.setRequestMethod("POST"); // ��û��� ����
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
