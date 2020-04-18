<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="${APP_PATH}/js/jquery/jquery-3.4.1.min.js"></script>
</head>
<body>
	
		Username :<input type="text" id = "uid"/>
		<br>
		Password :<input type="text" id="password"/>
		<br>
		<input type="submit" value="submit" onclick="login()"/>

	<script type="text/javascript">
		function login() {
			var uid = $("#uid").val();
			var password = $("#password").val();
			$.ajax({
				type : "POST",
				url  : "${APP_PATH}/loginAjax",
				data : "uid="+uid+"&password="+password,
				success : function(result) {
					if(result.json.code == 200){
						alert(result.json.sessionId);
						window.location.href="${APP_PATH}/blob/index";
					}else{
						
					}
				}
			});
		}
	</script>
</body>
</html>