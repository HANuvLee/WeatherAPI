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
					<button id="loginBtn" class="btn btn-link">login</button>
				</div>
			</div>
		</form>
	</div>
</body>
<script type="text/javascript" src="/resources/js/login.js" charset="utf-8"></script>
</html>