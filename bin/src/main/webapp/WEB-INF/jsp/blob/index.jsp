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
	<h4>Bolb Index</h4>
	
	<div id = "test">
	
	</div>
	
	<script type="text/javascript">
		$(function() {
			$.ajax({
				type	: "POST",
				url		: "${APP_PATH}/blob/getWebindex",
				success : function(result) {
					console.log(result);
					$.each(result.json.webList,function(index,web){
						var hr = $("<a></a>").append(web.title).attr("href","${APP_PATH}/blob/b"+web.id);
						hr.appendTo("#test");
						var br = $("<br>");
						br.appendTo("#test");
					});
				}
			})
		});
	
	</script>
</body>
</html>