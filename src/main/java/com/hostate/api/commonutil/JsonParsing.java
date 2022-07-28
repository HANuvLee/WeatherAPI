package com.hostate.api.commonutil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class JsonParsing {

	public JSONArray parse(JSONObject jsonObj) {
		//result��������
		JSONObject parse_result = (JSONObject)jsonObj.get("result");
		// response ��������
		JSONObject parse_response = (JSONObject)parse_result.get("response");
		// response�κ��� body ã�ƿ���
		JSONObject parse_body = (JSONObject) parse_response.get("body");
		// body �� ���� items �޾ƿ���
		JSONObject parse_items = (JSONObject) parse_body.get("items");
		// items�� ���� item�� �޾ƿɴϴ�. item : �ڿ� [ �� �����ϹǷ� jsonarray�Դϴ�.
		JSONArray parse_item = (JSONArray) parse_items.get("item");
		
		return parse_item;
	}

	public JSONArray parse2(JSONObject jsonObj) {
		//result��������
		JSONArray parse_item = (JSONArray)jsonObj.get("list");		
		return parse_item;
	}

}
