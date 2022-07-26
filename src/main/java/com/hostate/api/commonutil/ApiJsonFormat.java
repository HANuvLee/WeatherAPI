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
		
		// 날짜별 프론트에 보여질 JSON형태의 Hashmap값들을 담을 리스트, HashMap key:String / Value:Object
		ArrayList<HashMap<String, String>> item = new ArrayList<HashMap<String, String>>();
		// result가져오기
		JSONObject parse_result = (JSONObject) jsonObj.get("result");
		// response 가져오기
		JSONObject parse_response = (JSONObject) parse_result.get("response");
		// response로부터 body 찾아오기
		JSONObject parse_body = (JSONObject) parse_response.get("body");
		// body 로 부터 items 받아오기
		JSONObject parse_items = (JSONObject) parse_body.get("items");
		// items로 부터 item을 받아옵니다. item : 뒤에 [ 로 시작하므로 jsonarray입니다.
		JSONArray parse_item = (JSONArray) parse_items.get("item");
		
		// 조회날짜 추출
		String startDate = searchInfo.getStart_date();
		String endDate = searchInfo.getEnd_date();

		// 조회날짜를 int타입으로 형변환, 반복문 사용 시 조회범위를 구하기 위함
		int st = Integer.parseInt(startDate);
		int ed = Integer.parseInt(endDate);

		String[] date = new String[ed - st + 1];
		// 하루치 날씨 상태값을 담을 배열
		int[] sky = new int[ed - st + 1];
		// 하루치 강수확률을 담을 배열
		int[] pop = new int[ed - st + 1];
		// 최저온도
		double[] tmn = new double[ed - st + 1];
		// 최고온도
		double[] tmx = new double[ed - st + 1];
		
		//시작날짜부터 끝날짜 범위만큼 반복
		for (int i = 0; i <= ed - st; i++) {
			System.out.println("Top For" + i);
			//하늘 및 강수확률의 전체값을 나누기 위한 제수
			int num = 0;
			//하늘값의 전체 합
			double skyVal = 0;
			//강수화률의 전체 합
			double popVal = 0;
			for (int j = 0; j < parse_item.length(); j++) {
				JSONObject value = (JSONObject) parse_item.get(j);
				//JSON데이터 중 카테고리가 하늘이고 날짜가 조회시작날짜부터 끝날짜까지의 하루치씩 SKY값을 구하여 더한다
				if (value.get("category").equals("SKY") && value.get("fcstDate").equals(Integer.toString(st + i))) {
					skyVal += Integer.parseInt((String) value.get("fcstValue"));
					num += 1;
				}
				//JSON데이터 중 카테고리가 강수확률이고 날짜가 조회시작날짜부터 끝날짜까지의 하루치씩 POP값을 구하여 더한다
				if (value.get("category").equals("POP") && value.get("fcstDate").equals(Integer.toString(st + i))) {
					popVal += Integer.parseInt((String) value.get("fcstValue"));
				}
				//JSON데이터 중 카테고리가 최저온도이고 날짜가 조회시작날짜부터 끝날짜까지의 하루치씩 TMN값을 구하여 더한다
				if (value.get("category").equals("TMN") && value.get("fcstDate").equals(Integer.toString(st + i))) {
					//tmn배열에 대입
					tmn[i] = Double.parseDouble((String) value.get("fcstValue"));
				}
				//JSON데이터 중 카테고리가 최고온도이고 날짜가 조회시작날짜부터 끝날짜까지의 하루치씩 TMX값을 구하여 더한다
				if (value.get("category").equals("TMX") && value.get("fcstDate").equals(Integer.toString(st + i))) {
					//tmx배열에 대입
					tmx[i] = Double.parseDouble((String) value.get("fcstValue"));
				}
			}
			//sky 배열에 대입
			sky[i] = (int) Math.round(skyVal / num);
			//pop배열에 대입
			pop[i] = (int) Math.round(popVal / num);
		}

		// 날짜별 데이터를 담는 hashmap HashMap<String, Object>
		for (int i = 0; i <= ed - st; i++) {
			// Date키값에 시작날짜부터 끝날짜까지 담는다.
			HashMap<String, String> map = new HashMap<String, String>();
			//date키값과 날짜를 담는다
			map.put("date", Integer.toString(st + i));
			//sky키값  하루치sky평균값
			map.put("sky", Integer.toString(sky[i]));
			//pop키값  하루치 강수확률 평균값
			map.put("pop", Integer.toString(pop[i]));
			//tmn키값과 하루 최저온도
			map.put("tmn", String.valueOf(tmn[i]));
			//tmn키값과 하루 최고온도
			map.put("tmx", String.valueOf(tmx[i]));
			//리스트에 담아준다.
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
