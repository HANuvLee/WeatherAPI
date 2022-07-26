package com.hostate.api.commonutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.hostate.api.vo.Tb_weather_search_scope_info;

@Component
public class ApiJsonFormat {

	public JSONObject shortWeather(JSONObject jsonObj, Tb_weather_search_scope_info searchInfo) {
		
		// ��¥�� ����Ʈ�� ������ JSON������ Hashmap������ ���� ����Ʈ, HashMap key:String / Value:Object
		ArrayList<HashMap<String, String>> item = new ArrayList<HashMap<String, String>>();
		// result��������
		JSONObject parse_result = (JSONObject) jsonObj.get("result");
		// response ��������
		JSONObject parse_response = (JSONObject) parse_result.get("response");
		// response�κ��� body ã�ƿ���
		JSONObject parse_body = (JSONObject) parse_response.get("body");
		// body �� ���� items �޾ƿ���
		JSONObject parse_items = (JSONObject) parse_body.get("items");
		// items�� ���� item�� �޾ƿɴϴ�. item : �ڿ� [ �� �����ϹǷ� jsonarray�Դϴ�.
		JSONArray parse_item = (JSONArray) parse_items.get("item");
		
		// ��ȸ��¥ ����
		String startDate = searchInfo.getStart_date();
		String endDate = searchInfo.getEnd_date();

		// ��ȸ��¥�� intŸ������ ����ȯ, �ݺ��� ��� �� ��ȸ������ ���ϱ� ����
		int st = Integer.parseInt(startDate);
		int ed = Integer.parseInt(endDate);

		String[] date = new String[ed - st + 1];
		// �Ϸ�ġ ���� ���°��� ���� �迭
		int[] sky = new int[ed - st + 1];
		// �Ϸ�ġ ����Ȯ���� ���� �迭
		int[] pop = new int[ed - st + 1];
		// �����µ�
		double[] tmn = new double[ed - st + 1];
		// �ְ�µ�
		double[] tmx = new double[ed - st + 1];
		
		//���۳�¥���� ����¥ ������ŭ �ݺ�
		for (int i = 0; i <= ed - st; i++) {
			System.out.println("Top For" + i);
			//�ϴ� �� ����Ȯ���� ��ü���� ������ ���� ����
			int num = 0;
			//�ϴð��� ��ü ��
			double skyVal = 0;
			//����ȭ���� ��ü ��
			double popVal = 0;
			for (int j = 0; j < parse_item.length(); j++) {
				JSONObject value = (JSONObject) parse_item.get(j);
				//JSON������ �� ī�װ��� �ϴ��̰� ��¥�� ��ȸ���۳�¥���� ����¥������ �Ϸ�ġ�� SKY���� ���Ͽ� ���Ѵ�
				if (value.get("category").equals("SKY") && value.get("fcstDate").equals(Integer.toString(st + i))) {
					skyVal += Integer.parseInt((String) value.get("fcstValue"));
					num += 1;
				}
				//JSON������ �� ī�װ��� ����Ȯ���̰� ��¥�� ��ȸ���۳�¥���� ����¥������ �Ϸ�ġ�� POP���� ���Ͽ� ���Ѵ�
				if (value.get("category").equals("POP") && value.get("fcstDate").equals(Integer.toString(st + i))) {
					popVal += Integer.parseInt((String) value.get("fcstValue"));
				}
				//JSON������ �� ī�װ��� �����µ��̰� ��¥�� ��ȸ���۳�¥���� ����¥������ �Ϸ�ġ�� TMN���� ���Ͽ� ���Ѵ�
				if (value.get("category").equals("TMN") && value.get("fcstDate").equals(Integer.toString(st + i))) {
					//tmn�迭�� ����
					tmn[i] = Double.parseDouble((String) value.get("fcstValue"));
				}
				//JSON������ �� ī�װ��� �ְ�µ��̰� ��¥�� ��ȸ���۳�¥���� ����¥������ �Ϸ�ġ�� TMX���� ���Ͽ� ���Ѵ�
				if (value.get("category").equals("TMX") && value.get("fcstDate").equals(Integer.toString(st + i))) {
					//tmx�迭�� ����
					tmx[i] = Double.parseDouble((String) value.get("fcstValue"));
				}
			}
			//sky �迭�� ����
			sky[i] = (int) Math.round(skyVal / num);
			//pop�迭�� ����
			pop[i] = (int) Math.round(popVal / num);
		}

		// ��¥�� �����͸� ��� hashmap HashMap<String, Object>
		for (int i = 0; i <= ed - st; i++) {
			// DateŰ���� ���۳�¥���� ����¥���� ��´�.
			HashMap<String, String> map = new HashMap<String, String>();
			//dateŰ���� ��¥�� ��´�
			map.put("date", Integer.toString(st + i));
			//skyŰ��  �Ϸ�ġsky��հ�
			map.put("sky", Integer.toString(sky[i]));
			//popŰ��  �Ϸ�ġ ����Ȯ�� ��հ�
			map.put("pop", Integer.toString(pop[i]));
			//tmnŰ���� �Ϸ� �����µ�
			map.put("tmn", String.valueOf(tmn[i]));
			//tmnŰ���� �Ϸ� �ְ�µ�
			map.put("tmx", String.valueOf(tmx[i]));
			//����Ʈ�� ����ش�.
			item.add(map);
		}
		for (int i = 0; i < item.size(); i++) {
			for (Entry<String, String> elem : item.get(i).entrySet()) {
				System.out.println(String.format("key : %s, value : %s", elem.getKey(), elem.getValue()));
			}
		}
		
		jsonObj.put("item", item);
		
		return jsonObj;
	}
}
