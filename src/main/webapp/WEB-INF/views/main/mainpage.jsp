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
			<div class="weatherForm">
				<div class="weatherPng">2022. 07. 17<p>weatherPng</p></div>
				<div class="weatherInfo">
					<div class="weatherState"><b>weatherState</b></div>
					<div class="nowTemp">현재온도 : nowTemp</div>
					<div class="maxTemp">최고온도 : maxTemp</div>
					<div class="minTemp">최소온도 : minTemp</div>
					<div class="pop">강수확률 : pop</div>
					<div class="humidity">습도 : humidity</div>
				</div>
			</div>
			<div class="weatherForm">
				<div class="weatherPng">2022. 07. 17<p>weatherPng</p></div>
					<div class="weatherInfo">
						<div class="weatherState"><b>weatherState</b></div>
						<div class="nowTemp">현재온도 : nowTemp</div>
						<div class="maxTemp">최고온도 : maxTemp</div>
						<div class="minTemp">최소온도 : minTemp</div>
						<div class="pop">강수확률 : pop</div>
						<div class="humidity">습도 : humidity</div>
					</div>
			</div>
			<div class="weatherForm">
				<div class="weatherPng">2022. 07. 17<p>weatherPng</p></div>
				<div class="weatherInfo">
					<div class="weatherState"><b>weatherState</b></div>
					<div class="nowTemp">현재온도 : nowTemp</div>
					<div class="maxTemp">최고온도 : maxTemp</div>
					<div class="minTemp">최소온도 : minTemp</div>
					<div class="pop">강수확률 : pop</div>
					<div class="humidity">습도 : humidity</div>
				</div>
			</div>
		</div>
	</div>
	<c:if test="${sessionScope.user_id != null}">
	<div class="search">
		<form class="serarchform">
    		<label for="start">Start date:</label>
			<input type="date" id="startdate" name="trip-start" value="2022-07-15" min="2022-07-15" max="2022-07-22">
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    		<label for="start">End date:</label>
			<input type="date" id="endate" name="trip-start" value="2022-07-15" min="2022-07-15" max="2022-07-22">
			&nbsp;&nbsp;&nbsp;&nbsp;
      		<button type="button" class="btn btn-light searcBtn">search</button>
   		</form>
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
			<input type="date" id="endate" name="trip-start" value="2022-07-15" min="2022-07-15" max="2022-07-22" disabled="disabled">
			&nbsp;&nbsp;&nbsp;&nbsp;
      		<button type="button" class="btn btn-light searcBtn" disabled="disabled">search</button>
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
	startPage(); //메인 페이지 날씨정보 생성 
	
});
/****************************************** 타이머 생성 함수 ******************************************/
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
/*************************************************************************************************/
function startPage() {
	$.ajax({
		type: 'get',
		url: '/api/weather.do',
		timeout : 30000,
		contentType: 'application/json',
		dataType: 'json',
		success: function(data, status, xhr) {
			var today = getToday();
			var maxTempArr = new Array();
			var minTempArr = new Array;
			
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

function getToday() {
	 var date = new Date();
	 var year = date.getFullYear();
	 var month = ("0" + (1 + date.getMonth())).slice(-2);
	 var day = ("0" + date.getDate()).slice(-2);

	 return year + month + day;
}

function designWeatherBlock(item) {
	var maxTempArr = new Array();
	var minTempArr = new Array;
	
	for (var i = 0; i < item.length; i++) {
		if(item[i].category == "TMX"){
			maxTempArr.push(item[i].fcstValue);
		}else if(item[i].category == "TMN"){
			minTempArr.push(item[i].fcstValue);
		}
	}
	
	  $.each(maxTempArr, function(index, value){
          $(".weatherContents").append($("<div class=weatherForm>"
         
          + "<div class=weatherPng></div>"
			  +	"<div class=weatherInfo>"
				  +	"<div class=weatherState></div>"
				  +	"<div class=nowTemp></div>"
				  +	"<div class=maxTemp>"+value+"</div>"
				  + "<div class=minTemp></div>"
			  +	"</div>"
          + "</div>"));
      });
}
</script>
</html>