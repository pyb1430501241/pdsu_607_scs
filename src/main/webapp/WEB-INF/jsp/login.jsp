<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	
	<form action="${APP_PATH}/loginAjax">
		Username :<input type="text" name="uid"/>
		<br>
		Password :<input type="text" name="password"/>
		<br>
		<input type="submit" value="submit"/>
		<br>
	</form>

</body>
</html>