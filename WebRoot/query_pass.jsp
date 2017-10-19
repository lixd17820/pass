<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!doctype html>
<html lang="zh">
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

<title>查询通行证</title>
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
				<a class="navbar-brand" href="#">南通交警通行证查询</a>
			</div>
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav navbar-right">
					<li><a href="atm_pass.jsp">自助办理通行证</a></li>

				</ul>
			</div>
		</div>
	</nav>
	<div class="container">
		<div class="row">
			<div class="col-xs-12  col-md-4" style="background-color:#fff;">
				<div id="alert_error" class="alert alert-danger" role="alert">
					<span class="glyphicon glyphicon-exclamation-sign"
						aria-hidden="true"></span> <span class="sr-only">Error:</span> <span
						id="span_error"></span>
				</div>
				<form class="form-horizontal" role="form">
					<input type="hidden" id="random_code" />
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
						<a id="btn_query_pass"
							class="btn btn-primary col-xs-offset-4 col-xs-5"> <span
							class="glyphicon glyphicon-search" aria-hidden="true"></span>
							查询通行证
						</a>
					</div>
				</form>
			</div>
			<div class="col-xs-12  col-md-8">
				<table class="table" id="pass-table">
					<thead>
						<tr>
							<th>#</th>
							<th class="hidden-xs">号牌种类</th>
							<th class="hidden-xs">号牌号码</th>
							<th>生效日期</th>
							<th>有效期限</th>
							<th></th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(function() {
			$("#btn_query_pass").bind("click", queryPassInfo);
			$("#alert_error").hide();
			reloadSelect($("#hpzl"), hpzlKvs, false);
		});
		function showErrInfo(err) {
			$("#alert_error").show();
			$("#span_error").html("错误：" + err);
		}
		function queryPassInfo(e) {
			$("#alert_error").hide();
			var hpzl = $('#hpzl').val();
			var hpqz = $("#hpqz").val();
			var hphm = hpqz + _.trim(ifNull($('#hphm').val()));
			var sbdm = $('#sbdm').val();
			if (isEmpty(sbdm) || sbdm.length != 4) {
				showErrInfo('车辆识别号不能为空或不是四位');
				return;
			}
			if (isEmpty(hphm) || (hphm.length != 6 && hphm.length != 7)) {
				showErrInfo('号牌号码不正确');
				return;
			}
			if (isEmpty(hpzl)) {
				showErrInfo('号牌种类不能为空');
				return;
			}
			$
					.post(
							'http://www.ntjxj.com/forbid/services/forbid/queryPassInfoSbdm',
							{
								hpzl : hpzl,
								hphm : hphm,
								sbdm : sbdm
							}).then(showPassInfoInList).fail(function(e) {
						showErrInfo('服务器错误');
					});
		}

		function showPassInfoInList(ps) {
			$('.pass-info-tr').remove();
			if (ps.err) {
				showErrInfo(ps.err);
				return 0;
			}
			var tb = $('#pass-table');
			$.each(ps, function(i, p) {
				var tr = $("<tr class='pass-info-tr' />");
				$("<td  />").append(i + 1).appendTo(tr);
				$("<td class='hidden-xs' />").append(
						getDictValueByKey(hpzlKvs, p.hpzl)).appendTo(tr);
				$("<td class='hidden-xs' />").append(p.hphm).appendTo(tr);
				$("<td  />").append(ifNull(p.stDate).substr(0, 10))
						.appendTo(tr);
				$("<td  />").append(ifNull(p.endDate).substr(0, 10)).appendTo(
						tr);
				var detail = $("<a>详细信息</a>").attr("href",
						"pass_detail.jsp?id=" + p._id);
				$("<td />").append(detail).appendTo(tr);
				$("<thead />").append(tr).appendTo(tb);
			});
			return ps;
		}
	</script>
</body>
</html>