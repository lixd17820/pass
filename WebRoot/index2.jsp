<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	
%>
<!doctype html>
<html lang="zh">
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

<title>自助办理通行证</title>
<link rel="stylesheet" type="text/css"
	href="bootstrap/css/bootstrap.min.css">
<script src="lib/jquery-3.1.0.min.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
<script src="lib/lodash.3.10.js"></script>
<script type="text/javascript" src="js/comm.js"></script>
<script type="text/javascript" src="js/db.js"></script>

</head>

<body>
	<div id="info" style="min-height: 100px;">无内容</div>
	<select id="method">
		<option value="get">GET</option>
		<option value="post">POST</option>
		<option value="PUT">PUT</option>
		<option value="DELETE">delete</option>
	</select>
	<input type="text" id="url" />
	<a id="show_db" class="btn">测试</a>
	<script type="text/javascript">
		$(function() {
			$("#show_db").bind("click", testDb);
			
			
		});
		
		function testDb(){
			var id = "6e625bf1de13dd80b05e3dae02004837";
			var data = '{"title":"There is Nothing Left to Lose","artist":"Foo Fighters"}';
			$.ajax({
				url : "http://127.0.0.1:5984/cross/" + id,
				type : "post",
				data : {},
				contentType : "application/json", 
			    dataType : "json",
				success : function(d) {
					$("#info").html(JSON.stringify(d));
					console.log(JSON.stringify(d));
				}
			});
			
		}
		function testDb2() {
			var url = $("#url").val();
			var method = $("#method").val();
			//$.get("http://127.0.0.1:5984/" + url, {}, function(d) {
			//	$("#info").html(JSON.stringify(d));
			//	console.log(JSON.stringify(d));
			//});
			$.ajax({
				url : "http://127.0.0.1:5984/" + url,
				type : method,
				data : {},
				success : function(d) {
					$("#info").html(JSON.stringify(d));
					console.log(JSON.stringify(d));
				}
			});
		}
	</script>
</body>
</html>
