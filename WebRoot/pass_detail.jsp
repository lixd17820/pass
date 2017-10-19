<%@page import="com.pass.test.Test"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String id = request.getParameter("id");
	//String passInfo = Test.getPassInfo().toString();
%>

<!doctype html>
<html lang="zh">
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

<title>通行证详细信息</title>
<link rel="stylesheet" type="text/css"
	href="bootstrap/css/bootstrap.min.css">
<script src="lib/jquery-3.1.0.min.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
<script src="lib/lodash.3.10.js"></script>
<script type="text/javascript" src="js/comm.js"></script>
</head>
<body>
	<nav class="navbar navbar-default">
		<div class="container">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
					aria-expanded="false">
					<span class="sr-only">切换导航</span> <span class="icon-bar"></span> <span
						class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">南通交警通行证办理</a>
			</div>
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="query_pass.jsp">查询通行证</a></li>
				</ul>
			</div>
		</div>
	</nav>
	<div class="container">
		<div class="row">
			<div id="pass_info_div"
				class="col-xs-12 col-xs-offset-0 col-sm-10 col-sm-offset-1 col-md-8 col-md-offset-2 col-lg-6 col-lg-offset-3">
			</div>

		</div>
	</div>
	<script type="text/javascript">
		var id =
	<%=id%>
		;
	</script>
	<script type="text/javascript">
		$(function() {
			$.post('/forbid/services/forbid/queryPassPrintInfo', {
				passId : id
			}).then(showPass);
		});
		function showPass(pass) {
			//console.log(JSON.stringify(pass));
			var v = $("#pass_info_div");
			v.empty();
			$("<h3 />").addClass("text-center").html(pass.title).appendTo(v);
			$("<p />").addClass("text-right").html("编号：" + pass.bh).appendTo(v);
			appendTxt("号牌种类：" + pass.hpzl, v);
			appendTxt("号牌号码：" + pass.hphm, v);
			appendTxt("有效期：" + ifNull(pass.yxq), v);

			if (pass.print) {
				var pd = pass.print.replace(/\n/g, "<br/>");
				appendTxt("<strong>准行路线：</strong>", v);
				appendTxt(pd, v);
			} else {
				if (pass.areas) {
					appendTxt("<strong>准行路线：</strong>", v);
					$.each(pass.areas, function(i, d) {
						appendTxt((i + 1) + "、" + d, v);
					});
				}
			}
			if (pass.stForbid) {
				appendTxt("<strong>禁行区域：</strong>", v);
				$.each(pass.stForbid, function(i, d) {
					appendTxt((i + 1) + "、" + d, v);
				});
			}
			if (pass.readme) {
				appendTxt("<strong>通行证需知：</strong>", v);
				$.each(pass.readme, function(i, d) {
					appendTxt(d, v);
				});
			}
		}

		function appendTxt(txt, v) {
			$("<p />").addClass("text-left").html(txt).appendTo(v);
		}
	</script>
</body>
</html>