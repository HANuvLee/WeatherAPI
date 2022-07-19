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
	     		 <a class="navbar-brand nav-link active logstate" href="/login/login.do">�α���</a>
	     	</c:if>
	     	<c:if test="${sessionScope.user_id != null}">
				<a class="navbar-brand nav-link active logstate" href="/login/logout.do" class="logoutbtn">�α׾ƿ�</a>
			</c:if>
    	</div>
	</div>
</nav>
	<div class="headerContent">
		<c:if test="${sessionScope.user_id != null}">
	 		<span class="headerContentText">
	 			�Ʒ� �˻������ ���� ���ϴ� ���� ������ ��ȸ�غ�����.
	 		</span>
 		</c:if>
 		<c:if test="${sessionScope.user_id == null}">
	 		<span class="headerContentText">
	 			�¶������� <a href="/login/login.do">�α���</a>�ϰ�, ������ ��ȸ�غ�����.
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
			*���Ϻ��� �ִ� <b>7</b>�ϱ��� ��ȸ�����մϴ�.
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
			*���Ϻ��� �ִ� <b>7</b>�ϱ��� ��ȸ�����մϴ�.
		</div>
	</div>
	</c:if>
</body>
<script type="text/javascript" charset="utf-8">
$("document").ready(function() {
	startTime(); //���� ������ Ÿ�̸� ����
	setCalendar();//�޷� ���� ����
	firstvilageweather(); //������ ���� �� 3�ð� ������ ȭ�� ȣ��
	
	function setCalendar() {
		var toDay = getToday(); //yyyymmdd ����
		var toDay2 = getToday2(); //yyyy-mm-dd����
		var maxdd = parseInt(toDay) + 7; // ���� + 7
		
		maxdd = String(maxdd);
		
		//��ȸ���۳�¥ �Ӽ� ����
		$("#startdate").attr("value", toDay2);
		$("#startdate").attr("min", toDay2);
		$("#startdate").attr("max", toDay2.substr(0,8) + maxdd.substr(6));
		//��ȸ����¥ �Ӽ� ����
		$("#enddate").attr("value", toDay2);
		$("#enddate").attr("min", toDay2);
		$("#enddate").attr("max", toDay2.substr(0,8) + maxdd.substr(6));
		
		//��ȸ��ư Ŭ�� ��
		$("#searcWeatherBtn").click(function() {
			var start_date = parseInt($("#startdate").val().replace(/\-/g, "")); //"-"���ڸ� ��������ϴ� ���Խ�
			var end_date = parseInt($("#enddate").val().replace(/\-/g, "")); //"-"���ڸ� ��������ϴ� ���Խ�
			var num = end_date - start_date
			
			//��ȸ���۳�¥�� ����¥ ����
			if(num < 0){
				alert("���۳�¥�� ����¥�� Ȯ���ϼ���.");
			}else if(num == 0){
				alert("�ּ� 1�� �̻� ��ȸ�� �ּ���.");
			}else{
				alert("��ȸ�մϴ�.");
				searchvilageweather(start_date, end_date);
			}
	    });
	};
		
		/* console.log('"${sessionScope.user_id}"'); ���� ���̵�*/
	
/***************************************���ó�¥ (YYYYMMDD) ���� �Լ�*******************************************/
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

	/**************************************��ȸ��ư Ŭ�� �Լ�******************************************
	function serachClick() {
		var st = $("#startdate").val();
		var ed = $("#enddate").val();
		
		st = st.replace(/\-/g,"");
		ed = ed.replace(/\-/g,"");
		
		st = parseInt(st);
		ed = parseInt(ed);
		
		if(ed-st < 3){ //��ȸ������ 3�� �̸��ΰ�� �ܱ⿹���� ��ȸ ���� ȣ��
			searchvilageweather(st, ed);
		}else{ //�ƴϸ� ����ȣ��
			searchvilageweather(st, ed);
			searchmidtaweather(st, ed);
			searchmidlandweather(st, ed);
		}
	}*/
	
/**************************************���� ���� �� 3�ð� ������ �ܱ⿹����ȸ******************************************/
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
				let objarr = new Array();
				for(var i = 0; i < 3; i++){
					objarr.push(i)
				};

				console.log("fcstttime => " + parseInt(sItem[13].fcstTime));
				console.log("fcstttime => " + sItem[0].fcstTime);
				console.log("fcstttime => " + parseInt(sItem[11].fcstTime.substr(0,2)));
					
				if (dataHeader == "00"){
					console.log("success ==>");
					console.log(data);
					
					let rephtml = "";
					for(let i in objarr){ //���� ���� �� �� ���� �±׻���
						rephtml += "<div class=weatherForm id=weatherForm"+objarr[i]+">";
						rephtml += "<div class=weatherPng id=weatherPng"+objarr[i]+"><b>"+objarr[i]+""; //��������
						rephtml += "</b></div>"
						rephtml += "<div class=fcstTime id=fcstTime"+objarr[i]+"></div>" //�ð�
						rephtml += "<div class=sky id=sky"+objarr[i]+"></div>" //�ϴ�
						rephtml	+= "<div class=weatherInfo id=weatherInfo"+objarr[i]+">";
						rephtml += "<div class=pop id=pop"+objarr[i]+"></div>";
						rephtml += "<div class=maxTemp"+objarr[i]+"></div>";
						rephtml += "</div>";
						rephtml += "</div>";
					}
					$('.weatherContents').html(rephtml);
					
					for(let i in objarr){
						for(var j = 0; j < sItem.length; j++){
							if(parseInt(sItem[j].fcstTime.substr(0,2)) === i && sItem[j].category === "POP"){
								
							}$('div[id=pop'+objarr[i]+']').html(sItem[j].fcstTime);
						}continue;
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
/**************************************��ȸ��ư Ŭ�� �� �ִ� 3��ġ���� �ܱ⿹����ȸ******************************************/
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
	 /**************************************�߱��¿�����ȸ�Լ�*****************************************/
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
	/**************************************�߱����󿹺���ȸ�Լ�*****************************************/
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
	/***************************************Ÿ�̸� ���� �Լ�*******************************************/
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