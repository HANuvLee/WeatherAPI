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
	
	//단기예보 json 데이터 가공
	public JSONObject shortWeather(JSONObject jsonObj, Tb_weather_search_scope_info searchInfo) {

		// 날짜별 프론트에 보여질 JSON형태의 값들을 담을 리스트, HashMap key:String / Value:String
		ArrayList<HashMap<String, String>> item = new ArrayList<HashMap<String, String>>();
		
		JSONArray parse_item = jsonParsing.parse(jsonObj);
	
		// 조회날짜 추출
		String startDate = searchInfo.getStart_date();
		String endDate = searchInfo.getEnd_date();

		//조회범위 list값 : 조회시작날짜부터 끝날짜까지 , list크기: 조회범위
		List<LocalDate>betweenDate = datesBetween.getBetweenDate(startDate, endDate);

		// 하루치 날씨 상태값을 담을 배열
		int[] sky = new int[betweenDate.size()];
		// 하루치 강수확률을 담을 배열
		int[] pop = new int[betweenDate.size()];
		// 최저온도
		int[] tmn = new int[betweenDate.size()];
		// 최고온도
		int[] tmx = new int[betweenDate.size()];
		
		//시작날짜부터 끝날짜범위만큼 반복
		for (int i = 0; i < betweenDate.size(); i++) {
			//하늘 및 강수확률의 전체값을 나누기 위한 제수
			int num = 0;
			//하늘값의 전체 합
			double skySum = 0;
			//강수화률의 전체 합
			double popSum = 0;
			for (int j = 0; j < parse_item.length(); j++) {
				JSONObject value = (JSONObject) parse_item.get(j);
				//JSON데이터 중 카테고리가 하늘이고 날짜가 조회시작날짜부터 끝날짜까지의 하루치씩 SKY값을 구하여 더한다
				if (value.get("category").equals("SKY") && value.get("fcstDate").equals(betweenDate.get(i).toString().replaceAll("[^0-9]",""))) {
					skySum += Integer.parseInt((String) value.get("fcstValue"));
					num += 1;
				}
				//JSON데이터 중 카테고리가 강수확률이고 날짜가 조회시작날짜부터 끝날짜까지의 하루치씩 POP값을 구하여 더한다
				if (value.get("category").equals("POP") && value.get("fcstDate").equals(betweenDate.get(i).toString().replaceAll("[^0-9]",""))) {
					popSum += Integer.parseInt((String) value.get("fcstValue"));
				}
				//JSON데이터 중 카테고리가 최저온도이고 날짜가 조회시작날짜부터 끝날짜까지의 하루치씩 TMN값을 구하여 더한다
				if (value.get("category").equals("TMN") && value.get("fcstDate").equals(betweenDate.get(i).toString().replaceAll("[^0-9]",""))) {
					//tmn배열에 대입
					double tmnVal = Double.parseDouble((String) value.get("fcstValue"));
					tmn[i] =  (int) Math.round(tmnVal);
				}
				//JSON데이터 중 카테고리가 최고온도이고 날짜가 조회시작날짜부터 끝날짜까지의 하루치씩 TMX값을 구하여 더한다
				if (value.get("category").equals("TMX") && value.get("fcstDate").equals(betweenDate.get(i).toString().replaceAll("[^0-9]",""))) {
					//tmx배열에 대입
					double tmxVal = Double.parseDouble((String) value.get("fcstValue"));
					tmx[i] =  (int) Math.round(tmxVal);
				}
			}
			//sky의 평균값을 구한 후 소수점을 정수로 반올림 후 배열에 대입
			sky[i] = (int) Math.round(skySum / num);
			//pop의 평균값을 구한 후 소수점을 정수로 반올림 후 배열에 대입
			pop[i] = (int) Math.round(popSum / num);
		}
		
		// 날짜별 데이터를 담는 hashmap HashMap<String, Object>
		for (int i = 0; i <betweenDate.size(); i++) {
			// Date키값에 시작날짜부터 끝날짜까지 담는다.
			HashMap<String, String> map = new HashMap<String, String>();
			//date키값과 날짜를 담는다
			map.put("date", (betweenDate.get(i).toString().substring(5).replaceAll("[^0-9]",".")));
			//sky키값  하루치sky평균값 + 강수형태 평균값
			map.put("sky", String.valueOf(sky[i]));
			//pop키값  하루치 강수확률 평균값
			map.put("pop", String.valueOf(pop[i]));
			//tmn키값과 하루 최저온도
			map.put("tmn", String.valueOf(tmn[i]));
			//tmn키값과 하루 최고온도
			map.put("tmx", String.valueOf(tmx[i]));
			//리스트에 담아준다.
			item.add(map);
		}
		System.out.println("================================단기예보 가공데이터================================");
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

	//중기기온 & 육상예보조회
	public JSONObject midWeather(JSONObject jsonObj, JSONObject jsonObj2, Tb_weather_search_scope_info searchInfo) throws ParseException {
	
		// 날짜별 프론트에 보여질 JSON형태의 값들을 담을 리스트, HashMap key:String / Value:String
		ArrayList<HashMap<String, String>> item = new ArrayList<HashMap<String, String>>();
		
		//중기기온조회파싱
		JSONArray parse_item = jsonParsing.parse(jsonObj);
		//중기육상조회파싱
		JSONArray parse_item2 = jsonParsing.parse(jsonObj2);
		
		//조회날짜 추출
		String startDate = searchInfo.getStart_date();
		String endDate = searchInfo.getEnd_date();
		
		//시작날짜와 끝날짜 사이의 날짜 값들을 리스트에 담는다. for문 반목 시 해당 리스트 사이즈 이용 
		List<LocalDate>betweenDate = datesBetween.getBetweenDate(startDate, endDate);
	
		//금일날짜와 중기예보조회시 조회시작날짜의 차이를 구하는 함수(중기요청데이터의 형식에 맞추기 위함)
		int diffDays = datesBetween.getDiffDays(startDate, endDate);
		int diffDays2 = datesBetween.getDiffDays(startDate, endDate);
						
		//3일 이후 최저온도를 담을 배열
		String[] tamin = new String[betweenDate.size()];
		//3일 이후 최고온도를 담을 배열
		String[] tamax = new String[betweenDate.size()];
		//3일 이후 강수확률
		int[] rnst = new int[betweenDate.size()];
		//3일 이후 날씨
		int[] wf = new int[betweenDate.size()];
		
		//중기기온조회의 데이터값을 추출한 후 배열에 담아준다. (최대온도, 최소온도)
		for(int i = 0; i < betweenDate.size(); i++) {
			diffDays += 1;
			for(int j = 0; j<parse_item.length(); j++) {
				JSONObject value = (JSONObject) parse_item.get(j);
				tamax[i] = String.valueOf(value.get("taMax"+diffDays+"")); 
				tamin[i] = String.valueOf(value.get("taMin"+diffDays+""));
			}
		}
		
		//중기육상조회의 데이터값을 추출한 후 배열에 담아준다. (강수확률, 날씨상태)
		for(int i = 0; i < betweenDate.size(); i++) {
			diffDays2 += 1; //금일로부터 3후 데이타 조회를 위한 변수선언 반복하며 1씩증가
			int num = 0;
			//강수확률 합계
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
				//금일부터 diffDays 이후 오전 날씨상태
				if(value.has("wf"+diffDays2+"Am")) {
					num += 1;
					if(value.get("wf"+diffDays2+"Am").toString().contains("맑")) {
						wfSum += 1;
					}if(value.get("wf"+diffDays2+"Am").toString().contains("흐")) {
						wfSum += 3;
					}if(value.get("wf"+diffDays2+"Am").toString().contains("구")) {
						wfSum += 4;
					}
				}
				//금일부터 diffDays 이후 오후 날씨상태
				if(value.has("wf"+diffDays2+"Pm")) {
					if(value.get("wf"+diffDays2+"Pm").toString().contains("맑")) {
						wfSum += 1;
					}if(value.get("wf"+diffDays2+"Pm").toString().contains("흐")) {
						wfSum += 3;
					}if(value.get("wf"+diffDays2+"Pm").toString().contains("구")) {
						wfSum += 4;
					}
				}
			}
			//강수확률 평균값 가공 후 배열에 대입
			rnst[i] = (int) Math.round(rnstSum / num);
			//기상상태 평균값 가공 후 배열에 대입
			wf[i] = (int) Math.round(wfSum / num);
		}
		
		// 날짜별 데이터를 담는 hashmap HashMap<String, Object>
		for (int i = 0; i <betweenDate.size(); i++) {
			// Date키값에 시작날짜부터 끝날짜까지 담는다.
			HashMap<String, String> map = new HashMap<String, String>();
			//date키값과 날짜를 담는다
			map.put("date", (betweenDate.get(i).toString().substring(5).replaceAll("[^0-9]",".")));
			//sky키값  하루치sky평균값 + 강수형태 평균값
			map.put("sky", Integer.toString(wf[i]));
			//pop키값  하루치 강수확률 평균값
			map.put("pop", Integer.toString(rnst[i]));
			//tmn키값과 하루 최저온도
			map.put("tmn", String.valueOf(tamin[i]));
			//tmn키값과 하루 최고온도
			map.put("tmx", String.valueOf(tamax[i]));
			//리스트에 담아준다.
			item.add(map);
		}
		
		System.out.println("================================중기예보 가공데이터================================");
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
