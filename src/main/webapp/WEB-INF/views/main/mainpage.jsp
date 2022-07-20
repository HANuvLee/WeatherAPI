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
					for(let i in objarr){ //���� ���� �� �� ���� �±׻���
						formHtml += "<div class=weatherForm id=weatherForm>";
						formHtml += "<div class=weatherPng id=weatherPng><b id=fcstTime>" //��������
						formHtml += "</b></div>"
						formHtml += "<div class=sky id=TMP></div>" //���
						formHtml += "<div class=sky id=SKY>testtest</div>" //�ϴ�
						formHtml += "<div class=pty id=PTY></div>" //��������
						formHtml += "<div class=pop id=POP></div>"; //����Ȯ��
						formHtml += "<div class=reh id=REH></div>"; //����
						formHtml += "<div class=reh id=VVV></div>"; //����
						formHtml += "</div>";
					}
					$('.weatherContents').html(formHtml);
					
					//����API �����͸� ���±� ID�� ��ġ���� 
					for(let i in sItem){
						if(sItem[i].category == "SKY"){ //ī�װ��� SKY�̶��(�ϴû���)
							$("#SKY").attr("id", "SKY"+i+""); //���±׾��� div�±� �� id�� SKY�� div��  items�� ��� ��ȣ �߰�
							$("#fcstTime").attr("id", "fcstTime"+i+"");
							
							$("div[id=SKY"+i+"]").each(function(){//id���� ��ҹ�ȣ�� �߰��� �ش� �±��� �ؽ�Ʈ���� ������Ʈ
								if(sItem[i].fcstValue == 1){
									$(this).text("���� : ����");
								}else if(sItem[i].fcstValue == 3){
									$(this).text("���� : ��������");
								}else if(sItem[i].fcstValue == 4){
									$(this).text("���� : �帲");
								}
							});
							$("b[id=fcstTime"+i+"]").each(function(){//id���� ��ҹ�ȣ�� �߰��� �ش� �±��� �ؽ�Ʈ���� ������Ʈ
								$(this).text(sItem[i].fcstTime); //�ð� ������Ʈ
							});
						}else if(sItem[i].category == "TMP"){ //ī�װ��� SKY�̶��(�ϴû���)
							$("#TMP").attr("id", "TMP"+i+""); //���±׾��� div�±� �� id�� SKY�� div��  items�� ��� ��ȣ �߰�
							$("div[id=TMP"+i+"]").each(function(){ //id���� ��ҹ�ȣ�� �߰��� �ش� �±��� �ؽ�Ʈ���� ������Ʈ
								$(this).text("��� : " + sItem[i].fcstValue+"��"); 
							});
						}else if(sItem[i].category == "PTY"){ //ī�װ��� SKY�̶��(�ϴû���)
							$("#PTY").attr("id", "PTY"+i+""); //���±׾��� div�±� �� id�� SKY�� div��  items�� ��� ��ȣ �߰�
							$("div[id=PTY"+i+"]").each(function(){ //id���� ��ҹ�ȣ�� �߰��� �ش� �±��� �ؽ�Ʈ���� ������Ʈ
								if(sItem[i].fcstValue == 0){
									$(this).text("�������� : ����");
								}else if(sItem[i].fcstValue == 1){
									$(this).text("���� : ��");
								}else if(sItem[i].fcstValue == 2){
									$(this).text("���� : ��/��");
								}else if(sItem[i].fcstValue == 3){
									$(this).text("���� : ��");
								}else{
									$(this).text("���� : �ҳ���");
								}
							});
						}else if(sItem[i].category == "POP"){ //ī�װ��� SKY�̶��(�ϴû���)
							$("#POP").attr("id", "POP"+i+""); //���±׾��� div�±� �� id�� SKY�� div��  items�� ��� ��ȣ �߰�
							$("div[id=POP"+i+"]").each(function(){ //id���� ��ҹ�ȣ�� �߰��� �ش� �±��� �ؽ�Ʈ���� ������Ʈ
								$(this).text("����Ȯ�� : " + sItem[i].fcstValue+"%"); 
							});
						}else if(sItem[i].category == "REH"){ //ī�װ��� SKY�̶��(�ϴû���)
							console.log("2..REH");
							$("#REH").attr("id", "REH"+i+""); //���±׾��� div�±� �� id�� SKY�� div��  items�� ��� ��ȣ �߰�
							$("div[id=REH"+i+"]").each(function(){ //id���� ��ҹ�ȣ�� �߰��� �ش� �±��� �ؽ�Ʈ���� ������Ʈ
								$(this).text("���� : " + sItem[i].fcstValue+"%"); 
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