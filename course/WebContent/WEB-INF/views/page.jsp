<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>CourseSelect</title>
		<link rel="stylesheet"  type="text/css"  href="<c:url value="/css/easyui.css"/>">
		<link rel="stylesheet" type="text/css" href="<c:url  value="/css/icon.css"/> ">
		<link rel="stylesheet" type="text/css" href="<c:url  value="/css/demo.css"/> ">
		<link rel="stylesheet"  type="text/css"  href="<c:url value="/css/page.css"/>">
		<script type="text/javascript" src ="js/jquery.min.js"></script>
		<script type="text/javascript" src ="js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src ="js/page.js"></script>
	</head>
	<body>
		<div id="courseTab" class="easyui-tabs container"  >
			<div title="课程"  >
				<div  class="datagrid-toolbar">
					<table>
						<tr>
							<td>课程名称:</td>
							<td><input  type="text" id="cName" style="width:110px"></td>
							<td>
								<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" onclick="searchCourse()">查询</a>
							</td>
						</tr>
					</table>
				</div>
				<table id="course_table" ></table>
	        </div>
	        <div title="学生选课">
					<table id="student_table"  ></table>
	        </div>
		</div>
	</body>
</html>