<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@ page import="java.util.*"%>
<%@ page import="servlets.DatabaseMethods"%>
<%@ page import="dbObjects.*"%>
<html>
<head>
<link rel="stylesheet"
	href="/assignment5-webapplication/css/bootstrap.min.css"
	type="text/css" />
<link rel="stylesheet" href="/assignment5-webapplication/css/header.css"
	type="text/css" />
<link rel="stylesheet" href="/assignment5-webapplication/css/view_report.css" type="text/css" />
<script src="/assignment5-webapplication/js/jquery-1.11.1.min.js"></script>
<script src="/assignment5-webapplication/js/load_header.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>View All Users</title>
</head>
<body>
	<div id="header"></div>
	<div id="content-body-wrapper" class="content-body-wrapper">
    <div id="content-body" class="content-body">
	<div id="sidebar"></div>
	<div id="content">
	<h2 class="text-center">View All Users</h2>
	<div class="centered">
		<%
			DatabaseMethods db = new DatabaseMethods();
			LinkedList<User> allUsers = db.getAllUsers();
			request.setAttribute("test", allUsers);
		%>
		<center>
			<font size="4"> <display:table name="test" pagesize="10">
					<display:setProperty name="paging.banner.item_name" value="user" />
					<display:setProperty name="paging.banner.items_name" value="users" />
					<display:setProperty name="paging.banner.one_item_found" value="" />
					<display:setProperty name="paging.banner.all_items_found" value="" />
					<display:setProperty name="basic.msg.empty_list" value="None" />
					<display:setProperty name="paging.banner.some_items_found"
					value="<span class='pagebanner'>{0} total {1}, displaying {2} to {3}. </span>" />
					<display:setProperty name="paging.banner.onepage" value="" />			
					<display:column property="username" title="Username"
						style="width:155px;" sortable="true" />&nbsp;
					<display:column property="firstName" title="First Name"
						style="width:155px;" sortable="true" />&nbsp;
					<display:column property="lastName" title="Last Name"
						style="width:155px;" sortable="true" />&nbsp;
					<display:column property="joinDate" title="Member Since"
						format="{0,date,M/d/yyyy}" sortable="true" />&nbsp;
			</display:table>
			</font>
		</center>
	</div>
	</div>
	</div>
	</div>
</body>
</html>