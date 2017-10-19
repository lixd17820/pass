<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	long l = Math.round(Math.random() * 100000000);
	request.getSession().setAttribute("rCode", l + "");
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
			<div
				class="col-xs-12 col-xs-offset-0 col-sm-10 col-sm-offset-1 col-md-8 col-md-offset-2 col-lg-6 col-lg-offset-3">
				<div id="alert_error" class="alert alert-danger" role="alert">
					<span class="glyphicon glyphicon-exclamation-sign"
						aria-hidden="true"></span> <span class="sr-only">Error:</span> <span
						id="span_error"></span>
				</div>
				<div id="alert_ok" class="alert alert-success" role="alert">
					<span class="glyphicon glyphicon-exclamation-sign"
						aria-hidden="true"></span> <span class="sr-only">OK:</span> <span
						id="span_ok">通行证办理成功</span>
				</div>
				<form class="form-horizontal" role="form">
					<input type="hidden" id="r_code" value="<%=l%>" />
					<div class="form-group">
						<label for="hpzl" class="col-xs-4 control-label">号牌种类</label>
						<div class="col-xs-8">
							<select class="form-control" id="hpzl">
							</select>
						</div>
					</div>
					<div class="form-group">
						<label for="hphm" class="col-xs-4 control-label">号牌号码</label>
						<div class="col-xs-3">
							<select class="form-control" id="hpqz">
								<option value="苏">苏</option>
								<option value="京">京</option>
								<option value="津">津</option>
								<option value="冀">冀</option>
								<option value="晋">晋</option>
								<option value="蒙">蒙</option>
								<option value="辽">辽</option>
								<option value="吉">吉</option>
								<option value="黑">黑</option>
								<option value="沪">沪</option>
								<option value="浙">浙</option>
								<option value="皖">皖</option>
								<option value="闽">闽</option>
								<option value="赣">赣</option>
								<option value="鲁">鲁</option>
								<option value="豫">豫</option>
								<option value="鄂">鄂</option>
								<option value="湘">湘</option>
								<option value="粤">粤</option>
								<option value="桂">桂</option>
								<option value="琼">琼</option>
								<option value="渝">渝</option>
								<option value="川">川</option>
								<option value="贵">贵</option>
								<option value="云">云</option>
								<option value="藏">藏</option>
								<option value="陕">陕</option>
								<option value="甘">甘</option>
								<option value="青">青</option>
								<option value="宁">宁</option>
								<option value="新">新</option>
							</select>
						</div>
						<div class="col-xs-5">
							<input type="text" class="form-control" id="hphm"
								placeholder="号牌号码">
						</div>
					</div>
					<div class="form-group">
						<label for="sbdm" class="col-xs-4 control-label">识别代码</label>
						<div class="col-xs-8">
							<input type="text" class="form-control" id="sbdm"
								placeholder="车架号后四位">
						</div>
					</div>
					<div class="form-group">
						<label for="sjhm" class="col-xs-4 control-label"> 手机号码 </label>
						<div class="col-xs-8">
							<input type="text" id="sjhm" name="sjhm" value=""
								placeholder="手机号码" class="form-control" />
						</div>
					</div>
					<div class="form-group">
						<label for="jhm" class="col-xs-4 control-label"> 短信激活码 </label>
						<div class="col-xs-4">
							<input type="text" id="jhm" name="jhm" class="form-control" />
						</div>
						<a id="btn_send_jhm" class="btn btn-primary">获取激活码</a>
					</div>
					<div class="form-group">
						<label for="sxrq" class="col-xs-4 control-label">生效日期</label>
						<div class="col-xs-8">
							<select class="form-control" id="sxrq">
								<option value="1">今日生效</option>
								<option value="2">明日生效</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label for="glbm" class="col-xs-4 control-label">通行范围</label>
						<div class="col-xs-8">
							<select class="form-control" id="glbm">
								<option value="320600">南通市区</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<a id="btn_atm_pass"
							class="btn btn-primary col-xs-offset-4 col-xs-5"> <span
							class="glyphicon glyphicon-ok" aria-hidden="true"></span> 办理通行证
						</a>
					</div>
				</form>
			</div>

		</div>
		<div class="row">
			<div class="hidden-xs col-xs-12 col-xs-offset-0 col-sm-10 col-sm-offset-1 col-md-8 col-md-offset-2 col-lg-6 col-lg-offset-3">
				<h4>通行证办证需知：</h4>
				<ol>
					<li>本系统办证对象为中型或轻型非营运性质的普通货车，且核定载质量为5000千克以下。</li>
					<li>本市机动车办证时不能有未处理的违法行为或未缴的罚款。</li>
					<li>本系统所办理的通行证有效期为两天，具体准行范围可点击页面顶部右侧菜单“查询通行证”进行查询。</li>
				</ol>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(function() {
			$("#btn_send_jhm").bind("click", sendJhm);
			$("#btn_atm_pass").bind("click", atmPass);
			$("#alert_error").hide();
			$("#alert_ok").hide();
			reloadSelect($("#hpzl"), hpzlKvs, false);
		});

		function atmPass(e) {
			$("#alert_error").hide();
			$("#alert_ok").hide();
			$("#btn_atm_pass").addClass("disabled");
			var p = checkValue();
			//console.log(JSON.stringify(p));
			if (p && p.err && p.err.length > 0) {
				showErrInfo(p.err.join(","));
				return;
			}

			$.post("services/pass/atmPass", p).then(function(d) {
				if (d && d.err) {
					showErrInfo(d.err);
					$("#btn_atm_pass").removeClass("disabled");
					return;
				}
				alert("通行证办理成功");
				window.location = "query_pass.jsp";
			}).fail(function(e) {
				showErrInfo("服务器错误");
				$("#btn_atm_pass").removeClass("disabled");
			});

		}

		function showErrInfo(err) {
			$("#alert_error").show();
			$("#alert_ok").hide();
			$("#span_error").html("错误：" + err);
		}

		function showErrOk(info) {
			$("#alert_ok").show();
			$("#alert_error").hide();
			$("#span_ok").html("信息：" + info);
		}

		function sendJhm(e) {
			var sjhm = $("#sjhm").val();
			var rCode = $("#r_code").val();
			if (isEmpty(sjhm) || sjhm.length != 11) {
				showErrInfo("手机号码不正确");
				return;
			}
			$.ajax("services/pass/sendSjyzm", {
				data : {
					sjhm : sjhm,
					rCode : rCode
				},
				method : 'get',
				dataType : 'json',
				success : function(d) {
					//console.log(JSON.stringify(d));
					if (d && d.err) {
						showErrInfo(d.err);
					} else if (d.ok) {
						showErrOk(d.ok);
					}
				},
				error : function(e) {
					showErrInfo('服务器出现错误');
				}
			});
			$(this).addClass("disabled");
			var that = $(this);
			var c = 0;
			var inv = setInterval(function() {
				c++;
				that.html("　（" + c + "）　");
				if (c > 60) {
					that.removeClass("disabled");
					clearInterval(inv);
					that.html("获取激活码");
				}
			}, 1000);

		}

		function checkValue() {
			var err = [];
			var hpzl = $('#hpzl').val();
			var hpqz = $("#hpqz").val();
			var jhm = $("#jhm").val();
			var rCode = $("#r_code").val();
			var sxrq = $("#sxrq").val();
			var glbm = $("#glbm").val();
			var hphm = hpqz + _.trim(ifNull($('#hphm').val()));

			var sjhm = $('#sjhm').val();
			var sbdm = $('#sbdm').val();
			if (isEmpty(sbdm) || sbdm.length != 4) {
				err.push('车辆识别号不能为空或不是四位');
			}
			if (isEmpty(sjhm) || sjhm.length != 11) {
				err.push('手机号码不正确');
			}
			if (isEmpty(hphm) || (hphm.length != 6 && hphm.length != 7)) {
				err.push('号牌号码不正确');
			}
			if (isEmpty(hpzl)) {
				err.push('号牌种类不能为空');
			}
			if (isEmpty(jhm)) {
				err.push('激活码不能为空');
			}
			return {
				hpzl : hpzl,
				hphm : hphm,
				sjhm : sjhm,
				sbdm : sbdm,
				jhm : jhm,
				rCode : rCode,
				sxrq : sxrq,
				glbm : glbm,
				err : err
			};
		}
	</script>
</body>
</html>