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
	
		账号 :<input type="text" id = "uid"/>
		<br>
		密码 :<input type="text" id="password"/>
		<br>
		<img id = "img">
		<br>
		验证码:<input type="text" id = "code"/>
		<input type="hidden" id = "hidden"/>
		<br>
		<input type="submit" value="submit" onclick="login()"/>

	<script type="text/javascript">
		$(function() {
			code();
		});
		function code() {
			$.ajax({
				type : "GET",
				url  : "${APP_PATH}/user/getCodeForLogin",
				success : function(result) {
					$("#img").removeAttr("src");
					$("#hidden").removeAttr("value");
					$("#img").attr("src",result.json.img);
					$("#hidden").attr("value",result.json.token);
				}
			})
		}
	
		function login() {
			var uid = $("#uid").val();
			var password = $("#password").val();
			var hit = $("#hidden").val();
			var code = $("#code").val();
			$.ajax({
				type : "POST",
				url  : "${APP_PATH}/user/loginAjax",
				data : "uid="+uid+"&password="+password+"&code="+code+"&hit="+hit,
				success : function(result) {
					console.log(result);
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