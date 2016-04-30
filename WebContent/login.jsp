<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>木憧微信平台</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/bootstrap-theme.min.css" rel="stylesheet">
</head>
<body >

<div   style="background-color:#009AC6;height:500px">
<br><br><br><br><br>
	<img src="./img/login.jpeg" class="col-md-offset-2 col-md-4" style="height:400px;width:400px">
	<form action="./login"  method="post" class= "form-horizontal col-md-4" name="form" role="form" style="background-color:#009AC6;color:white" novalidate  >
	<br><br><br><br><br>
	<p class="h2 text-center"><strong>木憧微信平台</strong></p>
	<br><br>
		<div class="form-group ">
			<label class="col-sm-4 control-label" for="logName">登录名</label>
			<div class="col-sm-5 controls">
				<input type="text" id="logName" name="logName" class="form-control"  ng-model="entity.logName"  required >
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-4 control-label" for="password">密码</label>
			<div class="col-sm-5 controls">
				<input type="password" id="password" name="password" class="form-control" ng-model="entity.password" required ng-keyup="keyup($event);">
			</div>
		</div>
		<div>
			<button class="btn btn-default col-sm-offset-6" type="submit">登  录</button>
		</div>
		
	</form>
</div>
<div>
<br><br><br><br>
<p class="text-center">清华大学WMC组</p>
<p class="text-center">Designed and built by <a href="http://www.forallwin.cn" target="_blank">ForAllWin</a></p>
</div>
</body>
</html>