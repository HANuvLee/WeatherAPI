package com.hostate.api.commonutil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class JsonParsing {

	public JSONArray parse(JSONObject jsonObj) {
		//result가져오기
		JSONObject parseResult = (JSONObject)jsonObj.get("result");
		// response 가져오기
		JSONObject parseResponse = (JSONObject)parseResult.get("response");
		// response로부터 body 찾아오기
		JSONObject parseBody = (JSONObject) parseResponse.get("body");
		// body 로 부터 items 받아오기
		JSONObject parseItems = (JSONObject) parseBody.get("items");
		// items로 부터 item을 받아옵니다. item : 뒤에 [ 로 시작하므로 jsonarray입니다.
		JSONArray parseItem = (JSONArray) parseItems.get("item");
		
		return parseItem;
	}

	public JSONArray parse2(JSONObject jsonObj) {
		//result가져오기
		JSONArray parseItem = (JSONArray)jsonObj.get("list");		
		return parseItem;
	}

}
