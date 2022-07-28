package com.hostate.api.commonutil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class JsonParsing {

	public JSONArray parse(JSONObject jsonObj) {
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
		
		return parse_item;
	}

	public JSONArray parse2(JSONObject jsonObj) {
		//result가져오기
		JSONArray parse_item = (JSONArray)jsonObj.get("list");		
		return parse_item;
	}

}
