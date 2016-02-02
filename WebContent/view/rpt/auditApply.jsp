<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="qh.wmc.app.domain.ExpenseAccount" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>报销审批申请</title>
<STYLE type="text/css">
	#searchConditionTable td{
		border-style: none;
		border-width: 0px;
	}
	.caption{
		font-size: 24px;
		font-family: "黑体";
	}
	.banner td{
		border-style: none;
		border-width: 0px;
	}
	.report{
		font-size:12px;
		border-style: groove;
		border-width: 1px;
		border-collapse: collapse;
		border-color: black;

	}
	.report thead{
		border-style: solid;
		border-width: 1px;
		border-collapse: collapse;
		text-align:center;
		font-weight: bold;
	}
	.report tr,.report td{
		border-style: solid;
		border-width: 1px;
		border-collapse: collapse;
		padding: 5px;
	}
	.areaTitle{
		font-size: 12px;
		font-weight: bolder;
		height: 24px;
	}
	.blockTitle{
		font-size: 12px;
		font-weight: bold;
		height: 18px;
	}
	.areaSum{
	}
	.blockSum{
	}
	.sum{
	}
</STYLE>
</head>
<body>
<%
DecimalFormat currencyFmt = new DecimalFormat();
currencyFmt.applyPattern("##,###.00");
DateFormat dateFmt = DateFormat.getDateInstance();
List<ExpenseAccount> rows = (List<ExpenseAccount>)request.getAttribute("obj");
int rowIndex = 0;
%>
<table width="100%" class="report">
<thead>
<tr style="background-color:#BDBDBD"><td width="10%">日期</td><td width="20%">项目号</td><td width="40%">摘要</td><td width="20%">支出</td><td width="10%">支出方式</td>
</tr>
</thead>
<tbody>
<%
	for(ExpenseAccount row : rows) {
%>
<tr>
<td align="center"><%=dateFmt.format(row.getInputDate()) %></td><td><%=row.getFinanceProjCode() %></td><td><%=row.getSummary() %></td><td align="right"><%=currencyFmt.format(row.getAmount()) %></td><td align="center"><%=row.getPaymethod() %></td>
</tr>
<%
	}
%>
<tr><td colspan="2" format="#,##0.00" tdata="AllSum" tindex="4"><b>合计：############</b></td><td colspan="3" format="UpperMoney" tdata="AllSum" tindex="4"><b>大写：############</b></td>
</tr>
</tbody>
<tfoot>
</tfoot>

</table>
</body>
</html>