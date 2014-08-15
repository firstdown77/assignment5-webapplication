<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ page import="java.util.*" %>
<%@ page import="servlets.DatabaseMethods" %>
<%@ page import="dbObjects.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="/assignment5-webapplication/css/bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="/assignment5-webapplication/css/header.css" type="text/css" />
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
	List<String> testList = new LinkedList<String>();
	for (int i = 0; i < allUsers.size(); i++) {
		testList.add(i, allUsers.get(i).getFirstName());
	}
	request.setAttribute("test", testList); %>

	<center><display:table name="test" pagesize="10" />
	<display:column test />
	</center>
	</div>
</body>
</html>