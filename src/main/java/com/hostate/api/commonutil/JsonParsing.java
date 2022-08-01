package com.hostate.api.commonutil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class JsonParsing {

	public JSONArray parse(JSONObject jsonObj) {
		//result��������
		JSONObject parseResult = (JSONObject)jsonObj.get("result");
		// response ��������
		JSONObject parseResponse = (JSONObject)parseResult.get("response");
		// response�κ��� body ã�ƿ���
		JSONObject parseBody = (JSONObject) parseResponse.get("body");
		// body �� ���� items �޾ƿ���
		JSONObject parseItems = (JSONObject) parseBody.get("items");
		// items�� ���� item�� �޾ƿɴϴ�. item : �ڿ� [ �� �����ϹǷ� jsonarray�Դϴ�.
		JSONArray parseItem = (JSONArray) parseItems.get("item");
		
		return parseItem;
	}

	public JSONArray parse2(JSONObject jsonObj) {
		//result��������
		JSONArray parseItem = (JSONArray)jsonObj.get("list");		
		return parseItem;
	}

}
