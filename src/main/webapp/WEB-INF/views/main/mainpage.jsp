<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html>
<html>
<head>

<link rel="stylesheet" type="text/css" href="https://cdn.rawgit.com/axisj/axisj/master/ui/arongi/AXJ.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="/resources/css/mainpage.css" >
<script type="text/javascript" src="https://code.jquery.com/jquery-3.6.0.js"></script>
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
			<span class="headerContentText"> 아래 검색기능을 통해 원하는 날의 날씨를 조회해보세요. </span>
		</c:if>
		<c:if test="${sessionScope.user_id == null}">
			<span class="headerContentText"> 온라인으로 <a href="/login/login.do">로그인</a>하고, 날씨를 조회해보세요.
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
				<button type="button" id="searcWeatherBtn" class="btn btn-light">search</button>
				<div class="guidetext">
					*금일부터 최대 <b>7</b>일까지 조회가능합니다.
				</div>
			</div>
		</c:if>
	<%-- 	<c:if test="${sessionScope.user_id == null}">
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
		</c:if> --%>
		<h1 style="text-align: center;">AXGrid</h1>
		<div id="AXPage">
			<div id="AXPageBody">	
				<div id="AXdemoPageContent" style="padding: 3%;">
				<div id="AXGridTarget"></div>
				<h1 id="AXSelectUserTitle" style="text-align: center;"></h1>
				<div class="ax-wrap AXdemoPageContent" style="text-align: center;">
			            <label class="AXInputLabel">사용자 목록</label>
			            <select name="UsersList" class="AXSelect" id="AXSelect1" tabindex="7"></select> 
			             &nbsp;
			            <label class="AXInputLabel">검색날짜</label>
			            <input type="date" name="end_date" id="axEdDate" class="AXInput W100 AXdate"/>
			             &nbsp;
			            <span type="button" class="AXButton" id="AXSearchBtn">조회</span>
	            	</div>
				<div id="AXGridTarget2"></div>
				</div>	
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" charset="utf-8">
	var myGrid = new AXGrid(); // 그리드 변수를 초기화 합니다.
	var fnObj = {
	    pageStart: function(){
	        myGrid.setConfig({
	            targetID : "AXGridTarget", //grid div ID
	            colHeadAlign: "center", // 헤드의 기본 정렬 값
	            colGroup : [
	                {key:"no", label:"번호", width:"*", align:"center"},
	                {key:"id", label:"사용자아이디", width:"*", align:"center"},
	                {key:"name", label:"이름", width:"*", align:"center"},
	                {key:"stDate", label:"조회시작날짜", width:"*", align:"center"},
	                {key:"edDate", label:"조회끝날짜", width:"*", align:"center"},
	                {key:"crDate", label:"조회날짜", width:"*", align:"center"}
	            ],
	           /*  colHead: { // 예제) http://dev.axisj.com/samples/AXGrid/colhead.html
	                rows: [ // 컬럼 헤더를 병합할 수 있습니다. 사용법은 colGroup과 동일하며 key 대신 colSeq를 사용할 수 있습니다.
	                    [
	                    	{colspan:1, label:"사용자"},
	                    	{colspan:1, 
	                    		formatter: "select",
	                    		 editor: {
	                                    type: "AXSelect",
	                                    options: [
	                                        {optionValue: "1", optionText: "서울"},
	                                        {optionValue: "2", optionText: "대전"},
	                                        {optionValue: "3", optionText: "대구"},
	                                        {optionValue: "4", optionText: "부산"}
	                                    ]
	                                }
	                    	
	                    	},
	                		{key:"name", rowspan:2},
		      	            {key:"stDate", rowspan:2},
	      	                {key:"edDate", rowspan:2},
	      	                {key:"crDate", rowspan:2}
				        ],
	                	[	
	                		{key:"no"},
	                		{key:"id"}
	                	]
	                ],
	                onclick: function(){
	                	
	                } // {Function} -- 그리드의 컬럼 헤드를 클릭시 발생하는 이벤트 입니다. 아래 onclick 함수를 참고하세요.
	            }, */
	            body : {
	            	
	            	onclick: function(){
	            		toast.push(Object.toJSON({index:this.index, r:this.r, c:this.c, item:this.item}));
	                }
	          
	            },
	            page:{
	            	paging:true,
	            	pageSize: 10,  // {Number} -- 한 페이지장 표시할 데이터 수를 설정합니다.
	                status:{formatter: null}
	            }
	         
	        });
	 
	        myGrid.setList({
	        	method : "get",
	        	dataType: "json",
	        	contentType: 'application/json; charset=utf-8',
	        	ajaxUrl : "/main/selectSearchList.do",
	        });
	    }    
	
	};
	
	var myGrid2 = new AXGrid(); // 그리드 변수를 초기화 합니다.
	var fnObj2 = {
	    pageStart: function(data){
	    	myGrid2.click()
	        myGrid2.setConfig({
	            targetID : "AXGridTarget2", //grid div ID
	            colHeadAlign: "center", // 헤드의 기본 정렬 값
	            mergeCells: [0],
	            colGroup : [
	                {key:"user_name", label:"이름", width:"*", align:"center"},
	                {key:"create_date", label:"조회날짜", width:"*", align:"center"},
	                {key:"totalCnt", label:"조회수", width:"*", align:"center"}
	            ],
	            body : {
	            	marker : {
	            		display: function () { 
	            			return this.item.name ? true : false;
	            			},
	            		rows: [
	            			[{
								colSeq  : null, colspan: 2, formatter: function () {
									//응답데이터 리스트 요소 중 합계를 문구표시값이 있다면
									if(this.item.name != null || this.item.name != "");
									return this.item.name;
								}, align: "center", width:"*"
							},{
								colspan: 1, formatter: function () {
									//응답데이터 리스트 요소 중 조회수 전체 합계값이 있다면
									if(this.item.allTotalCnt != null || this.item.allTotalCnt != "");
									return this.item.allTotalCnt;
								}, align: "center", width:"*"
							}]
	            		]	
	            	},
	            	onclick : function(){
	            		let selectName = $("#AXSelect1 option:checked").text();
	            		console.log(selectName);
	            		console.log("=>" + Object.toJSON({index:this.index, r:this.r, c:this.c, item:this.item}));
	            		toast.push(Object.toJSON({item:this.item}));
	                }
	            },
	            page:{
	            	paging:false,
	            /* 	pageSize: 10,  // {Number} -- 한 페이지장 표시할 데이터 수를 설정합니다.
	                status:{formatter: null} */
	            }
	         
	        });
	        myGrid2.setList(data);
	        
	    }    
	
	};

	$("document").ready(function() { 
			startTime(); //메인 페이지 타이머 생성
			setCalendar();//달력 범위 설정
			firstvilageweather(); //페이지 최초 접속 시 API 요청함수
			
			function setCalendar() {
				const toDay = getToday(); //yyyy-mm-dd형식
				//date형식의 변수 선언
				const now = new Date();//현재 날짜 및 시간
				const after7= new Date(Date.parse(now) + 7 * 1000 * 60 * 60 * 24); //7일후
				
				//조회시작날짜 속성 설정
				$("#startdate").attr("value", toDay);
				$("#startdate").attr("min", toDay);
				$("#startdate").attr("max", after7.toISOString().substr(0,10));
				//조회끝날짜 속성 설정
				$("#enddate").attr("value", toDay);
				$("#enddate").attr("min", toDay);
				$("#enddate").attr("max", after7.toISOString().substr(0,10));
			};
			
			//조회버튼 클릭 시
			$("#searcWeatherBtn").click(function() {
				const toDay = getToday(); //yyyy-mm-dd형식
				let start_date = parseInt($("#startdate").val().replace(/\-/g, "")); //"-"문자를 모두제거하는 정규식, 서버 호출 시 인자갑으로 보내준다
				let end_date = parseInt($("#enddate").val().replace(/\-/g, "")); //"-"문자를 모두제거하는 정규식, 서버 호출 시 인자갑으로 보내준다
				
				//사용자가 선택한 시작날짜와 끝날짜
				let stDate = $("#startdate").val();
				let edDate = $("#enddate").val();
				
				//선택한 시작날짜 끝날짜 split함수로 데이터를 나누어 배열에 담는다.
				const stDateArr = stDate.split("-");
				const edDateArr = edDate.split("-");
				const toDayArr = toDay.split("-");
				
				//Date객체 생성
				stDate = new Date(stDateArr[0], stDateArr[1], stDateArr[2]);
				edDate = new Date(edDateArr[0], edDateArr[1], edDateArr[2]);
				nowDate = new Date(toDayArr[0], toDayArr[1], toDayArr[2]); 
				
				//getime()으로 날짜시간을 숫자로 반환 두 날의 차이를 구한다.(조건문에서 조건값에 활용할 변수를 설정하기 위해 사용)
				const stDiff = stDate.getTime()-nowDate.getTime();
				const edDiff = edDate.getTime()-nowDate.getTime();
				//일수 차이 (조건문에서 조건값에 활용)
				const stDiffDay = stDiff/(1000*60*60*24);
				const edDiffDay = edDiff/(1000*60*60*24);

			 	//조회날짜 기준에 따른 api호출 리스트
				if (start_date > end_date) {
					alert("시작날짜와 끝날짜를 확인하세요.");
					return false;
				} else if (stDiffDay < 3 && edDiffDay < 3) {
					alert("short!");
					searchShortweather(String(start_date), String(end_date)); //단기예보만 호출
				} else if (stDiffDay > 2 && edDiffDay > 2) {
					alert("middle!");
					searchMidweather(String(start_date),String(end_date)); //중기예보만호출
				} else {
					alert("All!");
					searchAllweather(String(start_date),String(end_date)) //모두호출
				} 
			});
			
			//날씨조회 사용자 목록 조회버튼
			$("#AXSearchBtn").click(function(){
				//오늘날짜 , yyyy-mm-dd형식
				const toDay = getToday();
				
				let user = $("#AXSelect1").val();
				let axEdDate = $("#axEdDate").val();
			
							
				//조회날짜 검증 및 날씨조회 사용자 정보 그리드 호출
				if(user == null || user == ""){
					alert("사용자를 선택해주세요");
					return false;
				}
				if(axEdDate == null || axEdDate == ""){
					alert("날짜를 선택해주세요");
					return false;
				}else{
					alert("selectAXsearchBtn!!");
					selectAxUser(axEdDate, user) //axgrid2 ajax 호출	
				}
			});
	
			/**************************************최초 접속 시 호출되는 함수******************************************/
			function firstvilageweather() {
				$.ajax({
							type : 'get',
							url : '/api/searchShortweather.do',
							contentType : 'application/json',
							dataType : 'json',
							success : function(data, status, xhr) {
								console.log("firsthvilageweather success ==>");
								console.log(data);
								main(data); //응답받은 데이터를 인자값으로 메인 페이지 생성 함수 호출
								fnObj.pageStart(); //그리드 1 호출
							},
							error : function(e, status, xhr, data) {
								console.log("error ==>");
								console.log(e);
							}
						});
			}
	
			/**************************************단기예보 호출 함수******************************************/
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
						console.log("searchShortweather success ==>");
						fnObj.pageStart(); //그리드 1 호출
						//그리드가 1개 이상 열려 있을 때(날씨조회 사용자 정보 그리드가 열려있을때)
						if($(".AXGrid").length > 1){
							//날씨조회 사용자정보를 조회 후 날씨검색을 조회했을 시 AXGrid2의 조회수 값을 업데이트 하기 위함
							selectAxUser($("#axEdDate").val(), $("#AXSelect1").val());
						}
	
					},
					error : function(e, status, xhr, data) {
						console.log("error ==>");
						console.log(e);
					}
				});
			}
	
			/**************************************중기예보 호출 함수******************************************/
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
						console.log("searchMidweather success ==>");
						main(data);
						fnObj.pageStart(); //그리드 1 호출
						//그리드가 1개 이상 열려 있을 때(날씨조회 사용자 정보 그리드가 열려있을때)
						if($(".AXGrid").length > 1){
							//날씨조회 사용자정보를 조회 후 날씨검색을 조회했을 시 AXGrid2의 조회수 값을 업데이트 하기 위함
							selectAxUser($("#axEdDate").val(), $("#AXSelect1").val());
						}
	
					},
					error : function(e, status, xhr, data) {
						console.log("error ==>");
						console.log(e);
					}
				});
			}
			/**************************************단기&중기예보 호출 함수******************************************/
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
						console.log("searchAllweather success ==>");
						main(data);
						fnObj.pageStart(); //그리드 1 호출
						//그리드가 1개 이상 열려 있을 때(날씨조회 사용자 정보 그리드가 열려있을때)
						if($(".AXGrid").length > 1){
							//날씨조회 사용자정보를 조회 후 날씨검색을 조회했을 시 AXGrid2의 조회수 값을 업데이트 하기 위함
							selectAxUser($("#axEdDate").val(), $("#AXSelect1").val());
						}

					},
					error : function(e, status, xhr, data) {
						console.log("error ==>");
						console.log(e);
					}
				});
			}
			/***************************************메인 페이지 생성 함수*******************************************/
			function main(data) {
				//선택했던 상대방ID 옵션값을 넘겨준다. 
				selectUsers($("#AXSelect1").val());
				//select태그의 값을 넣을 함수 호출
				console.log("main function Start.");
				//selectBox에 날씨조회이력이있는 사용자 추가
				if (data.length != 0) { //최초 접속 시 api 데이터가 성공적으로 전달될 때
					console.log("main function success ==>");
	
					let formHtml = "";
	
					for (let i = 0; i < data.list.length; i++) {
						formHtml += "<div class=weatherForm id=weatherForm>";
						formHtml += "<span id=fcstTime>"
								+ data.list[i].date + "</span>";
						if (data.list[i].sky == 1) {
							formHtml += "<div><img src='/resources/images/weather1.png' id=weatherPng"+data.list[i].sky+"></div>";
							formHtml += "<div class=SKY, id=SKY>날씨 : 맑음</div>";//하늘					
						}
						if (data.list[i].sky == 2) {
							formHtml += "<div><img src='/resources/images/weather2.png' id=weatherPng"+data.list[i].sky+"></div>";
							formHtml += "<div class=SKY, id=SKY>날씨 : 구름조금</div>";//하늘					
						}
						if (data.list[i].sky == 3) {
							formHtml += "<div><img src=/resources/images/weather3.png id=weatherPng"+data.list[i].sky+"></div>";
							formHtml += "<div class=SKY, id=SKY>날씨 : 구름많음</div>";//하늘					
						}
						if (data.list[i].sky == 4) {
							formHtml += "<div><img src=/resources/images/weather4.png id=weatherPng"+data.list[i].sky+"></div>";
							formHtml += "<div class=SKY, id=SKY>날씨 : 흐림</div>";//하늘					
						}
						formHtml += "<div class=POP, id=POP>강수확률 : "+ data.list[i].pop + "%</div>"; //강수확률
						formHtml += "<div class=TMN, id=TMN>최저기온 : "+ data.list[i].tmn + "℃</div>"; //최저기온
						formHtml += "<div class=TMX, id=TMX>최고기온 : "+ data.list[i].tmx + "℃</div>"; //최고기온
						formHtml += "</div>";
					}
					$('.weatherContents').html(formHtml);
	
				} else {
					console.log("main function fail");
					console.log(data);
				}
			}
			
			//날씨조회 사용자들의 정보를 selectbox option에 set.
			function selectUsers(userId){
				console.log("상대방이름 : " + userId);
				let sessionId ='${sessionScope.user_id}';
				let sessionName ='${sessionScope.user_name}';
				//조회 시 선택된 이름
				let selectedId = userId;
				$.ajax({
					type : 'get',
					url : '/main/selectBoxUsers.do',
					success : function(data, status, xhr){
						
						$('#AXSelect1').empty();
						
						//select태그 최상단에는 전체
						let optionFirst = $("<option value = 'all'>전체</option>");
						$('#AXSelect1').append(optionFirst);
						
						//DB에서 조회된 사용자 명단을 select태그에 추가
						for(let i=0; i<data.length; i++){
							let option = "";                
				
							option = $("<option value ="+data[i].user_id+">"+data[i].user_name+"</option>"); 
							$('#AXSelect1').append(option);
							
							//조회를 한 상태가 아니라면
							if(selectedId == null){
								if(sessionId == data[i].user_id){
									$("#AXSelect1").val(sessionId).attr("selected", "selected");
								}
							//조회를 한 상태라면	
							}else{ 
								$("#AXSelect1").val(selectedId).attr("selected", "selected");
							}
						}
					},
					error : function(e, status, xhr, data) {
						console.log("error ==>");
						console.log(e);
					}
				});
			}
			
			//날씨조회 사용자정보 이력과 날짜별 조회 횟수 또는 날씨조회 전체 사용자정보 이력과 날짜별 조회 횟수를 구하여 AXgrid2를 호출한다
			function selectAxUser(ed, user) {
				$.ajax({
					type : 'post',
					url : '/main/selectAXUser.do',
					data : {
						"end_date" : ed,
						"user_id" : user
					},
					success : function(data, status, xhr){
						console.log(data);
						$("#AXSelectUserTitle").html("날씨조회 사용자 정보");
						fnObj2.pageStart(data);
						
					},
					error : function(e, status, xhr, data) {
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
	
			/***************************************오늘날짜 (YYYY-MM-DD) 생성 함수*******************************************/
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