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
		<br>
		<h4 id = "st"></h4>
		<br>
		<a onclick="de()">退出</a>

	<script type="text/javascript">
		$(function() {
			code();
			isAuthenticated();
		});
		function code() {
			$.ajax({
				type : "GET",
				url  : "${APP_PATH}/user/getcodeforlogin",
				success : function(result) {
					$("#img").removeAttr("src");
					$("#hidden").removeAttr("value");
					$("#img").attr("src",result.json.img);
					$("#hidden").attr("value",result.json.token);
				}
			})
		}
		
		function setCookie(name,value)
		{
			var Days = 30;
			var exp = new Date();
			exp.setTime(exp.getTime() + Days*24*60*60*1000);
			document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
		}
		
		function getCookie(name)
		{
			var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
			if(arr=document.cookie.match(reg))
				return unescape(arr[2]);
			else
				return null;
		}
		
		function de() {
			$.ajax({
				headers : {
					Authorization : getCookie("Authorization")
				},
				type : "GET",
				url  : "${APP_PATH}/user/logout",
				success : function(result) {
					console.log(result);
				}
			})
		}
		
		function isAuthenticated() {
			$.ajax({
				headers : {
					Authorization : getCookie("Authorization")
				},
				type : "GET",
				url  : "${APP_PATH}/user/isAuthenticated",
				success : function(result) {
					console.log(result);
					$("#st").value = "";
					if(result.json.isAuthenticated == true){
						$("#st").append("未登录");
					}else {
						$("#st").append("已登录");
					}
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
				url  : "${APP_PATH}/user/loginajax",
				data : "uid="+uid+"&password="+password+"&code="+code+"&hit="+hit,
				success : function(result) {
					console.log(result);
					if(result.json.code == 200){
						setCookie("Authorization", result.json.AccessToken);
						window.location.href="${APP_PATH}/blob/index";
					}else{
						
					}
				}
			});
		}
	</script>
</body>
</html>