<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<script type="text/javascript" src="https://code.jquery.com/jquery-3.5.1.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<link href="/resources/css/mainpage.css" rel="stylesheet">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<title>list</title>
</head>
<body>
<nav class="parentheader">
	<div class="container-fluid header">
	
		<a class="navbar-brand logo">HOSTATE</a>
 		<div class="collapse navbar-collapse" id="navbarSupportedContent">
	 	 	<c:if test="${sessionScope.user_id == null}">
	     		 <a class="navbar-brand nav-link active logstate" href="/login/login.do">로그인</a>
	     	</c:if>
	     	<c:if test="${sessionScope.user_id != null}">
				<a class="navbar-brand nav-link active logstate" href="/login/logout.do" class="logoutbtn">로그아웃</a>
			</c:if>
    	</div>
	</div>
</nav>
	<div class="headerContent">
		<c:if test="${sessionScope.user_id != null}">
	 		<span class="headerContentText">
	 			아래 검색기능을 통해 원하는 날의 날씨를 조회해보세요.
	 		</span>
 		</c:if>
 		<c:if test="${sessionScope.user_id == null}">
	 		<span class="headerContentText">
	 			온라인으로 <a href="/login/login.do">로그인</a>하고, 날씨를 조회해보세요.
	 		</span>
 		</c:if>
 	</div>
	<div class="contents" id="contents">
		<div id="clockdate">
			<div class="clockdate-wrapper">
				<div id="clock"></div>
				<div id="date"></div>
			</div>
		</div>
		<div class="weatherContents">
	
		</div>
	</div>
	<c:if test="${sessionScope.user_id != null}">
	<div class="search">
   		<label for="start">Start date:</label>
		<input type="date" id="startdate" name="start_date">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   		<label for="start">End date:</label>
		<input type="date" id="enddate" name="end_date">
		&nbsp;&nbsp;&nbsp;&nbsp;
     	<button id="searcWeatherBtn" class="btn btn-light">search</button>  	
   		<div class="guidetext">
			*금일부터 최대 <b>7</b>일까지 조회가능합니다.
		</div>
	</div>
	</c:if>
	<c:if test="${sessionScope.user_id == null}">
	<div class="search">
		<form class="serarchform">
    		<label for="start">Start date:</label>
			<input type="date" id="startdate" name="trip-start" value="2022-07-15" min="2022-07-15" max="2022-07-22" disabled="disabled">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    		<label for="start">End date:</label>
			<input type="date" id="enddate" name="trip-start" value="2022-07-15" min="2022-07-15" max="2022-07-22" disabled="disabled">
			&nbsp;&nbsp;&nbsp;&nbsp;
      		<button type="button" id="searcWeatherBtn" class="btn btn-light" disabled="disabled">search</button>
   		</form>
   		<div class="guidetext">
			*금일부터 최대 <b>7</b>일까지 조회가능합니다.
		</div>
	</div>
	</c:if>
</body>
<script type="text/javascript" charset="utf-8">
$("document").ready(function() {
	startTime(); //메인 페이지 타이머 생성
	setCalendar();//달력 범위 설정
	firstvilageweather(); //페이지 최초 접속 시 API 요청함수
	
	
	function setCalendar() {
		var toDay = getToday(); //yyyymmdd 형식
		var toDay2 = getToday2(); //yyyy-mm-dd형식
		var maxdd = parseInt(toDay) + 7; // 금일 + 7
		var toDayNum = parseInt(toDay);
		
		maxdd = String(maxdd);
		
		//조회시작날짜 속성 설정
		$("#startdate").attr("value", toDay2);
		$("#startdate").attr("min", toDay2);
		$("#startdate").attr("max", toDay2.substr(0,8) + maxdd.substr(6));
		//조회끝날짜 속성 설정
		$("#enddate").attr("value", toDay2);
		$("#enddate").attr("min", toDay2);
		$("#enddate").attr("max", toDay2.substr(0,8) + maxdd.substr(6));
		
		//조회버튼 클릭 시
		$("#searcWeatherBtn").click(function() {
			var start_date = parseInt($("#startdate").val().replace(/\-/g, "")); //"-"문자를 모두제거하는 정규식
			var end_date = parseInt($("#enddate").val().replace(/\-/g, "")); //"-"문자를 모두제거하는 정규식
			var num = end_date - start_date //정수형으로 변한 두 날짜의 값의 차이를 구한다.
			
			//조회날짜 기준에 따른 api호출 리스트
			if(start_date > end_date){ 
				alert("plz chk your date state !!");
				return false;
			}else if(start_date-toDayNum < 3 && end_date-toDayNum < 3){
				alert("short!");
				searchShortweather(String(start_date), String(end_date)); //단기예보만 호출
			}else if(start_date-toDayNum > 2 && end_date-toDayNum > 2){
				alert("middle!");
				searchMidweather(String(start_date), String(end_date)); //중기예보만호출
			}else{
				alert("All!");
				searchAllweather(String(start_date), String(end_date)) //모두호출
			}
	    });
	};
		
/**************************************최초 접속 시 호출되는 함수******************************************/
	function firstvilageweather() {
		$.ajax({			
			type: 'get',
			url: '/api/searchShortweather.do',
			timeout : 60000,
			contentType: 'application/json',
			dataType: 'json',
			success: function(data, status, xhr) {
				
				let dataHeader = data.result.response.header.resultCode;
				console.log(data);
				let item = data.result.response.body.items.item;
						
				if(dataHeader == "00"){ //최초 접속 시 api 데이터 전달이 성공된다면
					console.log("firsthvilageweather success ==>");
					
					main(item); //응답받은 데이터를 인자값으로 메인 페이지 생성 함수 호출

				}else{
					console.log("firsthvilageweather fail ==>");
					console.log("firsthvilageweather" + item);
				}
			},
			error: function(e, status, xhr, data) {
				console.log("error ==>");
				console.log(e);
			}
		});
	}
	
	//단기예보만 호출
	function searchShortweather(st, ed) {
		console.log("searchShortweather start");
		$.ajax({
			type: 'get',
			url: '/api/searchShortweather.do',
			data:{
				"start_date" : st,
				"end_date" : ed
			},
			async: false,
			timeout : 30000,
			contentType: 'application/json',
			dataType: 'json',
			success: function(data, status, xhr) {
				
				let dataHeader = data.result.response.header.resultCode;
				let sItem = data.result.response.body.items.item;

				if (dataHeader == "00"){
					console.log("success ==>");
					console.log(data);
					
					
				}else{
					console.log("fail ==>");
					console.log(data);
					
				}
			},
			error: function(e, status, xhr, data) {
				console.log("error ==>");
				console.log(e);
			}
		});
	}

	//중기예보만 호출
	function searchMidweather(st, ed) {
		$.ajax({
			type: 'get',
			url: '/api/searchmidtaweather.do',
			data:{
				"start_date" : st,
				"end_date" : ed
			},
			async: false,
			timeout : 30000,
			contentType: 'application/json',
			dataType: 'json',
			success: function(data1, status, xhr) {
				let dataHeader1 = data1.result.response.header.resultCode;
				//--------------중기기온------------//
				if (dataHeader1 == "00"){
					console.log("searchmidtaweather success ==>");
					//--------------중기육상------------//
				}else{
					console.log("fail ==>");
					console.log(data1);
				}
			},
			error: function(e, status, xhr, data) {
				console.log("error ==>");
				console.log(e);
			}	
		});
	}
	//단기 중기 모두 호출
	function searchAllweather(st, ed) {
		//--------------------중기기온예보호출--------------------//
		$.ajax({
			type: 'get',
			url: '/api/searchmidtaweather.do',
			data:{
				"start_date" : st,
				"end_date" : ed
			},
			async: false,
			timeout : 30000,
			contentType: 'application/json',
			dataType: 'json',
			success: function(data1, status, xhr) {
				
			},
			error: function(e, status, xhr, data) {
				console.log("error ==>");
				console.log(e);
			}
		});
	}
	/***************************************메인 페이지 생성 함수*******************************************/
	function main(item) {
		 console.log("main function Start.");
		 
		//조회 시시작날짜와 끝날짜를 구한다.
		const st = $('#startdate').val();
		const ed = $('#enddate').val(); 
		//조회날짜의 범위를 구하기 위한 형변환과 정수로 타입 변환 
		const startDate = parseInt(st.replace(/\-/g,'')); 
		const endDate = parseInt(st.replace(/\-/g,''));
		const scope = endDate - startDate;
		
		let sky = [];
		let pop = [];
		let tmn = 0;
		let tmx = 0;
		let fcstdate;
		
		console.log("scope범위" + scope);
		
		if(item.length != 0){ //최초 접속 시 api 데이터가 성공적으로 전달될 때
			console.log("main function success ==>");
		
		for(let i in item){
			if(item[i].category == "SKY" && item[i].fcstDate == String(startDate)){
				sky.push(parseInt(item[i].fcstValue));
				fcstdate = item[i].fcstDate;
			}else if(item[i].category == "POP" && item[i].fcstDate == String(startDate)){
				pop.push(parseInt(item[i].fcstValue));
			}else if(item[i].category == "TMN" && item[i].fcstDate == String(startDate)){
				tmn = parseInt(item[i].fcstValue);
			}else if(item[i].category == "TMX" && item[i].fcstDate == String(startDate)){
				tmx = parseInt(item[i].fcstValue);
			}
		}
		//sky평균 계산
		let sum = 0;
		for(let i of sky){
			sum += i;
		}
		let skyAvg= sum / sky.length;
		//반올림
		skyAvg = Math.round(skyAvg); 
		if(skyAvg == 1){
			skyAvg = "맑음";
		}else if(skyAvg == 3){
			skyAvg = "구름많음";
		}else if(skyAvg == 4){
			skyAvg = "흐림";
		}
		
		//pop평균계산
		sum = 0; //다른 카테고리 평균값을 구하기 위해 sum초기화
		for(let i of pop){
			sum += i;
		}
        let popAvg= sum / pop.length;
		//반올림
		popAvg = Math.round(popAvg); 
		
		//날짜 변환
		let mm = fcstdate.substr(4);
		let dd =".";
		let position = 2;
		let output = [mm.slice(0, position), dd, mm.slice(position)].join('');

		
		let formHtml = "";
		
	   	for(let i = 0; i <= scope; i++){
			formHtml += "<div class=weatherForm id=weatherForm>";
			formHtml += "<span id=fcstTime>";
			formHtml += output;
			formHtml += "</span>";
			formHtml += "<span class=weatherPng id=weatherPng>"; //날짜
			formHtml += "</span>";
			formHtml += "<div class=SKY, id=SKY>날씨 :"+skyAvg+"</div>";//하늘
			formHtml += "<div class=POP, id=POP>강수확률 :"+popAvg+"%</div>"; //강수확률
			formHtml += "<div class=TMN, id=TMN>최저기온 :"+tmn+"</div>"; //최저기온
			formHtml += "<div class=TMX, id=TMX>최고기온 :"+tmx+"</div>"; //최고기온
			formHtml += "</div>";	
	    }
		$('.weatherContents').html(formHtml); 
				
		}else{
			console.log("main function fail");
			console.log(data);
		}
	}
	

	/***************************************타이머 생성 함수*******************************************/
	 function startTime() {
		    var today = new Date();
		    var hr = today.getHours();
		    var min = today.getMinutes();
		    var sec = today.getSeconds();
		    ap = (hr < 12) ? "<span>AM</span>" : "<span>PM</span>";
		    hr = (hr == 0) ? 12 : hr;
		    hr = (hr > 12) ? hr - 12 : hr;
		    //Add a zero in front of numbers<10
		    hr = checkTime(hr);
		    min = checkTime(min);
		    sec = checkTime(sec);
		    document.getElementById("clock").innerHTML = hr + ":" + min + ":" + sec + " " + ap;
		    
		    var months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
		    var days = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
		    var curWeekDay = days[today.getDay()];
		    var curDay = today.getDate();
		    var curMonth = months[today.getMonth()];
		    var curYear = today.getFullYear();
		    var date = curWeekDay+", "+curDay+" "+curMonth+" "+curYear;
		    document.getElementById("date").innerHTML = date;
		    
		    var time = setTimeout(function(){ startTime() }, 500);
		}
		function checkTime(i) {
		    if (i < 10) {
		        i = "0" + i;
		    }
		    return i;
		}
		
		/***************************************오늘날짜 (YYYYMMDD) 생성 함수*******************************************/
		function getToday(){
		    var date = new Date();
		    var year = date.getFullYear();
		    var month = ("0" + (1 + date.getMonth())).slice(-2);
		    var day = ("0" + date.getDate()).slice(-2);

		    return year + month + day;
		}
		/***************************************오늘날짜 (YYYY-MM-DD) 생성 함수*******************************************/
		function getToday2(){
		    var date = new Date();
		    var year = date.getFullYear();
		    var month = ("0" + (1 + date.getMonth())).slice(-2);
		    var day = ("0" + date.getDate()).slice(-2);

		    return year + "-" + month + "-" + day;
		}
});
</script>
</html>