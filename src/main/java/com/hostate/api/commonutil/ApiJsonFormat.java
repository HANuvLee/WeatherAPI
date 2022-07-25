package com.hostate.api.commonutil;

import java.util.Locale.Category;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.hostate.api.vo.Tb_weather_search_scope_info;

@Component
public class ApiJsonFormat {

	public JSONObject shortWeather(JSONObject jsonObj, Tb_weather_search_scope_info searchInfo) {
		System.out.println("JSONObject ApiJsonFormat class shortWeather start");
		System.out.println("JSONObject ApiJsonFormat class shortWeather start_date" + searchInfo.getStart_date());
		System.out.println("JSONObject ApiJsonFormat class shortWeather end_date" + searchInfo.getEnd_date());
		//result가져오기
		JSONObject parse_result = (JSONObject)jsonObj.get("result");
		// response 가져오기
		JSONObject parse_response = (JSONObject)parse_result.get("response");
		// response로부터 body 찾아오기
		JSONObject parse_body = (JSONObject) parse_response.get("body");
		// body 로 부터 items 받아오기
		JSONObject parse_items = (JSONObject) parse_body.get("items");
		// items로 부터 item을 받아옵니다. item : 뒤에 [ 로 시작하므로 jsonarray입니다.
		JSONArray parse_item = (JSONArray) parse_items.get("item");
		
		for(int i = 0; i<parse_item.length(); i++) {
			JSONObject value = (JSONObject) parse_item.get(i);
			System.out.println(value);
			if(value.get("category").equals("SKY") && value.get("fcstDate").equals("20220725")) {
				System.out.println("하늘상태"+i+" 값 : " + value.get("fcstValue"));
			}
		}
		return null;
	}

}
