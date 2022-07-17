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

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hostate.api.commonutil.ApiDateFormat;

//@RestController : �⺻ ������ �޼ҵ���� ��� @responsebody�� ���´�
//@RequestBody : Ŭ���̾�Ʈ ��û xml/json�� �ڹ� ��ü�� ��ȯ�Ͽ� ���� ���� �� �ִ�.
//@ResponseBody : �ڹ� ��ü�� xml/json���� ��ȯ���� ���䰴ü�� Body�� �Ǿ� ���۰����ϴ�.

@RestController
public class ApiController {

	@Autowired
	ApiDateFormat apiDateFormat;

	/*
	 * @API LIST ~
	 * 
	 * getVilageFcst �ܱ⿹����ȸ getMidTa �߱�����ȸ getMidLandFcst �߱����󿹺���ȸ
	 * 
	 */
	@RequestMapping(value = "/api/weather.do", method = RequestMethod.GET)
	public String restApiGetWeather() throws Exception {

		Date today = new Date(); // ���������� ���� �ð�
		Locale currentLocale = new Locale("KOREAN", "KOREA"); // ����
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm", currentLocale); // ���ϴ� �ð� ����
		StringBuilder baseTime = new StringBuilder(formatter.format(today));
		apiDateFormat.baseTimeFormat(baseTime); // base_time (��ǥ�ð�) �ĸ����� ���¸� ���߱� ���� ����
		
		System.out.println(baseTime.substring(8,12));
		
		String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" // https�Է� �� Java�� �ŷ��ϴ������� ���(keystore)�� ����ϰ��� �ϴ� ��������� ��ϵǾ� ���� �ʾ� ������ ���ܵǴ� ����߻�.
	
				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D" // ����Ű
				+ "&pageNo=1" // ��������ȣ
				+ "&numOfRows=36" // �� �о��� ��� ��
				+ "&dataType=JSON" // XML, JSON
				+ "&base_date=" + baseTime.substring(0, 8) //��ǥ����
				+ "&base_time=" + baseTime.substring(8,12) // + baseTime.substring(8)  //��ǥ�ð�
				+ "&nx=60"
				+ "&ny=127";

		HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8", "get", "");
		System.out.println("# RESULT : " + resultMap);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", resultMap);

		return jsonObj.toString();
	}

	@RequestMapping(value = "/api/searchweather.do", method = RequestMethod.GET)
	public String restApiSearchWeather() throws Exception {

		Date today = new Date(); // ���������� ���� �ð�
		Locale currentLocale = new Locale("KOREAN", "KOREA"); // ����
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm", currentLocale); // ���ϴ� �ð� ����

		StringBuilder tmfc = new StringBuilder(formatter.format(today));
		apiDateFormat.tmFcDateFormat(tmfc); // api ��ǥ�ð� �ĸ����� ���¸� ���߱� ���� ���� (0600 or 1800)

		String url = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa" // https�Է� �� Java�� �ŷ��ϴ� ������ ���(keystore)�� ����ϰ��� �ϴ� ��������� ��ϵǾ� ���� �ʾ� ������ ���ܵǴ� ����.

				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D" // �Ϲ�����Ű
				+ "&pageNo=1" // ��������ȣ
				+ "&numOfRows=10" // ������ rows
				+ "&dataType=JSON" // JSON, XNL
				+ "&regId=11B10101" // ���������ڵ� �⺻�� ����
				+ "&tmFc=" + tmfc; // ��ǥ�ð�

		HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8", "get", "");
		System.out.println("# RESULT : " + resultMap);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", resultMap);

		return jsonObj.toString();
	}
	
	public HashMap<String, Object> getDataFromJson(String url, String encoding, String type, String jsonStr)
			throws Exception {
		boolean isPost = false;

		if ("post".equals(type)) {
			isPost = true;
		} else {
			url = "".equals(jsonStr) ? url : url + "?request=" + jsonStr;
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
