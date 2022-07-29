<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<script type="text/javascript" src="https://code.jquery.com/jquery-3.5.1.js"></script>
<link rel="stylesheet" type="text/css" href="https://cdn.rawgit.com/axisj/axisj/master/ui/arongi/AXJ.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="/resources/css/mainpage.css" >
<script type="text/javascript" src="https://cdn.jsdelivr.net/jquery/1.12.3/jquery.min.js"></script>
<script type="text/javascript" src="https://cdn.rawgit.com/axisj/axisj/master/dist/AXJ.min.js"></script>
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
			<span class="headerContentText"> �Ʒ� �˻������ ���� ���ϴ� ���� ������ ��ȸ�غ�����. </span>
		</c:if>
		<c:if test="${sessionScope.user_id == null}">
			<span class="headerContentText"> �¶������� <a href="/login/login.do">�α���</a>�ϰ�, ������ ��ȸ�غ�����.
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
		<div class="weatherContents"></div>
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
		<h1 style="text-align: center;">AXGrid RWD</h1>
		<div id="AXPageBody">
			<div id="AXdemoPageContent" style="padding: 3%;">
				<div id="AXGridTarget"></div>	
			</div>	
		</div>
	</div>
</body>
<script type="text/javascript" charset="utf-8">
	var myGrid = new AXGrid(); // �׸��� ������ �ʱ�ȭ �մϴ�.
	var fnObj = {
	    pageStart: function(){
	        myGrid.setConfig({
	            targetID : "AXGridTarget",
	            colHeadAlign: "center", // ����� �⺻ ���� ��
	            colGroup : [
	                {key:"no", label:"��ȣ", width:"50", align:"right"},
	                {key:"id", label:"����ھ��̵�", width:"200"},
	                {key:"name", label:"�̸�", width:"100"},
	                {key:"stDate", label:"��ȸ���۳�¥", width:"100"},
	                {key:"edDate", label:"��ȸ����¥", width:"100", align:"right"},
	                {key:"crDate", label:"��ȸ�ð�", width:"80", align:"right"}
	                ],
	            body : {
	                onclick: function(){
	                    toast.push(Object.toJSON(this.item));
	                }
	            },
		        page : {
	                paging:false
		        }
	        });
	   		
	        var list = [];
	        
	        myGrid.setList({
	        	method : "get",
	        	ajaxUrl : "/main/selectSearchList.do",
	        	onLoad:function(){
	        		
	        	},
	            onError:function(){
	            	
	            }
	        });
	    }    
	};
	

	$("document").ready(function() {
			startTime(); //���� ������ Ÿ�̸� ����
			setCalendar();//�޷� ���� ����
			firstvilageweather(); //������ ���� ���� �� API ��û�Լ�
		 	setTimeout(fnObj.pageStart, 1);
	
			function setCalendar() {
				const toDay = getToday(); //yyyy-mm-dd����
				const now = new Date();//���� ��¥ �� �ð�
				const stDate = new Date(toDay)
				const edDate = new Date()
				const after7= new Date(Date.parse(now) + 7 * 1000 * 60 * 60 * 24); //7����
	
				//��ȸ���۳�¥ �Ӽ� ����
				$("#startdate").attr("value", toDay);
				$("#startdate").attr("min", toDay);
				$("#startdate").attr("max", after7.toISOString().substr(0,10));
				//��ȸ����¥ �Ӽ� ����
				$("#enddate").attr("value", toDay);
				$("#enddate").attr("min", toDay);
				$("#enddate").attr("max", after7.toISOString().substr(0,10));
	
				//��ȸ��ư Ŭ�� ��
				$("#searcWeatherBtn").click(function() {
					var start_date = parseInt($("#startdate").val().replace(/\-/g, "")); //"-"���ڸ� ��������ϴ� ���Խ�,  ���� ȣ�� �� ���ڰ����� �����ش�
					var end_date = parseInt($("#enddate").val().replace(/\-/g, "")); //"-"���ڸ� ��������ϴ� ���Խ�, ���� ȣ�� �� ���ڰ����� �����ش�
					//����ڰ� ������ �ϼ� ���̸� ���ϱ� ���� ���� 
					let stDate = $("#startdate").val();
					let edDate = $("#enddate").val();
					//���� split�Լ��� �����͸� ������ �迭�� ��´�.
					const stDateArr = stDate.split("-");
					const edDateArr = edDate.split("-");
					const toDayArr = toDay.split("-");
					//Date��ü ����
					stDate = new Date(stDateArr[0], stDateArr[1], stDateArr[2]);
					edDate = new Date(edDateArr[0], edDateArr[1], edDateArr[2]);
					nowDate = new Date(toDayArr[0], toDayArr[1], toDayArr[2]); 
					
					//getime()���� ��¥�ð��� ���ڷ� ��ȯ �� ���� ���̸� ���Ѵ�.
					const stDiff = stDate.getTime()-nowDate.getTime();
					const edDiff = edDate.getTime()-nowDate.getTime();
					//�ϼ� ����
					const stDiffDay = stDiff/(1000*60*60*24);
					const edDiffDay = edDiff/(1000*60*60*24);
	
				 	//��ȸ��¥ ���ؿ� ���� apiȣ�� ����Ʈ
					if (start_date > end_date) {
						alert("plz chk your date state !!");
						return false;
					} else if (stDiffDay < 3 && edDiffDay < 3) {
						alert("short!");
						searchShortweather(String(start_date), String(end_date)); //�ܱ⿹���� ȣ��
					} else if (stDiffDay > 2 && edDiffDay > 2) {
						alert("middle!");
						searchMidweather(String(start_date),String(end_date)); //�߱⿹����ȣ��
					} else {
						alert("All!");
						searchAllweather(String(start_date),String(end_date)) //���ȣ��
					} 
				});
			};
	
			/**************************************���� ���� �� ȣ��Ǵ� �Լ�******************************************/
			function firstvilageweather() {
				$.ajax({
							type : 'get',
							url : '/api/searchShortweather.do',
							contentType : 'application/json',
							dataType : 'json',
							success : function(data, status, xhr) {
	
								console
										.log("firsthvilageweather success ==>");
								console.log(data);
	
								main(data); //������� �����͸� ���ڰ����� ���� ������ ���� �Լ� ȣ��	
							},
							error : function(e, status, xhr, data) {
								console.log("error ==>");
								console.log(e);
							}
						});
			}
	
			/**************************************�ܱ⿹�� ȣ�� �Լ�******************************************/
			function searchShortweather(st, ed) {
				console.log("searchShortweather start");
				$.ajax({
					type : 'get',
					url : '/api/searchShortweather.do',
					data : {
						"start_date" : st,
						"end_date" : ed
					},
					contentType : 'application/json',
					dataType : 'json',
					success : function(data, status, xhr) {
						console.log(data);
						main(data);
	
					},
					error : function(e, status, xhr, data) {
						console.log("error ==>");
						console.log(e);
					}
				});
			}
	
			/**************************************�߱⿹�� ȣ�� �Լ�******************************************/
			function searchMidweather(st, ed) {
				$.ajax({
					type : 'get',
					url : '/api/searchmidtaweather.do',
					data : {
						"start_date" : st,
						"end_date" : ed
					},
					contentType : 'application/json',
					dataType : 'json',
					success : function(data, status, xhr) {
	
						console.log(data);
						main(data);
	
					},
					error : function(e, status, xhr, data) {
						console.log("error ==>");
						console.log(e);
					}
				});
			}
			/**************************************�ܱ�&�߱⿹�� ȣ�� �Լ�******************************************/
			function searchAllweather(st, ed) {
				$.ajax({
					type : 'get',
					url : '/api/searchAllweather.do',
					data : {
						"start_date" : st,
						"end_date" : ed
					},
					contentType : 'application/json',
					dataType : 'json',
					success : function(data, status, xhr) {
	
						console.log(data);
						main(data);
	
					},
					error : function(e, status, xhr, data) {
						console.log("error ==>");
						console.log(e);
					}
				});
			}
			/***************************************���� ������ ���� �Լ�*******************************************/
			function main(data) {
				console.log("main function Start.");
	
				if (data.length != 0) { //���� ���� �� api �����Ͱ� ���������� ���޵� ��
					console.log("main function success ==>");
					console.log(data);
					console.log("data length ==> " + data.list.length);
	
					let formHtml = "";
	
					for (let i = 0; i < data.list.length; i++) {
						formHtml += "<div class=weatherForm id=weatherForm>";
						formHtml += "<span id=fcstTime>"
								+ data.list[i].date + "</span>";
						if (data.list[i].sky == 1) {
							formHtml += "<div><img src='/resources/images/weather1.png' id=weatherPng"+data.list[i].sky+"></div>";
							formHtml += "<div class=SKY, id=SKY>���� : ����</div>";//�ϴ�					
						}
						if (data.list[i].sky == 2) {
							formHtml += "<div><img src='/resources/images/weather2.png' id=weatherPng"+data.list[i].sky+"></div>";
							formHtml += "<div class=SKY, id=SKY>���� : ��������</div>";//�ϴ�					
						}
						if (data.list[i].sky == 3) {
							formHtml += "<div><img src=/resources/images/weather3.png id=weatherPng"+data.list[i].sky+"></div>";
							formHtml += "<div class=SKY, id=SKY>���� : ��������</div>";//�ϴ�					
						}
						if (data.list[i].sky == 4) {
							formHtml += "<div><img src=/resources/images/weather4.png id=weatherPng"+data.list[i].sky+"></div>";
							formHtml += "<div class=SKY, id=SKY>���� : �帲</div>";//�ϴ�					
						}
						formHtml += "<div class=POP, id=POP>����Ȯ�� : "+ data.list[i].pop + "%</div>"; //����Ȯ��
						formHtml += "<div class=TMN, id=TMN>������� : "+ data.list[i].tmn + "��</div>"; //�������
						formHtml += "<div class=TMX, id=TMX>�ְ��� : "+ data.list[i].tmx + "��</div>"; //�ְ���
						formHtml += "</div>";
					}
					$('.weatherContents').html(formHtml);
	
				} else {
					console.log("main function fail");
					console.log(data);
				}
			}
	
			/***************************************Ÿ�̸� ���� �Լ�*******************************************/
			function startTime() {
				var today = new Date();
				var hr = today.getHours();
				var min = today.getMinutes();
				var sec = today.getSeconds();
				ap = (hr < 12) ? "<span>AM</span>"
						: "<span>PM</span>";
				hr = (hr == 0) ? 12 : hr;
				hr = (hr > 12) ? hr - 12 : hr;
				//Add a zero in front of numbers<10
				hr = checkTime(hr);
				min = checkTime(min);
				sec = checkTime(sec);
				document.getElementById("clock").innerHTML = hr
						+ ":" + min + ":" + sec + " " + ap;
	
				var months = [ 'January', 'February', 'March','April', 'May', 'June', 'July', 'August','September', 'October', 'November','December' ];
				var days = [ 'Sun', 'Mon', 'Tue', 'Wed', 'Thu','Fri', 'Sat' ];
				var curWeekDay = days[today.getDay()];
				var curDay = today.getDate();
				var curMonth = months[today.getMonth()];
				var curYear = today.getFullYear();
				var date = curWeekDay + ", " + curDay + " " + curMonth + " " + curYear;
				document.getElementById("date").innerHTML = date;
	
				var time = setTimeout(function() {
					startTime()
				}, 500);
			}
			function checkTime(i) {
				if (i < 10) {
					i = "0" + i;
				}
				return i;
			}
	
			/***************************************���ó�¥ (YYYY-MM-DD) ���� �Լ�*******************************************/
			function getToday() {
				var date = new Date();
				var year = date.getFullYear();
				var month = ("0" + (1 + date.getMonth())).slice(-2);
				var day = ("0" + date.getDate()).slice(-2);
	
				return year + "-" + month + "-" + day;
			}
		});
	
</script>
</html>