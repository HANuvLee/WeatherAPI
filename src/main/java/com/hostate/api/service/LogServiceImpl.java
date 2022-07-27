package com.hostate.api.service;

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

	

	//��ȸ �̷����� ����

	//��¥�����ͺ�ȯ
	HashMap<String, Object> resultData = new HashMap<String, Object>();
	Date today = new Date(); // ���������� ���� �ð�
	Locale currentLocale = new Locale("KOREAN", "KOREA"); // ����
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm", currentLocale);
	StringBuilder time = new StringBuilder(formatter.format(today)); // ��û�� �ð��� �ÿ� ���� ���Ѵ�.
	StringBuilder startDate = new StringBuilder(time); // yyyymmddhhmm ����
	StringBuilder baseTime = new StringBuilder(formatter.format(today)); // ��û�� �ð��� �ÿ� ���� ���Ѵ�.


	@Override
	public int searchWeatherLogInsert(Tb_weather_search_scope_info searchInfo) throws Exception {

		return logdao.searchWeatherLogInsert(searchInfo);
	}
	
	//�������� API
	@Override
	public JSONObject getFirstApi(Tb_weather_search_scope_info searchInfo) throws Exception {
		//api �����͸� ���� HashMap ����
		HashMap<String, Object> resultData = new HashMap<String, Object>();

		Date today = new Date(); //���������� ���� �ð�
		Locale currentLocale = new Locale("KOREAN", "KOREA"); //����
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", currentLocale);
		String time = new String(formatter.format(today)); //��û�� �ð��� �ÿ� ���� ���Ѵ�.
		String startDate = new String(time); //yyyymmddhhmm ����
		
		System.out.println("getFirstApi startDate =>" + startDate);
		
		//���������̹Ƿ� ��ȸ���۳�¥�� ����¥�� ���÷� �����ش�.
		searchInfo.setStart_date(startDate);
		searchInfo.setEnd_date(startDate);
		
		String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" // https�Է� �� Java�� �ŷ��ϴ�������

				// ���(keystore)�� ����ϰ���
				// �ϴ� ��������� ��ϵǾ� ���� �ʾ�
				// ������ ���ܵǴ�����߻�.
					+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D" // ����Ű
					+ "&pageNo=1" //��������ȣ
					+ "&numOfRows=254" //��� �� , default : �Ϸ�ġ ������ ������ ����
					+ "&dataType=JSON" // XML, JSON
					+ "&base_date=" + startDate // ��ǥ����
					+ "&base_time=0200" //��ǥ�ð� 0200�� ������ tmx�� tmn (���� �ְ�������°��� �����´�.)
					+ "&nx=60" + "&ny=127";
					
					resultData = getDataFromJson(url, "UTF-8", "get", "");
					System.out.println("# RESULT : " + resultData);
					
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("result", resultData);
					
					jsonObj = apiJsonFormat.shortWeather(jsonObj, searchInfo);
					
					return jsonObj;
		}

	
	//�ܱ⿹����ȸ ����
	@Override
	public JSONObject getShorWeather(Tb_weather_search_scope_info searchInfo) throws Exception {
		
		//api �����͸� ���� HashMap ����
		HashMap<String, Object> resultData = new HashMap<String, Object>();
		
		System.out.println("LogServiceImpl getShorWeather START");
		//���������� ���� �� ��û �ĸ����� ���¸� ���߱� �������� �޼��� ����, mm������ ��û �Ұ�

		String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" //https�Է� �� Java�� �ŷ��ϴ�������
																								// ���(keystore)�� ����ϰ���
																								// �ϴ� ��������� ��ϵǾ� ���� �ʾ�
																								// ������ ���ܵǴ�����߻�.
				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D" // ����Ű
				+ "&pageNo=1" //��������ȣ
				+ "&numOfRows=1000" //��� �� , default : �Ϸ�ġ ������ ������ ����
				+ "&dataType=JSON" // XML, JSON
				+ "&base_date=" + startDate.substring(0,8) // ��ǥ����
				+ "&base_time=0200" //��ǥ�ð� 0200�� ������ tmx�� tmn (���� �ְ�������°��� �����´�.)
				+ "&nx=60" + "&ny=127";

		resultData = getDataFromJson(url, "UTF-8", "get", "");
		System.out.println("# RESULT : " + resultData);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", resultData);
		
		jsonObj = apiJsonFormat.shortWeather(jsonObj, searchInfo);

		return jsonObj;
	}
	
	//�߱⿹����ȸ ����
	@Override
	public JSONObject getMidWeather(Tb_weather_search_scope_info searchInfo) throws Exception {	
		//�߱��¿�����ȸ api �����͸� ���� HashMap ����
		HashMap<String, Object> resultData1 = new HashMap<String, Object>();
		//�߱����󿹺���ȸapi �����͸� ���� HashMap ����
		HashMap<String, Object> resultData2 = new HashMap<String, Object>();
		
		System.out.println("LogServiceImpl getMidWeather START");
		System.out.println("getMidWeather startDate => " + startDate);
		System.out.println("getMidWeather baseTime => "+  baseTime);
		StringBuilder tmfc = new StringBuilder(baseTime);
		
		apiDateFormat.tmFcDateFormat(tmfc); // api ��ǥ�ð� �ĸ����� ���¸� ���߱� ���� ���� (0600 or 1800)
		System.out.println("getMidWeather tmfc => "+  tmfc);
		
		//�߱�����ȸ ��ûURL
		String url = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa"

				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D"
				// �Ϲ�����Ű
				+ "&pageNo=1" // ��������ȣ
				+ "&numOfRows=10" // ������ rows
				+ "&dataType=JSON" // JSON, XNL
				+ "&regId=11B10101" // ���������ڵ� �⺻�� ����
				+ "&tmFc=" + tmfc; // ��ǥ�ð�
		//�߱����󿹺���ȸ ��ûURL
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
		
		jsonObj = apiJsonFormat.midWeather(jsonObj,jsonObj2, searchInfo);
		
		return jsonObj;
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
