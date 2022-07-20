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
	firstvilageweather(); //페이지 접속 시 3시간 단위의 화면 호출
	
	function setCalendar() {
		var toDay = getToday(); //yyyymmdd 형식
		var toDay2 = getToday2(); //yyyy-mm-dd형식
		var maxdd = parseInt(toDay) + 7; // 금일 + 7
		
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
			var num = end_date - start_date
			
			//조회시작날짜와 끝날짜 검증
			if(num < 0){
				alert("시작날짜와 끝날짜를 확인하세요.");
			}else if(num == 0){
				alert("최소 1일 이상 조회해 주세요.");
			}else{
				alert("조회합니다.");
				searchvilageweather(start_date, end_date);
			}
	    });
	};
		
		/* console.log('"${sessionScope.user_id}"'); 세션 아이디*/
	
/***************************************오늘날짜 (YYYYMMDD) 생성 함수*******************************************/
	function getToday(){
	    var date = new Date();
	    var year = date.getFullYear();
	    var month = ("0" + (1 + date.getMonth())).slice(-2);
	    var day = ("0" + date.getDate()).slice(-2);

	    return year + month + day;
	}

	function getToday2(){
	    var date = new Date();
	    var year = date.getFullYear();
	    var month = ("0" + (1 + date.getMonth())).slice(-2);
	    var day = ("0" + date.getDate()).slice(-2);

	    return year + "-" + month + "-" + day;
	}

	/**************************************조회버튼 클릭 함수******************************************
	function serachClick() {
		var st = $("#startdate").val();
		var ed = $("#enddate").val();
		
		st = st.replace(/\-/g,"");
		ed = ed.replace(/\-/g,"");
		
		st = parseInt(st);
		ed = parseInt(ed);
		
		if(ed-st < 3){ //조회범위가 3일 미만인경우 단기예보만 조회 서비스 호출
			searchvilageweather(st, ed);
		}else{ //아니면 전부호출
			searchvilageweather(st, ed);
			searchmidtaweather(st, ed);
			searchmidlandweather(st, ed);
		}
	}*/
	
/**************************************최초 접속 시 3시간 단위의 단기예보조회******************************************/
	function firstvilageweather() {
		var toDay = getToday();
		$.ajax({			
			type: 'get',
			url: '/api/firsthvilageweather.do',
			data:{
				"start_date" : toDay
			},
			timeout : 60000,
			contentType: 'application/json',
			dataType: 'json',
			success: function(data, status, xhr) {
				
				let dataHeader = data.result.response.header.resultCode;
				let sItem = data.result.response.body.items.item;
				let formHtml = "";
				let objarr = new Array();
				
				for(let i in sItem){
					
				}

				for(var i = 0; i < 3; i++){
					objarr.push(i)
				};
					
				if (dataHeader == "00"){
					console.log("success ==>");
					console.log(data);
					
					let formHtml = "";
					for(let i in objarr){ //날씨 내용 폼 및 관련 태그생성
						formHtml += "<div class=weatherForm id=weatherForm>";
						formHtml += "<div class=weatherPng id=weatherPng><b id=fcstTime>" //날씨사진
						formHtml += "</b></div>"
						formHtml += "<div class=sky id=TMP></div>" //기온
						formHtml += "<div class=sky id=SKY>testtest</div>" //하늘
						formHtml += "<div class=pty id=PTY></div>" //강수형태
						formHtml += "<div class=pop id=POP></div>"; //강수확률
						formHtml += "<div class=reh id=REH></div>"; //습도
						formHtml += "<div class=reh id=VVV></div>"; //습도
						formHtml += "</div>";
					}
					$('.weatherContents').html(formHtml);
					
					//응답API 데이터를 폼태그 ID에 매치시켜 
					for(let i in sItem){
						if(sItem[i].category == "SKY"){ //카테고리가 SKY이라면(하늘상태)
							$("#SKY").attr("id", "SKY"+i+""); //폼태그안의 div태그 중 id가 SKY인 div에  items의 요소 번호 추가
							$("#fcstTime").attr("id", "fcstTime"+i+"");
							
							$("div[id=SKY"+i+"]").each(function(){//id값에 요소번호가 추가된 해당 태그의 텍스트값을 업데이트
								if(sItem[i].fcstValue == 1){
									$(this).text("날씨 : 맑음");
								}else if(sItem[i].fcstValue == 3){
									$(this).text("날씨 : 구름많음");
								}else if(sItem[i].fcstValue == 4){
									$(this).text("날씨 : 흐림");
								}
							});
							$("b[id=fcstTime"+i+"]").each(function(){//id값에 요소번호가 추가된 해당 태그의 텍스트값을 업데이트
								$(this).text(sItem[i].fcstTime); //시간 업데이트
							});
						}else if(sItem[i].category == "TMP"){ //카테고리가 SKY이라면(하늘상태)
							$("#TMP").attr("id", "TMP"+i+""); //폼태그안의 div태그 중 id가 SKY인 div에  items의 요소 번호 추가
							$("div[id=TMP"+i+"]").each(function(){ //id값에 요소번호가 추가된 해당 태그의 텍스트값을 업데이트
								$(this).text("기온 : " + sItem[i].fcstValue+"℃"); 
							});
						}else if(sItem[i].category == "PTY"){ //카테고리가 SKY이라면(하늘상태)
							$("#PTY").attr("id", "PTY"+i+""); //폼태그안의 div태그 중 id가 SKY인 div에  items의 요소 번호 추가
							$("div[id=PTY"+i+"]").each(function(){ //id값에 요소번호가 추가된 해당 태그의 텍스트값을 업데이트
								if(sItem[i].fcstValue == 0){
									$(this).text("강수형태 : 없음");
								}else if(sItem[i].fcstValue == 1){
									$(this).text("날씨 : 비");
								}else if(sItem[i].fcstValue == 2){
									$(this).text("날씨 : 비/눈");
								}else if(sItem[i].fcstValue == 3){
									$(this).text("날씨 : 눈");
								}else{
									$(this).text("날씨 : 소나기");
								}
							});
						}else if(sItem[i].category == "POP"){ //카테고리가 SKY이라면(하늘상태)
							$("#POP").attr("id", "POP"+i+""); //폼태그안의 div태그 중 id가 SKY인 div에  items의 요소 번호 추가
							$("div[id=POP"+i+"]").each(function(){ //id값에 요소번호가 추가된 해당 태그의 텍스트값을 업데이트
								$(this).text("강수확률 : " + sItem[i].fcstValue+"%"); 
							});
						}else if(sItem[i].category == "REH"){ //카테고리가 SKY이라면(하늘상태)
							console.log("2..REH");
							$("#REH").attr("id", "REH"+i+""); //폼태그안의 div태그 중 id가 SKY인 div에  items의 요소 번호 추가
							$("div[id=REH"+i+"]").each(function(){ //id값에 요소번호가 추가된 해당 태그의 텍스트값을 업데이트
								$(this).text("습도 : " + sItem[i].fcstValue+"%"); 
							});
						}
					}
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
/**************************************조회버튼 클릭 시 최대 3일치까지 단기예보조회******************************************/
	function searchvilageweather(st, ed) {
		$.ajax({			
			type: 'get',
			url: '/api/searchvilageweather.do',
			data:{
				"start_date" : st,
				"end_date" : ed
			},
			timeout : 30000,
			contentType: 'application/json',
			dataType: 'json',
			success: function(data, status, xhr) {
				
				let dataHeader = data.result.response.header.resultCode;
				let sItem = data.result.response.body.items.item;
		
				if (dataHeader == "00"){
					
				}
			},
			error: function(e, status, xhr, data) {
				console.log("error ==>");
				console.log(e);
			}
		});
	}
	 /**************************************중기기온예보조회함수*****************************************/
	 function searchmidtaweather(st, ed) {
		$.ajax({
			type: 'get',
			url: '/api/searchmidtaweather.do',
			data:{
				"startdate" : st,
				"enddate" : ed
			},
			timeout : 30000,
			contentType: 'application/json',
			dataType: 'json',
			success: function(data, status, xhr) {
				
				let dataHeader = data.result.response.header.resultCode;
				
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
	/**************************************중기육상예보조회함수*****************************************/
	 function searchmidlandweather(st, ed) { 
		$.ajax({
			type: 'get',
			url: '/api/searchmidlandweather.do',
			data:{
				"startdate" : st,
				"enddate" : ed
			},
			timeout : 30000,
			contentType: 'application/json',
			dataType: 'json',
			success: function(data, status, xhr) {
				
				let dataHeader = data.result.response.header.resultCode;
				
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

});
</script>
</html>