<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<table>
		<c:forEach items="${board}" var="list">
			<tr>
				<td align="center">${list.bno}</td>
				<td align="center">${list.title}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>