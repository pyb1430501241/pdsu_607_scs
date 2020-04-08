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

	<div id = "test"></div>
	<script type="text/javascript">
	
		$(function() {
			var t = ${id};
			$.ajax({
				type : "POST",
				url  : "${APP_PATH}/blob/blob",
				data : "id="+t,
				success : function(result) {
					console.log(result);
					var s = result.map.web.webDataString;
					var t = $("<a><a>").append(s);
					t.appendTo("#test");
				}
			});
		});
	
	</script>
</body>
</html>