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
		
		//현재날짜
		var now = new Date();
		
		
		
		//조회시작날짜 속성 설정
		$("#startdate").attr("value", toDay2);
		$("#startdate").attr("min", toDay2);
		$("#startdate").attr("max", toDay2.substr(0,8) + maxdd.substr(6));
		//조회끝날짜 속성 설정
		$("#enddate").attr("value", toDay2);
		$("#enddate").attr("min", toDay2);
		$("#enddate").attr("max", toDay2.substr(0,8) + maxdd.substr(6));
		
		console.log("이게 뭐시다냐 ==> "  + toDay2.substr(0,8) + maxdd.substr(6));
		
		//조회버튼 클릭 시
		$("#searcWeatherBtn").click(function() {
			var start_date = parseInt($("#startdate").val().replace(/\-/g, "")); //"-"문자를 모두제거하는 정규식
			var end_date = parseInt($("#enddate").val().replace(/\-/g, "")); //"-"문자를 모두제거하는 정규식
			
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
			contentType: 'application/json',
			dataType: 'json',
			success: function(data, status, xhr) {
					
				console.log("firsthvilageweather success ==>");
				console.log(data);
					
				main(data); //응답받은 데이터를 인자값으로 메인 페이지 생성 함수 호출	
			},
			error: function(e, status, xhr, data) {
				console.log("error ==>");
				console.log(e);
			}
		});
	}
	
/**************************************단기예보 호출 함수******************************************/
	function searchShortweather(st, ed) {
		console.log("searchShortweather start");
		$.ajax({
			type: 'get',
			url: '/api/searchShortweather.do',
			data:{
				"start_date" : st,
				"end_date" : ed
			},
			contentType: 'application/json',
			dataType: 'json',
			success: function(data, status, xhr) {
				console.log(data);
				main(data); 

			},
			error: function(e, status, xhr, data) {
				console.log("error ==>");
				console.log(e);
			}
		});
	}

	/**************************************중기예보 호출 함수******************************************/
	function searchMidweather(st, ed) {
		$.ajax({
			type: 'get',
			url: '/api/searchmidtaweather.do',
			data:{
				"start_date" : st,
				"end_date" : ed
			},
			contentType: 'application/json',
			dataType: 'json',
			success: function(data, status, xhr) {
				
				console.log(data);
				main(data); 
				
			},
			error: function(e, status, xhr, data) {
				console.log("error ==>");
				console.log(e);
			}	
		});
	}
	/**************************************단기&중기예보 호출 함수******************************************/
	function searchAllweather(st, ed) {
		$.ajax({
			type: 'get',
			url: '/api/searchAllweather.do',
			data:{
				"start_date" : st,
				"end_date" : ed
			},
			contentType: 'application/json',
			dataType: 'json',
			success: function(data, status, xhr) {
				
				console.log(data);
				main(data); 
				
			},
			error: function(e, status, xhr, data) {
				console.log("error ==>");
				console.log(e);
			}
		});
	}
	/***************************************메인 페이지 생성 함수*******************************************/
	function main(data) {
		 console.log("main function Start.");

		if(data.length != 0){ //최초 접속 시 api 데이터가 성공적으로 전달될 때
			console.log("main function success ==>");
			console.log(data);
			console.log("data length ==> " + data.list.length);
			
			let formHtml = "";
		
		    for(let i = 0; i < data.list.length; i++){
				formHtml += "<div class=weatherForm id=weatherForm>";
				formHtml += "<span id=fcstTime>"+data.list[i].date+"</span>";
				if(data.list[i].sky == 1){
					formHtml += "<div><img src='/resources/images/weather1.png' id=weatherPng"+data.list[i].sky+"></div>";
					formHtml += "<div class=SKY, id=SKY>날씨 : 맑음</div>";//하늘					
				}
				if(data.list[i].sky == 2){
					formHtml += "<div><img src='/resources/images/weather2.png' id=weatherPng"+data.list[i].sky+"></div>";
					formHtml += "<div class=SKY, id=SKY>날씨 : 구름조금</div>";//하늘					
				}if(data.list[i].sky == 3){
					formHtml += "<div><img src=/resources/images/weather3.png id=weatherPng"+data.list[i].sky+"></div>";
					formHtml += "<div class=SKY, id=SKY>날씨 : 구름많음</div>";//하늘					
				}
				if(data.list[i].sky == 4){
					formHtml += "<div><img src=/resources/images/weather4.png id=weatherPng"+data.list[i].sky+"></div>";
					formHtml += "<div class=SKY, id=SKY>날씨 : 흐림</div>";//하늘					
				}
				formHtml += "<div class=POP, id=POP>강수확률 : "+data.list[i].pop+"%</div>"; //강수확률
				formHtml += "<div class=TMN, id=TMN>최저기온 : "+data.list[i].tmn+"℃</div>"; //최저기온
				formHtml += "<div class=TMX, id=TMX>최고기온 : "+data.list[i].tmx+"℃</div>"; //최고기온
				formHtml += "</div>";
		    }$('.weatherContents').html(formHtml);
				
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