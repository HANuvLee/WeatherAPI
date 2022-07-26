package com.hostate.api.commonutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import java.util.Locale.Category;


import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hostate.api.vo.Tb_weather_search_scope_info;

@Component
public class ApiJsonFormat {
	
	@Autowired
	JsonParsing jsonParsing;
	
	//�ܱ⿹�� json ������ ����
	public JSONObject shortWeather(JSONObject jsonObj, Tb_weather_search_scope_info searchInfo) {

		// ��¥�� ����Ʈ�� ������ JSON������ ������ ���� ����Ʈ, HashMap key:String / Value:String
		ArrayList<HashMap<String, String>> item = new ArrayList<HashMap<String, String>>();
		
		JSONArray parse_item = jsonParsing.parse(jsonObj);
	
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
		
		JSONArray data = new JSONArray();
	      for(int i = 0; i < item.size(); i++) {
	    	  data.put(item.get(i));
	      }
	      
	    JSONObject obj = new JSONObject();
	    obj.put("Data Names:", data);
	    
	    return obj;
	}

	//�߱��� & ���󿹺���ȸ
	public JSONObject midWeather(JSONObject jsonObj, JSONObject jsonObj2, Tb_weather_search_scope_info searchInfo) {
	
		// ��¥�� ����Ʈ�� ������ JSON������ ������ ���� ����Ʈ, HashMap key:String / Value:String
		ArrayList<HashMap<String, String>> item = new ArrayList<HashMap<String, String>>();
		
		//�߱�����ȸ�Ľ�
		JSONArray parse_item = jsonParsing.parse(jsonObj);
		//�߱�������ȸ�Ľ�
		JSONArray parse_item2 = jsonParsing.parse(jsonObj2);
		
		// ��ȸ��¥ ����
		String startDate = searchInfo.getStart_date();
		String endDate = searchInfo.getEnd_date();
		
		// ��ȸ��¥�� intŸ������ ����ȯ, �ݺ��� ��� �� ��ȸ������ ���ϱ� ����
		int st = Integer.parseInt(startDate);
		int ed = Integer.parseInt(endDate);
		
		String[] date = new String[ed - st + 1];
		//3�� ���� �����µ��� ���� �迭
		int[] tamin = new int[ed - st + 1];
		//3�� ���� �ְ�µ��� ���� �迭
		int[] tamax = new int[ed - st + 1];
		//3�� ���� ����Ȯ��
		int[] rnst = new int[ed - st + 1];
		//3�� ���� ����
		int[] wf = new int[ed - st + 1];
		
		for(int i = 0; i <= ed-st; i++) {
			for(int j = 0; j<parse_item.length(); j++) {
				JSONObject value = (JSONObject) parse_item.get(j);
				System.out.println(value);
			}
		}
				

		
		
		return null;
	}

}
