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
	<input type="text" id = "str"/>
	<input type="submit" value="submit" onclick="insert()"/>
	<script type="text/javascript">
		function insert() {
			var str = $("#str").val();
			var title = "开发日记";
			var contype = 1;
			$.ajax({
				type : "POST",
				url  : "${APP_PATH}/blob/insert",
				data : "title="+title+"&contype="+contype+"&webDataString="+str,
				success : function(result) {
					console.log(result.json.hint);
				}
			});
		}
		
	</script>
</body>
</html>