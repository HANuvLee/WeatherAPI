package com.hostate.api.commonutil;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import java.util.Locale.Category;


import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JacksonInject.Value;
import com.hostate.api.vo.Tb_weather_search_scope_info;

@Component
public class ApiJsonFormat {
	
	@Autowired
	JsonParsing jsonParsing;
	
	@Autowired
	DatesBetweenTwoDates datesBetween;
	
	//�ܱ⿹�� json ������ ����
	public JSONObject shortWeather(JSONObject jsonObj, Tb_weather_search_scope_info searchInfo) {

		// ��¥�� ����Ʈ�� ������ JSON������ ������ ���� ����Ʈ, HashMap key:String / Value:String
		ArrayList<HashMap<String, String>> item = new ArrayList<HashMap<String, String>>();
		
		JSONArray parse_item = jsonParsing.parse(jsonObj);
	
		// ��ȸ��¥ ����
		String startDate = searchInfo.getStart_date();
		String endDate = searchInfo.getEnd_date();

		//��ȸ���� list�� : ��ȸ���۳�¥���� ����¥���� , listũ��: ��ȸ����
		List<LocalDate>betweenDate = datesBetween.getBetweenDate(startDate, endDate);

		// �Ϸ�ġ ���� ���°��� ���� �迭
		int[] sky = new int[betweenDate.size()];
		// �Ϸ�ġ ����Ȯ���� ���� �迭
		int[] pop = new int[betweenDate.size()];
		// �����µ�
		int[] tmn = new int[betweenDate.size()];
		// �ְ�µ�
		int[] tmx = new int[betweenDate.size()];
		
		//���۳�¥���� ����¥������ŭ �ݺ�
		for (int i = 0; i < betweenDate.size(); i++) {
			//�ϴ� �� ����Ȯ���� ��ü���� ������ ���� ����
			int num = 0;
			//�ϴð��� ��ü ��
			double skySum = 0;
			//����ȭ���� ��ü ��
			double popSum = 0;
			for (int j = 0; j < parse_item.length(); j++) {
				JSONObject value = (JSONObject) parse_item.get(j);
				//JSON������ �� ī�װ��� �ϴ��̰� ��¥�� ��ȸ���۳�¥���� ����¥������ �Ϸ�ġ�� SKY���� ���Ͽ� ���Ѵ�
				if (value.get("category").equals("SKY") && value.get("fcstDate").equals(betweenDate.get(i).toString().replaceAll("[^0-9]",""))) {
					skySum += Integer.parseInt((String) value.get("fcstValue"));
					num += 1;
				}
				//JSON������ �� ī�װ��� ����Ȯ���̰� ��¥�� ��ȸ���۳�¥���� ����¥������ �Ϸ�ġ�� POP���� ���Ͽ� ���Ѵ�
				if (value.get("category").equals("POP") && value.get("fcstDate").equals(betweenDate.get(i).toString().replaceAll("[^0-9]",""))) {
					popSum += Integer.parseInt((String) value.get("fcstValue"));
				}
				//JSON������ �� ī�װ��� �����µ��̰� ��¥�� ��ȸ���۳�¥���� ����¥������ �Ϸ�ġ�� TMN���� ���Ͽ� ���Ѵ�
				if (value.get("category").equals("TMN") && value.get("fcstDate").equals(betweenDate.get(i).toString().replaceAll("[^0-9]",""))) {
					//tmn�迭�� ����
					double tmnVal = Double.parseDouble((String) value.get("fcstValue"));
					tmn[i] =  (int) Math.round(tmnVal);
				}
				//JSON������ �� ī�װ��� �ְ�µ��̰� ��¥�� ��ȸ���۳�¥���� ����¥������ �Ϸ�ġ�� TMX���� ���Ͽ� ���Ѵ�
				if (value.get("category").equals("TMX") && value.get("fcstDate").equals(betweenDate.get(i).toString().replaceAll("[^0-9]",""))) {
					//tmx�迭�� ����
					double tmxVal = Double.parseDouble((String) value.get("fcstValue"));
					tmx[i] =  (int) Math.round(tmxVal);
				}
			}
			//sky�� ��հ��� ���� �� �Ҽ����� ������ �ݿø� �� �迭�� ����
			sky[i] = (int) Math.round(skySum / num);
			//pop�� ��հ��� ���� �� �Ҽ����� ������ �ݿø� �� �迭�� ����
			pop[i] = (int) Math.round(popSum / num);
		}
		
		// ��¥�� �����͸� ��� hashmap HashMap<String, Object>
		for (int i = 0; i <betweenDate.size(); i++) {
			// DateŰ���� ���۳�¥���� ����¥���� ��´�.
			HashMap<String, String> map = new HashMap<String, String>();
			//dateŰ���� ��¥�� ��´�
			map.put("date", (betweenDate.get(i).toString().substring(5).replaceAll("[^0-9]",".")));
			//skyŰ��  �Ϸ�ġsky��հ� + �������� ��հ�
			map.put("sky", String.valueOf(sky[i]));
			//popŰ��  �Ϸ�ġ ����Ȯ�� ��հ�
			map.put("pop", String.valueOf(pop[i]));
			//tmnŰ���� �Ϸ� �����µ�
			map.put("tmn", String.valueOf(tmn[i]));
			//tmnŰ���� �Ϸ� �ְ�µ�
			map.put("tmx", String.valueOf(tmx[i]));
			//����Ʈ�� ����ش�.
			item.add(map);
		}
		System.out.println("================================�ܱ⿹�� ����������================================");
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
	    obj.put("list", data);
	    
	    return obj;
	}

	//�߱��� & ���󿹺���ȸ
	public JSONObject midWeather(JSONObject jsonObj, JSONObject jsonObj2, Tb_weather_search_scope_info searchInfo) throws ParseException {
	
		// ��¥�� ����Ʈ�� ������ JSON������ ������ ���� ����Ʈ, HashMap key:String / Value:String
		ArrayList<HashMap<String, String>> item = new ArrayList<HashMap<String, String>>();
		
		//�߱�����ȸ�Ľ�
		JSONArray parse_item = jsonParsing.parse(jsonObj);
		//�߱�������ȸ�Ľ�
		JSONArray parse_item2 = jsonParsing.parse(jsonObj2);
		
		//��ȸ��¥ ����
		String startDate = searchInfo.getStart_date();
		String endDate = searchInfo.getEnd_date();
		
		//���۳�¥�� ����¥ ������ ��¥ ������ ����Ʈ�� ��´�. for�� �ݸ� �� �ش� ����Ʈ ������ �̿� 
		List<LocalDate>betweenDate = datesBetween.getBetweenDate(startDate, endDate);
	
		//���ϳ�¥�� �߱⿹����ȸ�� ��ȸ���۳�¥�� ���̸� ���ϴ� �Լ�(�߱��û�������� ���Ŀ� ���߱� ����)
		int diffDays = datesBetween.getDiffDays(startDate, endDate);
		int diffDays2 = datesBetween.getDiffDays(startDate, endDate);
						
		//3�� ���� �����µ��� ���� �迭
		String[] tamin = new String[betweenDate.size()];
		//3�� ���� �ְ�µ��� ���� �迭
		String[] tamax = new String[betweenDate.size()];
		//3�� ���� ����Ȯ��
		int[] rnst = new int[betweenDate.size()];
		//3�� ���� ����
		int[] wf = new int[betweenDate.size()];
		
		//�߱�����ȸ�� �����Ͱ��� ������ �� �迭�� ����ش�. (�ִ�µ�, �ּҿµ�)
		for(int i = 0; i < betweenDate.size(); i++) {
			diffDays += 1;
			for(int j = 0; j<parse_item.length(); j++) {
				JSONObject value = (JSONObject) parse_item.get(j);
				tamax[i] = String.valueOf(value.get("taMax"+diffDays+"")); 
				tamin[i] = String.valueOf(value.get("taMin"+diffDays+""));
			}
		}
		
		//�߱�������ȸ�� �����Ͱ��� ������ �� �迭�� ����ش�. (����Ȯ��, ��������)
		for(int i = 0; i < betweenDate.size(); i++) {
			diffDays2 += 1; //���Ϸκ��� 3�� ����Ÿ ��ȸ�� ���� �������� �ݺ��ϸ� 1������
			int num = 0;
			//����Ȯ�� �հ�
			double rnstSum = 0;
			double wfSum = 0;
			for(int j = 0; j<parse_item2.length(); j++) {
				JSONObject value = (JSONObject) parse_item2.get(j);
				if(value.has("rnSt"+diffDays2+"Am")) {
					rnstSum += (int) value.get("rnSt"+diffDays2+"Am");
					num += 1;
				}
				if(value.has("rnSt"+diffDays2+"Pm")) {
					rnstSum += (int) value.get("rnSt"+diffDays2+"Pm");
				}
				//���Ϻ��� diffDays ���� ���� ��������
				if(value.has("wf"+diffDays2+"Am")) {
					num += 1;
					if(value.get("wf"+diffDays2+"Am").toString().contains("��")) {
						wfSum += 1;
					}if(value.get("wf"+diffDays2+"Am").toString().contains("��")) {
						wfSum += 3;
					}if(value.get("wf"+diffDays2+"Am").toString().contains("��")) {
						wfSum += 4;
					}
				}
				//���Ϻ��� diffDays ���� ���� ��������
				if(value.has("wf"+diffDays2+"Pm")) {
					if(value.get("wf"+diffDays2+"Pm").toString().contains("��")) {
						wfSum += 1;
					}if(value.get("wf"+diffDays2+"Pm").toString().contains("��")) {
						wfSum += 3;
					}if(value.get("wf"+diffDays2+"Pm").toString().contains("��")) {
						wfSum += 4;
					}
				}
			}
			//����Ȯ�� ��հ� ���� �� �迭�� ����
			rnst[i] = (int) Math.round(rnstSum / num);
			//������ ��հ� ���� �� �迭�� ����
			wf[i] = (int) Math.round(wfSum / num);
		}
		
		// ��¥�� �����͸� ��� hashmap HashMap<String, Object>
		for (int i = 0; i <betweenDate.size(); i++) {
			// DateŰ���� ���۳�¥���� ����¥���� ��´�.
			HashMap<String, String> map = new HashMap<String, String>();
			//dateŰ���� ��¥�� ��´�
			map.put("date", (betweenDate.get(i).toString().substring(5).replaceAll("[^0-9]",".")));
			//skyŰ��  �Ϸ�ġsky��հ� + �������� ��հ�
			map.put("sky", Integer.toString(wf[i]));
			//popŰ��  �Ϸ�ġ ����Ȯ�� ��հ�
			map.put("pop", Integer.toString(rnst[i]));
			//tmnŰ���� �Ϸ� �����µ�
			map.put("tmn", String.valueOf(tamin[i]));
			//tmnŰ���� �Ϸ� �ְ�µ�
			map.put("tmx", String.valueOf(tamax[i]));
			//����Ʈ�� ����ش�.
			item.add(map);
		}
		
		System.out.println("================================�߱⿹�� ����������================================");
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
	    obj.put("list", data);
		
		return obj;
	}

}
