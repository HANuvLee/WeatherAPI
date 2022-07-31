<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@include file="/WEB-INF/views/common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<link href="/resources/css/login.css" rel="stylesheet">
<title>login</title>
</head>
<body>
	<div class="container">
		<form id="loginAction" action="/login/loginAction.do" method="post">
			<div class="table_box">
				<div class="login_box">
					<label for="user_id" style="padding-right: 7px;">id</label>
					<input type="text" id="user_id" name="user_id"><br>
					<label for="user_pw">pw</label>
					<input type="password" id="user_pw" name="user_pw" ><br>
				</div>
			</div>
		</form>
	</div>
	<button id="loginBtn" class="btn btn-link">login</button>
</body>
<script type="text/javascript" charset="utf-8">
$(function(){
	$("#loginBtn").on("click", function() {
		var user_id = $("#user_id").val();
		var user_pw= $("#user_pw").val();
		
		if(user_id == ''){
			alert("아이디를 입력해주세요.");
			return false;
		}else if(user_pw == ''){
			alert("비밀번호를 입력해 주세요.");
			return false;
		}else{
			$.ajax({
				type: 'POST',
				url: '/login/loginAction.do',
				data:{
					"user_id" : user_id,
					"user_pw" : user_pw
				},
				/* contentType: 'application/json',
				dataType: 'json', */
				success: function(data, status, xhr) {
					if(data.result == "success"){ //유저정보가 존재
						if(data.url == null || data.url == ""){ //요청주소가 빈값이거나 없다면
							alert("주소요청에 실패했습니다.");
							location.href = "/"; //스크립트에서 컨트롤러 요청 (최상위 주소)
						}else{							
							location.href = data.url; //스크립트에서 컨트롤러 요청
						}
					}else{
						alert("로그인에 실패했습니다.");
						return false;
					}
					
				},
				error: function(e, status, xhr, data) {
					console.log(data);
				}
			});
		}
	});
});

</script>
</html>