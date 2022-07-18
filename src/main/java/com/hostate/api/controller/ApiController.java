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

//@RestController : �⺻ ������ �޼ҵ���� ��� @responsebody�� ���´�
//@RequestBody : Ŭ���̾�Ʈ ��û xml/json�� �ڹ� ��ü�� ��ȯ�Ͽ� ���� ���� �� �ִ�.
//@ResponseBody : �ڹ� ��ü�� xml/json���� ��ȯ���� ���䰴ü�� Body�� �Ǿ� ���۰����ϴ�.

@RestController
public class ApiController {

	@Autowired
	ApiDateFormat apiDateFormat;

	@Autowired
	LogService logService;
	
	/*
	 * @API LIST ~
	 * 
	 * getVilageFcst �ܱ⿹����ȸ getMidTa �߱�����ȸ getMidLandFcst �߱����󿹺���ȸ
	 */

	// �ܱ⿹��
	@RequestMapping(value = "/api/searchvilageweather.do", method = RequestMethod.GET)
	public String getVilageFcst(HttpSession session, String startdate, String enddate) throws Exception {
		System.out.println("�ܱ⿹��ȣ��");
		
		String user_id = (String) session.getAttribute("user_id");
		int recordChk = logService.searchWeatherLogInsert(user_id, startdate, enddate); //��ȸ�������
		
		Date today = new Date(); // ���������� ���� �ð�
		Locale currentLocale = new Locale("KOREAN", "KOREA"); // ����
		SimpleDateFormat formatter = new SimpleDateFormat("HHmm", currentLocale); //
		StringBuilder baseTime = new StringBuilder(formatter.format(today)); //��û�� �ð��� �ÿ� ���� ���Ѵ�.
		
		StringBuilder startDate = new StringBuilder(startdate+baseTime);
		apiDateFormat.baseTimeFormat(startDate); // base_time (��ǥ�ð�) �ĸ����� ���¸� ���߱� ��������

		String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst" // https�Է� �� Java�� �ŷ��ϴ�������
																								// ���(keystore)�� ����ϰ���
																								// �ϴ� ��������� ��ϵǾ� ���� �ʾ�
																								// ������ ���ܵǴ�����߻�.

				+ "?serviceKey=4gFSoB%2B%2FlIzZcu1j3H9L1dYh4fTkBHtKn%2B0B6%2FYpI5El6YZcTUH%2B1O1QGxkjXnTFCtzlvGGRuK6gTFl73mL1sQ%3D%3D" // ����Ű
				+ "&pageNo=1" // ��������ȣ
				+ "&numOfRows=834" // �� �о��� ��� �� (����, ����, ��)
				+ "&dataType=JSON" // XML, JSON
				+ "&base_date=" + startDate.substring(0, 8) // ��ǥ����
				+ "&base_time=" + startDate.substring(8) // ��ǥ�ð�
				+ "&nx=60" 
				+ "&ny=127";

		HashMap<String, Object> resultMap = getDataFromJson(url, "UTF-8", "get", "");
		System.out.println("# RESULT : " + resultMap);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", resultMap);

		return jsonObj.toString();

	}

	// �߱��¿���
	@RequestMapping(value = "/api/searchmidtaweather.do", method = RequestMethod.GET)
	public String restApiSearchMidTaWeather(HttpSession session, String startdate, String enddate) throws Exception {
		System.out.println("�߱��¿���");
		System.out.println(startdate);
		System.out.println(enddate);

		Date today = new Date(); // ���������� ���� �ð�
		Locale currentLocale = new Locale("KOREAN", "KOREA"); // ����
		SimpleDateFormat formatter = new SimpleDateFormat("HHmm", currentLocale); //
		StringBuilder baseTime = new StringBuilder(formatter.format(today));

		StringBuilder tmfc = new StringBuilder(startdate + baseTime);
		apiDateFormat.tmFcDateFormat(tmfc); // api ��ǥ�ð� �ĸ����� ���¸� ���߱� ���� ���� (0600 or 1800)
		String url = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa" // https�Է� �� Java�� �ŷ��ϴ� ������
																					// ���(keystore)�� ����ϰ��� �ϴ� ���������
																					// ��ϵǾ� ���� �ʾ� ������ ���ܵǴ� ����.

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
	
		// �߱����󿹺�
		@RequestMapping(value = "/api/searchmidlandweather.do", method = RequestMethod.GET)
		public String restApiSearchMidLandWeather(HttpSession session, String startdate, String enddate) throws Exception {
			System.out.println("�߱����󿹺�");
			System.out.println(startdate);
			System.out.println(enddate);

			Date today = new Date(); // ���������� ���� �ð�
			Locale currentLocale = new Locale("KOREAN", "KOREA"); // ����
			SimpleDateFormat formatter = new SimpleDateFormat("HHmm", currentLocale); //
			StringBuilder baseTime = new StringBuilder(formatter.format(today));

			StringBuilder tmfc = new StringBuilder(startdate + baseTime);
			apiDateFormat.tmFcDateFormat(tmfc); // api ��ǥ�ð� �ĸ����� ���¸� ���߱� ���� ���� (0600 or 1800)
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

		if ("post".equals(type)) { //post���
			isPost = true;
		} else { //get���
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
