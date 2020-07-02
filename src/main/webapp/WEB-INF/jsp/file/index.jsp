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
	
	<form action="upload" method="post" enctype="multipart/form-data" id = "uploadForm">
		<input type="file" class = "btn btn-primary btn-lg" id = "file"/>
		<div style="padding-top:20px;" >
			<input type="button" onclick="upload()" value = "点击上传"/>
		</div>
	</form>
	<script type="text/javascript">
		function upload(){
		    var formData = new FormData()  //创建一个forData 
		    if($("#file")[0].files[0] == null){
		    	alert("请选择文件!");
		    	return ;
		    }
		    formData.append("file", $("#file")[0].files[0]);
		    //把file添加进去  name命名为img
		    $.ajax({
		        url: "${APP_PATH}/file/upload",
		        data: formData,
		        type: "POST",
		        processData: false, //因为data值是FormData对象，不需要对数据做处理。
		        contentType: false,
		        success: function(result) {
		        	console.log(result);
		        },
		      });
		}
	</script>
</body>
</html>