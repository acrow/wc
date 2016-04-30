<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width,user-scalable=no"/> 
<title>木憧微信平台</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/bootstrap-theme.min.css" rel="stylesheet">
<link href="bower_components/angular-ui-grid/ui-grid.min.css" rel="stylesheet">

<style type="text/css">
body
{
font-family:Microsoft YaHei, SimSun, SimHei;
}
.ngViewport.ng-scope{
    height: auto !important;
    overflow-y: hidden;
}

.ngTopPanel.ng-scope, .ngHeaderContainer{
    width: auto !important;
}
</style>
</head>
<body data-ng-app="reimburse">

<nav class="navbar  navbar-default">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">木憧科技微信平台</a>
    </div>


    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li class="dropdown" data-ng-class="{'active': activeMenu=='client'}">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="glyphicon glyphicon-phone"></span> 客户端设置 <span class="caret"></span></a>
          <ul class="dropdown-menu">
            <li><a href="#/report/auditRpt">客户端菜单</a></li>
            <li><a href="#/report/managerRpt=">客户端自动回复</a></li>
            <li role="separator" class="divider"></li>
            <li><a href="#/report/projRpt">网络参数</a></li>
          </ul>
        </li>
        <li class="dropdown" data-ng-class="{'active': activeMenu=='user'}">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="glyphicon glyphicon-user"></span> 用户管理 <span class="caret"></span></a>
          <ul class="dropdown-menu">
            <li><a href="#/report/auditRpt">用户列表</a></li>
            <li><a href="#/report/projRpt">用户分组</a></li>
            <li role="separator" class="divider"></li>
            <li><a href="#/report/managerRpt=">用户权限</a></li>
          </ul>
        </li>
        <li class="dropdown" data-ng-class="{'active': activeMenu=='message'}">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="glyphicon glyphicon-comment"></span> 消息管理 <span class="caret"></span></a>
          <ul class="dropdown-menu">
            <li><a href="#/report/auditRpt">图文消息</a></li>
            <li><a href="#/report/managerRpt=">文本消息</a></li>
            <li role="separator" class="divider"></li>
            <li><a href="#/report/projRpt">消息类型</a></li>
          </ul>
        </li>
        <li class="dropdown" data-ng-class="{'active': activeMenu=='message'}">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="glyphicon glyphicon-thumbs-up"></span> 投票管理 <span class="caret"></span></a>
          <ul class="dropdown-menu">
            <li><a href="#/report/auditRpt">投票调查</a></li>
            <li><a href="#/report/managerRpt=">报名</a></li>
          </ul>
        </li>
        <li class="dropdown" data-ng-class="{'active': activeMenu=='message'}">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="glyphicon glyphicon-list-alt"></span> 报表管理 <span class="caret"></span></a>
          <ul class="dropdown-menu">
            <li><a href="#/report/auditRpt">投票调查</a></li>
            <li><a href="#/report/managerRpt=">报名</a></li>
          </ul>
        </li>
        <li class="dropdown" data-ng-class="{'active': activeMenu=='message'}">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="glyphicon glyphicon-pencil"></span> 操作日志 <span class="caret"></span></a>
          <ul class="dropdown-menu">
            <li><a href="#/report/auditRpt">投票调查</a></li>
            <li><a href="#/report/managerRpt=">报名</a></li>
          </ul>
        </li>
        <li class="dropdown" data-ng-class="{'active': activeMenu=='setting'}">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="glyphicon glyphicon-cog "></span> 系统设置<span class="caret"></span></a>
          <ul class="dropdown-menu">
            <li><a href="javascript:void(0);" ng-click="password()">修改密码</a></li>
            <li><a href="javascript:void(0);" ng-click="logout()">登出</a></li>
            <li role="separator" class="divider"  ng-show="usr && usr.role=='管理员'"></li>
            <li><a href="#/staff" ng-show="usr && usr.role=='管理员'">人员管理</a></li>
            <li><a href="#/dict" ng-show="usr && usr.role=='管理员'">数据字典</a></li>
          </ul>
        </li>
      </ul>
<!--       <form class="navbar-form navbar-left" role="search"> -->
<!--         <div class="form-group"> -->
<!--           <input type="text" class="form-control" placeholder="Search"> -->
<!--         </div> -->
<!--         <button type="submit" class="btn btn-default">Submit</button> -->
<!--       </form> -->
      <ul class="nav navbar-nav navbar-right" ng-show="usr">
        	<li><a><span class="label label-default">{{usr.role}}:{{usr.name}}</span></a></li>
      </ul>
    
    </div><!-- /.navbar-collapse -->
  </div><!-- /.container-fluid -->
</nav>
<!-- <div class="container-fluid"> -->
<!-- 	<div class="row"> -->
<!-- 		<div class="col-md-2 text-center"> -->
<!-- 			<ul class="list-group"> -->
<!-- 			  <li class="text-info list-group-item"><span class="glyphicon glyphicon-cog" aria-hidden="true"></span>&nbsp;&nbsp;&nbsp;&nbsp;<a href="#/report/auditRpt">系统设置</a></li> -->
<!-- 			  <li class="text-muted list-group-item"><span class="glyphicon glyphicon-sunglasses" aria-hidden="true"></span>&nbsp;&nbsp;&nbsp;&nbsp;<a href="#/report/auditRpt">角色管理</a></li> -->
<!-- 			  <li class="text-success list-group-item"><span class="glyphicon glyphicon-user" aria-hidden="true"></span>&nbsp;&nbsp;&nbsp;&nbsp;<a href="#/report/auditRpt">微信用户</a></li> -->
<!-- 			  <li class="text-danger list-group-item"><span class="glyphicon glyphicon-send" aria-hidden="true"></span>&nbsp;&nbsp;&nbsp;&nbsp;<a href="#/report/auditRpt">信息发布</a></li> -->
<!-- 			  <li class="text-warning list-group-item"><span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span>&nbsp;&nbsp;&nbsp;&nbsp;<a href="#/report/auditRpt">报表管理</a></li> -->
<!-- 			  <li class="text-primary list-group-item"><span class="glyphicon glyphicon-flag" aria-hidden="true"></span>&nbsp;&nbsp;&nbsp;&nbsp;<a href="#/report/auditRpt">操作日志</a></li> -->
<!-- 			</ul> -->
<!-- 		</div> -->
<!-- 		<div data-ng-view class="page-container row-fluid col-md-10" > -->
 
<!--  		</div> -->
<!-- 	</div> -->
<!-- </div> -->

		<div data-ng-view class="page-container row-fluid" >
 
 		</div>
<div>
<p></p>
<p class="text-center">清华大学WMC组</p>
<p class="text-center">Designed and built by <a href="http://www.forallwin.cn" target="_blank">ForAllWin</a></p>
</div>
 <script src="js/lib/angular.min.js"></script>
 <script src="js/lib/angular-locale_zh-cn.min.js"></script>
 <script src="js/lib/angular-route.js"></script>
 <script src="js/lib/angular-resource.js"></script>
 <script src="js/lib/ui-bootstrap-tpls-0.11.0.js"></script>
<!--  <script src="js/lib/ui-grid.min.js"></script> -->
<script src="bower_components/angular-ui-grid/ui-grid.js"></script>
 <script src="js/app/app.js"></script>
 <script src="js/app/resource.js"></script>
 <script src="js/app/service.js"></script>
 
 <script src="js/lib/LodopFuncs.js"></script>
<!--  <object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>  -->
<!-- 	<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="./install_lodop32.exe"></embed> -->
<!-- </object>  -->
<script type="text/javascript">
	var LODOP;
</script>
 <script type="text/javascript">
		LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
</script>
</body>
</html>