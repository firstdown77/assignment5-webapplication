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
<script src="/assignment5-webapplication/js/jquery-1.11.1.min.js"></script>
<script src="/assignment5-webapplication/js/load_header.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>View All Users</title>
</head>
<body>
	<div id="header"></div>
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
					<display:column property="firstName" title="First Name"
						style="width:125px;" sortable="true" />&nbsp;
				<display:column property="lastName" title="Last Name"
						style="width:125px;" sortable="true" />&nbsp;
				<display:column property="username" title="Username"
						style="width:125px;" sortable="true" />&nbsp;
				<display:column property="joinDate" title="Member Since"
						format="{0,date,M/d/yyyy}" sortable="true" />&nbsp;
			</display:table>
			</font>
		</center>
	</div>
</body>
</html>